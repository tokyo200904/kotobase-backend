package kotobase_backend.modules.exam.mapper;

import kotobase_backend.modules.exam.dto.response.ExamDetailResponse;
import kotobase_backend.modules.exam.dto.response.ExamResponse;
import kotobase_backend.modules.exam.dto.response.ExamSectionResponse;
import kotobase_backend.modules.exam.entity.Exam;
import kotobase_backend.modules.exam.entity.ExamSection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExamQueryMapper {
    public ExamResponse toExamResponse(Exam exam) {
        ExamResponse examResponse = new ExamResponse();
        examResponse.setId(exam.getId());
        examResponse.setTitle(exam.getTitle());
        examResponse.setDurationMinutes(exam.getDurationMinutes());
        examResponse.setPassingScore(exam.getPassingScore());
        examResponse.setMaxScore(exam.getMaxScore());
        examResponse.setLevel(exam.getLevel().getLevel());
        examResponse.setTotalQuestions(exam.getTotalQuestions());
        return examResponse;
    }

    public ExamDetailResponse toExamDetailResponse(Exam exam) {

        List<ExamSectionResponse> exs = exam.getSections() == null ? List.of() : exam.getSections()
                .stream()
                .map(this::toExamSectionResponse)
                .toList();

        ExamDetailResponse ex = new ExamDetailResponse();
        ex.setId(exam.getId());
        ex.setTitle(exam.getTitle());
        ex.setDurationMinutes(exam.getDurationMinutes());
        ex.setPassingScore(exam.getPassingScore());
        ex.setMaxScore(exam.getMaxScore());
        ex.setLevel(exam.getLevel().getLevel());
        ex.setTotalQuestions(exam.getTotalQuestions());
        ex.setSections(exs);
        return ex;
    }

    private ExamSectionResponse toExamSectionResponse(ExamSection examSection) {
        ExamSectionResponse ex = new ExamSectionResponse();
        ex.setId(examSection.getId());
        ex.setName(examSection.getSectionName());
        ex.setMaxScore(examSection.getMaxScore());
        ex.setTotalQuestions(examSection.getTotalQuestions());
        ex.setMinPassingScore(examSection.getMinPassingScore());
        ex.setSectionType(examSection.getSectionType());
        ex.setDisplayOrder(examSection.getDisplayOrder());
        ex.setDurationMinutes(examSection.getDurationMinutes());
        return ex;
    }
}
