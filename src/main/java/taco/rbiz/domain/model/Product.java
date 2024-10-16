package taco.rbiz.domain.model;

import lombok.Data;

import java.util.List;

/**
 * 상품 정보를 담는다
 */
@Data
public class Product {

    private Long id;
    private String productName;
    private String description;
    private double price;
    private List<Option> options;

    public Product() {

    }

    public Product(String productName, String description, double price, List<Option> options) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.options = options;
    }
}
