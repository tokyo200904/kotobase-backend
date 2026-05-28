package kotobase_backend.modules.exam.service;

import kotobase_backend.modules.exam.dto.response.ExamResult;

public interface ExamResultService {
    public ExamResult getExamResult(Long attemptId, Integer userId);
}
