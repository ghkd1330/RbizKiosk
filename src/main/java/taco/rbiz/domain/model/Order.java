package taco.rbiz.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 주문 정보를 담는다
 */
@Data
public class Order {

    private String id; // 주문 ID, UUID로 생성
    private List<Product> products; // 주문한 상품 목록
    private boolean takeout; // 포장 여부, true면 포장, false면 매장식사
    private String paymentMethod; // 결제 방법, "cash" 또는 "credit"
    private boolean receipt; // 영수증 출력 여부
    private LocalDateTime orderTime; // 주문 시간

    public Order() {
        this.id = UUID.randomUUID().toString();
        this.orderTime = LocalDateTime.now();
    }

    public Order(List<Product> products, boolean takeout, String paymentMethod, boolean receipt) {
        this();
        this.products = products;
        this.takeout = takeout;
        this.paymentMethod = paymentMethod;
        this.receipt = receipt;
    }
}
