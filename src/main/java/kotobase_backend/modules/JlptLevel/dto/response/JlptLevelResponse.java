package kotobase_backend.modules.JlptLevel.dto.response;

import kotobase_backend.comom.enums.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JlptLevelResponse {
    private Integer id;
    private Level level;

}
