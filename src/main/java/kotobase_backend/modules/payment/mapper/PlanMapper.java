package kotobase_backend.modules.payment.mapper;

import kotobase_backend.modules.payment.dto.response.PlanResponse;
import kotobase_backend.modules.payment.entity.SubscriptionPlan;
import org.springframework.stereotype.Component;

@Component
public class PlanMapper {
    public PlanResponse toPlanResponse(SubscriptionPlan plan) {
        PlanResponse response = new PlanResponse();
        response.setId(plan.getId());
        response.setName(plan.getName());
        response.setDescription(plan.getDescription());
        response.setPrice(plan.getPrice());
        response.setDurationDays(plan.getDurationDays());
        return response;
    }
}
