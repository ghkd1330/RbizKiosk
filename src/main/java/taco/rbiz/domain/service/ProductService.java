package taco.rbiz.domain.service;

import org.springframework.stereotype.Service;
import taco.rbiz.domain.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private static final Map<Long, Product> store = new HashMap<>();
    private static long sequence = 0L;

    /**
     * 아직 미사용
     * @param product
     * @return
     */
    public Product save(Product product) {
        product.setId(++sequence);
        store.put(product.getId(), product);
        return product;
    }

    public Product findById(Long id) {
        return store.get(id);
    }

    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long productId, Product updateParam) {
        Product findProduct = findById(productId);
        findProduct.setProductName(updateParam.getProductName());
        findProduct.setPrice(updateParam.getPrice());
//        findProduct.setDescription(updateParam.getDescription());
//        findProduct.setOptions(updateParam.getOptions());
    }

    public void clearStore() {
        store.clear();
    }
}
