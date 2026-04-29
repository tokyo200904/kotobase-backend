package kotobase_backend.modules.topic.repository;

import kotobase_backend.modules.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {
    List<Topic> findByLessonId(Integer lessonId);
}
