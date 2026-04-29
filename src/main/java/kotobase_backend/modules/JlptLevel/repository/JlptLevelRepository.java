package kotobase_backend.modules.JlptLevel.repository;

import kotobase_backend.modules.JlptLevel.entity.JlptLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JlptLevelRepository extends JpaRepository<JlptLevel,Integer> {
}
