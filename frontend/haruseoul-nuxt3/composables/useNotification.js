import {ref, onMounted, onUnmounted} from "vue";

const notifications = ref([]); // 알림 목록 저장
const hasNewNotification = ref(false); // 새 알림 여부
let eventSource = null;

export function useNotification(userId) {
    onMounted(() => {
        // SSE 연결 시작
        eventSource = new EventSource(`http://localhost:8080/api/v1/notifications/${userId}`);

        eventSource.onopen = () => console.log("sse 연결 성공");

        // 새 알림 수신 시 처리
        eventSource.addEventListener("notification", (event) => {
            console.log("Received notification:", event.data);
            const notification = JSON.parse(event.data);
            notifications.value.push(notification);
            hasNewNotification.value = true;
        });


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
