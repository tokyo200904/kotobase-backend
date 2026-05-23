package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.comom.enums.StatusSection;
import kotobase_backend.comom.exceptions.CustomException.ForbiddenException;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.dto.request.AnswerSubmitRequest;
import kotobase_backend.modules.exam.dto.response.ExamStartResponse;
import kotobase_backend.modules.exam.dto.response.SectionResponse;
import kotobase_backend.modules.exam.dto.response.SectionSubmitResponse;
import kotobase_backend.modules.exam.entity.*;
import kotobase_backend.modules.exam.mapper.ExamAttemptMapper;
import kotobase_backend.modules.exam.repository.*;
import kotobase_backend.modules.exam.service.ExamAttemptService;
import kotobase_backend.modules.exam.service.scoring.ScoringQueueService;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
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

        if(attemptSection.getStatus() == StatusSection.locked){
            throw new ForbiddenException("phan thi chưa được mở, hãy hoàn thành phần thi trước đó");
        }

        if(attemptSection.getStatus() == StatusSection.completed){
            throw new ForbiddenException("Phần thi này đã kết thúc không thể xem lại đề");
        }

        ExamSection examSection = examSectionRepository.getExamSectionBySectionId(sectionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phần thi"));

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

    @Transactional
    @Override
    public void aotuSaveAnswer(Long attemptId, List<AnswerSubmitRequest> inputs, Integer userId) {

        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay luot thi"));
        if (!examAttempt.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Bạn không có quyền luu bài thi này");
        }
       if (examAttempt.getStatus() != AttemptStatus.in_progress) {
           throw new ResourceNotFoundException("bài thi không ở trạng thái đang làm bài ");
       }
       if (inputs == null || inputs.isEmpty()) {
           return ;
       }

       List<Long> questionIds = inputs.stream()
               .map(AnswerSubmitRequest::getQuestionId)
               .collect(Collectors.toList());

       List<ExamAttemptAnswer> oldList = examAttemptAnswerRepository
               .findByExamAttempt_IdAndQuestion_IdIn(attemptId, questionIds);

       Map<Long,ExamAttemptAnswer> answerMap = oldList.stream()
               .collect(Collectors.toMap(
                       ans -> ans.getQuestion().getId(),
                       ans -> ans
               ));

       List<ExamAttemptAnswer> saveAnswer = new ArrayList<>();

       for (AnswerSubmitRequest input : inputs) {

           ExamAttemptAnswer examAttemptAnswer;
           if (answerMap.containsKey(input.getQuestionId())) {
               examAttemptAnswer = answerMap.get(input.getQuestionId());
           }
           else {
               examAttemptAnswer = new ExamAttemptAnswer();
               examAttemptAnswer.setExamAttempt(examAttempt);
               Question question = questionRepository.getReferenceById(input.getQuestionId());
               examAttemptAnswer.setQuestion(question);
           }
           if (input.getSelectedAnswerId() != null) {
               Answer answer = answerRepository.getReferenceById(input.getSelectedAnswerId());
               examAttemptAnswer.setSelectedAnswer(answer);
           }
           else {
               examAttemptAnswer.setSelectedAnswer(null);
           }
           saveAnswer.add(examAttemptAnswer);
       }
       examAttemptAnswerRepository.saveAll(saveAnswer);
    }



    // hàm khi người dùng ấn hoàn thành phần thi
    @Transactional
    @Override
    public SectionSubmitResponse submitSection(Long sectionId, Long attemptId, Integer userId) {
        LocalDateTime now = LocalDateTime.now();

        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay luot thi"));

        if (examAttempt.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("ban khong co quyen thao tac");
        }
        if (examAttempt.getStatus() != AttemptStatus.in_progress) {
            throw new ResourceNotFoundException("bai thi nay da huy ban khong co quyen thao tac");
        }

        ExamAttemptSection attemptSection = examAttemptSectionRepository.findByExamAttempt_IdAndSection_Id(attemptId,sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay phan thi"));

        if (attemptSection.getStatus() == StatusSection.completed){
            throw new ResourceNotFoundException("phan thi nay da duoc nop");
        }

        attemptSection.setStatus(StatusSection.completed);
        attemptSection.setCompletedAt(now);
        examAttemptSectionRepository.save(attemptSection);

        ExamSection sectionInfo = examSectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay phan thi thi"));

        int disPlayOrder = sectionInfo.getDisplayOrder();

        Optional<ExamSection> examSection = examSectionRepository
                .findFirstByExam_IdAndDisplayOrderGreaterThanOrderByDisplayOrderAsc(sectionInfo.getExam().getId(), disPlayOrder);

        SectionSubmitResponse sectionSubmitResponse = new SectionSubmitResponse();

        if (examSection.isPresent()) {
            ExamSection nextExamSection = examSection.get();

            ExamAttemptSection nextAttemptSection = examAttemptSectionRepository
                    .findByExamAttempt_IdAndSection_Id(attemptId,nextExamSection.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("khong tim thay phan thi"));

            nextAttemptSection.setStartedAt(now);
            nextAttemptSection.setStatus(StatusSection.in_progress);
            examAttemptSectionRepository.save(nextAttemptSection);

            sectionSubmitResponse.setExamFinished(false);
            sectionSubmitResponse.setNextSectionId(nextExamSection.getId());
        }
        else {
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
        ExamAttempt newExamAttempt = new ExamAttempt();
        newExamAttempt.setExam(exam);
        newExamAttempt.setUser(user);
        newExamAttempt.setStatus(AttemptStatus.in_progress);
        newExamAttempt.setStartedAt(now);
        newExamAttempt = examAttemptRepository.save(newExamAttempt);

        List<ExamSection> ExamSections = examSectionRepository.findByExam_IdOrderByDisplayOrderAsc(exam.getId());
        if (ExamSections.isEmpty())
            throw new ResourceNotFoundException("đề thi bị lỗi cấu trúc không có phần thi");

        ExamAttemptSection firstSection = null;

        for (int i=0;i<ExamSections.size();i++) {
            ExamSection examSections = ExamSections.get(i);
            ExamAttemptSection examAttemptSection = new ExamAttemptSection();
            examAttemptSection.setSection(examSections);
            examAttemptSection.setExamAttempt(newExamAttempt);

            if (i == 0) {
                examAttemptSection.setStartedAt(now);
                examAttemptSection.setStatus(StatusSection.in_progress);
                firstSection = examAttemptSectionRepository.save(examAttemptSection);
            } else {
                examAttemptSection.setStatus(StatusSection.locked);
                examAttemptSectionRepository.save(examAttemptSection);
            }
        }
            ExamStartResponse examStartResponse = new ExamStartResponse();
            examStartResponse.setAttemptId(newExamAttempt.getId());
            examStartResponse.setResume(false);
            examStartResponse.setActiveSectionId(firstSection.getSection().getId());

            int time = ExamSections.get(0).getDurationMinutes();
            examStartResponse.setRemainingTimeSeconds(time * 60L);
            examStartResponse.setSavedAnswers(new HashMap<>());

        return examStartResponse;
    }

    private ExamStartResponse resumeExam(ExamAttempt examAttempt) {
        LocalDateTime now = LocalDateTime.now();

        ExamAttemptSection attemptSection = examAttemptSectionRepository.findByExamAttempt_IdAndStatus(examAttempt.getId(), StatusSection.in_progress)
                .orElseThrow(() -> new ResourceNotFoundException("phan thi bi loi trang thai "));

        ExamSection examSection = examSectionRepository.findById(attemptSection.getSection().getId())
                .orElseThrow(() -> new ResourceNotFoundException("khong tim thay bai thi"));

        long timeResume = Duration.between(attemptSection.getStartedAt(), now).getSeconds();
        long timeSection = examSection.getDurationMinutes() * 60L;

        if (timeResume > timeSection) {
            LocalDateTime endtime = attemptSection.getStartedAt().plusMinutes(examSection.getDurationMinutes());

            attemptSection.setStatus(StatusSection.completed);
            attemptSection.setCompletedAt(endtime);
            examAttemptSectionRepository.save(attemptSection);

            Optional<ExamSection> nextSection = examSectionRepository
                    .findFirstByExam_IdAndDisplayOrderGreaterThanOrderByDisplayOrderAsc
                            (examAttempt.getExam().getId(),examSection.getDisplayOrder());

            if (nextSection.isPresent()) {
                ExamAttemptSection nextExamAttemptSection = examAttemptSectionRepository
                        .findByExamAttempt_IdAndSection_Id(examAttempt.getId(),nextSection.get().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("khong tim thay phan thi"));

                nextExamAttemptSection.setStatus(StatusSection.in_progress);
                nextExamAttemptSection.setStartedAt(now);
                examAttemptSectionRepository.save(nextExamAttemptSection);

                ExamStartResponse examStartResponse = new ExamStartResponse();
                examStartResponse.setAttemptId(examAttempt.getId());
                examStartResponse.setResume(true);
                examStartResponse.setActiveSectionId(nextSection.get().getId());
                examStartResponse.setSavedAnswers(new HashMap<>());
                examStartResponse.setRemainingTimeSeconds(nextSection.get().getDurationMinutes() * 60L);
                return examStartResponse;
            }
            else {
                examAttempt.setStatus(AttemptStatus.submitted);
                examAttempt.setCompletedAt(endtime);
                examAttemptRepository.save(examAttempt);

                scoringQueueService.calculateScoreBackground(examAttempt.getId());
            }
        }

        long time1 = Duration.between(attemptSection.getStartedAt(), now).getSeconds();
        long time2 = examSection.getDurationMinutes()*60L;
        long time3 = time2 - time1;

        if (time3 <= 0){
            throw new RuntimeException("da het gio");
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
        examStartResponse.setActiveSectionId(attemptSection.getSection().getId());
        examStartResponse.setRemainingTimeSeconds(time3);
        examStartResponse.setSavedAnswers(answers);

        return examStartResponse;

    }
}
