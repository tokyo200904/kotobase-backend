package kotobase_backend.modules.progress.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class SubmitTestRequest {
    private Integer timeSpentSeconds;
    private List<AnswerRequest> answers;

    @Data
    public static class AnswerRequest {
        private Integer questionItemId;
        private Integer selectedItemId;
    }
}