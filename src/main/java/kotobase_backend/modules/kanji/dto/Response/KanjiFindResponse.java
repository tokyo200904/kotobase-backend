package kotobase_backend.modules.kanji.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class KanjiFindResponse {
    private Integer id;
    private String characters;
    private String meaning;
    private String han;
}
