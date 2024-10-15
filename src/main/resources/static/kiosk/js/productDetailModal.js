document.addEventListener("DOMContentLoaded", function() {
    const modalCheckbox = document.getElementById("modal-detail");
    const card = document.getElementById("b-set");
    const areasToDim = ["left-side", "item-list-main", "cart-footer", "order-footer"];

    // 카드 클릭 시 모달 열기
    card.addEventListener("click", function() {
        modalCheckbox.checked = true;
    });

    // 모달 열릴 때 dimmed 클래스 추가 및 제거
    modalCheckbox.addEventListener("change", function() {
        areasToDim.forEach(areaClass => {
            const element = document.querySelector(`.${areaClass}`);
            if (element) {
                if (modalCheckbox.checked) {
                    element.classList.add("dimmed");
                } else {
                    element.classList.remove("dimmed");
                }
            }
        });
    });

    // 모달 내부 클릭 시 이벤트 전파 중지
    const modalContent = document.querySelector('.modal-content-detail');
    modalContent.addEventListener('click', function(event) {
        event.stopPropagation();
    });

    // 모달 배경 클릭 시 모달 닫기
    const modalBg = document.querySelector('.modal-bg-detail');
    modalBg.addEventListener('click', function() {
        modalCheckbox.checked = false;
    });
});

// 모든 체크박스를 선택하고, 이벤트 리스너 추가
document.querySelectorAll('.block-checkbox').forEach((checkbox) => {
    checkbox.addEventListener('change', (event) => {
        // 체크박스의 부모 .block 요소를 선택
        const blockDiv = event.target.closest('.block');

        // 체크 상태에 따라 클래스 추가/제거
        if (event.target.checked) {
            blockDiv.classList.add('checked-border');
        } else {
            blockDiv.classList.remove('checked-border');
        }
    });
});

// 굽기 옵션과 음료 옵션은 하나만 선택 가능하게 설정
const grillingOptions = document.querySelectorAll('.modal-row-done .block-checkbox');
const drinkOptions = document.querySelectorAll('.modal-row-drink .block-checkbox');
const takeoutOptions = document.querySelectorAll('.modal-takeout .block-checkbox');
const payOptions = document.querySelectorAll('.modal-pay .block-checkbox');

// 하나만 선택 가능하도록 하는 함수
function singleSelect(checkboxes) {
    checkboxes.forEach((checkbox) => {
        checkbox.addEventListener('change', (event) => {
            if (event.target.checked) {
                // 다른 체크박스의 체크를 해제
                checkboxes.forEach((box) => {
                    if (box !== event.target) {
                        box.checked = false;
                        updateBorder(box); // 체크 해제 시 테두리 제거
                    }
                });
            }
            updateBorder(event.target); // 현재 체크된 상태를 업데이트
        });
    });
}

// 테두리 색상 변경 함수
function updateBorder(checkbox) {
    const blockDiv = checkbox.closest('.block');
    if (checkbox.checked) {
        blockDiv.classList.add('checked-border');
    } else {
        blockDiv.classList.remove('checked-border');
    }
}

// 굽기 옵션과 음료 옵션은 하나씩만 선택 가능
singleSelect(grillingOptions);
singleSelect(drinkOptions);
singleSelect(takeoutOptions);
singleSelect(payOptions);

// 야채 옵션은 다중 선택 가능하므로 별도의 처리 필요 없음
document.querySelectorAll('.modal-row-salad .block-checkbox').forEach((checkbox) => {
    checkbox.addEventListener('change', () => updateBorder(checkbox));
});


