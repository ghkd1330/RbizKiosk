package taco.rbiz.domain.model;

import lombok.Data;
import taco.rbiz.domain.model.enums.OrderStatus;
import taco.rbiz.domain.model.enums.PackingOption;

import java.time.LocalDateTime;

/**
 * 주문 정보를 담는다
 */
@Data
public class Order {

    private String orderNumber;
    private Cart cart;
    private double totalAmount;
    private OrderStatus status;
    private LocalDateTime timeStamp;
    private PackingOption packingOption;
    private boolean receiptOption;

    public Order() {

    }

    public Order(String orderNumber, Cart cart, double totalAmount, OrderStatus status,
                 LocalDateTime timeStamp, PackingOption packingOption, boolean receiptOption) {
        this.orderNumber = orderNumber;
        this.cart = cart;
        this.totalAmount = totalAmount;
        this.status = status;
        this.timeStamp = timeStamp;
        this.packingOption = packingOption;
        this.receiptOption = receiptOption;
    }
}
