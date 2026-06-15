package kotobase_backend.modules.user.mapper;

import kotobase_backend.modules.payment.repository.UserSubscriptionRepository;
import kotobase_backend.modules.user.dto.response.UserAdminResponse;
import kotobase_backend.modules.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserAdminMapper {

    private final UserSubscriptionRepository subscriptionRepository;

    public UserAdminResponse toResponse(User user) {
        boolean isVip = subscriptionRepository.existsByUserIdAndStatusAndEndDateAfter(
                user.getId(),
                kotobase_backend.comom.enums.SubscriptionStatus.ACTIVE,
                LocalDateTime.now()
        );

        return UserAdminResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .provider(user.getProvider().name())
                .isEnabled(user.getIsEnabled())
                .isPremium(isVip)
                .createdAt(user.getCreatedAt())
                .build();
    }
}