package kotobase_backend.modules.vocab.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VocabCompactResponse {
    private Integer id;
    private String word;
    private String meaning;
}