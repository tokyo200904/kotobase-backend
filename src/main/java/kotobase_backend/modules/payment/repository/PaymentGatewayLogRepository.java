package kotobase_backend.modules.payment.repository;

import kotobase_backend.modules.payment.entity.PaymentGatewayLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentGatewayLogRepository extends JpaRepository<PaymentGatewayLog, Integer> {
}
