document.addEventListener("DOMContentLoaded", function() {
    // 주문 모달 관련 요소들
    const modalOrderCheckbox = document.getElementById("modal-order");
    const orderBtn = document.getElementById("order-btn");
    const areasToDim = ["left-side", "item-list-main", "cart-footer", "order-footer"];

    // 주문하기 버튼 클릭 시 모달 열기
    orderBtn.addEventListener("click", function() {
        modalOrderCheckbox.checked = true;
    });

    // 모달 열릴 때 dimmed 클래스 추가 및 제거
    modalOrderCheckbox.addEventListener("change", function() {
        areasToDim.forEach(areaClass => {
            const element = document.querySelector(`.${areaClass}`);
            if (element) {
                if (modalOrderCheckbox.checked) {
                    element.classList.add("dimmed");
                } else {
                    element.classList.remove("dimmed");
                }
            }
        });
    });

    // 모달 내부 클릭 시 이벤트 전파 중지
    const modalContent = document.querySelector('.modal-content');
    modalContent.addEventListener('click', function(event) {
        event.stopPropagation();
    });

    // 모달 배경 클릭 시 모달 닫기
    const modalBg = document.querySelector('.modal-bg');
    modalBg.addEventListener('click', function() {
        modalOrderCheckbox.checked = false;
    });
});
