package kotobase_backend.modules.grammar.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class GrammarAdminRequest {
    private String title;
    private String structure;
    private String meaning;
    private String usages;
    private String note;
    private Integer levelId;
    private Integer lessonId;
    private List<ExampleGrammarRequest> examples;
    private List<GrammarExerciseRequest> exercises;

    @Data
    public static class ExampleGrammarRequest {
        private Integer id;
        private String content;
        private String meaning;
        private Integer displayOrder;
    }

    @Data
    public static class GrammarExerciseRequest {
        private Integer id;
        private String questionText;
        private String brokenChunks;
        private String explanation;
    }
}