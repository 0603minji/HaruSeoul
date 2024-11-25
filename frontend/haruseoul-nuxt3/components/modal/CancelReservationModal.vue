<script setup>
import { useRouter } from 'vue-router'
import { nextTick } from 'vue';
import {ref, defineProps, defineEmits} from 'vue';
import {useReservationFetch} from "~/composables/useReservationFetch.js";
import {useDataFetch} from "~/composables/useDataFetch.js";

const router = useRouter();

const props = defineProps({
  showModal: String,
  selectedReservationId: Number,
  fetchReservation:Function,
  publishedProgramId: Number,
  reservationId: Number,
  programId: Number
});

console.log("프롭스", props)
console.log("publishedProgramId 이다",props.publishedProgramId)
console.log("reservationId 이다",props.reservationId)

const emit = defineEmits(['close', 'cancel']);

const errorMessage = ref('');

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
        `guest/reservations/${props.selectedReservationId}/cancel`,
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

    // API 요청 보내기
    await useReservationFetch(
        `guest/published-programs`,
        {
          method: "PUT",  // 예약을 취소하는 PUT 요청
          headers: {
            Authorization: `Bearer ${token}`,
          },
          body: {
            id: props.publishedProgramId,
            reservationId: props.reservationId,
          },
        }
    );

    // 개설된 프로그램이 더이상 존재하지 않는다면 프로그램 상태르 Published -> Unpublished로 변경
    await useDataFetch(`host/programs/${props.programId}/statusCheck`, {
      method: "PUT",
      headers: {
        "Content-type": "application/json"
      }
    });

    // cancel 이벤트 전달
    emit('cancel');

    // 모달 닫기
    closeModal();

    // 예약 취소 후, p=1 위치로 리다이렉트
    await router.push({path: '/guest/reservations', query: {p: 1}});

    // nextTick: 데이터가 변경되는 DOM 처리가 완료된 후에, 진행되도록 해주는 함수, Vue3부터 지원
    await nextTick(() => {
      props.fetchReservation(); // 쿼리 파라미터가 바뀌면 fetch 호출
    });

  } catch (error) {
    errorMessage.value = '예약 취소 실패: ' + error.message;
  }
};

</script>

<template>
  <div v-show="showModal===`cancelReservationModal`" class="modal">
    <div class="modal-content">
      <p style="font-size: 15px; font-weight: bold">정말 예약을 취소하시겠습니까?</p>
      <div style="width: 180px; padding-left: 20px; padding-top: 15px; display: flex; justify-content: space-between">
        <button class="n-btn n-btn:hover" style="background-color:#DB4455; color: white;" @click="cancelReservation">확인</button>
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
