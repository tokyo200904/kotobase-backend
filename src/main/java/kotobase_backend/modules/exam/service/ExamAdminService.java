package kotobase_backend.modules.exam.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.exam.dto.request.ExamAdminRequest;
import kotobase_backend.modules.exam.dto.response.ExamAdminResponse;
import org.springframework.data.domain.Pageable;

public interface ExamAdminService {
    PageResponse<ExamAdminResponse> getAllExams(String search, Integer levelId, Pageable pageable);
    ExamAdminResponse getExamById(Long id);
    ExamAdminResponse createExam(ExamAdminRequest request);
    ExamAdminResponse updateExam(Long id, ExamAdminRequest request);
    void deleteExam(Long id);
}
