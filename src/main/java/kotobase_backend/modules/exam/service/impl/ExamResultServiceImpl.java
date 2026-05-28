package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.dto.response.ExamResult;
import kotobase_backend.modules.exam.dto.response.SectionResult;
import kotobase_backend.modules.exam.entity.Exam;
import kotobase_backend.modules.exam.entity.ExamAttempt;
import kotobase_backend.modules.exam.entity.ExamAttemptSection;
import kotobase_backend.modules.exam.repository.ExamAttemptRepository;
import kotobase_backend.modules.exam.repository.ExamAttemptSectionRepository;
import kotobase_backend.modules.exam.repository.ExamRepository;
import kotobase_backend.modules.exam.repository.QuestionGroupRepository;
import kotobase_backend.modules.exam.service.ExamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamResultServiceImpl implements ExamResultService {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamRepository examRepository;
    private final ExamAttemptSectionRepository examAttemptSectionRepository;
    private final QuestionGroupRepository questionGroupRepository;

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




    private void validate(ExamAttempt attempt, Integer userId) {
        if (!attempt.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("bạn không có quyền truy cập vào");
        }
        if (attempt.getStatus() != AttemptStatus.submitted){
            throw new ResourceNotFoundException("bài thi chưa hoàn thành");
        }
    }
}
