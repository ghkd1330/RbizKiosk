$(document).ready(function() {
    const langModalElement = document.getElementById('langModal');
    const langModal = new bootstrap.Modal(langModalElement);

    $('.confirm-btn-order').click(function() {
        let selectedLanguageId = $('input[name="radio"]:checked').val();
        let langCode = getLanguageCode(selectedLanguageId); // 함수로 언어 코드를 결정

        if (selectedLanguageId) {
            $.ajax({
                type: 'POST',
                url: '/updateLanguage',
                contentType: 'application/json',
                data: JSON.stringify({ languageId: selectedLanguageId }),
                success: function(response) {
                    // 로컬 스토리지에 저장
                    localStorage.setItem('selectedLocale', selectedLanguageId);
                    // URL에 lang 매개변수를 추가하여 페이지 리로드
                    window.location.href = window.location.pathname + '?lang=' + langCode;
                },
                error: function() {
                    alert('언어 설정에 실패했습니다. 다시 시도해 주세요.');
                }
            });
        } else {
            alert('언어를 선택해 주세요.');
        }
    });

    function getLanguageCode(languageId) {
        switch (parseInt(languageId)) {
            case 2: return 'en';
            case 3: return 'zh';
            case 4: return 'ja';
            case 5: return 'hi';
            case 6: return 'es';
            case 7: return 'ar';
            case 8: return 'bn';
            case 9: return 'pt';
            case 10: return 'ru';
            case 11: return 'fr';
            case 12: return 'ur';
            default: return 'ko';
        }
    }
});


