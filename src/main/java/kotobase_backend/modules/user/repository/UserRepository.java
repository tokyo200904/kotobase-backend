package kotobase_backend.modules.user.repository;

import kotobase_backend.modules.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByEmail(String email);
    public boolean existsByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN UserSubscription s ON s.user = u AND s.status = 'ACTIVE' AND s.endDate > CURRENT_TIMESTAMP " +
            "WHERE (:search IS NULL OR u.fullName LIKE %:search% OR u.email LIKE %:search%) " +
            "AND (:isEnabled IS NULL OR u.isEnabled = :isEnabled) " +
            "AND (:isPremium IS NULL OR " +
            "      (:isPremium = true AND s.id IS NOT NULL) OR " +
            "      (:isPremium = false AND s.id IS NULL)) " +
            "ORDER BY u.createdAt DESC")
    Page<User> adminSearchUsers(@Param("search") String search,
                                @Param("isEnabled") Boolean isEnabled,
                                @Param("isPremium") Boolean isPremium,
                                Pageable pageable);
}
