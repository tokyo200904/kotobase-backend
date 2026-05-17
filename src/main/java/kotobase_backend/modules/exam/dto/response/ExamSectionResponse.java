package kotobase_backend.modules.exam.dto.response;

import kotobase_backend.comom.enums.SectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSectionResponse {
    private Long id;
    private String name;
    private int durationMinutes;
    private SectionType sectionType;
    private int totalQuestions;
    private BigDecimal maxScore;
    private int minPassingScore;
    private int displayOrder;

}
