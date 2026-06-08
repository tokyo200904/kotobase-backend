package kotobase_backend.modules.payment.repository;

import kotobase_backend.modules.payment.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {
    List<SubscriptionPlan> findByIsActiveTrueOrderByPriceAsc();
}
