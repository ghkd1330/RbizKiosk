package taco.rbiz.domain.service;

import org.springframework.stereotype.Service;
import taco.rbiz.domain.model.ChatMessage;

import java.util.List;

@Service
public class ChatService {

    /**
     * Admin이나 Client가 채팅을 입력하면 ChatMessage 객체를 생성하여 저장
     * @param message : 채팅 메시지 객체
     */
    public void sendMessage(ChatMessage message) {

    }

    /**
     * 종료된 채팅 내용을 확인할 수 있도록 ChatMessage 객체들을 조회
     * @return : ChatMessage 객체들
     */
    public List<ChatMessage> getChatHistory() {

        return null;
    }
}
