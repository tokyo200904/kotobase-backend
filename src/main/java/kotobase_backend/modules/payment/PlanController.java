package kotobase_backend.modules.payment;

import kotobase_backend.modules.payment.dto.response.PlanResponse;
import kotobase_backend.modules.payment.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @GetMapping
    public ResponseEntity<List<PlanResponse>> getActivePlans() {
        return ResponseEntity.ok(planService.getActivePlans());
    }
}
