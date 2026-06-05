package kotobase_backend.modules.progress.repository;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.entity.UserLevelProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLevelProgressRepository extends JpaRepository<UserLevelProgress, Integer> {
    Optional<UserLevelProgress> findByUserIdAndLevelIdAndItemType(Integer userId, Integer levelId, ItemType itemType);
}
