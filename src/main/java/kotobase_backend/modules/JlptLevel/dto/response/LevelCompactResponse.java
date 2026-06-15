package kotobase_backend.modules.JlptLevel.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LevelCompactResponse {
    private Integer id;
    private String levelName;
}