package kotobase_backend.modules.payment.dto.request;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PlanAdminRequest {
    private String name;
    private Integer durationDays;
    private BigDecimal price;
    private String description;
    private Boolean isActive;
}
