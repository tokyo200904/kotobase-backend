package kotobase_backend.modules.kanji.dto.Request;

import kotobase_backend.comom.enums.KanjiType;
import lombok.Data;
import java.util.List;
@Data
public class KanjiAdminRequest {
    private String characters;
    private String meaning;
    private Integer strokeCount;
    private String han;
    private Integer levelId;
    private List<ReadingDetail> onReadings;
    private List<ReadingDetail> kunReadings;
    private List<ExampleRequest> examples;

    @Data
    public static class ReadingDetail {
        private Integer id;
        private String reading;
        private String romaji;
    }

    @Data
    public static class ExampleRequest {
        private Integer id;
        private String content;
        private String meaning;
        private Integer displayOrder;
        private Integer audioId;
    }
}