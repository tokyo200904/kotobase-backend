package kotobase_backend.modules.exam.service;

public interface ExamTransitionService {
    public void processSectionSubmit(Long attemptId, Long currentSectionId, boolean isForceSubmit);
    public void forceSubmitEntireExam(Long attemptId);
}
