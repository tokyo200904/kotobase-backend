package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
