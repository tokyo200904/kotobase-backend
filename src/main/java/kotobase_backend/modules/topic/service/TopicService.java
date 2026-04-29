package kotobase_backend.modules.topic.service;

import kotobase_backend.modules.topic.dto.response.TopicReponse;

import java.util.List;

public interface TopicService {
    List<TopicReponse> findByLessonId(Integer lessonId);
}
