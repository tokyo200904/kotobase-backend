package kotobase_backend.modules.practice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardKanjiResponse {
    private Integer id;
    private String character;
    private String meaning;
    private String han;
}
