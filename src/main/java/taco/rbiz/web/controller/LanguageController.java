package taco.rbiz.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class LanguageController {

    @PostMapping("/updateLanguage")
    @ResponseBody
    public ResponseEntity<String> updateLanguage(@RequestParam("languageId") int languageId) {
        // Business Logic : 선택된 언어 ID에 따른 처리 (세션이나 쿠키 정보로 저장)
        String result = "언어 설정 완료 : " + languageId;

        return ResponseEntity.ok(result);
    }
}
