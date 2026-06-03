package kotobase_backend.modules.progress.dto.request;

import lombok.Data;

@Data
public class SubmitReviewRequest {
    private Integer progressId;
    private Boolean isCorrect;
    private Integer timeSpentSeconds;
}