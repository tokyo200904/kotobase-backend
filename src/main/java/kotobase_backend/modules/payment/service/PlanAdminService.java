package kotobase_backend.modules.payment.service;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.payment.dto.request.PlanAdminRequest;
import kotobase_backend.modules.payment.dto.response.PlanAdminResponse;
import org.springframework.data.domain.Pageable;

public interface PlanAdminService {
    PageResponse<PlanAdminResponse> getAllPlans(String search, Boolean isActive, Pageable pageable);
    PlanAdminResponse getPlanById(Integer id);
    PlanAdminResponse createPlan(PlanAdminRequest request);
    PlanAdminResponse updatePlan(Integer id, PlanAdminRequest request);
    PlanAdminResponse toggleActiveStatus(Integer id);
}
