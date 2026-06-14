package kotobase_backend.modules.payment.repository;

import kotobase_backend.modules.payment.entity.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Integer> {
    List<SubscriptionPlan> findByIsActiveTrueOrderByPriceAsc();

    @Query("SELECT s FROM SubscriptionPlan s WHERE " +
            "(:search IS NULL OR s.name LIKE %:search%) " +
            "AND (:isActive IS NULL OR s.isActive = :isActive) " +
            "ORDER BY s.id DESC")
    Page<SubscriptionPlan> adminSearchPlans(@Param("search") String search,
                                            @Param("isActive") Boolean isActive,
                                            Pageable pageable);
}
