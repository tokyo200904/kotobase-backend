package kotobase_backend.modules.progress.repository;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.entity.UserItemProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserItemProgressRepository extends JpaRepository<UserItemProgress, Integer> {
    Optional<UserItemProgress> findByUser_IdAndItemIdAndItemType(Integer userId, Integer itemId, ItemType itemType);
}
