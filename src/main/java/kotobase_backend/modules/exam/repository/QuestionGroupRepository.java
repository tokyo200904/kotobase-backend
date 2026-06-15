package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.QuestionGroups;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionGroupRepository extends JpaRepository<QuestionGroups, Long> {
    List<QuestionGroups> findBySection_Exam_Id(Long examId);
    List<QuestionGroups> findBySection_IdOrderByDisplayOrderAsc(Long sectionId);
}
