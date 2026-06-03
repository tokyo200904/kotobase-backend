package kotobase_backend.modules.progress;

import kotobase_backend.modules.progress.dto.response.DashboardStatsResponse;
import kotobase_backend.modules.progress.service.StatisticsService;
import kotobase_backend.security.userdetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statsService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsResponse> getDashboard(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        return ResponseEntity.ok(statsService.getDashboardStats(userId));
    }
}
