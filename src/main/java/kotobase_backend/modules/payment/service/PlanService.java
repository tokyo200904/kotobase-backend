package kotobase_backend.modules.payment.service;

import kotobase_backend.modules.payment.dto.response.PlanResponse;

import java.util.List;

public interface PlanService {
    List<PlanResponse> getActivePlans();
}
