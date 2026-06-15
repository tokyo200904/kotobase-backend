package kotobase_backend.modules.user.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.user.dto.request.ManualPremiumRequest;
import kotobase_backend.modules.user.dto.response.UserAdminResponse;
import kotobase_backend.modules.user.dto.response.UserDetailAdminResponse;
import org.springframework.data.domain.Pageable;

public interface UserAdminService {
    PageResponse<UserAdminResponse> getAllUsers(String search, Boolean isEnabled, Boolean isPremium, Pageable pageable);
    UserDetailAdminResponse getUserDetail(Integer userId);
    UserAdminResponse toggleUserStatus(Integer userId);
    UserAdminResponse grantManualPremium(Integer userId, ManualPremiumRequest request);
}