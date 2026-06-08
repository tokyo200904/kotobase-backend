package kotobase_backend.modules.payment.service.scoring;

import kotobase_backend.comom.enums.SubscriptionStatus;
import kotobase_backend.modules.payment.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionCleanupTask {

    private final UserSubscriptionRepository subscriptionRepository;

    @Scheduled(cron = "0 1 0 * * *")
    @Transactional
    public void expireSubscriptions() {
        log.info("--- [CRON JOB] Bắt đầu quét các thẻ Premium hết hạn ---");

        LocalDateTime now = LocalDateTime.now();

        try {
            int expiredCount = subscriptionRepository.expireOldSubscriptions(
                    SubscriptionStatus.ACTIVE,
                    SubscriptionStatus.EXPIRED,
                    now
            );

            if (expiredCount > 0) {
                log.info("--- [CRON JOB] Đã thu hồi quyền Premium của {} người dùng ---", expiredCount);
            } else {
                log.info("--- [CRON JOB] Không có thẻ nào hết hạn hôm nay ---");
            }
        } catch (Exception e) {
            log.error("--- [CRON JOB] Lỗi khi thu hồi thẻ Premium: ", e);
        }
    }
}