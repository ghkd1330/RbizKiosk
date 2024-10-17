package taco.rbiz.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import taco.rbiz.domain.model.Admin;
import taco.rbiz.domain.service.AdminLoginService;
import taco.rbiz.web.login.SessionConst;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminLoginService adminLoginService;

    @GetMapping("/admin/adminPage")
    public String adminPage(HttpServletRequest request, Model model) {
        log.info("AdminController: Accessing adminPage");

        HttpSession session = request.getSession(false);
        Admin loginAdmin = (Admin) session.getAttribute(SessionConst.LOGIN_ADMIN);
        model.addAttribute("admin", loginAdmin);

        if (loginAdmin == null) {
            return "redirect:/";
        }

        return "admin/adminPage"; // Return the view name for the admin page
    }

}
