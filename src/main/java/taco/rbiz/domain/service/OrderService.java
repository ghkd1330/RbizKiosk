package taco.rbiz.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import taco.rbiz.domain.model.Order;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();

    /**
     * Order Modal -> 결제 버튼 누르면 주문 객체 생성
     * @param order : 주문 스택에 들어갈 주문 객체
     */
    public void saveOrder(Order order) {
        orders.add(order);
        log.info("Order saved: {}", order);
    }

    /**
     * 아직 미사용
     * Admin Page -> 실시간 주문 스택에서 확인할 수 있도록 모든 주문 정보 조회
     * @return : 주문 객체들 Return
     */
    public List<Order> getAllOrders() {
        return orders;
    }
}
