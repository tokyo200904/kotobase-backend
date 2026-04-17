package kotobase_backend.modules.vocab.dto.response;

import lombok.Data;

@Data
public class VocabResponse {
    private long id;
    private String kanji;
    private String reading;
    private String meaning;
    private String example;

}
