package kotobase_backend.modules.payment;

import jakarta.servlet.http.HttpServletRequest;
import kotobase_backend.modules.payment.dto.request.PaymentRequest;
import kotobase_backend.modules.payment.dto.response.PaymentResponse;
import kotobase_backend.modules.payment.entity.Transaction;
import kotobase_backend.modules.payment.repository.TransactionRepository;
import kotobase_backend.modules.payment.service.PaymentService;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.modules.user.repository.UserRepository;
import kotobase_backend.security.userdetail.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest requestDTO,
                                                         HttpServletRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null || userDetails.getUserId() == null) {
            throw new RuntimeException("Bạn cần đăng nhập để thực hiện thanh toán");
        }

        Integer userId = userDetails.getUserId();

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin tài khoản"));
        PaymentResponse response = paymentService.createPaymentLink(requestDTO.getPlanId(), currentUser, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay-ipn")
    public ResponseEntity<String> handleIPN(HttpServletRequest request) {
        String response = paymentService.processIpnWebhook(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-status/{orderId}")
    public ResponseEntity<?> checkStatus(@PathVariable String orderId,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer currentUserId = userDetails.getUserId();
        Transaction transaction = transactionRepository.findById(orderId).orElse(null);

        if (transaction == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Đơn hàng không tồn tại"));
        }

        if (!transaction.getUser().getId().equals(currentUserId)) {
            return ResponseEntity.status(403).body(Map.of("message", "Bạn không có quyền xem đơn hàng này"));
        }

        return ResponseEntity.ok(Map.of(
                "orderId", transaction.getId(),
                "status", transaction.getStatus()
        ));
    }
}
