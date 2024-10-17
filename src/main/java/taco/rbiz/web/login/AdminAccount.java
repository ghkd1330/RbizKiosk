package taco.rbiz.web.login;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import taco.rbiz.domain.model.Admin;
import taco.rbiz.domain.model.repository.AdminRepository;

@Component
@RequiredArgsConstructor
public class AdminAccount {

    private final AdminRepository adminRepository;

    @PostConstruct
    public void init() {
        // Admin 계정 생성
         adminRepository.createAccount(new Admin("admin", "1234"));
    }
}
