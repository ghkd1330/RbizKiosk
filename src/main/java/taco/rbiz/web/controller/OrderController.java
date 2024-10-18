package taco.rbiz.web.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import taco.rbiz.domain.model.Cart;
import taco.rbiz.domain.model.Order;
import taco.rbiz.domain.service.OrderService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order/submit")
    public String submitOrder(@RequestParam(value = "diningOption", required = false) String diningOption,
                              @RequestParam(value = "paymentMethod", required = false) String paymentMethod,
                              @RequestParam(value = "receipt", required = false) String receipt,
                              HttpSession session,
                              Model model) {
        if (diningOption == null || paymentMethod == null) {
            // 에러 메시지와 함께 원래 페이지로 돌아감
            model.addAttribute("orderError", true);
            model.addAttribute("missingDiningOption", diningOption == null);
            model.addAttribute("missingPaymentMethod", paymentMethod == null);
            // 장바구니 정보를 다시 모델에 추가
            Cart cart = (Cart) session.getAttribute("userCart");
            model.addAttribute("cart", cart);
            model.addAttribute("totalPrice", cart.getTotalPrice());
            model.addAttribute("totalQuantity", cart.getTotalQuantity());
            return "main/main"; // 원래 페이지로 돌아감
        }

        // 주문 처리 로직
        boolean isTakeout = "takeout".equals(diningOption);
        boolean isReceipt = receipt != null;

        // Order 객체 생성
        Order order = new Order();
        Cart cart = (Cart) session.getAttribute("userCart");
        order.setProducts(cart.getItems());
        order.setTakeout(isTakeout);
        order.setPaymentMethod(paymentMethod);
        order.setReceipt(isReceipt);

        // 주문 저장 또는 처리
        orderService.saveOrder(order);

        // 주문 완료 후 장바구니 비우기
        session.removeAttribute("userCart");

        // 주문 완료 플래그 설정
        model.addAttribute("orderSuccess", true);

        // **빈 장바구니를 모델에 추가**
        Cart emptyCart = new Cart();
        model.addAttribute("cart", emptyCart);
        model.addAttribute("totalPrice", emptyCart.getTotalPrice());
        model.addAttribute("totalQuantity", emptyCart.getTotalQuantity());

        // 원래 페이지로 돌아감
        return "main/main";
    }

}
