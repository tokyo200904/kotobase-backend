package kotobase_backend.modules.payment.service.impl;

import kotobase_backend.modules.payment.dto.response.PlanResponse;
import kotobase_backend.modules.payment.entity.SubscriptionPlan;
import kotobase_backend.modules.payment.mapper.PlanMapper;
import kotobase_backend.modules.payment.repository.SubscriptionPlanRepository;
import kotobase_backend.modules.payment.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final SubscriptionPlanRepository planRepository;
    private final PlanMapper planMapper;

    @Override
    public List<PlanResponse> getActivePlans() {
        List<SubscriptionPlan> plans = planRepository.findByIsActiveTrueOrderByPriceAsc();

        return plans.stream()
                .map(planMapper::toPlanResponse)
                .toList();
    }
}
