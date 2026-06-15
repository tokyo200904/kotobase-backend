package kotobase_backend.modules.lesson;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.lesson.dto.request.LessonAdminRequest;
import kotobase_backend.modules.lesson.dto.response.LessonAdminResponse;
import kotobase_backend.modules.lesson.dto.response.LessonCompactResponse;
import kotobase_backend.modules.lesson.service.LessonAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/lesson")
@RequiredArgsConstructor
public class LessonAdminController {

    private final LessonAdminService lessonService;

    @GetMapping
    public ResponseEntity<PageResponse<LessonAdminResponse>> getAllLessons(@RequestParam(required = false) String search,
                                                                           @RequestParam(required = false) Integer levelId,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "15") int size) {
        return ResponseEntity.ok(lessonService.getAllLessons(search, levelId, PageRequest.of(page, size)));
    }

    @GetMapping("/compact")
    public ResponseEntity<List<LessonCompactResponse>> getCompactLessons(@RequestParam Integer levelId) {
        return ResponseEntity.ok(lessonService.getCompactLessonsByLevel(levelId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonAdminResponse> getLessonById(@PathVariable Integer id) {
        return ResponseEntity.ok(lessonService.getLessonById(id));
    }

    @PostMapping
    public ResponseEntity<LessonAdminResponse> createLesson(@RequestBody LessonAdminRequest request) {
        return ResponseEntity.ok(lessonService.createLesson(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonAdminResponse> updateLesson(@PathVariable Integer id, @RequestBody LessonAdminRequest request) {
        return ResponseEntity.ok(lessonService.updateLesson(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLesson(@PathVariable Integer id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.ok("Xóa bài học thành công!");
    }
}