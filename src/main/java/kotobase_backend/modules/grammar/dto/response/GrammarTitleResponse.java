package kotobase_backend.modules.grammar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammarTitleResponse {
    private Integer id;
    private String title;
}
