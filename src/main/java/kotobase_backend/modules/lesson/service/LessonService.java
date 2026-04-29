package kotobase_backend.modules.lesson.service;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.modules.lesson.dto.response.LessonResponse;
import kotobase_backend.modules.lesson.entity.Lesson;

import java.util.List;

public interface LessonService {
    List<LessonResponse> findByLevel(Integer levelId);

}
