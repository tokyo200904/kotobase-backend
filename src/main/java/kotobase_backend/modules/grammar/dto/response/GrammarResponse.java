package kotobase_backend.modules.grammar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrammarResponse {
    private Integer id;
    private String title;
    private String structure;
    private String meaning;
    private String usages;
    private String note;
}
