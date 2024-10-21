package taco.rbiz.domain.service;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import taco.rbiz.domain.model.Order;
import taco.rbiz.domain.model.Product;
import taco.rbiz.domain.model.util.OrderQueue;

import java.util.Map;

@Slf4j
@Service
public class ModbusService {

    @Value("${master.ip}")
    private String MASTER_IP; // Master IP

    private static final int MASTER_PORT = 502; // Modbus TCP 기본 포트
    private static final int SLAVE_ID = 255; // Slave ID
    private final OrderQueue orderQueue;

    public ModbusService(OrderQueue orderQueue) {
        this.orderQueue = orderQueue;
    }

    private ModbusTCPMaster master;

    @PostConstruct
    public void init() {
        connect();
    }

    @PreDestroy
    public void cleanup() {
        if (master != null) {
            master.disconnect();
        }
    }

    private void connect() {
        try {
            master = new ModbusTCPMaster(MASTER_IP, MASTER_PORT);
            master.setTimeout(3000);
            master.connect();
            log.info("Connected to Modbus Master at {}:{}", MASTER_IP, MASTER_PORT);
        } catch (Exception e) {
            log.error("Failed to connect to Modbus Master: ", e);
            master = null;
        }
    }

    @Scheduled(fixedDelay = 1000) // 1초마다 실행
    public void checkAndSendOrder() {
        try {
            if (master == null) {
                connect();
            }

            if (master != null) {
                if (!orderQueue.isEmpty()) {
                    // 주소 114, 115, 116 읽기
                    InputRegister[] registers = master.readMultipleRegisters(SLAVE_ID, 114, 3);
                    int flag114 = registers[0].getValue();
                    int flag115 = registers[1].getValue();
                    int flag116 = registers[2].getValue();
                    log.info("Flags: 114={}, 115={}, 116={}", flag114, flag115, flag116);

                    if (flag114 == 0 && flag115 == 0 && flag116 == 0) {
                        // 수신 가능 상태
                        Order order = orderQueue.pollOrder(); // 큐에서 주문 가져오기
                        if (order != null) {
                            // wait(100) 밀리초 (필요에 따라 추가)
                            Thread.sleep(100);

                            // 주소 100에 flag 1로 설정
                            master.writeSingleRegister(SLAVE_ID, 100, new SimpleRegister(1));

                            // 주문 정보 전송 (101 ~ 112)
                            sendOrderToModbus(order);

                            log.info("Order sent to Modbus: {}", order.getId());
                        }
                    }
                }
            } else {
                log.warn("Modbus Master is not connected.");
            }
        } catch (Exception e) {
            log.error("Error in ModbusService: ", e);
            // 연결 문제가 발생하면 마스터를 닫고 재연결 시도
            if (master != null) {
                master.disconnect();
                master = null;
            }
        }
    }

    private void sendOrderToModbus(Order order) throws ModbusException {
        // 최대 4개의 제품 단위를 처리하기 위한 배열 초기화
        int[] productCodes = {5, 5, 5, 5}; // 기본값 5
        int[] saladOptions = {1, 1, 1, 1}; // 기본값 1로 변경
        int[] drinkOptions = {0, 0, 0, 0}; // 기본값 0

        // 제품 코드 매핑
        for (int i = 0, index = 0; i < order.getProducts().size() && index < 4; i++) {
            Product product = order.getProducts().get(i);
            int quantity = product.getQuantity();

            for (int q = 0; q < quantity && index < 4; q++, index++) {
                // 제품 코드 결정
                int productCode = getProductCode(product);
                productCodes[index] = productCode;

                // 샐러드 옵션 처리
                saladOptions[index] = hasSaladOption(product) ? 0 : 1;

                // 음료 옵션 처리
                drinkOptions[index] = getDrinkOptionCode(product);
            }
        }

        // 주소 101~104에 제품 코드 전송
        for (int i = 0; i < 4; i++) {
            log.info("Product Code {}: {}", i, productCodes[i]);
            master.writeSingleRegister(SLAVE_ID, 101 + i, new SimpleRegister(productCodes[i]));
        }

        // 주소 105~108에 샐러드 옵션 전송
        for (int i = 0; i < 4; i++) {
            log.info("Salad Option {}: {}", i, saladOptions[i]);
            master.writeSingleRegister(SLAVE_ID, 105 + i, new SimpleRegister(saladOptions[i]));
        }

        // 주소 109~112에 음료 옵션 전송
        for (int i = 0; i < 4; i++) {
            log.info("Drink Option {}: {}", i, drinkOptions[i]);
            master.writeSingleRegister(SLAVE_ID, 109 + i, new SimpleRegister(drinkOptions[i]));
        }
    }

    private int getProductCode(Product product) {
        String productName = product.getProductName();
        Map<String, Object> options = product.getOptions();

        if (productName.contains("소고기")) {
            String grillingOption = (String) options.get("grilling");
            if (grillingOption != null) {
                if (grillingOption.contains("레어")) {
                    return 1;
                } else if (grillingOption.contains("미디움")) {
                    return 2;
                } else if (grillingOption.contains("웰던")) {
                    return 3;
                }
            }
        } else if (productName.contains("소시지")) {
            return 4;
        }
        // 매칭되지 않는 경우 기본값 5
        return 5;
    }

    private boolean hasSaladOption(Product product) {
        Map<String, Object> options = product.getOptions();
        // salad가 키로 존재하지 않으면 true 반환
        if (!options.containsKey("salad")) {
            return true;
        }
        // salad가 키로 존재하고, 값이 null이 아니고 빈 리스트가 아니라면 false 반환
        Object saladOption = options.get("salad");
        return saladOption == null || !((Iterable<?>) saladOption).iterator().hasNext();
    }

    private int getDrinkOptionCode(Product product) {
        Map<String, Object> options = product.getOptions();
        Object drinkOption = options.get("drink");

        if (drinkOption != null) {
            if (drinkOption.equals("콜라")) {
                return 1;
            } else if (drinkOption.equals("사이다")) {
                return 2;
            }
        }
        // 음료 옵션이 없거나 매칭되지 않는 경우 0
        return 0;
    }

}
