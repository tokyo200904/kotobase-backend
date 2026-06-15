package kotobase_backend.modules.lesson.repository;

import kotobase_backend.comom.enums.TargetType;
import kotobase_backend.modules.lesson.entity.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Integer> {

    List<Lesson> findByLevelIdAndLessonTypeOrderByLessonOrderAsc(Integer levelId, TargetType targetType);
    Optional<Lesson> findByIdAndLessonType(Integer lessonId, TargetType targetType);

    @Query("SELECT l FROM Lesson l WHERE " +
            "(:search IS NULL OR l.title LIKE %:search%) " +
            "AND (:levelId IS NULL OR l.level.id = :levelId) " +
            "ORDER BY l.level.id ASC, l.lessonOrder ASC")
    Page<Lesson> adminSearchLessons(@Param("search") String search,
                                    @Param("levelId") Integer levelId,
                                    Pageable pageable);

    List<Lesson> findByLevel_IdOrderByLessonOrderAsc(Integer levelId);
}
