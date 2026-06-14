package kotobase_backend.modules.payment.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class PlanAdminResponse {
    private Integer id;
    private String name;
    private Integer durationDays;
    private BigDecimal price;
    private String description;
    private Boolean isActive;
}
