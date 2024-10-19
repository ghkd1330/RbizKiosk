package taco.rbiz.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import taco.rbiz.web.controller.util.log.LogInterceptor;
import taco.rbiz.web.login.LoginCheckInterceptor;

import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1) // 1st Order
                .addPathPatterns("/**") // 적용할 URL Pattern 지정
                .excludePathPatterns("/kiosk/css/**", "/kiosk/js/**", "/kiosk/img/**", "/**/*.png", "/**/*.jpg",
                                     "/*.ico", "/error", "/webjars/**"
                ); // White List

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns(
                        "/admin/adminPage", "/admin/adminPage/**", "/admin/adminPage/*"
                );

        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREAN); // 기본 언어 설정
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // 언어 변경을 위한 파라미터명
        return interceptor;
    }

}
