package kotobase_backend.modules.lesson.mapper;

import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.JlptLevel.repository.JlptLevelRepository;
import kotobase_backend.modules.lesson.dto.request.LessonAdminRequest;
import kotobase_backend.modules.lesson.dto.response.LessonAdminResponse;
import kotobase_backend.modules.lesson.entity.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonAdminMapper {

    private final JlptLevelRepository levelRepository;

    public void updateEntityFromRequest(LessonAdminRequest request, Lesson lesson) {
        lesson.setTitle(request.getTitle());
        lesson.setLessonType(request.getLessonType());
        lesson.setLessonOrder(request.getLessonOrder());

        JlptLevel level = levelRepository.findById(request.getLevelId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Cấp độ (Level)"));
        lesson.setLevel(level);
    }

    public LessonAdminResponse toResponse(Lesson lesson) {
        return LessonAdminResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .lessonType(lesson.getLessonType())
                .lessonOrder(lesson.getLessonOrder())
                .levelId(lesson.getLevel().getId())
                .levelName(lesson.getLevel().getLevel().name())
                .build();
    }
}