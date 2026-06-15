package kotobase_backend.modules.user;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.user.dto.request.ManualPremiumRequest;
import kotobase_backend.modules.user.dto.response.UserAdminResponse;
import kotobase_backend.modules.user.dto.response.UserDetailAdminResponse;
import kotobase_backend.modules.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/user")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService userAdminService;

    @GetMapping
    public ResponseEntity<PageResponse<UserAdminResponse>> getAllUsers(@RequestParam(required = false) String search,
                                                                       @RequestParam(required = false) Boolean isEnabled,
                                                                       @RequestParam(required = false) Boolean isPremium,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "15") int size) {
        return ResponseEntity.ok(userAdminService.getAllUsers(search, isEnabled, isPremium, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailAdminResponse> getUserDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(userAdminService.getUserDetail(id));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<UserAdminResponse> toggleUserStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(userAdminService.toggleUserStatus(id));
    }

    @PostMapping("/{id}/grant-premium")
    public ResponseEntity<UserAdminResponse> grantManualPremium(
            @PathVariable Integer id,
            @RequestBody ManualPremiumRequest request) {
        return ResponseEntity.ok(userAdminService.grantManualPremium(id, request));
    }
}