package kotobase_backend.modules.practice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PracticeAnswerResponse {
    private Long id;
    private String content;
    private Boolean isCorrect;
    private Integer correctOrder;
}