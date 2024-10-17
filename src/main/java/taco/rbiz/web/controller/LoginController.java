package taco.rbiz.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import taco.rbiz.domain.model.Admin;
import taco.rbiz.domain.service.AdminLoginService;
import taco.rbiz.web.login.SessionConst;
import taco.rbiz.web.controller.dto.LoginDTO;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final AdminLoginService adminLoginService;


    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@Validated @ModelAttribute("loginDTO") LoginDTO loginDTO, BindingResult bindingResult,
                               HttpServletRequest request, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("loginDTO", loginDTO);
            return "home";
        }

        Admin loginAdmin = adminLoginService.login(loginDTO.getAdminId(), loginDTO.getPassword());
        log.info("login? {}", loginAdmin);

        if (loginAdmin == null) {
            bindingResult.reject("loginFail", "ID 또는 Password 오류");
            model.addAttribute("loginDTO", loginDTO);
            return "home";
        }

        // Login 성공 처리
        // getSession() : Session이 있으면 있는 Session Return, 없으면 Create New Session
        HttpSession session = request.getSession();
        // Session에 Login 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_ADMIN, loginAdmin);

        // Redirect URL 적용
        return "redirect:/admin/adminPage";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
