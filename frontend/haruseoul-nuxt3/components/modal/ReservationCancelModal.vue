<script setup>
import {ref, defineProps, defineEmits} from 'vue';

const props = defineProps({
  showModal: Boolean,
  currentReservationId: Number
});

const emit = defineEmits(['close', 'cancel']);

const errorMessage = ref('');
const successMessage = ref('');

// 모달 닫기
const closeModal = () => {
  emit('close');
};

// 예약 취소 확인 시 API 요청 처리
const cancelReservation = async () => {
  try {
    const token = localStorage.getItem("token");
    const currentDate = new Date().toISOString(); // 현재 시간

    // API 요청 보내기
    await useReservationFetch(
        `guest/reservations/${props.currentReservationId}/cancel`,
        {
          method: "PUT",  // 예약을 취소하는 PUT 요청
          headers: {
            Authorization: `Bearer ${token}`,
          },
          data: {
            deleteDate: currentDate,  // 삭제일자 현재 날짜
          },
        }
    );

    // cancel 이벤트 전달
    emit('cancel');

    // 모달 닫기
    closeModal();

    // 새로고침
    window.location.reload(); // 페이지 새로고침

  } catch (error) {
    errorMessage.value = '예약 취소 실패: ' + error.message;
  }
};
</script>

<template>
  <div v-if="showModal" class="modal">
    <div class="modal-content">
      <p style="font-size: 15px; font-weight: bold">정말 예약을 취소하시겠습니까?</p>
      <div style="width: 180px; padding-left: 20px; padding-top: 15px; display: flex; justify-content: space-between">
        <button class="n-btn n-btn:hover" style="color:#DB4455" @click="cancelReservation">확인</button>
        <button class="n-btn n-btn:hover" @click="closeModal">닫기</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 3;
}

.modal-content {
  background-color: white;
  padding: 35px;
  border-radius: 10px;
}
</style>
