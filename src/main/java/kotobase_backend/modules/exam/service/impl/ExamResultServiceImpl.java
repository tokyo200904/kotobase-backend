package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.dto.response.ExamResult;
import kotobase_backend.modules.exam.entity.ExamAttempt;
import kotobase_backend.modules.exam.repository.ExamAttemptRepository;
import kotobase_backend.modules.exam.repository.ExamAttemptSectionRepository;
import kotobase_backend.modules.exam.repository.ExamRepository;
import kotobase_backend.modules.exam.repository.QuestionGroupRepository;
import kotobase_backend.modules.exam.service.ExamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamResultServiceImpl implements ExamResultService {
    private final ExamAttemptRepository examAttemptRepository;
    private final ExamRepository examRepository;
    private final ExamAttemptSectionRepository examAttemptSectionRepository;
    private final QuestionGroupRepository questionGroupRepository;
    @Override
    public ExamResult getExamResult(Long attemptId, Integer userId) {
        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new ResourceNotFoundException("không tìm thấy lượt thi"));

        validate(examAttempt, userId);

        return null;
    }

    private void validate(ExamAttempt attempt, Integer userId) {
        if (attempt.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("bạn không có quyền truy cập vào");
        }
        if (attempt.getStatus() != AttemptStatus.submitted){
            throw new ResourceNotFoundException("bài thi chưa hoàn thành");
        }
    }
}
