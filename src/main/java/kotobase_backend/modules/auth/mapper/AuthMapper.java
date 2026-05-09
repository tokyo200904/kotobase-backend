package kotobase_backend.modules.auth.mapper;

import kotobase_backend.modules.auth.dto.response.UserInfoResponse;
import kotobase_backend.modules.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public UserInfoResponse mapToUserInfoResponse(User user) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setId(user.getId());
        userInfoResponse.setEmail(user.getEmail());
        userInfoResponse.setFullName(user.getFullName());
        userInfoResponse.setRole(user.getRoleName().getRole());
        userInfoResponse.setPhoto(user.getPhoto());
        return userInfoResponse;
    }
}
