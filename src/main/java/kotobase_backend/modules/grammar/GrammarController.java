package kotobase_backend.modules.grammar;

import kotobase_backend.modules.grammar.dto.response.GrammarResponse;
import kotobase_backend.modules.grammar.dto.response.GrammarTitleResponse;
import kotobase_backend.modules.grammar.service.GrammarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/grammar")
@RequiredArgsConstructor
public class GrammarController {
    private final GrammarService grammarService;

    @GetMapping("/by-lesson")
    public ResponseEntity<List<GrammarTitleResponse>>  getByLesson(@RequestParam Integer lessonId){
        return ResponseEntity.ok(grammarService.findByLessonId(lessonId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GrammarResponse> getById(@PathVariable Integer id){
        return ResponseEntity.ok(grammarService.findById(id));
    }
}
