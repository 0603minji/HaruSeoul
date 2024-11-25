<script setup>
import {defineEmits} from 'vue';

const emit = defineEmits(['close']);
// 모달 닫기
const closeModal = () => {
  emit('close');
};
</script>

<template>
  <div class="modal">
    <div class="modal-content">
      <div style="display: flex; flex-direction: column; align-items: center;">
        <p style="font-size: 15px; font-weight: bold">결제가 완료되었습니다.</p>
        <p style="font-size: 15px; font-weight: bold">예약 목록을 확인하시겠습니까?</p>
      </div>
      <div style="width: 180px; padding-left: 20px; padding-top: 15px; display: flex; justify-content: space-between">
        <NuxtLink to="../guest/reservations"
                  class="n-btn n-btn:hover"
                  style="background-color:#249AE4; color: white;">확인
        </NuxtLink>
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
  z-index: 20;
}

.modal-content {
  background-color: white;
  padding: 35px;
  border-radius: 10px;
}
</style>