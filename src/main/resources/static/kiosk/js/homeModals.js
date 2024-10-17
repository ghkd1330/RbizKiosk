$(document).ready(function() {
    const langModalElement = document.getElementById('langModal');
    const langModal = new bootstrap.Modal(langModalElement);

    $('.confirm-btn-order').click(function() {
        // 선택된 언어의 ID 가져오기
        let selectedLanguageId = $('input[name="radio"]:checked').val();

        if (selectedLanguageId) {
            // 서버에 선택된 언어 ID를 POST 요청으로 전송
            $.ajax({
                type: 'POST',
                url: '/updateLanguage',
                data: { languageId: selectedLanguageId },
                success: function(response) {
                    // 요청 성공 시, 서버로부터의 응답을 알림 메시지로 표시
                    alert(response);

                    // 모달 닫기
                    langModal.hide();
                },
                error: function() {
                    alert('언어 설정에 실패했습니다. 다시 시도해 주세요.');
                }
            });
        } else {
            alert('언어를 선택해 주세요.');
        }
    });
});