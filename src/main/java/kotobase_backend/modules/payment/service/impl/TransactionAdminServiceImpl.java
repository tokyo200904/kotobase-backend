package kotobase_backend.modules.payment.service.impl;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.enums.TransactionStatus;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.payment.dto.response.TransactionAdminResponse;
import kotobase_backend.modules.payment.dto.response.TransactionDetailAdminResponse;
import kotobase_backend.modules.payment.entity.PaymentGatewayLog;
import kotobase_backend.modules.payment.entity.Transaction;
import kotobase_backend.modules.payment.mapper.TransactionAdminMapper;
import kotobase_backend.modules.payment.repository.PaymentGatewayLogRepository;
import kotobase_backend.modules.payment.repository.TransactionRepository;
import kotobase_backend.modules.payment.service.TransactionAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionAdminServiceImpl implements TransactionAdminService {

    private final TransactionRepository transactionRepository;
    private final PaymentGatewayLogRepository logRepository;
    private final TransactionAdminMapper transactionMapper;

    @Override
    public PageResponse<TransactionAdminResponse> getAllTransactions(String search, TransactionStatus status, Pageable pageable) {
        Page<Transaction> page = transactionRepository.adminSearchTransactions(search, status, pageable);
        return PageResponse.of(page.map(transactionMapper::toSummaryResponse));
    }

    @Override
    public TransactionDetailAdminResponse getTransactionDetail(String transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã giao dịch: " + transactionId));

        List<PaymentGatewayLog> logs = logRepository.findByTransaction_IdOrderByCreatedAtDesc(transactionId);

        return transactionMapper.toDetailResponse(transaction, logs);
    }
}