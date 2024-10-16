package taco.rbiz.domain.service;

import org.springframework.stereotype.Service;
import taco.rbiz.domain.model.Cart;
import taco.rbiz.domain.model.Product;

import java.util.Map;

@Service
public class CartService {

    /**
     * ItemDetailModal -> 추가 버튼 누르면 장바구니에 상품 추가
     * @param cart : 담을 장바구니 객체
     * @param product : 장바구니에 추가할 상품 객체
     * @param selectedOptions : 장바구니에 추가하는 상품의 옵션 객체
     */
    public void addItemToCart(Cart cart, Product product, Map<String, String> selectedOptions) {

    }

    /**
     * Main -> 장바구니 Footer에서 X 누르면 장바구니에서 해당 상품 삭제
     * @param cart : 장바구니 객체
     * @param cartItemId : 장바구니에 담긴 CartItem 객체의 id
     */
    public void removeItemFromCart(Cart cart, String cartItemId) {

    }

    /**
     * Main -> 장바구니 Footer에서 +, - 버튼 누르면 수량 변경
     * @param cart : 장바구니 객체
     * @param productId : 장바구니에 담긴 수정할 상품 객체의 id
     * @param quantity : 더할 숫자 -> int quantity = 1 or -1
     */
    public void updateItemQuantity(Cart cart, String productId, int quantity) {

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
