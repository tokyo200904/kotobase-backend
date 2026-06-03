package kotobase_backend.modules.progress;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.progress.dto.request.ItemRequest;
import kotobase_backend.modules.progress.dto.request.SubmitReviewRequest;
import kotobase_backend.modules.progress.service.StatisticsService;
import kotobase_backend.modules.progress.service.UserItemProgressService;
import kotobase_backend.security.userdetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/progress")
@RequiredArgsConstructor
public class progressController {
    private final UserItemProgressService userItemProgressService;
    private final StatisticsService statisticsService;

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggle(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                      @RequestBody ItemRequest itemRequest){
        Integer userId = userDetails.getUserId();
        try {
            boolean isAdd = userItemProgressService.addOrUnAdd(userId, itemRequest);
            Map<String, Object> map = new HashMap<>();
            map.put("isAdd", isAdd);
            if(isAdd){
                map.put("message", "Đã lưu vào danh sách ôn tập");
            }
            else{
                map.put("message", "Đã xóa khỏi danh sách ôn tập");
            }
            return ResponseEntity.ok(map);
        }
        catch (ResourceNotFoundException e){
            Map<String, Object> errorResp = new HashMap<>();
            errorResp.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResp);
        }
    }

    @GetMapping("/next")
    public ResponseEntity<?> getNextQuestion(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestParam ItemType type) {
        Integer userId = userDetails.getUserId();
        try {
            return ResponseEntity.ok(userItemProgressService.getNextQuestion(userId, type));
        } catch (RuntimeException e) {
            return ResponseEntity.ok().body(Map.of("message", e.getMessage(), "isDone", true));
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitAnswer(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestBody SubmitReviewRequest request) {
        Integer userId = userDetails.getUserId();

        userItemProgressService.submitAnswer(userId, request);
        return ResponseEntity.ok(Map.of("message", "Đã nộp bài thành công"));
    }

    @GetMapping("/extra-practice")
    public ResponseEntity<?> getExtraPractice(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestParam ItemType type) {
        Integer userId = userDetails.getUserId();
        try {
            return ResponseEntity.ok(userItemProgressService.getExtraPracticeQuestion(userId, type));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/extra-practice/submit")
    public ResponseEntity<?> submitExtraPractice(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody SubmitReviewRequest request) {

        Integer userId = userDetails.getUserId();

        userItemProgressService.submitExtraPracticeAnswer(userId, request);
        return ResponseEntity.ok(Map.of("message", "Đã cộng điểm luyện tập và giữ chuỗi!"));
    }
}
