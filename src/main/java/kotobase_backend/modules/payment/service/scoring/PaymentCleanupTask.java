package kotobase_backend.modules.payment.service.scoring;

import kotobase_backend.modules.payment.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCleanupTask {
    private final TransactionRepository transactionRepository;

    @Scheduled(cron = "0 0/15 * * * *")
    @Transactional
    public void cleanupAbandonedTransactions() {
        log.info("--- [CRON JOB] Bắt đầu quét đơn hàng PENDING quá hạn ---");

        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(20);

        try {
            int updatedCount = transactionRepository.cancelExpiredTransactions("PENDING", "CANCELLED", cutoffTime);

            if (updatedCount > 0) {
                log.info("--- [CRON JOB] Đã hủy thành công {} đơn hàng quá hạn ---", updatedCount);
            } else {
                log.info("--- [CRON JOB] Database sạch sẽ, không có rác ---");
            }
        } catch (Exception e) {
            log.error("--- [CRON JOB] Lỗi khi dọn dẹp đơn hàng: ", e);
        }
    }
}