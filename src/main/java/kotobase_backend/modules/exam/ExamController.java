package kotobase_backend.modules.exam;

import jakarta.validation.Valid;
import kotobase_backend.modules.exam.dto.request.ExamRequest;
import kotobase_backend.modules.exam.dto.response.ExamResponse;
import kotobase_backend.modules.exam.dto.response.PageExamResponse;
import kotobase_backend.modules.exam.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;

    @GetMapping("/by_level")
    public ResponseEntity<PageExamResponse<ExamResponse>> getExamByLevel(@Valid @ModelAttribute ExamRequest request) {
        return ResponseEntity.ok(examService.getExamByLevel(request));
    }
}
