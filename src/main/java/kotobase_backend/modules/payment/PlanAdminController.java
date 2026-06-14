package kotobase_backend.modules.payment;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.modules.payment.dto.request.PlanAdminRequest;
import kotobase_backend.modules.payment.dto.response.PlanAdminResponse;
import kotobase_backend.modules.payment.service.PlanAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/plans")
@RequiredArgsConstructor
public class PlanAdminController {
    private final PlanAdminService planAdminService;

    @GetMapping
    public ResponseEntity<PageResponse<PlanAdminResponse>> getAllPlans(@RequestParam(required = false) String search,
                                                                       @RequestParam(required = false) Boolean isActive,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(planAdminService.getAllPlans(search, isActive, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanAdminResponse> getPlanById(@PathVariable Integer id) {
        return ResponseEntity.ok(planAdminService.getPlanById(id));
    }

    @PostMapping
    public ResponseEntity<PlanAdminResponse> createPlan(@RequestBody PlanAdminRequest request) {
        return ResponseEntity.ok(planAdminService.createPlan(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanAdminResponse> updatePlan(@PathVariable Integer id,
                                                        @RequestBody PlanAdminRequest request) {
        return ResponseEntity.ok(planAdminService.updatePlan(id, request));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<PlanAdminResponse> toggleStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(planAdminService.toggleActiveStatus(id));
    }
}