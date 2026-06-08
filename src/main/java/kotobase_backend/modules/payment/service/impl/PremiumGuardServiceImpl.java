package kotobase_backend.modules.payment.service.impl;

import kotobase_backend.comom.enums.SubscriptionStatus;
import kotobase_backend.modules.payment.repository.UserSubscriptionRepository;
import kotobase_backend.modules.payment.service.PremiumGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PremiumGuardServiceImpl implements PremiumGuardService {

    private final UserSubscriptionRepository subscriptionRepository;

    public boolean check(Integer userId) {
        return subscriptionRepository
                .existsByUserIdAndStatusAndEndDateAfter(userId, SubscriptionStatus.ACTIVE, LocalDateTime.now());
    }

    public void enforcePremium(Integer userId, String message) {
        if (!check(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
        }
    }
}