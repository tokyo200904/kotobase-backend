package kotobase_backend.modules.grammar.dto.response;

import kotobase_backend.modules.examples.dto.response.ExampleResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<ExampleResponse> examples;
}
