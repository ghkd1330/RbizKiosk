package taco.rbiz.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String goHome() {
        return "home";
    }

    // 임시 (home(주문하기) -> main)
    @GetMapping("/main")
    public String goMain() {
        return "main/main";
    }

    // 임시 (
}
