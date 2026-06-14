package kotobase_backend.modules.kanji.dto.Response;

import kotobase_backend.comom.enums.KanjiType;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class KanjiAdminResponse {
    private Integer id;
    private String characters;
    private String meaning;
    private Integer strokeCount;
    private String han;
    private Integer levelId;
    private String levelName;
    private List<ReadingDetailResponse> onReadings;
    private List<ReadingDetailResponse> kunReadings;
    private List<ExampleResponse> examples;

    @Data
    @Builder
    public static class ReadingDetailResponse {
        private Integer id;
        private String reading;
        private String romaji;
    }

    @Data
    @Builder
    public static class ExampleResponse {
        private Integer id;
        private String content;
        private String meaning;
        private Integer displayOrder;
    }
}