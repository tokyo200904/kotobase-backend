package kotobase_backend.modules.topic.service.Impl;

import kotobase_backend.comom.exceptions.CustomException.BadRequestException;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.lesson.repository.LessonRepository;
import kotobase_backend.modules.topic.dto.response.TopicReponse;
import kotobase_backend.modules.topic.entity.Topic;
import kotobase_backend.modules.topic.mapper.TopicMapper;
import kotobase_backend.modules.topic.repository.TopicRepository;
import kotobase_backend.modules.topic.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final LessonRepository lessonRepository;
    private final TopicMapper topicMapper;

    @Override
    public List<TopicReponse> findByLessonId(Integer lessonId) {
        if (lessonId == null) {
            throw new BadRequestException("khong tim thay lessonId");
        }
        lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson không tồn tại"));

        List<Topic> topic = topicRepository.findByLessonId(lessonId);
        return topic.stream()
                .map(topicMapper::topicToTopicReponse)
                .toList();
    }
}
