package kotobase_backend.security.oauth2;

import jakarta.transaction.Transactional;
import kotobase_backend.comom.enums.AuthProvider;
import kotobase_backend.comom.enums.RoleName;
import kotobase_backend.comom.exceptions.CustomException.OAuth2AuthenticationProcessingException;
import kotobase_backend.modules.user.entity.Role;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.modules.user.repository.RoleRepository;
import kotobase_backend.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String googleId = (String) attributes.get("sub");
        String email    = (String) attributes.get("email");
        String name     = (String) attributes.get("name");
        String picture  = (String) attributes.get("picture");
        Boolean emailVerified = (Boolean) attributes.get("email_verified");

        if (email == null || email.isBlank()) {
            throw new OAuth2AuthenticationException("không tim thay email từ google");
        }
        if (!Boolean.TRUE.equals(emailVerified)) {
            throw new OAuth2AuthenticationException("Email Google chưa được xác thực");
        }

        log.info("Google login Email: {}, Name: {}", email, name);

        User user = userRepository.findByEmail(email)
                .map(existingUser -> updateExistingUser(existingUser,
                        googleId,
                        name,
                        picture))
                .orElseGet(() -> createNewGoogleUser(
                        googleId,
                        email,
                        name,
                        picture
                ));

        if (Boolean.FALSE.equals(user.getIsEnabled())) {
            throw new OAuth2AuthenticationException("Tài khoản đã bị khóa");
        }

        log.info("lưu tài khoản với google thành công: id={}, email={}", user.getId(), user.getEmail());

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRoleName().getRole()
                        )
                ),
                attributes,
                "email"
        );
    }

    private User updateExistingUser(User existingUser, String googleId, String fullName, String picture) {


        if (existingUser.getGoogleId() == null) {
            existingUser.setGoogleId(googleId);
        }

        if (existingUser.getPhoto() == null || existingUser.getPhoto().isBlank()) {
            existingUser.setPhoto(picture);
        }

        if (existingUser.getFullName() == null || existingUser.getFullName().isBlank()) {
            existingUser.setFullName(fullName);
        }

        return userRepository.save(existingUser);
    }


    private User createNewGoogleUser(String googleId, String email, String fullName, String picture) {

        Role userRole = roleRepository.findByRole(RoleName.USER)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy role USER"
                        )
                );

        User newUser = User.builder()
                .googleId(googleId)
                .email(email)
                .fullName(fullName)
                .photo(picture != null ? picture : "default-avatar.png")
                .provider(AuthProvider.GOOGLE)
                .roleName(userRole)
                .isEnabled(true)
                .build();
        return userRepository.save(newUser);
    }
}