package kotobase_backend.modules.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionPlan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "duration_days")
    private Integer durationDays;

    private BigDecimal price;

    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}