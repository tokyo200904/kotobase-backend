package kotobase_backend.modules.practice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PracticeQuestionResponse {
    private Long id;
    private String content;
    private String explanation;
    private List<PracticeAnswerResponse> answers;
}