package kotobase_backend.modules.payment.mapper;

import kotobase_backend.modules.payment.dto.response.TransactionAdminResponse;
import kotobase_backend.modules.payment.dto.response.TransactionDetailAdminResponse;
import kotobase_backend.modules.payment.entity.PaymentGatewayLog;
import kotobase_backend.modules.payment.entity.Transaction;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TransactionAdminMapper {

    public TransactionAdminResponse toSummaryResponse(Transaction transaction) {
        return TransactionAdminResponse.builder()
                .id(transaction.getId())
                .userEmail(transaction.getUser() != null ? transaction.getUser().getEmail() : "N/A")
                .userFullName(transaction.getUser() != null ? transaction.getUser().getFullName() : "N/A")
                .planName(transaction.getPlan() != null ? transaction.getPlan().getName() : "N/A")
                .amount(transaction.getAmount())
                .gatewayTransId(transaction.getGatewayTransId())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    public TransactionDetailAdminResponse toDetailResponse(Transaction transaction, List<PaymentGatewayLog> logs) {
        List<TransactionDetailAdminResponse.GatewayLogResponse> logResponses = logs.stream()
                .map(log -> TransactionDetailAdminResponse.GatewayLogResponse.builder()
                        .resultCode(log.getResultCode())
                        .gatewayTransId(log.getGatewayTransId())
                        .rawPayload(log.getRawPayload())
                        .createdAt(log.getCreatedAt() != null ?
                                log.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : null)
                        .build())
                .toList();

        return TransactionDetailAdminResponse.builder()
                .transactionInfo(toSummaryResponse(transaction))
                .logs(logResponses)
                .build();
    }
}