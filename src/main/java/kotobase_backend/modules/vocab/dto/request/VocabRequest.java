package kotobase_backend.modules.vocab.dto.request;

import lombok.Data;

@Data
public class VocabRequest {
    private int page = 1;
    private int limit = 10;
    private String search = "";

}
