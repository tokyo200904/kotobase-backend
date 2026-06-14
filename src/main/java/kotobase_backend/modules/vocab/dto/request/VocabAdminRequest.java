package kotobase_backend.modules.vocab.dto.request;
import lombok.Data;
import java.util.List;

@Data
public class VocabAdminRequest {
    private String word;
    private String romaji;
    private String meaning;
    private String reading;
    private Integer levelId;
    private Integer topicId;
    private List<ExampleVocabRequest> examples;

    @Data
    public static class ExampleVocabRequest {
        private Integer id;
        private String content;
        private String meaning;
        private Integer displayOrder;
    }
}
