package kotobase_backend.modules.payment.dto.response;

import kotobase_backend.comom.enums.TransactionStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionAdminResponse {
    private String id;
    private String userEmail;
    private String userFullName;
    private String planName;
    private BigDecimal amount;
    private String gatewayTransId;
    private TransactionStatus status;
    private LocalDateTime createdAt;
}