package kotobase_backend.modules.payment.service;

import jakarta.servlet.http.HttpServletRequest;
import kotobase_backend.modules.payment.dto.response.PaymentResponse;
import kotobase_backend.modules.user.entity.User;

public interface PaymentService {
    public PaymentResponse createPaymentLink(Integer planId, User user, HttpServletRequest request);
    public String processIpnWebhook(HttpServletRequest request);

}
