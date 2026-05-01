package kotobase_backend.modules.lesson.repository;

import kotobase_backend.comom.enums.Level;
import kotobase_backend.comom.enums.TargetType;
import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import kotobase_backend.modules.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Integer> {

    List<Lesson> findByLevelIdAndLessonTypeOrderByLessonOrderAsc(Integer levelId, TargetType targetType);
}
