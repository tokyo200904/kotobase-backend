package kotobase_backend.modules.practice.repository;

import kotobase_backend.modules.practice.entity.GrammarExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarExerciseRepository extends JpaRepository<GrammarExercise, Integer> {
    List<GrammarExercise> findByGrammar_Id(Integer grammarId);
}
