package kotobase_backend.modules.kanji.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class KanjiReadingResponse {
    private String reading;
    private String romaji;

}
