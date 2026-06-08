package kotobase_backend.modules.topic;

import kotobase_backend.modules.topic.dto.response.TopicReponse;
import kotobase_backend.modules.topic.service.TopicService;
import kotobase_backend.security.userdetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/topic")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping("/{id}")
    public ResponseEntity<List<TopicReponse>> getAllTopics(@PathVariable Integer id,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer userId = (userDetails != null) ? userDetails.getUserId() : null;

        return ResponseEntity.ok(topicService.findByLessonId(id,userId));
    }
}
