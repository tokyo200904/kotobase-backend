package kotobase_backend.modules.exam.service.impl;

import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.modules.exam.dto.response.SectionResponse;
import kotobase_backend.modules.exam.entity.Exam;
import kotobase_backend.modules.exam.entity.ExamAttempt;
import kotobase_backend.modules.exam.entity.ExamSection;
import kotobase_backend.modules.exam.mapper.ExamAttemptMapper;
import kotobase_backend.modules.exam.repository.ExamAttemptRepository;
import kotobase_backend.modules.exam.repository.ExamSectionRepository;
import kotobase_backend.modules.exam.service.ExamAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamAttemptServiceImpl implements ExamAttemptService {

    private final ExamAttemptRepository examAttemptRepository;
    private final ExamSectionRepository examSectionRepository;
    private final ExamAttemptMapper examAttemptMapper;

    @Override
    public SectionResponse getSectionById(Long attemptId, Long sectionId, Integer currentUserId) {
        ExamAttempt attempt = examAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dữ liệu lượt thi"));

        if (!attempt.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("Bạn không có quyền xem bài thi này.");
        }
        if (attempt.getStatus() == AttemptStatus.submitted) {
            throw new RuntimeException("Bài thi này đã được nộp");
        }
        if (attempt.getStatus() == AttemptStatus.abandoned) {
            throw new RuntimeException("Bài thi này đã bị hủy");
        }

        ExamSection examSection = examSectionRepository.getExamSectionBySectionId(sectionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phần thi"));

        return examAttemptMapper.toExamSectionResponse(examSection);
    }
}
