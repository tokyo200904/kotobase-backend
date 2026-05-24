package kotobase_backend.modules.exam.service;

import kotobase_backend.modules.exam.dto.request.AnswerSubmitRequest;
import kotobase_backend.modules.exam.dto.response.ExamResumeState;
import kotobase_backend.modules.exam.dto.response.ExamStartResponse;
import kotobase_backend.modules.exam.dto.response.SectionResponse;
import kotobase_backend.modules.exam.dto.response.SectionSubmitResponse;

import java.util.List;

public interface ExamAttemptService {
    public SectionResponse getSectionDetail(Long attemptId, Long sectionId, Integer currentUserId);
    public ExamStartResponse startOrResumeExam(Integer userId, Long examId);
    public SectionSubmitResponse submitSection(Long sectionId, Long attemptId, Integer userId);
    public void aotuSaveAnswer(Long attemptId ,List<AnswerSubmitRequest> inputs, Integer userId);
    public ExamResumeState getExamResumeState(Long attemptId, Integer userId);
}
