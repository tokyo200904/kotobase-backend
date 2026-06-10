package kotobase_backend.modules.practice.dto.response;

import kotobase_backend.modules.examples.dto.response.ExampleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardVocabResponse {
    private Integer id;
    private String reading;
    private String meaning;
    private List<ExampleResponse> examples;
}
