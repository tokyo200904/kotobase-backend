package kotobase_backend.modules.exam.dto.response;

import kotobase_backend.comom.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamDetailResponse {
    private Long id;
    private String title;
    private Level level;
    private int durationMinutes;
    private int passingScore;
    private BigDecimal maxScore;
    private Integer totalQuestions;
    List<ExamSectionResponse> sections;
}
