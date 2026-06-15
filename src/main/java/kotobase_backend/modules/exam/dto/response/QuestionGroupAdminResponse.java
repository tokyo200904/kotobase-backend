package kotobase_backend.modules.exam.dto.response;

import kotobase_backend.comom.enums.GroupType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class QuestionGroupAdminResponse {
    private Long id;
    private GroupType groupType;
    private String content;
    private int displayOrder;
    private Long audioId;
    private String audioUrl;
    private Long imageId;
    private String imageUrl;
    private List<QuestionAdminResponse> questions;

    @Data
    @Builder
    public static class QuestionAdminResponse {
        private Long id;
        private String content;
        private BigDecimal point;
        private int displayOrder;
        private Long audioId;
        private String audioUrl;
        private Long imageId;
        private String imageUrl;
        private List<AnswerAdminResponse> answers;
    }

    @Data
    @Builder
    public static class AnswerAdminResponse {
        private Long id;
        private String content;
        private Boolean isCorrect;
        private int displayOrder;
        private String explanation;
    }
}