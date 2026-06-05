package kotobase_backend.modules.progress.repository;

import kotobase_backend.comom.enums.ItemType;
import kotobase_backend.modules.progress.entity.UserItemProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserItemProgressRepository extends JpaRepository<UserItemProgress, Integer> {
    Optional<UserItemProgress> findByUser_IdAndItemIdAndItemType(Integer userId, Integer itemId, ItemType itemType);

    @Query("SELECT p FROM UserItemProgress p " +
            "WHERE p.user.id = :userId AND p.nextReviewDate <= CURRENT_TIMESTAMP " +
            "ORDER BY p.nextReviewDate ASC")
    List<UserItemProgress> findDueItemsForUser(@Param("userId") Integer userId);

    Optional<UserItemProgress> findByUserIdAndItemIdAndItemType(Integer userId, Integer itemId, ItemType itemType);

    @Query("SELECT p FROM UserItemProgress p " +
            "WHERE p.user.id = :userId " +
            "AND p.itemType = :type " +
            "AND p.nextReviewDate <= CURRENT_TIMESTAMP " +
            "ORDER BY p.nextReviewDate ASC")
    List<UserItemProgress> findDueItemsByType(@Param("userId") Integer userId, @Param("type") ItemType type);

    @Query(value = "SELECT * FROM user_item_progress p " +
            "WHERE p.user_id = :userId AND p.item_type = :type " +
            "ORDER BY RAND() LIMIT 10",
            nativeQuery = true)
    List<UserItemProgress> getRandomItemForPractice(@Param("userId") Integer userId, @Param("type") String type);

    @Query("SELECT p.status, COUNT(p) FROM UserItemProgress p " +
            "WHERE p.user.id = :userId " +
            "GROUP BY p.status")
    List<Object[]> countItemsByStatus(@Param("userId") Integer userId);

    boolean existsByUserIdAndItemIdAndItemType(Integer userId, Integer itemId, ItemType itemType);

    @Query("SELECT p.itemId FROM UserItemProgress p " +
            "WHERE p.user.id = :userId " +
            "AND p.itemType = :type")
    List<Integer> findSavedItemIds(@Param("userId") Integer userId, @Param("type") ItemType type);

    @Query("SELECT COUNT(p) FROM UserItemProgress p " +
            "WHERE p.user.id = :userId " +
            "AND p.itemType = :type " +
            "AND p.nextReviewDate <= CURRENT_TIMESTAMP")
    int countDueItemsByType(@Param("userId") Integer userId, @Param("type") ItemType type);

    Page<UserItemProgress> findByUserIdAndItemType(Integer userId, ItemType itemType, Pageable pageable);

    boolean existsByUserIdAndItemTypeAndItemId(Integer userId, ItemType itemType, Integer itemId);
}

