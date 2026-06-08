package kotobase_backend.modules.payment.service.impl;

import kotobase_backend.modules.payment.repository.UserSubscriptionRepository;
import kotobase_backend.modules.payment.service.PremiumGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PremiumGuardServiceImpl implements PremiumGuardService {

    private final UserSubscriptionRepository subscriptionRepository;

    public boolean check(Integer userId) {
        return subscriptionRepository.isUserPremium(userId);
    }

    public void enforcePremium(Integer userId, String message) {
        if (!check(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
        }
    }
}