package taco.rbiz.domain.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 상품 정보를 담는다
 */
@Data
public class Product {

    private String id;
    private String productName;
    private String description;
    private double price;
    private Map<String, Object> options = new HashMap<>();
    private int quantity;

    public Product() {
        // 상품 생성 시 고유한 ID 할당
        this.id = UUID.randomUUID().toString();
    }

    public Product(String productName, String description, double price, Map<String, Object> options, int quantity) {
        this();
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.options = options;
        this.quantity = quantity;
    }

    @JsonAnySetter
    public void addOption(String key, Object value) {
        this.options.put(key, value);
    }
}

