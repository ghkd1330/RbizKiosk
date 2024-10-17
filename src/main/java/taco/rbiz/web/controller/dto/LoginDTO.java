package taco.rbiz.web.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDTO {

    @NotEmpty
    private String adminId;
    @NotEmpty
    private String password;
}
