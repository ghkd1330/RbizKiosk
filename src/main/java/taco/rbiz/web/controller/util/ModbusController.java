package taco.rbiz.web.controller.util;

import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ModbusController {

    private static final String MASTER_IP = "192.168.137.102"; // Master IP
    private static final int MASTER_PORT = 502; // Modbus TCP 기본 포트
    private static final int SLAVE_ID = 255; // Slave ID
//    private static final int ADDRESS = 1; // 고정된 Holding Register 주소

    @PostMapping("/sendSignal")
    public String sendSignal(@RequestParam("value") int value, @RequestParam("address") int address, Model model) {
        // value가 1 ~ 255 사이의 값인지 확인
        if (value < 1 || value > 255) {
            model.addAttribute("message", "값이 유효하지 않습니다. 1 ~ 255 사이의 값을 입력하세요.");
            return "index";
        }

        ModbusTCPMaster master = null;
        try {
            // Modbus TCP Master 초기화
            master = new ModbusTCPMaster(MASTER_IP, MASTER_PORT);
            master.setTimeout(3000); // Set Timeout
            master.connect();

            // Holding Register에 값 쓰기 (Master에 신호 전송)
            master.writeSingleRegister(SLAVE_ID, address, new SimpleRegister(value));

            // 성공 메시지 반환
            model.addAttribute("message", "Modbus 신호 전송 성공: 주소 " + address + "에 값 " + value + "을(를) 보냈습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Modbus 신호 전송 실패: " + e.getMessage());
        } finally {
            if (master != null) {
                master.disconnect();
            }
        }

        return "index"; // 결과 페이지로 이동
    }

    @GetMapping("/readSignal")
    public String readSignal(@RequestParam("address-read") int address, Model model) {
        if (address < 0) {
            model.addAttribute("message", "주소가 유효하지 않습니다. 0 이상의 값을 입력하세요.");
            return "index";
        }

        try {
            // Modbus TCP Master 초기화
            ModbusTCPMaster master = new ModbusTCPMaster(MASTER_IP, MASTER_PORT);
            master.connect();

            // Holding Register에서 값 읽기 (Master에서 신호 읽기)
            InputRegister[] registers = master.readMultipleRegisters(SLAVE_ID, address, 1);
            int value = registers[0].getValue();

            // 성공 메시지 반환
            model.addAttribute("message", "Modbus 신호 읽기 성공: 주소 " + address + "에서 값 " + value + "을(를) 읽었습니다.");
            master.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Modbus 신호 읽기 실패: " + e.getMessage());
        }

        return "index";
    }
}
