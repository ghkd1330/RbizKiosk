package taco.rbiz.domain.model;

import lombok.Data;

import java.util.Map;

/**
 * 장바구니에 담긴 개별 상품 항목들
 */
@Data
public class CartItem {

    private Product product; // 장바구니에 담긴 상품
    private Integer quantity; // 그리고 그 상품이 몇개 담겼는지
    private Map<String, String> selectedOptions;

    public CartItem() {

    }

    public CartItem(Product product, Integer quantity, Map<String, String> selectedOptions) {
        this.product = product;
        this.quantity = quantity;
        this.selectedOptions = selectedOptions;
    }
}
