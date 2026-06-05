package kotobase_backend.modules.progress.dto.response;

import kotobase_backend.comom.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationItemsResponse {
    private ItemType itemType;
    private List<?> items;
}
