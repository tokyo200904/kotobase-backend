package kotobase_backend.modules.exam.repository;

import kotobase_backend.comom.enums.AttemptStatus;
import kotobase_backend.comom.enums.StatusSection;
import kotobase_backend.modules.exam.entity.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
    List<ExamAttempt> findByStatus(AttemptStatus attemptStatus);
    Optional<ExamAttempt> findByUser_IdAndExam_IdAndStatus(Integer userId, Long examId, AttemptStatus status);
}
