package taco.rbiz.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import taco.rbiz.web.controller.util.log.LogInterceptor;
import taco.rbiz.web.login.LoginCheckInterceptor;

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

//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(2)
//                .addPathPatterns("/**") // 모든 경로에 적용하되,
//                .excludePathPatterns( // 나중에 수정
//                        "/", "/login", "/updateLanguage", "/logout", // Home, Join, Login, Logout
//                        "/kiosk/css/**", "/kiosk/js/**", "/kiosk/img/**", "/*.ico", "/error", "/webjars/**",
//                        "/**/*.png", "/**/*.jpg" // Resource 조회, Error와 같은 부분은 적용하지 않는다
//                );
    }
}
