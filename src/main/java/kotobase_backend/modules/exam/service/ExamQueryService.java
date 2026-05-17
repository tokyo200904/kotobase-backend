package kotobase_backend.modules.exam.service;

import kotobase_backend.modules.exam.dto.request.ExamRequest;
import kotobase_backend.modules.exam.dto.response.ExamDetailResponse;
import kotobase_backend.modules.exam.dto.response.ExamResponse;
import kotobase_backend.modules.exam.dto.response.PageExamResponse;

public interface ExamQueryService {
    public PageExamResponse<ExamResponse> getExamByLevel(ExamRequest examRequest);
    public ExamDetailResponse getDetailExamById(Long id);
}
