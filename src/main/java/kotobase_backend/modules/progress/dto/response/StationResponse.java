package kotobase_backend.modules.progress.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StationResponse {
    private Integer id;
    private Integer stationOrder;
    private String title;
    private String description;
    private int totalItems;
    private String status;
}
