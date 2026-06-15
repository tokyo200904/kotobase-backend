package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.exam.dto.request.ExamAdminRequest;
import kotobase_backend.modules.exam.dto.response.ExamAdminResponse;
import kotobase_backend.modules.exam.entity.Exam;
import kotobase_backend.modules.exam.mapper.ExamAdminMapper;
import kotobase_backend.modules.exam.repository.ExamRepository;
import kotobase_backend.modules.exam.service.ExamAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExamAdminServiceImpl implements ExamAdminService {

    private final ExamRepository examRepository;
    private final ExamAdminMapper examMapper;

    @Override
    public PageResponse<ExamAdminResponse> getAllExams(String search, Integer levelId, Pageable pageable) {
        Page<Exam> page = examRepository.adminSearchExams(search, levelId, pageable);
        return PageResponse.of(page.map(examMapper::toResponse));
    }

    @Override
    public ExamAdminResponse getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đề thi"));
        return examMapper.toResponse(exam);
    }

    @Override
    @Transactional
    public ExamAdminResponse createExam(ExamAdminRequest request) {
        Exam exam = new Exam();
        exam.setCreatedAt(LocalDateTime.now());
        examMapper.updateEntityFromRequest(request, exam);
        Exam saved = examRepository.save(exam);
        return examMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ExamAdminResponse updateExam(Long id, ExamAdminRequest request) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đề thi"));
        examMapper.updateEntityFromRequest(request, exam);
        Exam updated = examRepository.save(exam);
        return examMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đề thi"));
        examRepository.delete(exam);
    }
}