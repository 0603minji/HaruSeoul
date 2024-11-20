import { ref, onMounted, onUnmounted } from "vue";

const notifications = ref([]); // 알림 목록 저장
const hasNewNotification = ref(false); // 새 알림 여부
let eventSource = null;

export function useNotification(userId) {
    onMounted(() => {
        // SSE 연결 시작
        eventSource = new EventSource(`http://localhost:8080/api/v1/notifications/${userId}`);

        // 새 알림 수신 시 처리
        eventSource.onmessage = (event) => {
            console.log("Received message", event.data);
            const notification = JSON.parse(event.data);
            notifications.value.push(notification); // 알림 목록에 추가
            hasNewNotification.value = true; // 새 알림이 있음을 표시
            console.log("새 알림:", notification);
        };

        // 연결 오류 시 처리
        eventSource.onerror = (error) => {
            console.error("SSE 연결 오류:", error);
            eventSource.close(); // SSE 연결 닫기
        };
    });

    onUnmounted(() => {
        // 컴포넌트가 언마운트될 때 SSE 연결 종료
        if (eventSource) eventSource.close();
    });

    return {
        notifications,
        hasNewNotification,
    };
}
