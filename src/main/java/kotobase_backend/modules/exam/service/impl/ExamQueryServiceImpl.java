package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.JlptLevel.repository.JlptLevelRepository;
import kotobase_backend.modules.exam.dto.request.ExamRequest;
import kotobase_backend.modules.exam.dto.response.ExamResponse;
import kotobase_backend.modules.exam.dto.response.PageExamResponse;
import kotobase_backend.modules.exam.entity.Exam;
import kotobase_backend.modules.exam.mapper.ExamMapper;
import kotobase_backend.modules.exam.repository.ExamRepository;
import kotobase_backend.modules.exam.service.ExamQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamQueryServiceImpl implements ExamQueryService {
    private final ExamRepository examRepository;
    private final JlptLevelRepository jlptLevelRepository;
    private final ExamMapper examMapper;

    @Override
    public PageExamResponse<ExamResponse> getExamByLevel(ExamRequest examRequest) {
        boolean check = jlptLevelRepository.existsById(examRequest.getLevelId());
        if (!check) {
            throw new ResourceNotFoundException("không tìm thấy level");
        }
        int limit =  examRequest.getLimit();
        int page = examRequest.getPage() - 1;

        Pageable pageable = PageRequest.of(page,limit,Sort.by("createdAt").descending());

        Page<Exam> pageExam = examRepository.findByLevel_IdAndIsPublishedTrue(examRequest.getLevelId(), pageable);

        List<ExamResponse> data = pageExam.getContent().stream()
                .map(examMapper::toExamResponse)
                .toList();

        PageExamResponse<ExamResponse> response = new PageExamResponse<>();
        response.setData(data);
        response.setPage(page);
        response.setLimit(limit);
        response.setTotalPages(pageExam.getTotalPages());
        response.setTotalElements(pageExam.getTotalElements());
        return response;
    }

    @Override
    public ExamResponse getExamById(Long id) {
        if (id == null) {
            throw new ResourceNotFoundException("mã bài thị null");
        }
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("không tìm thấy đề thi"));
        return examMapper.toExamResponse(exam);
    }


}
