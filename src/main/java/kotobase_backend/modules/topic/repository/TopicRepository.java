package kotobase_backend.modules.topic.repository;

import kotobase_backend.modules.topic.entity.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    List<Topic> findByLessonId(Integer lessonId);

    @Query("SELECT t FROM Topic t WHERE " +
            "(:search IS NULL OR t.name LIKE %:search%) " +
            "AND (:lessonId IS NULL OR t.lesson.id = :lessonId) " +
            "ORDER BY t.lesson.id ASC, t.id ASC")
    Page<Topic> adminSearchTopics(@Param("search") String search,
                                  @Param("lessonId") Integer lessonId,
                                  Pageable pageable);

    List<Topic> findByLesson_IdOrderByIdAsc(Integer lessonId);
}
