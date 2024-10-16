package taco.rbiz.domain.model;

import lombok.Data;

/**
 * User Language Setting 관리
 */
@Data
public class LanguageSetting {

    private String selectedLanguage;

    public LanguageSetting() {

    }

    public LanguageSetting(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }
}
