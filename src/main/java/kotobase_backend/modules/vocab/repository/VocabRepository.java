package kotobase_backend.modules.vocab.repository;

import kotobase_backend.modules.vocab.entity.Vocab;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabRepository extends JpaRepository<Vocab, Integer> {

    @EntityGraph(attributePaths = "exampleVocabs")
    @Query("select distinct v from Vocab v " +
            "join v.vocabularyTopics vt " +
            "where vt.topic.id = :topicId")
    Page<Vocab> findByTopicId(@Param("topicId") Integer topicId, Pageable pageable);
}
