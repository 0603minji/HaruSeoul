<script setup>
import { ref, defineExpose, defineEmits } from 'vue';

// 상태 관리 변수
const isVisible = ref(false); // 모달의 표시 상태
const selectedReservationId = ref(null); // 선택된 예약 ID

// 부모에게 전달할 이벤트 정의
const emit = defineEmits();

// 모달 열기
const openModal = (reservationId) => {
  isVisible.value = true;
  selectedReservationId.value = reservationId;
};

// 모달 닫기
const closeModal = () => {
  isVisible.value = false;
  selectedReservationId.value = null;
};

// 취소 확인 처리
const confirmCancel = () => {
  // 'cancel' 이벤트를 부모 컴포넌트로 전달
  emit('cancel', selectedReservationId.value);
  closeModal(); // 모달 닫기
};

// `openModal` 메서드를 외부에서 사용할 수 있도록 노출
defineExpose({
  openModal,
});
</script>

<template>
  <div v-if="isVisible" class="modal-overlay" @click.self="closeModal">
    <div class="modal-content">
      <h3>예약을 취소하시겠습니까?</h3>
      <div class="modal-actions">
        <button @click="confirmCancel" class="confirm-btn">예</button>
        <button @click="closeModal" class="cancel-btn">아니요</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
}

.modal-actions button {
  margin: 10px;
  padding: 10px 20px;
  border: none;
  cursor: pointer;
  font-size: 16px;
}

.confirm-btn {
  background-color: #DB4455;
  color: white;
}

.cancel-btn {
  background-color: #ccc;
}
</style>