package kotobase_backend.modules.progress.dto.request;

import kotobase_backend.comom.enums.ItemType;
import lombok.Data;
import java.util.List;

@Data
public class StationAdminRequest {
    private Integer levelId;
    private ItemType itemType;
    private Integer stationOrder;
    private String title;
    private String description;
    private List<Integer> targetItemIds;
}