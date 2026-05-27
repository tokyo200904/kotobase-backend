package kotobase_backend.modules.exam.service;

import java.util.Map;

public interface ExamAttemptAnswerService {
    public void aotuSaveAnswers(Long attemptId, Map<Long, Long> answersRam);
}
