console.log('aiChat.js 로드됨');

// Touch to Start AI Chat
document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('ai-start-btn').addEventListener('click', function () {
        let button = document.getElementById('ai-start-btn');
        let newContent = document.getElementById('ai-chat-div').innerHTML;

        // New <div> 생성
        let tempDiv = document.createElement('div');
        tempDiv.innerHTML = newContent;

        // tempDiv의 자식 노드를 가져와서 새로운 <div>로 생성
        let newDiv = tempDiv.firstElementChild;

        // Button을 새로운 <div>로 교체
        button.parentNode.replaceChild(newDiv, button);

        // 새로운 DOM Element들이 생성되었으므로, 이벤트 리스너를 다시 추가
        initializeRecording();
    });

    // Page 로드시 초기화 함수 호출
    initializeRecording();
});

// Record
function initializeRecording() {
    console.log('initializeRecording() 호출됨');

    const startButton = document.getElementById('startButton-ai');
    const stopButton = document.getElementById('stopButton');
    const sendButton = document.getElementById('sendButton');
    const statusDisplay = document.getElementById('status-and-text');

    // startButton이 존재하는지 확인
    if (startButton && stopButton && statusDisplay && sendButton) {
        console.log('DOM 요소 참조 완료');

        // 기존에 등록된 이벤트 리스너 제거 (중복 방지)
        startButton.onclick = null;
        stopButton.onclick = null;
        sendButton.onclick = null;

        // Event Listener 추가
        startButton.addEventListener('click', startRecordingHandler);
        stopButton.addEventListener('click', stopRecordingHandler);
        sendButton.addEventListener('click', sendMessageHandler);

        let mediaRecorder;
        let audioChunks = [];

        function startRecordingHandler() {
            startRecording();
            startButton.disabled = true;
            stopButton.disabled = false;
            statusDisplay.value = '녹음 중...';
        }

        function stopRecordingHandler() {
            stopRecording();
            startButton.disabled = false;
            stopButton.disabled = true;
            statusDisplay.value = '음성 처리 중...';
        }

        function sendMessageHandler() {
            const userMessage = statusDisplay.value.trim();
            if (userMessage === '') {
                alert('메시지를 입력해주세요.');
                return;
            }

            // 메시지를 대화 영역에 추가 (사용자 메시지)
            addMessageToConversation('from-me', userMessage);

            // 서버로 메시지 전송
            sendButton.disabled = true;
            statusDisplay.value = '답변을 생성 중입니다...';

            fetch('/process-text', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ message: userMessage })
            })
                .then(response => response.json())
                .then(data => {
                    // 모델의 응답을 대화 영역에 추가
                    addMessageToConversation('from-ai', data.reply);
                    statusDisplay.value = '';
                    sendButton.disabled = false;
                })
                .catch(error => {
                    console.error('Error:', error);
                    statusDisplay.value = '답변 생성 중 오류 발생';
                    sendButton.disabled = false;
                });
        }

        function addMessageToConversation(senderClass, messageContent) {
            const messageContainer = document.querySelector('.messages-area .message');
            if (!messageContainer) {
                console.error('메시지를 추가할 .message 요소를 찾을 수 없습니다.');
                return;
            }

            const messageBubble = document.createElement('p');
            messageBubble.classList.add(senderClass);
            messageBubble.textContent = messageContent;

            messageContainer.appendChild(messageBubble);

            // 스크롤을 맨 아래로 이동
            const messagesDiv = document.querySelector('.messages-area');
            messagesDiv.scrollTop = messagesDiv.scrollHeight;
        }

        function startRecording() {
            console.log('녹음 시작');
            navigator.mediaDevices.getUserMedia({ audio: true })
                .then(stream => {
                    console.log('마이크 스트림 얻음');
                    mediaRecorder = new MediaRecorder(stream);
                    mediaRecorder.start();
                    console.log('MediaRecorder 시작');

                    // 20초 후에 녹음 중지
                    setTimeout(() => {
                        if (mediaRecorder && mediaRecorder.state !== 'inactive') {
                            stopRecording();
                            startButton.disabled = false;
                            stopButton.disabled = true;
                        }
                    }, 20000);

                    mediaRecorder.ondataavailable = event => {
                        audioChunks.push(event.data);
                    };

                    mediaRecorder.onstop = () => {
                        console.log('녹음 중지');
                        const audioBlob = new Blob(audioChunks, { type: 'audio/webm' });
                        audioChunks = [];

                        // 오디오 파일을 서버로 전송하여 텍스트로 변환
                        const formData = new FormData();
                        formData.append('audioFile', audioBlob, 'recording.webm');

                        fetch('/upload-audio', {
                            method: 'POST',
                            body: formData
                        })
                            .then(response => response.text())
                            .then(data => {
                                console.log('Server Response: ', data);
                                statusDisplay.value = data;
                                sendButton.disabled = false;
                                // 변환된 텍스트는 여기서 대화 영역에 추가하지 않습니다.
                            })
                            .catch(error => {
                                console.error('Error:', error);
                                statusDisplay.value = '음성 처리 중 오류 발생';
                            });
                    };
                })
                .catch(error => {
                    console.error('Mic Access Error:', error);
                    statusDisplay.value = '마이크 엑세스 거부됨';
                });
        }

        function stopRecording() {
            console.log('녹음 중지');
            if (mediaRecorder && mediaRecorder.state !== 'inactive') {
                mediaRecorder.stop();
            }
        }
    } else {
        console.error('DOM 요소 참조 실패');
    }
}
