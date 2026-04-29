package kotobase_backend.modules.topic;

import kotobase_backend.modules.topic.dto.response.TopicReponse;
import kotobase_backend.modules.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/topic")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping("/{id}")
    public ResponseEntity<List<TopicReponse>> getAllTopics(@PathVariable Integer id) {
        return ResponseEntity.ok(topicService.findByLessonId(id));
    }
}
