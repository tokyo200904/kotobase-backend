package kotobase_backend.modules.exam;

import kotobase_backend.modules.exam.dto.response.ExamResult;
import kotobase_backend.modules.exam.service.ExamResultService;
import kotobase_backend.security.userdetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/exam/result")
@RequiredArgsConstructor
public class ExamResultController {
    private final ExamResultService examResultService;

    @GetMapping("/{attemptId}")
    public ResponseEntity<ExamResult> getExamResult(@PathVariable Long attemptId,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        return ResponseEntity.ok(examResultService.getExamResult(attemptId, userId));
    }
}
