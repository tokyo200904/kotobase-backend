package kotobase_backend.modules.user.dto.response;

import kotobase_backend.modules.payment.dto.response.TransactionAdminResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDetailAdminResponse {
    private UserAdminResponse userInfo;
    private List<TransactionAdminResponse> paymentHistory;
}