package kotobase_backend.modules.kanji.dto.Response;

import kotobase_backend.comom.enums.KanjiType;
import kotobase_backend.comom.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KanjiDetelResponse {
    private Integer id;
    private String characters;
    private String meaning;
    private Integer strokeCount;
    private List<KanjiReadingResponse> on;
    private List<KanjiReadingResponse> kun;
    private Level level;
}
