package kotobase_backend.modules.auth;

import jakarta.validation.Valid;
import kotobase_backend.modules.auth.dto.request.LoginRequest;
import kotobase_backend.modules.auth.dto.request.RegisterRequest;
import kotobase_backend.modules.auth.dto.response.AuthResponse;
import kotobase_backend.modules.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }
}
