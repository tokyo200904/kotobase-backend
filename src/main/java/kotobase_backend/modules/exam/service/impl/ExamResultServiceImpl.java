package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.dto.response.ExamResult;
import kotobase_backend.modules.exam.dto.response.ExamReviewResponse;
import kotobase_backend.modules.exam.dto.response.SectionResult;
import kotobase_backend.modules.exam.entity.*;
import kotobase_backend.modules.exam.repository.*;
import kotobase_backend.modules.exam.service.ExamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamResultServiceImpl implements ExamResultService {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamRepository examRepository;
    private final ExamAttemptSectionRepository examAttemptSectionRepository;
    private final QuestionGroupRepository questionGroupRepository;
    private final ExamAttemptAnswerRepository examAttemptAnswerRepository;
    private final ExamSectionRepository examSectionRepository;

    @Override
    @Transactional
    public ExamResult getExamResult(Long attemptId, Integer userId) {
        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy lượt thi"));

        validate(examAttempt, userId);

        ExamResult examResult = new ExamResult();
        examResult.setAttemptId(attemptId);
        examResult.setExamId(examAttempt.getExam().getId());
        examResult.setTitle(examAttempt.getExam().getTitle());
        examResult.setTotalScore(examAttempt.getTotalScore());
        examResult.setIsPassed(examAttempt.getIsPassed());
        examResult.setMaxScore(examAttempt.getExam().getMaxScore());
        if (examAttempt.getCompletedAt() != null) {
            examResult.setTimeFinished(Duration.between(examAttempt.getStartedAt(),examAttempt.getCompletedAt()).toMinutes());
        }

        List<ExamAttemptSection> examAttemptSection = examAttemptSectionRepository.findByExamAttempt_Id(attemptId);
        List<SectionResult> sectionResults = examAttemptSection.stream()
                .map(sec ->{
                    SectionResult sr = new SectionResult();
                    sr.setSetionId(sec.getSection().getId());
                    sr.setSetionName(sec.getSection().getSectionName());
                    sr.setSectionType(sec.getSection().getSectionType());
                    sr.setMaxScore(sec.getSection().getMaxScore());
                    sr.setScore(sec.getSectionScore());
                    sr.setIsPass(sec.getIsPassedSection());
                    return sr;
                }).collect(Collectors.toList());
        examResult.setSections(sectionResults);
        return examResult;
    }

    @Override
    @Transactional
    public ExamReviewResponse getExamReviewDetails(Long attemptId, Integer userId) {
        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài thi"));
        validate(attempt, userId);

        List<ExamAttemptAnswer> attemptAnswers = examAttemptAnswerRepository.findByExamAttempt_Id(attemptId);

        Map<Long, ExamAttemptAnswer> userAnswerMap = attemptAnswers.stream()
                .collect(Collectors.toMap(
                        a -> a.getQuestion().getId(),
                        a -> a
                ));

        Long examId = attempt.getExam().getId();
        List<ExamSection> examSections = examSectionRepository.findByExam_IdOrderByDisplayOrderAsc(examId);

        ExamReviewResponse response = new ExamReviewResponse();
        response.setAttemptId(attemptId);

        List<ExamReviewResponse.SectionReviewDto> sectionDtos = new ArrayList<>();

        for (ExamSection section : examSections) {
            ExamReviewResponse.SectionReviewDto sectionDto = new ExamReviewResponse.SectionReviewDto();
            sectionDto.setSectionId(section.getId());
            sectionDto.setSectionName(section.getSectionName());

            List<ExamReviewResponse.QuestionGroupReviewDto> groupDtos = new ArrayList<>();

        for (QuestionGroups group : section.getQuestionGroups()) {
            ExamReviewResponse.QuestionGroupReviewDto groupDto = new ExamReviewResponse.QuestionGroupReviewDto();
            groupDto.setGroupId(group.getId());
            groupDto.setContent(group.getContent());
            groupDto.setImageUrl(group.getImage() != null ? group.getImage().getImageUrl() : null);
            groupDto.setAudioUrl(group.getAudio() != null ?group.getAudio().getUrl() : null);

            List<ExamReviewResponse.QuestionReviewDto> questionDtos = new ArrayList<>();
            for (Question q :  group.getQuestions()) {
                ExamReviewResponse.QuestionReviewDto qDto = new ExamReviewResponse.QuestionReviewDto();
                qDto.setQuestionId(q.getId());
                qDto.setContent(q.getContent());
                qDto.setImageUrl(q.getImage() != null ? q.getImage().getImageUrl() : null);
                qDto.setAudioUrl(q.getAudio() != null ? q.getAudio().getUrl() : null);

                List<ExamReviewResponse.AnswerReviewDto> answerDtos = new ArrayList<>();
                for (Answer a : q.getAnswers()) {
                    ExamReviewResponse.AnswerReviewDto aDto = new ExamReviewResponse.AnswerReviewDto();
                    aDto.setId(a.getId());
                    aDto.setContent(a.getContent());
                    answerDtos.add(aDto);

                    if (Boolean.TRUE.equals(a.getIsCorrect())){
                        qDto.setCorrectAnswerId(a.getId());
                        qDto.setExplanation(a.getExplanation());
                    }
                }
                qDto.setAnswers(answerDtos);

                ExamAttemptAnswer userAnswer = userAnswerMap.get(q.getId());
                if (userAnswer != null && userAnswer.getSelectedAnswer() != null) {
                    qDto.setUserSelectedAnswerId(userAnswer.getSelectedAnswer().getId());
                    qDto.setIsCorrect(userAnswer.getIsCorrectSnapshot());
                }else {
                    qDto.setUserSelectedAnswerId(null);
                    qDto.setIsCorrect(false);
                }
                questionDtos.add(qDto);
            }
            groupDto.setQuestions(questionDtos);
            groupDtos.add(groupDto);
        }
        sectionDto.setQuestionGroups(groupDtos);
        sectionDtos.add(sectionDto);
    }
    response.setSections(sectionDtos);
    return response;
    }


    private void validate(ExamAttempt attempt, Integer userId) {
        if (!attempt.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("bạn không có quyền truy cập vào");
        }
        if (attempt.getStatus() != AttemptStatus.submitted){
            throw new ResourceNotFoundException("bài thi chưa hoàn thành");
        }
    }
}
