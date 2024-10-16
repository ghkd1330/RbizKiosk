package taco.rbiz.domain.service;

import org.springframework.stereotype.Service;
import taco.rbiz.domain.model.Cart;
import taco.rbiz.domain.model.Order;
import taco.rbiz.domain.model.enums.PackingOption;

@Service
public class OrderService {

    /**
     * Order Modal -> 결제 버튼 누르면 주문 객체 생성
     * @param cart : 주문 객체에 들어갈 장바구니 객체(주문된 상품들 정보)
     * @param packingOption : 주문 객체에 포함될 포장/매장 여부
     * @param receiptOption : 주문 객체에 포함될 영수증 출력 여부
     * @return : 주문 객체 Return
     */
    public Order createOrder(Cart cart, PackingOption packingOption, boolean receiptOption) {

        return null;
    }

    /**
     * 아직 미사용
     * Admin Page -> 실시간 주문 스택에서 확인할 수 있도록 주문 정보 조회
     * @param orderId : 주문 번호를 통해 주문 객체 조회
     * @return : 주문 객체 Return
     */
    public Order getOrderById(String orderId) {

        return null;
    }
}
