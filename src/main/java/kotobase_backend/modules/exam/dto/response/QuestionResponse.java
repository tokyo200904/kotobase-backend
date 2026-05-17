package kotobase_backend.modules.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private String content;
    private BigDecimal points;
    private Integer displayOrder;
    private String audioUrl;
    private String imageUrl;
    List<AnswerResponse> answers;
}
