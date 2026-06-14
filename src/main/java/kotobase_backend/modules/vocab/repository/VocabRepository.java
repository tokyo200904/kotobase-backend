package kotobase_backend.modules.vocab.repository;

import kotobase_backend.modules.kanji.entity.Kanji;
import kotobase_backend.modules.vocab.entity.Vocab;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabRepository extends JpaRepository<Vocab, Integer> {

    @EntityGraph(attributePaths = "exampleVocabs")
    Page<Vocab> findByTopic_Id(Integer topicId, Pageable pageable);

    @Query(value = "SELECT * FROM vocabularies v " +
            "WHERE v.level_id = :levelId AND v.id != :correctId " +
            "ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Vocab> findRandomDistractors(@Param("levelId") Integer levelId, @Param("correctId") Integer correctId);

    @Query(value = "SELECT * " +
            "FROM vocabularies  " +
            "JOIN vocabulary_topics vt " +
            "WHERE topic_id = :topicId " +
            "AND id != :correctId " +
            "ORDER BY RAND() " +
            "LIMIT 3", nativeQuery = true)
    List<Vocab> findRandom(@Param("topicId") Integer topicId, @Param("correctId") Integer correctId);

    List<Vocab> findByTopic_Id(Integer topicId);


    @Query("SELECT v FROM Vocab v WHERE " +
            "(:search IS NULL OR v.word LIKE %:search% OR v.meaning LIKE %:search% OR v.reading LIKE %:search%) " +
            "AND (:levelId IS NULL OR v.level.id = :levelId) " +
            "AND (:topicId IS NULL OR v.topic.id = :topicId) " +
            "ORDER BY v.id DESC")
    Page<Vocab> adminSearchVocabs(@Param("search") String search,
                                  @Param("levelId") Integer levelId,
                                  @Param("topicId") Integer topicId,
                                  Pageable pageable);
}


