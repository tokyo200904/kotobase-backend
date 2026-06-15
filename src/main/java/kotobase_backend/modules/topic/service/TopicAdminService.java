package kotobase_backend.modules.topic.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.topic.dto.request.TopicAdminRequest;
import kotobase_backend.modules.topic.dto.response.TopicAdminResponse;
import kotobase_backend.modules.topic.dto.response.TopicCompactResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TopicAdminService {
    PageResponse<TopicAdminResponse> getAllTopics(String search, Integer lessonId, Pageable pageable);
    List<TopicCompactResponse> getCompactTopicsByLesson(Integer lessonId);
    TopicAdminResponse getTopicById(Integer id);
    TopicAdminResponse createTopic(TopicAdminRequest request);
    TopicAdminResponse updateTopic(Integer id, TopicAdminRequest request);
    void deleteTopic(Integer id);
}
