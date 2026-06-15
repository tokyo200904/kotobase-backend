package kotobase_backend.modules.exam.dto.request;
import kotobase_backend.comom.enums.SectionType;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ExamAdminRequest {
    private String title;
    private Integer totalQuestions;
    private BigDecimal maxScore;
    private int durationMinutes;
    private int passingScore;
    private Boolean isPublished;
    private Integer levelId;

    private List<ExamSectionRequest> sections;

    @Data
    public static class ExamSectionRequest {
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