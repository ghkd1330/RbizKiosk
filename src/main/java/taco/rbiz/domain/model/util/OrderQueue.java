package taco.rbiz.domain.model.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import taco.rbiz.domain.model.Order;

import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
public class OrderQueue {

    private final ConcurrentLinkedQueue<Order> orderQueue = new ConcurrentLinkedQueue<>();

    public void addOrder(Order order) {
        orderQueue.add(order);
    }

    public Order pollOrder() {
        return orderQueue.poll();
    }

    public boolean isEmpty() {
        return orderQueue.isEmpty();
    }
}
