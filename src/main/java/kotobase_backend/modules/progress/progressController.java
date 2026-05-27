package kotobase_backend.modules.progress;

import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.progress.dto.request.ItemRequest;
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
}
