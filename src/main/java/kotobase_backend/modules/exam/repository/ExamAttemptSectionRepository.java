package kotobase_backend.modules.exam.repository;

import kotobase_backend.comom.enums.StatusSection;
import kotobase_backend.modules.exam.entity.ExamAttemptSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamAttemptSectionRepository extends JpaRepository<ExamAttemptSection, Long> {
    Optional<ExamAttemptSection> findByExamAttempt_IdAndSection_Id(Long attemptId, Long sectionId);
    Optional<ExamAttemptSection> findByExamAttempt_IdAndStatus(Long attemptId, StatusSection status);
    List<ExamAttemptSection> findByExamAttempt_Id(Long attemptId);
    Optional<ExamAttemptSection> findFirstByExamAttempt_IdAndStatusOrderBySection_DisplayOrderAsc(Long attemptId, StatusSection status);
    List<ExamAttemptSection> findByStatus(StatusSection status);
}
