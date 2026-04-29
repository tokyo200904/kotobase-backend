package kotobase_backend.modules.vocab.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVocabResponse<T> {
    private List<T> data;
    private int page;
    private int limit;
    private int totalPages;
    private long totalElements;
}
