package kotobase_backend.modules.lesson.repository;

import kotobase_backend.comom.enums.TargetType;
import kotobase_backend.modules.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Integer> {

    List<Lesson> findByLevelIdAndLessonTypeOrderByLessonOrderAsc(Integer levelId, TargetType targetType);
    Optional<Lesson> findByIdAndLessonType(Integer lessonId, TargetType targetType);
}
