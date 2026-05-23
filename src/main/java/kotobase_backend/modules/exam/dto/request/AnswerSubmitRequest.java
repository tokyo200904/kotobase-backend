package kotobase_backend.modules.exam.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerSubmitRequest {
    @NotNull(message = "Question ID không được để trống")
    private Long questionId;
    private Long selectedAnswerId;
}
