package kotobase_backend.modules.exam.service;

import kotobase_backend.modules.exam.dto.response.ExamResult;
import kotobase_backend.modules.exam.dto.response.ExamReviewResponse;

public interface ExamResultService {
    public ExamResult getExamResult(Long attemptId, Integer userId);
    public ExamReviewResponse getExamReviewDetails(Long attemptId, Integer userId);
}
