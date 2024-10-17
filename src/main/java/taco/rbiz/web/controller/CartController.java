package taco.rbiz.web.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import taco.rbiz.domain.model.Cart;

@Slf4j
@Controller
@RequestMapping("/cart")
public class CartController {

    @PostMapping("/create")
    public String createCart(HttpSession session) {
        log.info("createCart");

        Cart cart = new Cart();
        session.setAttribute("userCart", cart);
        return "redirect:/main";
    }

    @PostMapping("/delete")
    public String deleteCart(HttpSession session) {
        log.info("deleteCart");

        session.removeAttribute("userCart");
        return "redirect:/";
    }
}
