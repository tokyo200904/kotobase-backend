package kotobase_backend.modules.exam.service;

import kotobase_backend.modules.exam.dto.request.ExamRequest;
import kotobase_backend.modules.exam.dto.response.ExamResponse;
import kotobase_backend.modules.exam.dto.response.PageExamResponse;
import kotobase_backend.modules.exam.entity.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamService {
    public PageExamResponse<ExamResponse> getExamByLevel(ExamRequest examRequest);
}
