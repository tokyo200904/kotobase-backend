package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.comom.enums.StatusSection;
import kotobase_backend.comom.exceptions.CustomException.ForbiddenException;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.ExamAutosaveController;
import kotobase_backend.modules.exam.component.ExamTimerManager;
import kotobase_backend.modules.exam.dto.request.AnswerSubmitRequest;
import kotobase_backend.modules.exam.dto.response.ExamResumeState;
import kotobase_backend.modules.exam.dto.response.ExamStartResponse;
import kotobase_backend.modules.exam.dto.response.SectionResponse;
import kotobase_backend.modules.exam.dto.response.SectionSubmitResponse;
import kotobase_backend.modules.exam.entity.*;
import kotobase_backend.modules.exam.mapper.ExamAttemptMapper;
import kotobase_backend.modules.exam.repository.*;
import kotobase_backend.modules.exam.service.ExamAttemptService;
import kotobase_backend.modules.exam.service.ExamAutosaveService;
import kotobase_backend.modules.exam.service.ExamTransitionService;
import kotobase_backend.modules.exam.service.scoring.ScoringQueueService;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamAttemptServiceImpl implements ExamAttemptService {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamSectionRepository examSectionRepository;
    private final ExamAttemptMapper examAttemptMapper;
    private final ExamRepository examRepository;
    private final ExamAttemptSectionRepository examAttemptSectionRepository;
    private final ExamAttemptAnswerRepository examAttemptAnswerRepository;
    private final UserRepository userRepository;
    private final ScoringQueueService scoringQueueService;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ExamTimerManager  examTimerManager;
    private final ExamTransitionService  examTransitionService;
    private final ExamAutosaveService examAutosaveService;

    //hàm lấy danh sách câu hỏi đáp án
    @Override
    public SectionResponse getSectionDetail(Long attemptId, Long sectionId, Integer currentUserId) {
        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dữ liệu lượt thi"));

        if (!attempt.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("Bạn không có quyền xem bài thi này.");
        }
        if (attempt.getStatus() == AttemptStatus.submitted) {
            throw new RuntimeException("Bài thi này đã được nộp");
        }
        if (attempt.getStatus() == AttemptStatus.abandoned) {
            throw new RuntimeException("Bài thi này đã bị hủy");
        }

        ExamAttemptSection attemptSection = examAttemptSectionRepository.findByExamAttempt_IdAndSection_Id(attemptId,sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy dữ liệu phần thi của bai thi"));

        if(attemptSection.getStatus() == StatusSection.completed){
            throw new ForbiddenException("Phần thi này đã kết thúc không thể xem lại ");
        }
        ExamSection examSection = examSectionRepository.getExamSectionBySectionId(sectionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phần thi"));

        if (attemptSection.getStatus() == StatusSection.locked){
            attemptSection.setStatus(StatusSection.in_progress);
            attemptSection.setStartedAt(LocalDateTime.now());
            attemptSection = examAttemptSectionRepository.save(attemptSection);

            Instant endTime = attemptSection.getStartedAt()
                    .atZone(ZoneId.systemDefault()).toInstant()
                    .plusSeconds(examSection.getDurationMinutes() * 60L);

            Instant deadLine = attempt.getExpireAt().atZone(ZoneId.systemDefault()).toInstant();
            if (endTime.isAfter(deadLine)) {
                endTime = deadLine;
            }
            examTimerManager.scheduleForceSubmit(attemptId,sectionId,endTime
            ,() -> examTransitionService.processSectionSubmit(attemptId,sectionId,true));
        }

        return examAttemptMapper.toExamSectionResponse(examSection);
    }

    // hàm khi người dùng ấn bắt đầu thi sẻ xem người dùng đang thi rở hay moi bắt đầu
    @Transactional
    @Override
    public ExamStartResponse startOrResumeExam(Integer userId, Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay bai thi"));
        if (!exam.getIsPublished())
            throw new ResourceNotFoundException("de thi chua duoc mo");

        Optional<ExamAttempt> examAttempt = examAttemptRepository
                .findByUser_IdAndExam_IdAndStatus(userId,examId,AttemptStatus.in_progress);
        return examAttempt.map(this::resumeExam).orElseGet(() -> newExam(userId, exam));
    }
    @Override
    public ExamResumeState getExamResumeState(Long attemptId, Integer userId) {
        LocalDateTime now = LocalDateTime.now();

        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lượt thi"));

        if (!examAttempt.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Bạn không có quyền truy cập lượt thi này");
        }
        if (examAttempt.getStatus() != AttemptStatus.in_progress) {
            throw new RuntimeException("EXAM_ALREADY_FINISHED");
        }

        if (examAttempt.getExpireAt() != null && now.isAfter(examAttempt.getExpireAt())) {
            examTransitionService.forceSubmitEntireExam(attemptId);
            throw new RuntimeException("EXAM_TIME_UP_ABSOLUTE");
        }

        Optional<ExamAttemptSection> inProgressOpt = examAttemptSectionRepository
                .findByExamAttempt_IdAndStatus(attemptId, StatusSection.in_progress);

        ExamAttemptSection targetAttemptSection;
        long remainingTimeSeconds;

        if (inProgressOpt.isPresent()) {
            targetAttemptSection = inProgressOpt.get();
            long elapsedSeconds = Duration.between(targetAttemptSection.getStartedAt(), now).getSeconds();
            long totalSeconds = targetAttemptSection.getSection().getDurationMinutes() * 60L;
            remainingTimeSeconds = totalSeconds - elapsedSeconds;

            if (remainingTimeSeconds <= 0) {
                examTransitionService.processSectionSubmit(attemptId, targetAttemptSection.getSection().getId(), true);
                throw new RuntimeException("SECTION_TIME_UP_REFRESH_AGAIN");
            }
        } else {
            targetAttemptSection = examAttemptSectionRepository
                    .findFirstByExamAttempt_IdAndStatusOrderBySection_DisplayOrderAsc(attemptId, StatusSection.locked)
                    .orElseThrow(() -> {
                        examTransitionService.forceSubmitEntireExam(attemptId);
                        return new RuntimeException("EXAM_ALREADY_FINISHED");
                    });
            remainingTimeSeconds = targetAttemptSection.getSection().getDurationMinutes() * 60L;
        }
        ExamSection targetSection = targetAttemptSection.getSection();
        List<ExamAttemptAnswer> examAttemptAnswers = examAttemptAnswerRepository.findByExamAttempt_Id(attemptId);
        Map<Long, Long> answerMap = examAttemptAnswers.stream()
                .filter(a -> a.getSelectedAnswer() != null)
                .collect(Collectors.toMap(
                        a -> a.getQuestion().getId(),
                        a -> a.getSelectedAnswer().getId()
                ));
        ExamResumeState state = new ExamResumeState();
        state.setAttemptId(attemptId);
        state.setNameSession(targetSection.getSectionName());
        state.setSessionId(targetSection.getId());
        state.setRemainingTime(remainingTimeSeconds);
        state.setSavedAnswers(answerMap);
        return state;
    }

    @Transactional
    @Override
    public SectionSubmitResponse submitSection(Long sectionId, Long attemptId, Integer userId) {
        LocalDateTime now = LocalDateTime.now();

        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lượt thi"));

        if (!examAttempt.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Bạn không có quyền thao tác trên bài thi này");
        }
        if (examAttempt.getStatus() != AttemptStatus.in_progress) {
            throw new ResourceNotFoundException("Bài thi này đã kết thúc hoặc bị hủy");
        }

        ExamAttemptSection attemptSection = examAttemptSectionRepository.findByExamAttempt_IdAndSection_Id(attemptId, sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phần thi"));

        if (attemptSection.getStatus() == StatusSection.completed){
            throw new ResourceNotFoundException("Phần thi này đã được nộp");
        }

        examAutosaveService.flushSingleAttempt(attemptId);
        examTimerManager.cancelTimer(attemptId, sectionId);

        attemptSection.setStatus(StatusSection.completed);
        attemptSection.setCompletedAt(now);
        examAttemptSectionRepository.save(attemptSection);

        ExamSection sectionInfo = attemptSection.getSection();
        int disPlayOrder = sectionInfo.getDisplayOrder();

        Optional<ExamSection> nextExamSectionOpt = examSectionRepository
                .findFirstByExam_IdAndDisplayOrderGreaterThanOrderByDisplayOrderAsc(sectionInfo.getExam().getId(), disPlayOrder);

        SectionSubmitResponse sectionSubmitResponse = new SectionSubmitResponse();

        if (nextExamSectionOpt.isPresent()) {
            ExamSection nextExamSection = nextExamSectionOpt.get();

            sectionSubmitResponse.setExamFinished(false);
            sectionSubmitResponse.setNextSectionId(nextExamSection.getId());

        } else {
            examTimerManager.cancelMasterTimer(attemptId);

            examAttempt.setStatus(AttemptStatus.submitted);
            examAttempt.setCompletedAt(now);
            examAttemptRepository.save(examAttempt);

            scoringQueueService.calculateScoreBackground(attemptId);

            sectionSubmitResponse.setExamFinished(true);
            sectionSubmitResponse.setNextSectionId(null);
        }

        return sectionSubmitResponse;
    }


    private ExamStartResponse newExam(Integer userId, Exam exam) {
        LocalDateTime now = LocalDateTime.now();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay user"));

        List<ExamSection> ExamSections = examSectionRepository.findByExam_IdOrderByDisplayOrderAsc(exam.getId());
        if (ExamSections.isEmpty())
            throw new ResourceNotFoundException("đề thi bị lỗi cấu trúc không có phần thi");

        int totalExamMinutes = ExamSections.stream().mapToInt(ExamSection::getDurationMinutes).sum();
        LocalDateTime timeAt = now.plusMinutes(totalExamMinutes);

        ExamAttempt newExamAttempt = new ExamAttempt();
        newExamAttempt.setExam(exam);
        newExamAttempt.setUser(user);
        newExamAttempt.setStatus(AttemptStatus.in_progress);
        newExamAttempt.setStartedAt(now);
        newExamAttempt.setExpireAt(timeAt);
        newExamAttempt = examAttemptRepository.save(newExamAttempt);

        final long attemptId = newExamAttempt.getId();
        examTimerManager.scheduleMasterTimer(
                attemptId,
                timeAt.atZone(ZoneId.systemDefault()).toInstant(),
                () -> examTransitionService.forceSubmitEntireExam(attemptId)
        );

        ExamAttemptSection firstSection = null;

        for (int i=0;i<ExamSections.size();i++) {
            ExamSection examSections = ExamSections.get(i);
            ExamAttemptSection examAttemptSection = new ExamAttemptSection();
            examAttemptSection.setSection(examSections);
            examAttemptSection.setExamAttempt(newExamAttempt);
            examAttemptSection.setStatus(StatusSection.locked);
            examAttemptSectionRepository.save(examAttemptSection);
            if (i == 0) {
                firstSection =  examAttemptSection;
            }
        }
            ExamStartResponse examStartResponse = new ExamStartResponse();
            examStartResponse.setAttemptId(attemptId);
            examStartResponse.setResume(false);
            examStartResponse.setActiveSectionId(firstSection.getSection().getId());

            examStartResponse.setRemainingTimeSeconds(firstSection.getSection().getDurationMinutes());
            examStartResponse.setSavedAnswers(new HashMap<>());

        return examStartResponse;
    }

    private ExamStartResponse resumeExam(ExamAttempt examAttempt) {
        LocalDateTime now = LocalDateTime.now();

        if (examAttempt.getExpireAt() != null && now.isAfter(examAttempt.getExpireAt())) {
            examTransitionService.forceSubmitEntireExam(examAttempt.getId());
            throw new RuntimeException("het thoi gian");
        }

        Optional<ExamAttemptSection> inProgressOpt = examAttemptSectionRepository
                .findByExamAttempt_IdAndStatus(examAttempt.getId(), StatusSection.in_progress);

        ExamAttemptSection targetSecsion;
        long remainingTimeSeconds;
        if (inProgressOpt.isPresent()) {
            targetSecsion = inProgressOpt.get();
            long time1 = Duration.between(targetSecsion.getStartedAt(), now).getSeconds();
            long time2 = targetSecsion.getSection().getDurationMinutes()*60L;
            remainingTimeSeconds = time2 - time1;
            if (remainingTimeSeconds < 0) {
                examTransitionService.processSectionSubmit(examAttempt.getId(), targetSecsion.getSection().getId(),true);
                throw new ResourceNotFoundException("het thoi gian");
            }
        } else{
            targetSecsion = examAttemptSectionRepository
                    .findFirstByExamAttempt_IdAndStatusOrderBySection_DisplayOrderAsc(examAttempt.getId(), StatusSection.locked)
                    .orElseThrow(() -> {
                        examTransitionService.forceSubmitEntireExam(examAttempt.getId());
                        return new RuntimeException("bai thi da ket thuc");
                    });
            remainingTimeSeconds = targetSecsion.getSection().getDurationMinutes()*60L;
        }
        List<ExamAttemptAnswer> attemptAnswers = examAttemptAnswerRepository.findByExamAttempt_Id(examAttempt.getId());
        Map<Long ,Long> answers = attemptAnswers.stream()
                .filter(a -> a.getSelectedAnswer() != null)
                .collect(Collectors.toMap(
                        a -> a.getQuestion().getId(),
                        a -> a.getSelectedAnswer().getId()

                ));
        ExamStartResponse examStartResponse = new ExamStartResponse();
        examStartResponse.setAttemptId(examAttempt.getId());
        examStartResponse.setResume(true);
        examStartResponse.setActiveSectionId(targetSecsion.getSection().getId());
        examStartResponse.setRemainingTimeSeconds(remainingTimeSeconds);
        examStartResponse.setSavedAnswers(answers);

        return examStartResponse;

    }
}
