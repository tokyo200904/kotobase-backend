package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.QuestionGroups;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionGroupRepository extends JpaRepository<QuestionGroups, Long> {

}
