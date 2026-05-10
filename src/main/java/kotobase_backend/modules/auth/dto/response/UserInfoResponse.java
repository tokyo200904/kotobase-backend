package kotobase_backend.modules.auth.dto.response;

import kotobase_backend.comom.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    private Integer id;
    private String email;
    private RoleName role;
    private String fullName;
    private String photo;
}
