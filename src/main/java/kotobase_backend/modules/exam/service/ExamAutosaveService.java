package kotobase_backend.modules.exam.service;

public interface ExamAutosaveService {
    public void addAnswer(Long attemptId, Long questionId, Long answerId);
    public void flushBufferToDatabase();
    public void flushSingleAttempt(Long attemptId);
}
