package kotobase_backend.modules.progress.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class BulkReorderRequest {

    private List<ReorderItem> items;

    @Data
    public static class ReorderItem {
        private Integer id;
        private Integer newOrder;
    }
}