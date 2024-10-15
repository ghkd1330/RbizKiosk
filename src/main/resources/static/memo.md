1. 전송 버튼을 클릭하면 textarea에 입력된 텍스트가 main.html의 <div class="message">에 <p class="from-me margin-t_one">내용</p>로 추가된다.
2. 또한 전송 버튼을 누를 때 textarea에 입력된 텍스트를 openai api를 통해 gpt4o-2024-05-13 모델에 입력하고, 모델의 출력을 main.html의 <div class="message">에 <p class="from-them margin-t_one">내용</p>로 추가된다.







< 남은 구현 >
* 메인 상품 리스트 아이콘 누르면 같은 모달 뜨면서 이름/사진/설명/가격/체크박스 동적으로 변경 ㅇ
1. 상품 디테일 모달 ui ㅇ
2. 주문하기 모달 ui ㅇ
3. 결제 수단 선택 모달 ui ㅇ
4. 큐알 코드 모달 ui ㅇ
5. 결제 완료 모달 ui (5초 지나면 홈으로 또는 홈버튼 만들기) ㅇ
6. 백엔드 로직 구현 (어드민 제외)
7. 언어 설정 화면 ui
8. 어드민 로그인 화면 ui
9. 어드민 페이지 ui
10. 로그인 및 어드민 백엔드 로직 구현 (고객 채팅 데이터와 직원 채팅 데이터를 한곳에서 모아두고 양쪽에 뿌린다)
11. gpt 프롬프트 쿼리 로직 구현
12. ui 수정 및 국제화 (환율 계산 or 시간나면 환율 api)
* UI/UX 수정 사항들 : 홈 화면 꾸미기, 메인화면 전체적으로 맞추기, 챗봇 초기화면 살짝 꾸미기 및 동적 화면, 메인화면 스크롤 컨트롤, 채팅화면 꾸미기, 장바구니에 추가 등 알림 띄워주기
13. modbus 로직 구현



위의 코드에서 s-set를 클릭했을 때는 <div class="modal-row-done detail-block>...</div>가 안보이게 하고, b-single을 클릭했을 때는 <div class="modal-row-drink detail-block>...</div>이 안보이게 하고, s-single을 클릭했을 때는 <div class="modal-row-done detail-block>...</div>와 <div class="modal-row-drink detail-block>...</div>가 안보이게 수정해줘.

s-set 모달일 때는 .modal-row-salad 선택자 스타일의 grid-area가 1 / 2 / 2 / 3이 되게 하고, .modal-row-drink 선택자 스타일의 grid-area가 2 / 2 / 3 / 3이 되게 해줘.
그리고 s-single 모달일 때는 .modal-row-salad 선택자 스타일의 grid-area가 1 / 2 / 2 / 3이 되게 코드를 수정해줘.

Order Modal에서 결제 버튼을 누르면 QR Modal이 열리는데, 이때 QR Modal이 열리고, 3초 후에 class="modal-body"인 요소의 내용이 바뀌도록 수정해줘. 그리고 또 3초 후에는 홈 화면으로 이동하도록 코드를 수정해줘.