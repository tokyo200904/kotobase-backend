package kotobase_backend.modules.exam.service.impl;

import jakarta.transaction.Transactional;
import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.comom.enums.StatusSection;
import kotobase_backend.modules.exam.ExamAutosaveController;
import kotobase_backend.modules.exam.component.ExamTimerManager;
import kotobase_backend.modules.exam.dto.response.ExamWebSocketPayload;
import kotobase_backend.modules.exam.entity.ExamAttempt;
import kotobase_backend.modules.exam.entity.ExamAttemptSection;
import kotobase_backend.modules.exam.entity.ExamSection;
import kotobase_backend.modules.exam.repository.ExamAttemptRepository;
import kotobase_backend.modules.exam.repository.ExamAttemptSectionRepository;
import kotobase_backend.modules.exam.repository.ExamSectionRepository;
import kotobase_backend.modules.exam.service.ExamAutosaveService;
import kotobase_backend.modules.exam.service.ExamTransitionService;
import kotobase_backend.modules.exam.service.scoring.ScoringQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamTransitionServiceImpl implements ExamTransitionService {
    private ExamTimerManager timerManager;
    private SimpMessagingTemplate messagingTemplate;
    private ExamAutosaveService autosaveService;
    private ExamAttemptRepository attemptRepository;
    private ExamAttemptSectionRepository attemptSectionRepository;
    private ExamSectionRepository sectionRepository;
    private ScoringQueueService scoringQueueService;

    @Transactional
    @Override
    public void processSectionSubmit(Long attemptId, Long currentSectionId, boolean isForceSubmit) {
        ExamAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài thi"));

        ExamAttemptSection currentAttemptSection = attemptSectionRepository
                .findByExamAttempt_IdAndSection_Id(attemptId, currentSectionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tiến độ phần thi"));

        if (currentAttemptSection.getStatus() == StatusSection.completed) {
            return;
        }
        if (!isForceSubmit) {
            timerManager.cancelTimer(attemptId, currentSectionId);
        }

        autosaveService.flushSingleAttempt(attemptId);

        LocalDateTime now = LocalDateTime.now();
        currentAttemptSection.setStatus(StatusSection.completed);
        currentAttemptSection.setCompletedAt(now);
        attemptSectionRepository.save(currentAttemptSection);

        ExamSection masterCurrentSection = sectionRepository.findById(currentSectionId).orElseThrow();

        Optional<ExamSection> nextSectionOpt = sectionRepository
                .findFirstByExam_IdAndDisplayOrderGreaterThanOrderByDisplayOrderAsc(
                        attempt.getExam().getId(), masterCurrentSection.getDisplayOrder());

        ExamWebSocketPayload payload = new ExamWebSocketPayload();

        if (nextSectionOpt.isPresent()) {
            payload.setAction("MOVE_NEXT");
            payload.setNextSectionId(nextSectionOpt.get().getId());
            payload.setMessage(isForceSubmit ? "Hết giờ! Tự động chuyển sang phần thi tiếp theo." : "Đang chuyển phần thi...");

        } else {

            attempt.setStatus(AttemptStatus.submitted);
            attempt.setCompletedAt(now);
            attemptRepository.save(attempt);

            timerManager.cancelMasterTimer(attemptId);

            scoringQueueService.calculateScoreBackground(attemptId);

            payload.setAction("FINISHED");
            payload.setMessage("Bài thi đã kết thúc!");
        }

        messagingTemplate.convertAndSend("/topic/exam/" + attemptId, payload);
    }

    @Transactional
    public void forceSubmitEntireExam(Long attemptId) {
        ExamAttempt attempt = attemptRepository.findById(attemptId).orElseThrow();
        if (attempt.getStatus() == AttemptStatus.submitted) return;

        autosaveService.flushSingleAttempt(attemptId);

        attempt.setStatus(AttemptStatus.submitted);
        attempt.setCompletedAt(LocalDateTime.now());
        attemptRepository.save(attempt);

        attemptSectionRepository.findByExamAttempt_IdAndStatus(attemptId, StatusSection.in_progress)
                .ifPresent(sec -> {
                    sec.setStatus(StatusSection.completed);
                    sec.setCompletedAt(LocalDateTime.now());
                    attemptSectionRepository.save(sec);
                    timerManager.cancelTimer(attemptId, sec.getSection().getId());
                });

        scoringQueueService.calculateScoreBackground(attemptId);

        ExamWebSocketPayload payload = new ExamWebSocketPayload();
        payload.setAction("FINISHED");
        payload.setMessage("Đã hết tổng thời gian bài thi! Hệ thống tự động thu bài.");
        messagingTemplate.convertAndSend("/topic/exam/" + attemptId, payload);
    }
}