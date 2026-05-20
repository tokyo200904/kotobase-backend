package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
