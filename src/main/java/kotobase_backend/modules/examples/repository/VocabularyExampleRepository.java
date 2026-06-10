package kotobase_backend.modules.examples.repository;

import kotobase_backend.modules.examples.entity.ExampleVocab;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VocabularyExampleRepository extends JpaRepository<ExampleVocab, Integer> {
    List<ExampleVocab> findByVocabulary_Id(Integer vocabularyId);
}