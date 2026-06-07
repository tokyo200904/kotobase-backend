package kotobase_backend.modules.payment.repository;

import kotobase_backend.modules.payment.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Integer> {
    Optional<UserSubscription> findFirstByUser_IdAndStatus(Integer userId, String status);
}
