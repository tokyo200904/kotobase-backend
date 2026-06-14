package kotobase_backend.comom.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> data;
    private int page;
    private int limit;
    private int totalPages;
    private long totalElements;

    public static <T> PageResponse<T> of(Page<T> springPage) {
        return PageResponse.<T>builder()
                .data(springPage.getContent())
                .page(springPage.getNumber())
                .limit(springPage.getSize())
                .totalPages(springPage.getTotalPages())
                .totalElements(springPage.getTotalElements())
                .build();
    }
}