package kotobase_backend.modules.topic;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.topic.dto.request.TopicAdminRequest;
import kotobase_backend.modules.topic.dto.response.TopicAdminResponse;
import kotobase_backend.modules.topic.dto.response.TopicCompactResponse;
import kotobase_backend.modules.topic.service.TopicAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/topic")
@RequiredArgsConstructor
public class TopicAdminController {

    private final TopicAdminService topicService;

    @GetMapping
    public ResponseEntity<PageResponse<TopicAdminResponse>> getAllTopics(@RequestParam(required = false) String search,
                                                                         @RequestParam(required = false) Integer lessonId,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "15") int size) {
        return ResponseEntity.ok(topicService.getAllTopics(search, lessonId, PageRequest.of(page, size)));
    }

    @GetMapping("/compact")
    public ResponseEntity<List<TopicCompactResponse>> getCompactTopics(@RequestParam Integer lessonId) {
        return ResponseEntity.ok(topicService.getCompactTopicsByLesson(lessonId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicAdminResponse> getTopicById(@PathVariable Integer id) {
        return ResponseEntity.ok(topicService.getTopicById(id));
    }

    @PostMapping
    public ResponseEntity<TopicAdminResponse> createTopic(@RequestBody TopicAdminRequest request) {
        return ResponseEntity.ok(topicService.createTopic(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicAdminResponse> updateTopic(@PathVariable Integer id, @RequestBody TopicAdminRequest request) {
        return ResponseEntity.ok(topicService.updateTopic(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTopic(@PathVariable Integer id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok("Xóa chủ đề thành công!");
    }
}