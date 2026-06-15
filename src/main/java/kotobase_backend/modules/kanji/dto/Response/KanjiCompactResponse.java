package kotobase_backend.modules.kanji.dto.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KanjiCompactResponse {
    private Integer id;
    private String characters;
    private String meaning;
}