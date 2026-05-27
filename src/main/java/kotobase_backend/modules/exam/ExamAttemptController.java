package kotobase_backend.modules.exam;

import kotobase_backend.modules.exam.dto.response.ExamResumeState;
import kotobase_backend.modules.exam.dto.response.ExamStartResponse;
import kotobase_backend.modules.exam.dto.response.SectionResponse;
import kotobase_backend.modules.exam.dto.response.SectionSubmitResponse;
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
        return ResponseEntity.ok(examAttemptService.getSectionDetail(attemptId, sectionId, userId));
    }

    @PostMapping("/{examId}/start")
    ResponseEntity<ExamStartResponse> startExam(@PathVariable Long examId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        return ResponseEntity.ok(examAttemptService.startOrResumeExam(userId, examId));
    }

    @PostMapping("/{attemptId}/sections/{sectionId}/submit")
    public ResponseEntity<SectionSubmitResponse> submitSection(@PathVariable Long attemptId,
                                                               @PathVariable Long sectionId,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails){
        Integer userId = userDetails.getUserId();
        return ResponseEntity.ok(examAttemptService.submitSection( sectionId, attemptId, userId));
    }

    @GetMapping("/{attemptId}/resume")
    public ResponseEntity<ExamResumeState> resumeExamF5(@PathVariable Long attemptId,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        Integer userId = userDetails.getUserId();
        return ResponseEntity.ok(examAttemptService.getExamResumeState(attemptId, userId));
    }

}
