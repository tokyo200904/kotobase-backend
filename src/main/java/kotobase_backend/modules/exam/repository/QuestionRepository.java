package kotobase_backend.modules.exam.repository;

import kotobase_backend.modules.exam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT q FROM Question q " +
            "JOIN FETCH q.questionGroup qg " +
            "JOIN FETCH qg.section " +
            "WHERE q.id IN :id")
    List<Question> findAllWithGroupAndSection(@Param("id") List<Long> ids);
}

