package kotobase_backend.modules.payment.service.impl;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.exceptions.CustomException.ResourceNotFoundException;
import kotobase_backend.modules.payment.dto.request.PlanAdminRequest;
import kotobase_backend.modules.payment.dto.response.PlanAdminResponse;
import kotobase_backend.modules.payment.entity.SubscriptionPlan;
import kotobase_backend.modules.payment.mapper.PlanAdminMapper;
import kotobase_backend.modules.payment.repository.SubscriptionPlanRepository;
import kotobase_backend.modules.payment.service.PlanAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanAdminServiceImpl implements PlanAdminService {

    private final SubscriptionPlanRepository planRepository;
    private final PlanAdminMapper planMapper;

    @Override
    public PageResponse<PlanAdminResponse> getAllPlans(String search, Boolean isActive, Pageable pageable) {
        Page<SubscriptionPlan> page = planRepository.adminSearchPlans(search, isActive, pageable);
        return PageResponse.of(page.map(planMapper::toResponse));
    }

    @Override
    public PlanAdminResponse getPlanById(Integer id) {
        SubscriptionPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy gói cước"));
        return planMapper.toResponse(plan);
    }

    @Override
    @Transactional
    public PlanAdminResponse createPlan(PlanAdminRequest request) {
        SubscriptionPlan plan = new SubscriptionPlan();
        planMapper.updateEntityFromRequest(request, plan);
        SubscriptionPlan saved = planRepository.save(plan);
        return planMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public PlanAdminResponse updatePlan(Integer id, PlanAdminRequest request) {
        SubscriptionPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy gói cước"));
        planMapper.updateEntityFromRequest(request, plan);
        SubscriptionPlan updated = planRepository.save(plan);
        return planMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public PlanAdminResponse toggleActiveStatus(Integer id) {
        SubscriptionPlan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy gói cước"));

        plan.setIsActive(!plan.getIsActive());
        SubscriptionPlan updated = planRepository.save(plan);

        return planMapper.toResponse(updated);
    }
}