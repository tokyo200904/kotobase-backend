package kotobase_backend.modules.exam.mapper;

import kotobase_backend.modules.exam.dto.response.ExamResponse;
import kotobase_backend.modules.exam.entity.Exam;
import org.springframework.stereotype.Component;

@Component
public class ExamMapper {
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
}
