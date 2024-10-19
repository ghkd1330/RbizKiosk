package taco.rbiz.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;
import java.util.Map;

@Slf4j
@Controller
public class LanguageController {

    @PostMapping("/updateLanguage")
    @ResponseBody
    public ResponseEntity<String> updateLanguage(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        int languageId;
        try {
            languageId = Integer.parseInt(payload.get("languageId"));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("잘못된 언어 ID입니다.");
        }

        // 선택된 언어 ID에 따른 Locale 설정
        HttpSession session = request.getSession();
        Locale selectedLocale = switch (languageId) {
            case 2 -> Locale.ENGLISH;
            case 3 -> Locale.SIMPLIFIED_CHINESE;
            case 4 -> Locale.JAPANESE;
            case 5 -> new Locale("hi", "IN");
            case 6 -> new Locale("es", "ES");
            case 7 -> new Locale("ar", "DZ");
            case 8 -> new Locale("bn", "BD");
            case 9 -> new Locale("pt", "BR");
            case 10 -> new Locale("ru", "RU");
            case 11 -> Locale.FRENCH;
            case 12 -> new Locale("ur", "PK");
            default -> Locale.KOREAN;
        };

        // 세션에 선택된 Locale 저장
        session.setAttribute("lang", selectedLocale);
        log.info("Selected Locale stored in session: {}", selectedLocale);

        // 현재 스레드의 Locale 설정
        LocaleContextHolder.setLocale(selectedLocale);
        return ResponseEntity.ok("언어 설정 완료 : " + selectedLocale.getDisplayLanguage());
    }
}
