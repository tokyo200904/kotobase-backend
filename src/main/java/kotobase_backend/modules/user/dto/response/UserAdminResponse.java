package kotobase_backend.modules.user.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserAdminResponse {
    private Integer id;
    private String fullName;
    private String email;
    private String provider;
    private Boolean isEnabled;
    private Boolean isPremium;
    private LocalDateTime createdAt;
}