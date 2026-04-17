package kotobase_backend.modules.vocab.repository;

import kotobase_backend.modules.vocab.entity.vocab;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VocabRepository extends JpaRepository<vocab, Long> {
    Page<vocab> findByKanjiContaining(String keyword, Pageable pageable);
}
