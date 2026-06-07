package kotobase_backend.modules.payment.repository;

import kotobase_backend.modules.payment.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Modifying
    @Query("UPDATE Transaction t " +
            "SET t.status = :newStatus " +
            "WHERE t.status = :oldStatus " +
            "AND t.createdAt < :cutoffTime")
    int cancelExpiredTransactions(@Param("oldStatus") String oldStatus,
                                  @Param("newStatus") String newStatus,
                                  @Param("cutoffTime") LocalDateTime cutoffTime);
}
