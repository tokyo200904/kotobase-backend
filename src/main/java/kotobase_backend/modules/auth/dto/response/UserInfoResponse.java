package kotobase_backend.modules.auth.dto.response;

import kotobase_backend.comom.enums.RoleName;
import lombok.Data;

@Data
public class UserInfoResponse {
    private Integer id;
    private String email;
    private RoleName role;
    private String fullName;
    private String photo;
    private Boolean isEnabled;

}
