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
    @Query("select distinct v from Vocab v " +
            "join v.vocabularyTopics vt " +
            "where vt.topic.id = :topicId")
    Page<Vocab> findByTopicId(@Param("topicId") Integer topicId, Pageable pageable);

    @Query(value = "SELECT * FROM vocabularies v " +
            "WHERE v.level_id = :levelId AND v.id != :correctId " +
            "ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Vocab> findRandomDistractors(@Param("levelId") Integer levelId, @Param("correctId") Integer correctId);

    @Query(value = "SELECT v.* " +
            "FROM vocabularies v " +
            "JOIN vocabulary_topics vt " +
            "ON vt.vocabulary_id = v.id " +
            "WHERE vt.topic_id = :topicId " +
            "AND v.id != :correctId " +
            "ORDER BY RAND() " +
            "LIMIT 3", nativeQuery = true)
    List<Vocab> findRandom(@Param("topicId") Integer topicId, @Param("correctId") Integer correctId);

    List<Vocab> findByVocabularyTopics_Topic_Id(Integer topicId);
}
