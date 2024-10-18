package taco.rbiz.web;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import taco.rbiz.domain.model.Cart;
import taco.rbiz.domain.model.Product;
import taco.rbiz.web.controller.dto.LoginDTO;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "home";
    }

    @GetMapping("/main")
    public String goMain(HttpSession session, Model model) {
        // Session에서 Cart 객체 가져오기
        Cart cart = (Cart) session.getAttribute("userCart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("userCart", cart);
        }
        model.addAttribute("cart", cart);
        log.info("Cart items: {}", cart.getItems());

        // 총 수량과 총 금액 계산
        int totalQuantity = 0;
        double totalPrice = 0.0;
        for (Product product : cart.getItems()) {
            totalQuantity += product.getQuantity();
            totalPrice += product.getPrice() * product.getQuantity();
        }
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("totalPrice", totalPrice);

        return "main/main";
    }

    @GetMapping("/modbus")
    public String goModbus() {
        return "index";
    }

    @GetMapping("/test")
    public String goTest() {
        return "uiTest";
    }
}
