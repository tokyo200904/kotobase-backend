package kotobase_backend.modules.payment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationDays;
}