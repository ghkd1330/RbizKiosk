package taco.rbiz.domain.model;

import lombok.Data;

/**
 * Admin 계정 정보
 */
@Data
public class Admin {

    private String username;
    private String password;

    public Admin() {

    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
