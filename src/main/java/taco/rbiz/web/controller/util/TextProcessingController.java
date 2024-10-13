package taco.rbiz.web.controller.util;

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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TextProcessingController {

    @Value("${openai.api-key}")
    private String openaiApiKey;

    @PostMapping(value = "/process-text", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, String>> processText(@org.springframework.web.bind.annotation.RequestBody Map<String, String> payload) {
        String userMessage = payload.get("message");

        try {
            String reply = getChatGPTResponse(userMessage);

            Map<String, String> response = new HashMap<>();
            response.put("reply", reply);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.singletonMap("reply", "답변 생성 중 오류 발생"));
        }
    }

    private String getChatGPTResponse(String userMessage) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // 요청 페이로드 생성
        JSONObject json = new JSONObject();
        json.put("model", "gpt-4");
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", userMessage);
        messages.put(message);
        json.put("messages", messages);

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
}
