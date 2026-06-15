package kotobase_backend.modules.user.service.Impl;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.enums.SubscriptionStatus;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.payment.dto.response.TransactionAdminResponse;
import kotobase_backend.modules.payment.entity.SubscriptionPlan;
import kotobase_backend.modules.payment.entity.UserSubscription;
import kotobase_backend.modules.payment.mapper.TransactionAdminMapper;
import kotobase_backend.modules.payment.repository.SubscriptionPlanRepository;
import kotobase_backend.modules.payment.repository.TransactionRepository;
import kotobase_backend.modules.payment.repository.UserSubscriptionRepository;
import kotobase_backend.modules.user.dto.request.ManualPremiumRequest;
import kotobase_backend.modules.user.dto.response.UserAdminResponse;
import kotobase_backend.modules.user.dto.response.UserDetailAdminResponse;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.modules.user.mapper.UserAdminMapper;
import kotobase_backend.modules.user.repository.UserRepository;
import kotobase_backend.modules.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {

    private final UserRepository userRepository;
    private final UserSubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final TransactionRepository transactionRepository;
    private final UserAdminMapper userMapper;
    private final TransactionAdminMapper transactionMapper;

    @Override
    public PageResponse<UserAdminResponse> getAllUsers(String search, Boolean isEnabled, Boolean isPremium, Pageable pageable) {
        Page<User> users = userRepository.adminSearchUsers(search, isEnabled, isPremium, pageable);
        return PageResponse.of(users.map(userMapper::toResponse));
    }

    @Override
    public UserDetailAdminResponse getUserDetail(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        UserAdminResponse userInfo = userMapper.toResponse(user);

        List<TransactionAdminResponse> history = transactionRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(transactionMapper::toSummaryResponse).toList();

        return UserDetailAdminResponse.builder()
                .userInfo(userInfo)
                .paymentHistory(history)
                .build();
    }

    @Override
    @Transactional
    public UserAdminResponse toggleUserStatus(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
        user.setIsEnabled(!user.getIsEnabled());
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public UserAdminResponse grantManualPremium(Integer userId, ManualPremiumRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));

        SubscriptionPlan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy gói cước"));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now;

        UserSubscription currentActive = subscriptionRepository.findFirstByUser_IdAndStatus(userId, SubscriptionStatus.ACTIVE).orElse(null);

        if (currentActive != null && currentActive.getEndDate().isAfter(now)) {
            startDate = currentActive.getEndDate();
        }

        LocalDateTime endDate = startDate.plusDays(plan.getDurationDays());
        UserSubscription newSubscription = UserSubscription.builder()
                .user(user)
                .plan(plan)
                .startDate(startDate)
                .endDate(endDate)
                .status(SubscriptionStatus.ACTIVE)
                .build();

        subscriptionRepository.save(newSubscription);
        return userMapper.toResponse(user);
    }
}