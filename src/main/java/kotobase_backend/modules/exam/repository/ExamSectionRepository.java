package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.ExamSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamSectionRepository extends JpaRepository<ExamSection, Long> {

    @Query("select distinct s " +
            "from ExamSection s " +
            "left join fetch s.questionGroups qg " +
            "where s.id = :sectionId " +
            "order by qg.displayOrder ")
    Optional<ExamSection> getExamSectionBySectionId(@Param("sectionId") Long sectionId);

    List<ExamSection> findByExam_IdOrderByDisplayOrderAsc(Long examId);

    Optional<ExamSection> findFirstByExam_IdAndDisplayOrderGreaterThanOrderByDisplayOrderAsc(Long examId, int displayOrder);
}
