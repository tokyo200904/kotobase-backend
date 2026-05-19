package kotobase_backend.modules.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionSubmitResponse {
    private boolean isExamFinished;
    private Long nextSectionId;
}
