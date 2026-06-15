package kotobase_backend.modules.exam.dto.request;

import kotobase_backend.comom.enums.GroupType;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class QuestionGroupAdminRequest {
    private GroupType groupType;
    private String content;
    private int displayOrder;
    private Integer audioId;
    private Long imageId;
    private List<QuestionAdminRequest> questions;

    @Data
    public static class QuestionAdminRequest {
        private Long id;
        private String content;
        private BigDecimal point;
        private int displayOrder;
        private Integer audioId;
        private Long imageId;
        private List<AnswerAdminRequest> answers;
    }

    @Data
    public static class AnswerAdminRequest {
        private Long id;
        private String content;
        private Boolean isCorrect;
        private int displayOrder;
        private String explanation;
    }
}