package kotobase_backend.modules.kanji.dto.Response;

import kotobase_backend.comom.enums.KanjiType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class KanjisResponse {
    private Integer id;
    private String characters;
    private Boolean isLocked;
}
