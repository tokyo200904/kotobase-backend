package kotobase_backend.modules.lesson;

import kotobase_backend.modules.lesson.dto.response.LessonResponse;
import kotobase_backend.modules.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/lesson")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/levels/{id}")
    public ResponseEntity<List<LessonResponse>> findAll(@PathVariable Integer id) {
        return ResponseEntity.ok(lessonService.findByLevel(id));
    }
}
