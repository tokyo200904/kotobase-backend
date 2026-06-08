package kotobase_backend.modules.payment.repository;

import kotobase_backend.comom.enums.SubscriptionStatus;
import kotobase_backend.modules.payment.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
    Optional<UserSubscription> findFirstByUser_IdAndStatus(Integer userId, SubscriptionStatus status);

    @Query("SELECT COUNT(s) > 0 FROM UserSubscription s " +
            "WHERE s.user.id = :userId " +
            "AND s.status = 'ACTIVE' " +
            "AND s.endDate > CURRENT_TIMESTAMP")
    boolean isUserPremium(@Param("userId") Integer userId);

    @Modifying
    @Query("UPDATE UserSubscription s " +
            "SET s.status = :newStatus " +
            "WHERE s.status = :oldStatus " +
            "AND s.endDate < :currentTime")
    int expireOldSubscriptions(@Param("oldStatus") SubscriptionStatus oldStatus,
                               @Param("newStatus") SubscriptionStatus newStatus,
                               @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT COUNT(s) > 0 " +
            "FROM UserSubscription s " +
            "WHERE s.user.id = :userId " +
            "AND s.status = :activeStatus " +
            "AND s.endDate > CURRENT_TIMESTAMP")
    boolean isUserPremium(@Param("userId") Integer userId, @Param("activeStatus") SubscriptionStatus activeStatus);
}
