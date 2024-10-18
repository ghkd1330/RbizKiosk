package taco.rbiz.domain.service;

import org.springframework.stereotype.Service;
import taco.rbiz.domain.model.Cart;
import taco.rbiz.domain.model.Product;

import java.util.Iterator;

@Service
public class CartService {

    /**
     * ItemDetailModal -> 추가 버튼 누르면 장바구니에 상품 추가
     * @param cart : 담을 장바구니 객체
     * @param product : 장바구니에 추가할 상품 객체
     */
    public void addItemToCart(Cart cart, Product product) {
        for (Product item : cart.getItems()) {
            if (item.getProductName().equals(product.getProductName())
                && item.getOptions().equals(product.getOptions())) {
                // 동일 상품 및 옵션일 때 수량만 증가
                item.setQuantity(item.getQuantity() + product.getQuantity());
                return;
            }
        }

        // 새로운 Product 생성하여 장바구니에 추가
        cart.addItem(product);
    }

    /**
     * Main -> 장바구니 Footer에서 X 누르면 장바구니에서 해당 상품 삭제
     * @param cart : 장바구니 객체
     * @param productId : 장바구니에 담긴 Product 객체의 id
     */
    public void removeItemFromCart(Cart cart, String productId) {
        cart.getItems().removeIf(product -> product.getId().equals(productId));

    }

    /**
     * Main -> 장바구니 Footer에서 +, - 버튼 누르면 수량 변경
     * @param cart : 장바구니 객체
     * @param productId : 장바구니에 담긴 수정할 상품 객체의 id
     * @param quantityChange : 더할 숫자 -> int quantity = 1 or -1
     */
    public void updateItemQuantity(Cart cart, String productId, int quantityChange) {
        Iterator<Product> iterator = cart.getItems().iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getId().equals(productId)) {
                int newQuantity = product.getQuantity() + quantityChange;
                if (newQuantity <= 0) {
                    iterator.remove();
                } else {
                    product.setQuantity(newQuantity);
                }
                break;
            }
        }
    }

    /**
     * Main -> 장바구니 Footer에서 총 금액과 수량을 확인할 수 있도록
     *         cartItem 객체의 총 수량과 cartItem 객체들의 총 금액을 구함
     * @param cart : 장바구니 객체
     * @return : 총 금액 및 총 수량 Return
     */
    public double getCartTotal(Cart cart) {

        return 0;
    }
}
