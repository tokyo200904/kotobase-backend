package kotobase_backend.modules.lesson.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.lesson.dto.request.LessonAdminRequest;
import kotobase_backend.modules.lesson.dto.response.LessonAdminResponse;
import kotobase_backend.modules.lesson.dto.response.LessonCompactResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LessonAdminService {
    PageResponse<LessonAdminResponse> getAllLessons(String search, Integer levelId, Pageable pageable);
    List<LessonCompactResponse> getCompactLessonsByLevel(Integer levelId);
    LessonAdminResponse getLessonById(Integer id);
    LessonAdminResponse createLesson(LessonAdminRequest request);
    LessonAdminResponse updateLesson(Integer id, LessonAdminRequest request);
    void deleteLesson(Integer id);
}
