package kotobase_backend.modules.lesson.service.Impl;
import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.lesson.dto.request.LessonAdminRequest;
import kotobase_backend.modules.lesson.dto.response.LessonAdminResponse;
import kotobase_backend.modules.lesson.dto.response.LessonCompactResponse;
import kotobase_backend.modules.lesson.entity.Lesson;
import kotobase_backend.modules.lesson.mapper.LessonAdminMapper;
import kotobase_backend.modules.lesson.repository.LessonRepository;
import kotobase_backend.modules.lesson.service.LessonAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonAdminServiceImpl implements LessonAdminService {

    private final LessonRepository lessonRepository;
    private final LessonAdminMapper lessonMapper;

    @Override
    public PageResponse<LessonAdminResponse> getAllLessons(String search, Integer levelId, Pageable pageable) {
        Page<Lesson> page = lessonRepository.adminSearchLessons(search, levelId, pageable);
        return PageResponse.of(page.map(lessonMapper::toResponse));
    }

    @Override
    public List<LessonCompactResponse> getCompactLessonsByLevel(Integer levelId) {
        return lessonRepository.findByLevel_IdOrderByLessonOrderAsc(levelId)
                .stream().map(l -> LessonCompactResponse.builder()
                        .id(l.getId()).title(l.getTitle()).build()).toList();
    }

    @Override
    public LessonAdminResponse getLessonById(Integer id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài học"));
        return lessonMapper.toResponse(lesson);
    }

    @Override
    @Transactional
    public LessonAdminResponse createLesson(LessonAdminRequest request) {
        Lesson lesson = new Lesson();
        lesson.setCreatedAt(LocalDateTime.now());
        lessonMapper.updateEntityFromRequest(request, lesson);
        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public LessonAdminResponse updateLesson(Integer id, LessonAdminRequest request) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bài học"));
        lessonMapper.updateEntityFromRequest(request, lesson);
        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    @Override
    @Transactional
    public void deleteLesson(Integer id) {
        lessonRepository.deleteById(id);
    }
}