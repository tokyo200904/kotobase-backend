package kotobase_backend.modules.exam.service;

import kotobase_backend.modules.exam.dto.response.SectionResponse;

public interface ExamAttemptService {
    public SectionResponse getSectionById(Long attemptId, Long sectionId, Integer currentUserId);

}
