package kotobase_backend.modules.lesson.service.Impl;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.comom.exceptions.CustomException.BadRequestException;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.JlptLevel.repository.JlptLevelRepository;
import kotobase_backend.modules.lesson.dto.response.LessonResponse;
import kotobase_backend.modules.lesson.entity.Lesson;
import kotobase_backend.modules.lesson.mapper.LessonMapper;
import kotobase_backend.modules.lesson.repository.LessonRepository;
import kotobase_backend.modules.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    private final LessonMapper lessonMapper;

    private final JlptLevelRepository jlptLevelRepository;

    @Override
    public List<LessonResponse> findByLevel(Integer levelId) {
        if (levelId == null) {
            throw new BadRequestException("level không được để trống");
        }

        JlptLevel jlptLevel = jlptLevelRepository.findById(levelId)
                .orElseThrow(() -> new ResourceNotFoundException("level không tồn tại"));

        List<Lesson> lessons = lessonRepository.findByLevelOrderByLessonOrderAsc(jlptLevel);
        return lessons.stream()
                .map(lessonMapper::toLessonResponse)
                .toList();
    }
}
