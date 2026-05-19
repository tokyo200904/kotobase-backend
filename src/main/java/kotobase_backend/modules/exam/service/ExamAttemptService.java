package kotobase_backend.modules.exam.service;

import kotobase_backend.modules.exam.dto.response.ExamStartResponse;
import kotobase_backend.modules.exam.dto.response.SectionResponse;
import kotobase_backend.modules.exam.dto.response.SectionSubmitResponse;

public interface ExamAttemptService {
    public SectionResponse getSectionDetail(Long attemptId, Long sectionId, Integer currentUserId);
    public ExamStartResponse startOrResumeExam(Integer userId, Long examId);
    public SectionSubmitResponse submitSection(Long sectionId, Long attemptId, Integer userId);
}
