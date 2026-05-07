package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Page<Exam> findByLevel_IdAndIsPublishedTrue(Integer levelId, Pageable pageable);
}
