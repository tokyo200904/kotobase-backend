package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.ExamAttemptAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAttemptAnswerRepository extends JpaRepository<ExamAttemptAnswer, Long> {
    List<ExamAttemptAnswer> findByExamAttempt_Id(Long examAttemptId);
    List<ExamAttemptAnswer> findByExamAttempt_IdAndQuestion_IdIn(Long examAttemptId, List<Long> questionIds);
}
