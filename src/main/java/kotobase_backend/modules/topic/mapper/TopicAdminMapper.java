package kotobase_backend.modules.topic.mapper;

import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.lesson.entity.Lesson;
import kotobase_backend.modules.lesson.repository.LessonRepository;
import kotobase_backend.modules.topic.dto.request.TopicAdminRequest;
import kotobase_backend.modules.topic.dto.response.TopicAdminResponse;
import kotobase_backend.modules.topic.entity.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicAdminMapper {

    private final LessonRepository lessonRepository;

    public void updateEntityFromRequest(TopicAdminRequest request, Topic topic) {
        topic.setName(request.getName());

        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Bài học"));
        topic.setLesson(lesson);
    }

    public TopicAdminResponse toResponse(Topic topic) {
        return TopicAdminResponse.builder()
                .id(topic.getId())
                .name(topic.getName())
                .lessonId(topic.getLesson().getId())
                .lessonTitle(topic.getLesson().getTitle())
                .levelName(topic.getLesson().getLevel().getLevel().name())
                .build();
    }
}