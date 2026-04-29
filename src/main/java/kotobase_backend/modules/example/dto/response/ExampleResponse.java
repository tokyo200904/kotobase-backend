package kotobase_backend.modules.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleResponse {
    private Integer id;
    private String content;
    private String meaning;
    private String romaji;
}
