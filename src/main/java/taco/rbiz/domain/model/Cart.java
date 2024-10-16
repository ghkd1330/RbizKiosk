package taco.rbiz.domain.model;

import lombok.Data;

import java.util.List;

/**
 * 사용자의 장바구니
 */
@Data
public class Cart {

    private List<CartItem> items;
}
