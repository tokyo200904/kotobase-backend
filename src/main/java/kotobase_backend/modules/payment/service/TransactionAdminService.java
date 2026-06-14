package kotobase_backend.modules.payment.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.enums.TransactionStatus;
import kotobase_backend.modules.payment.dto.response.TransactionAdminResponse;
import kotobase_backend.modules.payment.dto.response.TransactionDetailAdminResponse;
import org.springframework.data.domain.Pageable;

public interface TransactionAdminService {
    PageResponse<TransactionAdminResponse> getAllTransactions(String search, TransactionStatus status, Pageable pageable);
    TransactionDetailAdminResponse getTransactionDetail(String transactionId);
}
