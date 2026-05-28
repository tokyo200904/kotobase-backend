package kotobase_backend.modules.exam.service.scoring;


import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.comom.enums.StatusSection;
import kotobase_backend.modules.exam.component.ExamTimerManager;
import kotobase_backend.modules.exam.entity.ExamAttempt;
import kotobase_backend.modules.exam.entity.ExamAttemptSection;
import kotobase_backend.modules.exam.repository.ExamAttemptRepository;
import kotobase_backend.modules.exam.repository.ExamAttemptSectionRepository;
import kotobase_backend.modules.exam.repository.ExamSectionRepository;
import kotobase_backend.modules.exam.service.ExamTransitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ExamSystemRecoveryService {

    private final ExamAttemptSectionRepository attemptSectionRepository;
    private final ExamAttemptRepository attemptRepository;
    private final ExamSectionRepository sectionRepository;
    private final ExamTimerManager timerManager;
    private final ExamTransitionService transitionService;

    @EventListener(ApplicationReadyEvent.class)
    public void recoverScheduledTasks() {
        System.out.println("[Recovery System] Bắt đầu khôi phục...");

        Instant now = Instant.now();
        int recoveredCount = 0;
        int expiredCount = 0;

        List<ExamAttemptSection> activeSections =
                attemptSectionRepository.findByStatusWithAttemptAndSection(StatusSection.in_progress);

        for (ExamAttemptSection section : activeSections) {
            if (section.getStartedAt() == null) continue;

            final Long attemptId = section.getExamAttempt().getId();
            final Long sectionId = section.getSection().getId();
            final int durationMinutes = section.getSection().getDurationMinutes();

            Instant exactEndTime = section.getStartedAt()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .plusSeconds(durationMinutes * 60L);

            if (exactEndTime.isBefore(now)) {
                transitionService.processSectionSubmit(attemptId, sectionId, true);
                expiredCount++;
            } else {
                timerManager.scheduleForceSubmit(attemptId, sectionId, exactEndTime,
                        () -> transitionService.processSectionSubmit(attemptId, sectionId, true));
                recoveredCount++;
            }
        }
        List<ExamAttempt> inProgressAttempts =
                attemptRepository.findByStatus(AttemptStatus.in_progress);

        for (ExamAttempt attempt : inProgressAttempts) {
            boolean hasActiveSection = activeSections.stream()
                    .anyMatch(s -> s.getExamAttempt().getId().equals(attempt.getId()));
            if (hasActiveSection) continue;

            final Long attemptId = attempt.getId();

            if (attempt.getExpireAt() == null) continue;

            Instant expireInstant = attempt.getExpireAt()
                    .atZone(ZoneId.systemDefault()).toInstant();

            if (expireInstant.isBefore(now)) {
                transitionService.forceSubmitEntireExam(attemptId);
                expiredCount++;
            } else {
                timerManager.scheduleMasterTimer(attemptId, expireInstant,
                        () -> transitionService.forceSubmitEntireExam(attemptId));
                recoveredCount++;
            }
        }

        System.out.println("[Recovery System] Đã khôi phục timer: " + recoveredCount);
        System.out.println("[Recovery System] Đã xử lý quá hạn trực tiếp: " + expiredCount);
    }

    @Transactional
    public void processExpiredSection(Long attemptId, Long sectionId) {
        transitionService.processSectionSubmit(attemptId, sectionId, true);
    }

    @Transactional
    public void processExpiredAttempt(Long attemptId) {
        transitionService.forceSubmitEntireExam(attemptId);
    }
}