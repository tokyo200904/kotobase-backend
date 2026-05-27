package kotobase_backend.modules.progress.dto.request;

import jakarta.validation.constraints.NotNull;
import kotobase_backend.comom.enums.ItemType;
import lombok.Data;

@Data
public class ItemRequest {

    @NotNull(message = "Id không được để trống")
    private Integer itemId;

    @NotNull(message = "loại item không được để trống")
    private ItemType itemType;
}
