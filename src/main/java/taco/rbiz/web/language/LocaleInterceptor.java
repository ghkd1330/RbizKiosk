package taco.rbiz.web.language;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;

@Slf4j
public class LocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        Locale sessionLocale = (Locale) session.getAttribute("lang");

        if (sessionLocale != null) {
            LocaleContextHolder.setLocale(sessionLocale);
            log.info("Locale retrieved from session: {}", sessionLocale);  // 세션에서 가져온 값을 로그로 출력
        } else {
            LocaleContextHolder.setLocale(Locale.KOREAN);
            log.info("No locale found in session. Defaulting to Korean.");
        }

        return true;
    }

}
