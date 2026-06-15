package kotobase_backend.modules.exam.mapper;

import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.JlptLevel.repository.JlptLevelRepository;
import kotobase_backend.modules.exam.dto.request.ExamAdminRequest;
import kotobase_backend.modules.exam.dto.response.ExamAdminResponse;
import kotobase_backend.modules.exam.entity.Exam;
import kotobase_backend.modules.exam.entity.ExamSection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExamAdminMapper {

    private final JlptLevelRepository levelRepository;

    public void updateEntityFromRequest(ExamAdminRequest request, Exam exam) {
        exam.setTitle(request.getTitle());
        exam.setTotalQuestions(request.getTotalQuestions());
        exam.setMaxScore(request.getMaxScore());
        exam.setDurationMinutes(request.getDurationMinutes());
        exam.setPassingScore(request.getPassingScore());

        if (request.getIsPublished() != null) {
            exam.setIsPublished(request.getIsPublished());
        } else if (exam.getId() == 0) {
            exam.setIsPublished(false);
        }

        JlptLevel level = levelRepository.findById(request.getLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Level"));
        exam.setLevel(level);

        if (exam.getSections() != null) {
            exam.getSections().clear();
        } else {
            exam.setSections(new ArrayList<>());
        }

        if (request.getSections() != null) {
            List<ExamSection> newSections = request.getSections().stream().map(req -> {
                ExamSection sec = new ExamSection();
                sec.setSectionName(req.getSectionName());
                sec.setSectionType(req.getSectionType());
                sec.setDurationMinutes(req.getDurationMinutes());
                sec.setMinPassingScore(req.getMinPassingScore());
                sec.setTotalQuestions(req.getTotalQuestions());
                sec.setMaxScore(req.getMaxScore());
                sec.setDisplayOrder(req.getDisplayOrder());
                sec.setExam(exam);
                return sec;
            }).toList();
            exam.getSections().addAll(newSections);
        }
    }

    public ExamAdminResponse toResponse(Exam exam) {
        List<ExamAdminResponse.ExamSectionResponse> sectionResponses = exam.getSections() == null ? List.of() :
                exam.getSections().stream().map(sec -> ExamAdminResponse.ExamSectionResponse.builder()
                        .id(sec.getId())
                        .sectionName(sec.getSectionName())
                        .sectionType(sec.getSectionType())
                        .durationMinutes(sec.getDurationMinutes())
                        .minPassingScore(sec.getMinPassingScore())
                        .totalQuestions(sec.getTotalQuestions())
                        .maxScore(sec.getMaxScore())
                        .displayOrder(sec.getDisplayOrder())
                        .build()
                ).toList();

        return ExamAdminResponse.builder()
                .id(exam.getId())
                .title(exam.getTitle())
                .totalQuestions(exam.getTotalQuestions())
                .maxScore(exam.getMaxScore())
                .durationMinutes(exam.getDurationMinutes())
                .passingScore(exam.getPassingScore())
                .isPublished(exam.getIsPublished())
                .createdAt(exam.getCreatedAt())
                .levelId(exam.getLevel().getId())
                .levelName(exam.getLevel().getLevel().name())
                .sections(sectionResponses)
                .build();
    }
}