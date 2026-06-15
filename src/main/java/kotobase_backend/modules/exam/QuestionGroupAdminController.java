package kotobase_backend.modules.exam;

import kotobase_backend.modules.exam.dto.request.QuestionGroupAdminRequest;
import kotobase_backend.modules.exam.dto.response.QuestionGroupAdminResponse;
import kotobase_backend.modules.exam.service.QuestionGroupAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/exam/group")
@RequiredArgsConstructor
public class QuestionGroupAdminController {
    private final QuestionGroupAdminService groupService;

    @GetMapping("/sections/{sectionId}/question-groups")
    public ResponseEntity<List<QuestionGroupAdminResponse>> getGroupsBySection(@PathVariable Long sectionId) {
        return ResponseEntity.ok(groupService.getGroupsBySectionId(sectionId));
    }

    @PostMapping("/sections/{sectionId}/question-groups")
    public ResponseEntity<QuestionGroupAdminResponse> createGroup(
            @PathVariable Long sectionId,
            @RequestBody QuestionGroupAdminRequest request) {
        return ResponseEntity.ok(groupService.createGroup(sectionId, request));
    }

    @PutMapping("/question-groups/{groupId}")
    public ResponseEntity<QuestionGroupAdminResponse> updateGroup(
            @PathVariable Long groupId,
            @RequestBody QuestionGroupAdminRequest request) {
        return ResponseEntity.ok(groupService.updateGroup(groupId, request));
    }

    @DeleteMapping("/question-groups/{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok("Xóa nhóm câu hỏi thành công!");
    }
}