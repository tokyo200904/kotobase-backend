package kotobase_backend.modules.payment.repository;

import kotobase_backend.modules.payment.entity.PaymentGatewayLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentGatewayLogRepository extends JpaRepository<PaymentGatewayLog, Integer> {
    List<PaymentGatewayLog> findByTransaction_IdOrderByCreatedAtDesc(String transactionId);
}
