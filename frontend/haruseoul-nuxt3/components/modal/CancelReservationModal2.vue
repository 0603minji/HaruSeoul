<script setup>
import { useRoute, useRouter } from 'vue-router'
import { nextTick } from 'vue';
import {ref, defineProps, defineEmits} from 'vue';
import {useReservationFetch} from "~/composables/useReservationFetch.js";
import {useDataFetch} from "~/composables/useDataFetch.js";

const route = useRoute;
const router = useRouter();

const props = defineProps({
  showModal: String,
  selectedReservationId: Number,
  fetReservation:Function,
  reservation: {
    type: Object,
    default: () => ({}) // 기본값 설정
  },
  programId: Number
});

const emit = defineEmits(['close', 'cancel', 'updateGuestConsent']);

const errorMessage = ref('');

// 모달 닫기
const closeModal = () => {
  emit('close');
};

// 예약 취소 확인 시 API 요청 처리
const cancelReservation = async () => {
  console.log("erasd")
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
            id: props.reservation.program.publishedProgramId,
            reservationId: props.reservation.reservationId,
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

    cancelParticipationHandler();

  } catch (error) {
    errorMessage.value = '예약 취소 실패: ' + error.message;
  }
};

const cancelParticipationHandler = async () => {
  console.log('          cancelParticipationHandler called.');
  const guestConsentResponse = await useDataFetch(`host/reservations/consent`, {
    method: "PUT",
    headers: {
      "Content-type": "application/json"
    },
    body: {
      "id": props.selectedReservationId,
      "guestConsent": 3 // 예약취소로 초기화
    }
  });
  console.log('          Reservation Update result: ', guestConsentResponse);
  emit('updateGuestConsent', 3);
}

</script>

<template>
  <div v-show="showModal ===`cancelReservationModal2`" class="modal">
    <div class="modal-content">
      <p style="font-size: 15px; font-weight: bold">정말 예약을 취소하시겠습니까?</p>
      <div style="width: 180px; padding-left: 20px; padding-top: 15px; display: flex; justify-content: space-between">
        <button class="n-btn n-btn:hover" style="background-color:#DB4455; color: white;" @click.prevent="cancelReservation">확인</button>
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
