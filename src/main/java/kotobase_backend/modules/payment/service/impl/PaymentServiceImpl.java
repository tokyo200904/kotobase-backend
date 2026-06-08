package kotobase_backend.modules.payment.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import kotobase_backend.comom.enums.SubscriptionStatus;
import kotobase_backend.comom.enums.TransactionStatus;
import kotobase_backend.modules.payment.dto.response.PaymentResponse;
import kotobase_backend.modules.payment.entity.PaymentGatewayLog;
import kotobase_backend.modules.payment.entity.SubscriptionPlan;
import kotobase_backend.modules.payment.entity.Transaction;
import kotobase_backend.modules.payment.entity.UserSubscription;
import kotobase_backend.modules.payment.repository.PaymentGatewayLogRepository;
import kotobase_backend.modules.payment.repository.SubscriptionPlanRepository;
import kotobase_backend.modules.payment.repository.TransactionRepository;
import kotobase_backend.modules.payment.repository.UserSubscriptionRepository;
import kotobase_backend.modules.payment.service.PaymentService;
import kotobase_backend.modules.payment.util.VNPayUtil;
import kotobase_backend.modules.user.entity.User;
import kotobase_backend.security.config.VNPayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final VNPayConfig vnPayConfig;
    private final TransactionRepository transactionRepository;
    private final SubscriptionPlanRepository planRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PaymentGatewayLogRepository logRepository;


    @Transactional
    public PaymentResponse createPaymentLink(Integer planId, User user, HttpServletRequest request) {

        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Gói cước không tồn tại"));

        String orderId = "KOTO_" + System.currentTimeMillis();
        long amountVND = plan.getPrice().longValue();
        long amountVNPay = amountVND * 100L;

        Transaction transaction = Transaction.builder()
                .id(orderId)
                .user(user)
                .plan(plan)
                .amount(plan.getPrice())
                .status(TransactionStatus.PENDING)
                .build();
        transactionRepository.save(transaction);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnPayConfig.getVersion());
        vnp_Params.put("vnp_Command", vnPayConfig.getCommand());
        vnp_Params.put("vnp_TmnCode", vnPayConfig.getTmnCode());
        vnp_Params.put("vnp_Amount", String.valueOf(amountVNPay));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", orderId);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan goi " + plan.getName());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        vnp_Params.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));
        cld.add(Calendar.MINUTE, 15);
        vnp_Params.put("vnp_ExpireDate", formatter.format(cld.getTime()));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        try {
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                            .append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                    if (i < fieldNames.size() - 1) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
        } catch (Exception e) {
            log.error("Lỗi khi Encode URL", e);
            throw new RuntimeException("Lỗi hệ thống khi tạo link thanh toán");
        }

        String vnp_SecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        String paymentUrl = vnPayConfig.getPayUrl() + "?" + query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;

        return PaymentResponse.builder()
                .code("00")
                .message("Tạo link thanh toán thành công")
                .paymentUrl(paymentUrl)
                .orderId(orderId)
                .build();
    }



    @Transactional
    public String processIpnWebhook(HttpServletRequest request) {
        log.info("--- BẮT ĐẦU NHẬN IPN TỪ VNPAY ---");
        try {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(
                            URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()),
                            URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString())
                    );
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");

            String signValue = hashAllFields(fields);
            if (!signValue.equals(vnp_SecureHash)) {
                log.error("Sai chữ ký IPN. Khả năng giả mạo cao!");
                return "{\"RspCode\":\"97\",\"Message\":\"Invalid Checksum\"}";
            }

            String orderId = request.getParameter("vnp_TxnRef");
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
            long vnpAmount = Long.parseLong(request.getParameter("vnp_Amount")) / 100;

            PaymentGatewayLog logEntry = PaymentGatewayLog.builder()
                    .gatewayTransId(vnp_TransactionNo)
                    .resultCode(vnp_ResponseCode)
                    .rawPayload(request.getQueryString())
                    .build();

            Transaction transaction = transactionRepository.findById(orderId).orElse(null);
            if (transaction == null) {
                return "{\"RspCode\":\"01\",\"Message\":\"Order not found\"}";
            }

            logEntry.setTransaction(transaction);
            logRepository.save(logEntry);

            if (transaction.getStatus() != TransactionStatus.PENDING) {
                log.info("Đơn IPN {} đã được xử lý trước đó", orderId);
                return "{\"RspCode\":\"02\",\"Message\":\"Order already confirmed\"}";
            }

            if (transaction.getAmount().longValue() != vnpAmount) {
                return "{\"RspCode\":\"04\",\"Message\":\"Invalid amount\"}";
            }

            if ("00".equals(vnp_ResponseCode)) {
                transaction.setStatus(TransactionStatus.SUCCESS);
                transaction.setGatewayTransId(vnp_TransactionNo);

                grantPremiumAccess(transaction.getUser(), transaction.getPlan());
                log.info("!!! THÀNH CÔNG: Đã cấp Premium cho user_id = {} !!!", transaction.getUser().getId());
            } else {
                transaction.setStatus(TransactionStatus.FAILED);
                log.warn("Đơn hàng thất bại từ VNPay, mã lỗi: {}", vnp_ResponseCode);
            }

            transactionRepository.save(transaction);
            return "{\"RspCode\":\"00\",\"Message\":\"Confirm Success\"}";

        } catch (Exception e) {
            log.error("Lỗi sập hệ thống khi xử lý IPN: ", e);
            return "{\"RspCode\":\"99\",\"Message\":\"Unknown error\"}";
        }
    }

    private String hashAllFields(Map<String, String> fields) {
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fieldNames.size(); i++) {
            String fieldName = fieldNames.get(i);
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName).append("=").append(fieldValue);
                if (i < fieldNames.size() - 1) {
                    sb.append("&");
                }
            }
        }
        return VNPayUtil.hmacSHA512(vnPayConfig.getHashSecret(), sb.toString());
    }

    private void grantPremiumAccess(User user, SubscriptionPlan plan) {
        UserSubscription currentSub = userSubscriptionRepository
                .findFirstByUser_IdAndStatus(user.getId(), SubscriptionStatus.ACTIVE).orElse(null);

        if (currentSub != null && currentSub.getEndDate().isAfter(LocalDateTime.now())) {
            currentSub.setEndDate(currentSub.getEndDate().plusDays(plan.getDurationDays()));
            userSubscriptionRepository.save(currentSub);
        } else {
            UserSubscription newSub = UserSubscription.builder()
                    .user(user)
                    .plan(plan)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(plan.getDurationDays()))
                    .status(SubscriptionStatus.ACTIVE)
                    .build();
            userSubscriptionRepository.save(newSub);
        }
    }
}