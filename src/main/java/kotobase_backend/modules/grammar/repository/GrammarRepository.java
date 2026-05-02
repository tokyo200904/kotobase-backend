package kotobase_backend.modules.grammar.repository;

import kotobase_backend.modules.grammar.entity.Grammar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, Integer> {

    @Query("select distinct g from Grammar g " +
            "left join fetch g.exampleGrammars " +
            "where g.lesson.id = :lessonId ")
    List<Grammar> findAllByLessonId(@Param("lessonId") Integer lessonId);
}
