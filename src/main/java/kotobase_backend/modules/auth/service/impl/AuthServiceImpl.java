package kotobase_backend.modules.auth.service.impl;

import kotobase_backend.comom.enums.RoleName;
import kotobase_backend.modules.auth.dto.request.LoginRequest;
import kotobase_backend.modules.auth.dto.request.RegisterRequest;
import kotobase_backend.modules.auth.dto.response.AuthResponse;
import kotobase_backend.modules.auth.dto.response.UserInfoResponse;
import kotobase_backend.modules.auth.mapper.AuthMapper;
import kotobase_backend.modules.auth.service.AuthService;
import kotobase_backend.modules.user.entity.Role;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.modules.user.repository.RoleRepository;
import kotobase_backend.modules.user.repository.UserRepository;
import kotobase_backend.security.jwt.JwtService;
import kotobase_backend.security.userdetail.CustomUserDetails;
import kotobase_backend.security.userdetail.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthMapper authMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("email không tồn tại"));
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtService.genarateToken(userDetails);

            return AuthResponse.builder()
                    .token(token)
                    .userInfo(authMapper.mapToUserInfoResponse(user))
                    .build();

        }
        catch (DisabledException e) {
            throw new RuntimeException("tài khoản đả bị ban");
        }
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        boolean exists = userRepository.existsByEmail(request.getEmail());
        if (exists) {
            throw new BadCredentialsException("email đã tồn tại");
        }
        Role roleUser = roleRepository.findByRole(RoleName.user)
                .orElseThrow(() -> new RuntimeException("khong tim thay role"));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullname());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoleName(roleUser);

        User savedUser = userRepository.save(user);

        CustomUserDetails customUserDetails = new CustomUserDetails(savedUser);

        String token = jwtService.genarateToken(customUserDetails);

        return AuthResponse.builder()
                .token(token)
                .userInfo(authMapper.mapToUserInfoResponse(savedUser))
                .build();
    }
}
