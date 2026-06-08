package kotobase_backend.modules.payment.service;

public interface PremiumGuardService {
    public boolean check(Integer userId);
    public void enforcePremium(Integer userId, String message);
}
