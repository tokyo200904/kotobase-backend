package kotobase_backend.modules.lesson.mapper;

import kotobase_backend.modules.lesson.dto.response.LessonResponse;
import kotobase_backend.modules.lesson.entity.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper {
    public LessonResponse  toLessonResponse(Lesson lesson) {
        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setId(lesson.getId());
        lessonResponse.setLessonOrder(lesson.getLessonOrder());
        lessonResponse.setTitle(lesson.getTitle());
        return lessonResponse;
    }
}
