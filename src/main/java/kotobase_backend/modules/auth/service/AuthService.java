package kotobase_backend.modules.auth.service;

import kotobase_backend.modules.auth.dto.request.LoginRequest;
import kotobase_backend.modules.auth.dto.response.AuthResponse;

public interface AuthService {
    public AuthResponse login(LoginRequest request);
}
