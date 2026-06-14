package kotobase_backend.modules.payment.mapper;

import kotobase_backend.modules.payment.dto.request.PlanAdminRequest;
import kotobase_backend.modules.payment.dto.response.PlanAdminResponse;
import kotobase_backend.modules.payment.entity.SubscriptionPlan;
import org.springframework.stereotype.Component;

@Component
public class PlanAdminMapper {
    public void updateEntityFromRequest(PlanAdminRequest request, SubscriptionPlan plan) {
        plan.setName(request.getName());
        plan.setDurationDays(request.getDurationDays());
        plan.setPrice(request.getPrice());
        plan.setDescription(request.getDescription());
        if (request.getIsActive() != null) {
            plan.setIsActive(request.getIsActive());
        } else if (plan.getId() == null) {
            plan.setIsActive(true);
        }
    }

    public PlanAdminResponse toResponse(SubscriptionPlan plan) {
        return PlanAdminResponse.builder()
                .id(plan.getId())
                .name(plan.getName())
                .durationDays(plan.getDurationDays())
                .price(plan.getPrice())
                .description(plan.getDescription())
                .isActive(plan.getIsActive())
                .build();
    }
}

