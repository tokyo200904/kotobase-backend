package kotobase_backend.modules.exam;

import kotobase_backend.modules.exam.dto.response.SectionResponse;
import kotobase_backend.modules.exam.service.ExamAttemptService;
import kotobase_backend.security.userdetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/exam/attempts")
@RequiredArgsConstructor
public class ExamAttemptController {
    private final ExamAttemptService examAttemptService;

    @GetMapping("/{attemptId}/sections/{sectionId}")
    public ResponseEntity<SectionResponse> getSectionDetail(@PathVariable Long attemptId,
                                                            @PathVariable Long sectionId,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        return ResponseEntity.ok(examAttemptService.getSectionById(attemptId, sectionId, userId));
    }
}
