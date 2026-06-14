package kotobase_backend.modules.vocab.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class VocabAdminResponse {
    private Integer id;
    private String word;
    private String romaji;
    private String meaning;
    private String reading;
    private Integer levelId;
    private String levelName;
    private TopicSummaryResponse topic;
    private List<ExampleVocabResponse> examples;

    @Data
    @Builder
    public static class TopicSummaryResponse {
        private Integer id;
        private String name;
        private String lessonTitle;
    }

    @Data
    @Builder
    public static class ExampleVocabResponse {
        private Integer id;
        private String content;
        private String meaning;
        private Integer displayOrder;
    }
}