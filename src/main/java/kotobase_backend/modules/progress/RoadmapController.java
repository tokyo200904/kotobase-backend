package kotobase_backend.modules.progress;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.dto.response.SubmitTestRequest;
import kotobase_backend.modules.progress.service.RoadmapService;
import kotobase_backend.security.userdetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/roadmaps")
@RequiredArgsConstructor
public class RoadmapController {
    private final RoadmapService roadmapService;

    @GetMapping
    public ResponseEntity<?> getRoadmap(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestParam Integer levelId,
                                        @RequestParam ItemType type) {

        Integer userId = (userDetails != null) ? userDetails.getUserId() : null;
        return ResponseEntity.ok(roadmapService.getRoadmap(userId, levelId, type));
    }

    @PostMapping("/stations/{stationId}/complete")
    public ResponseEntity<?> completeStation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @PathVariable Integer stationId) {
        Integer userId = (userDetails != null) ? userDetails.getUserId() : null;
        try {
            roadmapService.completeStation(userId, stationId);
            return ResponseEntity.ok(Map.of("message", "Vượt ải thành công! Đã mở khóa trạm tiếp theo và đưa từ vựng vào hệ thống ôn tập."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stations/{stationId}/items")
    public ResponseEntity<?> getStationItems(@PathVariable Integer stationId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = (userDetails != null) ? userDetails.getUserId() : null;
        return ResponseEntity.ok(roadmapService.getStationItems(stationId, userId));
    }

    @PostMapping("/stations/{stationId}/submit-test")
    public ResponseEntity<?> submitTest(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable Integer stationId,
                                        @RequestBody SubmitTestRequest request) {
        Integer userId = (userDetails != null) ? userDetails.getUserId() : null;
        return ResponseEntity.ok(roadmapService.submitAndEvaluateTest(userId, stationId, request));
    }
}
