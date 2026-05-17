package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.Exam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    Page<Exam> findByLevel_IdAndIsPublishedTrue(Integer levelId, Pageable pageable);

    @Query("select e from Exam e " +
            "left join fetch e.sections " +
            "where e.id = :id")
    Optional<Exam> findDetailById(@Param("id") Long id);
}
