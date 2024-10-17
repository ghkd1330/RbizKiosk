package taco.rbiz.domain.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Admin 계정 정보
 */
@Data
public class Admin {

    private Long id; // ID for DB

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String password;

    public Admin(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
