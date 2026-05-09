package kotobase_backend.modules.auth;

import kotobase_backend.modules.auth.dto.request.LoginRequest;
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
        AuthResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

}
