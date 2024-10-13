package taco.rbiz.web.controller.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import okhttp3.*;
import okhttp3.RequestBody;
import org.json.JSONObject;
import okhttp3.MediaType;


@RestController
public class AudioController {

    // OpenAI API Key를 환경 변수에서 가져오기
    @Value("${openai.api-key}")
    private String openaiApiKey;

    @PostMapping("/upload-audio")
    public ResponseEntity<String> uploadAudio(@RequestParam("audioFile") MultipartFile audioFile) {
        try {
            // 업로드된 파일을 임시 디렉토리에 저장
            Path tempDir = Files.createTempDirectory("");
            File tempFile = tempDir.resolve("audio.webm").toFile();
            audioFile.transferTo(tempFile);

            // FFmpeg을 사용하여 webm을 mp3로 변환
            File mp3File = tempDir.resolve("audio.mp3").toFile();
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-i", tempFile.getAbsolutePath(),
                    "-ar", "16000", // Whisper API 요구사항에 따라 샘플링 레이트 설정
                    "-ac", "1",     // 모노 채널
                    mp3File.getAbsolutePath()
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // FFmpeg 출력 로그 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder processOutput = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                processOutput.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            // 원본 파일 삭제
            tempFile.delete();

            if (exitCode != 0) {
                System.err.println("FFmpeg 오류:\n" + processOutput);
                return ResponseEntity.status(500).body("오디오 변환 중 오류가 발생했습니다.");
            }

            // OpenAI Whisper API를 사용하여 음성을 텍스트로 변환
            String transcript = transcribeAudio(mp3File);

            // 변환된 텍스트를 반환
            return ResponseEntity.ok(transcript);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("오디오 업로드 중 오류가 발생했습니다.");
        } finally {
            // mp3 파일 삭제
            // 변환이 성공하든 실패하든 mp3 파일을 삭제합니다.
            try {
                Files.deleteIfExists(Paths.get("audio.mp3"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String transcribeAudio(File audioFile) throws IOException, InterruptedException {
        OkHttpClient client = new OkHttpClient();

        // OpenAI API 요청 설정
        MediaType mediaType = MediaType.parse("audio/mpeg");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("model", "whisper-1")
                .addFormDataPart("file", audioFile.getName(),
                        RequestBody.create(mediaType, audioFile))
                .build();
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/audio/transcriptions")
                .post(body)
                .addHeader("Authorization", "Bearer " + openaiApiKey)
                .build();

        int maxRetries = 3;
        int retryCount = 0;
        int retryDelay = 2000; // milliseconds

        while (true) {
            // API 호출 및 응답 처리
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);
                String transcript = json.getString("text");

                // 오디오 파일 삭제
                audioFile.delete();

                return transcript;
            } else if (response.code() == 429 && retryCount < maxRetries) {
                // 429 Error 처리 : Delay 후 재시도
                retryCount++;
                System.out.println("429 Error 발생, " + retryCount + "ms 후 재시도합니다. 재시도 횟수 : " + retryCount);
                Thread.sleep(retryDelay);
                // 지수 백오프 적용
                retryDelay *= 2;
            } else {
                // 그 외의 오류 처리
                String errorBody = response.body().string();
                System.out.println("Whisper API 호출 실패: " + response);
                System.out.println("Response Body: " + errorBody);
                throw new IOException("Whisper API 호출 실패: " + response);
            }
        }
    }
}
