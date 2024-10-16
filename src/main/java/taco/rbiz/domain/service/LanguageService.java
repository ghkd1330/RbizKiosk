package taco.rbiz.domain.service;

import org.springframework.stereotype.Service;

@Service
public class LanguageService {

    /**
     * Client 마다의 언어 설정 정보를 세션이나 쿠키에 저장
     * @param language : 언어 설정 Modal에서 Client가 선택한 언어
     */
    public void setLanguage(String language) {

    }

    /**
     * Client 마다의 언어 설정 정보를 세션이나 쿠키에서 가져옴
     * @return : Client가 설정한 언어 Return
     */
    public String getLanguage() {

        return null;
    }
}
