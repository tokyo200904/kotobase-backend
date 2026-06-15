package kotobase_backend.modules.exam;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.exam.dto.request.ExamAdminRequest;
import kotobase_backend.modules.exam.dto.response.ExamAdminResponse;
import kotobase_backend.modules.exam.service.ExamAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/exam/attempts")
@RequiredArgsConstructor
public class ExamAdminController {

    private final ExamAdminService examAdminService;

    @GetMapping
    public ResponseEntity<PageResponse<ExamAdminResponse>> getAllExams(@RequestParam(required = false) String search,
                                                                       @RequestParam(required = false) Integer levelId,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(examAdminService.getAllExams(search, levelId, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamAdminResponse> getExamById(@PathVariable Long id) {
        return ResponseEntity.ok(examAdminService.getExamById(id));
    }

    @PostMapping
    public ResponseEntity<ExamAdminResponse> createExam(@RequestBody ExamAdminRequest request) {
        return ResponseEntity.ok(examAdminService.createExam(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamAdminResponse> updateExam(@PathVariable Long id,
                                                        @RequestBody ExamAdminRequest request) {
        return ResponseEntity.ok(examAdminService.updateExam(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExam(@PathVariable Long id) {
        examAdminService.deleteExam(id);
        return ResponseEntity.ok("Xóa đề thi thành công!");
    }
}