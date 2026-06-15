package kotobase_backend.modules.exam.dto.response;

import kotobase_backend.comom.enums.SectionType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ExamAdminResponse {
    private long id;
    private String title;
    private Integer totalQuestions;
    private BigDecimal maxScore;
    private int durationMinutes;
    private int passingScore;
    private Boolean isPublished;
    private LocalDateTime createdAt;
    private Integer levelId;
    private String levelName;
    private List<ExamSectionResponse> sections;

    @Data
    @Builder
    public static class ExamSectionResponse {
        private Long id;
        private String sectionName;
        private SectionType sectionType;
        private int durationMinutes;
        private int minPassingScore;
        private int totalQuestions;
        private BigDecimal maxScore;
        private int displayOrder;
    }
}