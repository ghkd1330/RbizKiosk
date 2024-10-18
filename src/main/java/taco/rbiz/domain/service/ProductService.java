package taco.rbiz.domain.service;

import org.springframework.stereotype.Service;
import taco.rbiz.domain.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private static final Map<String, Product> store = new HashMap<>();

    /**
     * @param product : ProductController(RestController)에서 받아온 상품 객체
     * @return : Product 객체를 Return
     */
    public Product save(Product product) {
        store.put(product.getId(), product);
        return product;
    }

    public Product findById(String id) {
        return store.get(id);
    }

    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(String productId, Product updateParam) {
        Product findProduct = findById(productId);
        if (findProduct != null) {
            findProduct.setProductName(updateParam.getProductName());
            findProduct.setPrice(updateParam.getPrice());
            findProduct.setDescription(updateParam.getDescription());
            findProduct.setOptions(updateParam.getOptions());
            findProduct.setQuantity(updateParam.getQuantity());
        }
    }

    public void clearStore() {
        store.clear();
    }
}
