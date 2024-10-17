package taco.rbiz.domain.model.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import taco.rbiz.domain.model.Admin;

import java.util.*;

@Slf4j
@Repository
public class AdminRepository {

    private static Map<Long, Admin> store = new HashMap<>();
    private static long sequence = 0L;

    public Admin createAccount(Admin admin) {
        admin.setId(++sequence);
        log.info("save: admin={}", admin);
        store.put(admin.getId(), admin);
        return admin;
    }

    public Admin findById(Long id) {
        return store.get(id);
    }

    public Optional<Admin> findByLoginId(String loginId) {
        return findAll().stream() // Admin 다 찾아서
                .filter(a -> a.getLoginId().equals(loginId)) // Parameter로 받은 loginId와 같은 Admin만 남기기
                .findFirst(); // 그중 첫 번째 Admin을 Return
    }

    public List<Admin> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
