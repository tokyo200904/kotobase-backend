package kotobase_backend.modules.grammar.repository;

import kotobase_backend.modules.grammar.entity.Grammar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT g FROM Grammar g WHERE " +
            "(:search IS NULL OR g.title LIKE %:search% OR g.meaning LIKE %:search%) " +
            "AND (:levelId IS NULL OR g.level.id = :levelId) " +
            "AND (:lessonId IS NULL OR g.lesson.id = :lessonId) " +
            "ORDER BY g.id DESC")
    Page<Grammar> adminSearchGrammars(@Param("search") String search,
                                      @Param("levelId") Integer levelId,
                                      @Param("lessonId") Integer lessonId,
                                      Pageable pageable);
}

