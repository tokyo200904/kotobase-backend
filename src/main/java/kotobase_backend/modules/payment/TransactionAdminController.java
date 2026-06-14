package kotobase_backend.modules.payment;

import kotobase_backend.comom.dto.response.PageResponse;
import kotobase_backend.comom.enums.TransactionStatus;
import kotobase_backend.modules.payment.dto.response.TransactionAdminResponse;
import kotobase_backend.modules.payment.dto.response.TransactionDetailAdminResponse;
import kotobase_backend.modules.payment.service.TransactionAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/admin/transaction")
@RequiredArgsConstructor
public class TransactionAdminController {
    private final TransactionAdminService transactionService;

    @GetMapping
    public ResponseEntity<PageResponse<TransactionAdminResponse>> getAllTransactions(@RequestParam(required = false) String search,
                                                                                     @RequestParam(required = false) TransactionStatus status,
                                                                                     @RequestParam(defaultValue = "0") int page,
                                                                                     @RequestParam(defaultValue = "15") int size) {

        return ResponseEntity.ok(transactionService.getAllTransactions(search, status, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDetailAdminResponse> getTransactionDetail(@PathVariable String id) {
        return ResponseEntity.ok(transactionService.getTransactionDetail(id));
    }
}