package kotobase_backend.modules.exam.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ExamResult {
    private Long attemptId;
    private Long examId;
    private String title;
    private Long timeFinished;
    private BigDecimal totalScore;
    private BigDecimal maxScore;
    private Boolean isPassed;
    List<SectionResult> sections;
}
