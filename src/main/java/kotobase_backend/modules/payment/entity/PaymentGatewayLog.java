package kotobase_backend.modules.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_gateway_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentGatewayLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Transaction transaction;

    @Column(name = "gateway_trans_id")
    private String gatewayTransId;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "result_code")
    private String resultCode;

    @Column(name = "raw_payload", columnDefinition = "TEXT")
    private String rawPayload;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}