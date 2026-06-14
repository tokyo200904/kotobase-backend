package kotobase_backend.modules.payment.dto.response;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TransactionDetailAdminResponse {
    private TransactionAdminResponse transactionInfo;
    private List<GatewayLogResponse> logs;

    @Data
    @Builder
    public static class GatewayLogResponse {
        private String resultCode;
        private String gatewayTransId;
        private String rawPayload;
        private String createdAt;
    }
}