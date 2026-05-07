package kotobase_backend.modules.exam.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageExamResponse<T> {
    private List<T> data;
    private int page;
    private int limit;
    private int totalPages;
    private long totalElements;
}
