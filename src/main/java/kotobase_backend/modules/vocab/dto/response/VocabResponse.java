package kotobase_backend.modules.vocab.dto.response;

import kotobase_backend.modules.examples.dto.response.ExampleResponse;
import lombok.Data;

import java.util.List;

@Data
public class VocabResponse {
    private Integer id;
    private String word;
    private String reading;
    private String meaning;
    private String romaji;
    private List<ExampleResponse> examples;
}
