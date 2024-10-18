package taco.rbiz.web.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import taco.rbiz.domain.model.Cart;
import taco.rbiz.domain.model.Product;
import taco.rbiz.domain.service.CartService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

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

    @PostMapping("/removeItem")
    @ResponseBody // JSON 응답을 반환하기 위해 @ResponseBody 추가
    public ResponseEntity<?> removeItem(@RequestBody Map<String, String> payload, HttpSession session) {
        String productId = payload.get("productId");
        Cart cart = (Cart) session.getAttribute("userCart");
        if (cart != null) {
            cartService.removeItemFromCart(cart, productId);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateQuantity")
    @ResponseBody // JSON 응답을 반환하기 위해 @ResponseBody 추가
    public ResponseEntity<?> updateQuantity(@RequestBody Map<String, Object> payload, HttpSession session) {
        String productId = (String) payload.get("productId");
        int quantityChange = Integer.parseInt(payload.get("quantityChange").toString());
        Cart cart = (Cart) session.getAttribute("userCart");
        if (cart != null) {
            cartService.updateItemQuantity(cart, productId, quantityChange);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/summary")
    @ResponseBody // JSON 응답을 반환하기 위해 @ResponseBody 추가
    public ResponseEntity<Map<String, Object>> getCartSummary(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("userCart");
        int totalQuantity = 0;
        double totalPrice = 0.0;
        if (cart != null) {
            for (Product product : cart.getItems()) {
                totalQuantity += product.getQuantity();
                totalPrice += product.getPrice() * product.getQuantity();
            }
        }
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalQuantity", totalQuantity);
        summary.put("totalPrice", totalPrice);
        return ResponseEntity.ok(summary);
    }
}
