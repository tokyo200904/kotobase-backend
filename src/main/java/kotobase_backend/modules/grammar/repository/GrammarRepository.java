package kotobase_backend.modules.grammar.repository;

import kotobase_backend.modules.grammar.entity.Grammar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrammarRepository extends JpaRepository<Grammar, Integer> {

}
