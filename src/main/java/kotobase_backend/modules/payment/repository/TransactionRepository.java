package kotobase_backend.modules.payment.repository;

import jakarta.persistence.LockModeType;
import kotobase_backend.comom.enums.TransactionStatus;
import kotobase_backend.modules.payment.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Modifying
    @Query("UPDATE Transaction t " +
            "SET t.status = :newStatus " +
            "WHERE t.status = :oldStatus " +
            "AND t.createdAt < :cutoffTime")
    int cancelExpiredTransactions(@Param("oldStatus") TransactionStatus oldStatus,
                                  @Param("newStatus") TransactionStatus newStatus,
                                  @Param("cutoffTime") LocalDateTime cutoffTime);

    @Modifying
    @Query("UPDATE Transaction t " +
            "SET t.status = :newStatus " +
            "WHERE t.user.id = :userId " +
            "AND t.status = :oldStatus " +
            "AND t.id != :successOrderId")
    void cancelOtherPendingTransactions(@Param("userId") Integer userId,
                                        @Param("successOrderId") String successOrderId,
                                        @Param("oldStatus") TransactionStatus oldStatus,
                                        @Param("newStatus") TransactionStatus newStatus);

    @Modifying
    @Query("UPDATE Transaction t " +
            "SET t.status = :newStatus " +
            "WHERE t.user.id = :userId " +
            "AND t.status = :oldStatus")
    void cancelPendingTransactions(@Param("userId") Integer userId,
                                   @Param("oldStatus") TransactionStatus oldStatus,
                                   @Param("newStatus") TransactionStatus newStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Transaction t WHERE t.id = :id")
    Optional<Transaction> findByIdWithLock(@Param("id") String id);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:search IS NULL OR t.id LIKE %:search% OR t.user.email LIKE %:search% OR t.user.fullName LIKE %:search%) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "ORDER BY t.createdAt DESC")
    Page<Transaction> adminSearchTransactions(@Param("search") String search,
                                              @Param("status") TransactionStatus status,
                                              Pageable pageable);
    List<Transaction> findByUserIdOrderByCreatedAtDesc(Integer userId);
}
