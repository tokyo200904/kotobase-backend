package kotobase_backend.modules.exam;

import jakarta.validation.Valid;
import kotobase_backend.modules.exam.dto.request.ExamRequest;
import kotobase_backend.modules.exam.dto.response.ExamDetailResponse;
import kotobase_backend.modules.exam.dto.response.ExamResponse;
import kotobase_backend.modules.exam.dto.response.PageExamResponse;
import kotobase_backend.modules.exam.service.ExamQueryService;
import kotobase_backend.security.userdetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {
    private final ExamQueryService examService;

    @GetMapping("/by_level")
    public ResponseEntity<PageExamResponse<ExamResponse>> getExamByLevel(@Valid @ModelAttribute ExamRequest request,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = (userDetails != null) ? userDetails.getUserId() : null;
        return ResponseEntity.ok(examService.getExamByLevel(request,userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamDetailResponse> getExamById(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getDetailExamById(id));
    }

}
