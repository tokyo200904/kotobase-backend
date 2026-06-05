package kotobase_backend.modules.progress.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SavedItemResponse {
    private Integer progressId;
    private Integer targetItemId;
    private String itemType;
    private String meaning;

    private String kanjiCharacter;

    private String vocabularyWord;

    private Integer memoryLevel;
    private String status;
    private LocalDateTime nextReviewDate;
}