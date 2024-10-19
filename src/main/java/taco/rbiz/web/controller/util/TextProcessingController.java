package taco.rbiz.web.controller.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
public class TextProcessingController {

    @Value("${openai.api-key}")
    private String openaiApiKey;

    @PostMapping(value = "/process-text", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, String>> processText(
            @org.springframework.web.bind.annotation.RequestBody Map<String, String> payload,
            HttpSession session) {

        String userMessage = payload.get("message");

        // 세션에서 대화 이력 가져오기
        List<Message> conversation = (List<Message>) session.getAttribute("conversation");
        if (conversation == null) {
            conversation = new ArrayList<>();
            session.setAttribute("conversation", conversation);
        }

        // 사용자의 새로운 메시지를 대화 이력에 추가
        conversation.add(new Message("user", userMessage));

        try {
            // 대화 이력을 사용하여 GPT 응답 생성
            String reply = getChatGPTResponse(conversation);

            // GPT의 응답을 대화 이력에 추가
            conversation.add(new Message("assistant", reply));

            Map<String, String> response = new HashMap<>();
            response.put("reply", reply);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("reply", "답변 생성 중 오류 발생"));
        }
    }

    private String getChatGPTResponse(List<Message> conversation) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // 대화 이력을 사용하여 요청 페이로드 생성
        JSONObject json = getJsonObject(conversation);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + openaiApiKey)
                .build();

        Response response = client.newCall(request).execute();

        if (response.isSuccessful()) {
            String responseBody = response.body().string();
            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray choices = responseJson.getJSONArray("choices");
            String reply = choices.getJSONObject(0).getJSONObject("message").getString("content");
            return reply.trim();
        } else {
            String errorBody = response.body().string();
            System.err.println("OpenAI API 호출 실패: " + response);
            System.err.println("Response Body: " + errorBody);
            throw new IOException("OpenAI API 호출 실패: " + response);
        }
    }

    private JSONObject getJsonObject(List<Message> conversation) {
        // 요청 페이로드 생성
        JSONObject json = new JSONObject();
        json.put("model", "gpt-4");

        JSONArray messages = new JSONArray();

        // 시스템 메시지 추가
        String systemMessageContent = "다음 규칙들을 지키면서 제공되는 정보들을 활용하여 답변을 생성해. 그리고 이전 대화 내용을 참고하여 답변해.\n" +
                "<제공되는 정보>\n" +
                "1. \"TACOTICS\"는 로보월드라는 전시회에서 처음 선보이는 협동로봇 타코 매장이다.\n" +
                "2. 매장 근처 화장실은 오른쪽 건물 2층에 있으며, 화장실 비밀번호는 1234이다.\n" +
                "3. 음식이 나오는데까지는 5분 정도가 소요된다.\n" +
                "4. 타코틱스의 사장은 황찬욱이다.\n" +
                "5. 판매중인 메뉴는 소고기 타코, 소시지 타코이다.\n" +
                "6. 소고기 타코는 신선한 소고기와 풍부한 향신료가 어우러진, 매력적인 멕시코식 타코이다.\n" +
                "7. 소시지 타코는 육즙 가득한 소시지와 신선한 채소가 어우러진, 풍미 가득한 멕시코식 타코이다.\n" +
                "<규칙>\n" +
                "1. 무조건 질문에 사용된 언어로 답변해야 한다. 예를 들어, 영어로 질문하면 답변도 무조건 영어이어야 한다.\n" +
                "2. 답변을 너무 길게 하면 안된다.\n" +
                "3. 매장에서 고객을 응대하는 것처럼 친절한 말투로 답변해야 한다.";

        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", systemMessageContent);
        messages.put(systemMessage);

        // 대화 이력을 messages 배열에 추가
        for (Message msg : conversation) {
            JSONObject message = new JSONObject();
            message.put("role", msg.getRole());
            message.put("content", msg.getContent());
            messages.put(message);
        }

        json.put("messages", messages);
        return json;
    }

    // 대화 이력을 관리하기 위한 Message 클래스
    public static class Message {
        private String role; // "user" 또는 "assistant"
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }
}
