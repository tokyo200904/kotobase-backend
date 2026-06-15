package kotobase_backend.modules.progress.dto.response;

import kotobase_backend.comom.enums.ItemType;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class StationAdminResponse {
    private Integer id;
    private Integer levelId;
    private String levelName;
    private ItemType itemType;
    private Integer stationOrder;
    private String title;
    private String description;
    private Integer totalItems;
    private List<Integer> targetItemIds;
}