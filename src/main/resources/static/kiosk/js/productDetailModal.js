document.addEventListener("DOMContentLoaded", function() {
    const modalCheckbox = document.getElementById("modal-detail");
    const bSetCard = document.getElementById("b-set");
    const sSetCard = document.getElementById("s-set");
    const bSingleCard = document.getElementById("b-single");
    const sSingleCard = document.getElementById("s-single");
    const modalRowDone = document.querySelector(".modal-row-done");
    const modalRowSalad = document.querySelector(".modal-row-salad");
    const modalRowDrink = document.querySelector(".modal-row-drink");
    const areasToDim = ["left-side", "item-list-main", "cart-footer", "order-footer"];

    // 모달 내용 업데이트를 위한 요소들
    const modalHeader = document.querySelector('.modal-content-detail header p');
    const modalImage = document.querySelector('.modal-info-detail .image-wrapper img');
    const modalDescription = document.getElementById('detail-explain');
    const modalPrice = document.getElementById('detail-price');

    // 메뉴 데이터 객체
    const menuData = {
        "b-set": {
            name: "소고기 타코 세트",
            image: "/kiosk/img/mainitemlist/beef_set.png",
            description: "신선한 소고기와 다채로운 야채가 어우러진 풍성한 타코 세트. 음료 선택 포함.",
            price: "가격 : 12,000원"
        },
        "s-set": {
            name: "소시지 타코 세트",
            image: "/kiosk/img/mainitemlist/sausage_set.png",
            description: "육즙 가득한 소시지와 신선한 채소로 가득한 타코 세트. 음료 선택 포함.",
            price: "가격 : 10,000원"
        },
        "b-single": {
            name: "소고기 타코 단품",
            image: "/kiosk/img/mainitemlist/beef_single.png",
            description: "고소한 소고기와 풍부한 향신료가 조화로운 단품 타코. 신선한 야채가 곁들여짐.",
            price: "가격 : 10,000원"
        },
        "s-single": {
            name: "소시지 타코 단품",
            image: "/kiosk/img/mainitemlist/sausage_single.png",
            description: "깊은 풍미의 소시지와 신선한 채소가 어우러진 단품 타코. 한 입 가득 육즙이 느껴짐.",
            price: "가격 : 8,000원"
        }
    };

    // 모달 열기 함수
    function openModal(cardType) {
        // 모든 옵션 섹션 초기화
        modalRowDone.style.display = "";
        modalRowSalad.style.display = "";
        modalRowDrink.style.display = "";
        modalRowSalad.style.gridArea = "";
        modalRowDrink.style.gridArea = "";

        // 카드 타입에 따라 옵션 섹션 숨기기 및 grid-area 조정
        if (cardType === "s-set") {
            modalRowDone.style.display = "none";

            // grid-area 조정
            modalRowSalad.style.gridArea = "1 / 2 / 2 / 3";
            modalRowDrink.style.gridArea = "2 / 2 / 3 / 3";
        } else if (cardType === "b-single") {
            modalRowDrink.style.display = "none";
        } else if (cardType === "s-single") {
            modalRowDone.style.display = "none";
            modalRowDrink.style.display = "none";

            // grid-area 조정
            modalRowSalad.style.gridArea = "1 / 2 / 2 / 3";
        }

        // 모달 내용 업데이트
        const data = menuData[cardType];
        if (data) {
            modalHeader.textContent = data.name;
            modalImage.src = data.image;
            modalDescription.textContent = data.description;
            modalPrice.textContent = data.price;
        }

        // 모달 열기
        modalCheckbox.checked = true;
    }

    // 카드 클릭 이벤트 리스너 추가
    bSetCard.addEventListener("click", function() {
        openModal("b-set");
    });

    sSetCard.addEventListener("click", function() {
        openModal("s-set");
    });

    bSingleCard.addEventListener("click", function() {
        openModal("b-single");
    });

    sSingleCard.addEventListener("click", function() {
        openModal("s-single");
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

        // 모달이 닫힐 때 체크박스 모두 해제
        if (!modalCheckbox.checked) {
            // 모든 체크박스 해제 및 테두리 제거
            const allCheckboxes = document.querySelectorAll('.modal-content-detail .block-checkbox');
            allCheckboxes.forEach((checkbox) => {
                checkbox.checked = false;
                updateBorder(checkbox);
            });
        }
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

// 굽기 옵션과 음료 옵션은 하나만 선택 가능하게 설정
const grillingOptions = document.querySelectorAll('.modal-row-done .block-checkbox');
const drinkOptions = document.querySelectorAll('.modal-row-drink .block-checkbox');
const takeoutOptions = document.querySelectorAll('.modal-takeout .block-checkbox');
const payOptions = document.querySelectorAll('.modal-pay .block-checkbox');
const receiptOptions = document.querySelectorAll('.modal-receipt .block-checkbox');

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
singleSelect(receiptOptions);

// 야채 옵션은 다중 선택 가능하므로 별도의 처리 필요 없음
document.querySelectorAll('.modal-row-salad .block-checkbox').forEach((checkbox) => {
    checkbox.addEventListener('change', () => updateBorder(checkbox));
});

document.addEventListener("DOMContentLoaded", function() {
    // QR Modal이 열릴 때 이벤트 감지
    const qrModal = document.getElementById('staticBackdrop');

    qrModal.addEventListener('shown.bs.modal', function () {
        // 3초 후에 modal-body의 내용 변경
        setTimeout(function() {
            // modal-header 내용 변경
            const modalHeader = qrModal.querySelector('.modal-header');
            modalHeader.innerHTML = `
                <h1 class="modal-title fs-5">결제 완료!</h1>
            `;

            // modal-body 내용 변경
            const modalBody = qrModal.querySelector('.modal-body');
            modalBody.innerHTML = `
                <div style="flex-direction: column; justify-content: center; align-items: center; text-align: center; font-size: 22px;">
                    <p>잠시 후 홈으로 이동합니다.</p>
                    <div style="width: 100px; height: 100px; margin-left: 65px; margin-bottom: 70px;">
                        <img src="/kiosk/img/qrmodal/done.png" alt="DoneImage" style="object-fit: cover; max-width: inherit; max-height: inherit; height: inherit; width: inherit;">
                    </div>
                    <div class="progress" role="progressbar" aria-label="Animated striped example" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100">
                        <div class="progress-bar progress-bar-striped progress-bar-animated" style="width: 75%;"></div>
                    </div>
                    <div style="margin-top: 20px;">
                </div>
            `;
        }, 5000);

        // 추가 3초 후에 홈 화면으로 이동
        setTimeout(function() {
            window.location.href = '/'; // 홈 화면의 URL로 변경하세요.
        }, 8000);
    });
});
