package taco.rbiz.domain.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자의 장바구니
 */
@Data
public class Cart {

    private List<Product> items = new ArrayList<>();

    public void addItem(Product product) {
        items.add(product);
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(Product::getPrice).sum();
    }

    public int getTotalQuantity() {
        return items.size();
    }
}
