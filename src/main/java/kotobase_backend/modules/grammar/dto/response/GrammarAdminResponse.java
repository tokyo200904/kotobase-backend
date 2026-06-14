package kotobase_backend.modules.grammar.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class GrammarAdminResponse {
    private Integer id;
    private String title;
    private String structure;
    private String meaning;
    private String usages;
    private String note;
    private Integer levelId;
    private String levelName;
    private Integer lessonId;
    private String lessonTitle;
    private List<ExampleGrammarResponse> examples;
    private List<GrammarExerciseResponse> exercises;

    @Data
    @Builder
    public static class ExampleGrammarResponse {
        private Integer id;
        private String content;
        private String meaning;
        private Integer displayOrder;
    }

    @Data
    @Builder
    public static class GrammarExerciseResponse {
        private Integer id;
        private String questionText;
        private String brokenChunks;
        private String explanation;
    }
}