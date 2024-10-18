package taco.rbiz.web.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import taco.rbiz.domain.model.Cart;
import taco.rbiz.domain.model.Product;
import taco.rbiz.domain.service.CartService;
import taco.rbiz.domain.service.ProductService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @PostMapping("/addProduct")
    public Product addProduct(@RequestBody Map<String, Object> payload, HttpSession session) {
        log.info("addProduct : {}", payload);

        Product product = new Product();
        product.setProductName((String) payload.get("productName"));
        product.setDescription((String) payload.get("description"));
        product.setPrice(Double.parseDouble(payload.get("price").toString()));

        Map<String, Object> options = (Map<String, Object>) payload.get("selectedOptions");
//        if (options == null) {
//            options = new HashMap<>();
//        }
        product.setOptions(options);

        // Quantity는 1로 고정
        Object quantityObj = payload.get("quantity");
        int quantity = 1;
        if (quantityObj != null) {
            quantity = Integer.parseInt(quantityObj.toString());
        }
        product.setQuantity(quantity);

        productService.save(product);
        log.info("Save product : {}", product);

        // Session에서 Cart 객체 가져오기
        Cart cart = (Cart) session.getAttribute("userCart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("userCart", cart);
        }

        cartService.addItemToCart(cart, product);

        log.info("product : {}", product);
        log.info("cart : {}", cart);

        return product;
    }
}
