package kotobase_backend.modules.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamWebSocketPayload {
    private String action;
    private Long nextSectionId;
    private String message;
}
