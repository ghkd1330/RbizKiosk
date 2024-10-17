package taco.rbiz.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taco.rbiz.domain.model.Admin;
import taco.rbiz.domain.model.repository.AdminRepository;

@Service
@RequiredArgsConstructor
public class AdminLoginService {

    private final AdminRepository adminRepository;

    /**
     * 아직 미사용
     * @param adminId :
     * @param password :
     * @return :
     */
    public Admin login(String adminId, String password) {
        return adminRepository.findByLoginId(adminId) // Parameter로 받은 loginId로 찾은 등록된 Admin들 중에
                .filter(m -> m.getPassword().equals(password)) // Parameter로 받은 password와 일치하는 Admin Return
                .orElse(null); // 없으면 Return Null
    }

    /**
     * 아직 미사용
     */
    public void logout() {

    }

    /**
     * 아직 미사용
     * @return :
     */
    public boolean isAuthenticated() {

        return false;
    }
}
