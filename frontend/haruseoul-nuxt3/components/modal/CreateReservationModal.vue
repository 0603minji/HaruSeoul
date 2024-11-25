<script setup>
import {ref, watch, watchEffect} from 'vue';
import ReservationDatePicker from "~/components/filter/ReservationDatePicker.vue";

// Props
const props = defineProps({
  pId: {
    type: Number,
    default: null
  },
  hostId: {
    type: Number,
    default: null
  },
  groupSizeMax: {
    type: Number,
    default: null
  },
  price: {
    type: Number,
    default: null
  }
});

// emit
const emit = defineEmits(['closeModal', 'open-move-modal']);

const reRenderTrigger = ref(false);
const userDetails = useUserDetails();
const route = useRoute();

/*=== variables ======================================================================================================*/
// Selected dates 프로그램 진행일
let selectedPublishedProgram = ref(null);

const numberOfGuest = ref(1);

const isModalVisible = ref("");

// === function ========================================================================================================
// Handle selection change
const updateNumberOfGuest = (selectedOption) => {
  numberOfGuest.value = selectedOption;
  console.log('******* CreateReservationModal: updateNumberOfGuest called');
  console.log('          ->  numberOfGuest: ', numberOfGuest.value);
};

const updateSelectedPublishedProgram = (selectedOptions) => {
  selectedPublishedProgram.value = selectedOptions;
  console.log('******* CreateReservationModal: updateSelectedPublishedProgram called');
  console.log('          ->  selectedPublishedProgram: ', selectedPublishedProgram.value);
};

const submitHandler = async () => {
  console.log('******* CreateReservationModal: submitHandler called');
  if (userDetails.isAnonymous()) {
    // 로그인되지 않은 경우 로그인 페이지로 이동하고, 현재 경로를 returnURL로 설정
    return navigateTo(`/signin?returnURL=${route.fullPath}`);
  }
  const reservationCreateDto = {
    publishedProgramId: selectedPublishedProgram.value.id,
    guestId: userDetails.id.value,
    numberOfGuest: numberOfGuest.value,
    reservationRequirement: null
  };
  try {
    console.log('          ->  POST guest/reservations');
    const response = await useDataFetch(`guest/reservations`, {
      method: "POST",
      headers: {
        "Content-type": "application/json"
      },
      body: reservationCreateDto
    });
    console.log("응답 값:", response)
  } catch (error) {
    console.error('         Error creating Reservations: ', error);
  }

  // 모달창 닫기
  closeModal();
};

const closeModal = () => {
  selectedPublishedProgram.value = null;
  emit('closeModal');

  setTimeout(() => {
    reRenderTrigger.value = !reRenderTrigger.value;
  }, 300); // 0.3 seconds delay (300ms)
}

// 버튼 클릭 시 부모 컴포넌트로 이벤트 전달
const openMoveReservationModal = () => {
  emit('open-move-modal');
};
</script>

<template>
  <aside class="popup modal">
    <header class="popup-header">
      <h1 class="font-size:9">예약하기</h1>
      <button @click.prevent="closeModal()" class="n-btn n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form @submit.prevent="submitHandler" class="popup-body" action="">
      <!-- 진행일 선택 -->
      <ReservationDatePicker
          :key="reRenderTrigger"
          :hostId="props.hostId"
          :groupSizeMax="props.groupSizeMax"
          @update-number-of-guest="updateNumberOfGuest"
          @update-selected-published-program="updateSelectedPublishedProgram"/>

      <div>
        <div style="display: flex; height: 32px; padding: 10px 0 0 10px; align-items: center; margin-bottom: 14px;">
          <span class="font-size:8">가격</span>
        </div>
        <div style="display: flex; flex-direction: column; align-items: end; padding: 3px 30px;">
          <div v-if="props.price" style="width: 100%; display: flex; justify-content: space-between;">
            <span>1인 기준</span>
            <span style="font-weight: bold;">{{ props.price }} 원</span>
          </div>
          <div v-if="numberOfGuest" style="padding-top: 5px;">
            <span>x </span>
            <span style="font-weight: bold;">{{ numberOfGuest }} 명</span>
          </div>
          <div v-if="props.price && numberOfGuest"
               style="padding-top: 10px; width: 100%; display: flex; justify-content: space-between;">
            <span>총 결제금액</span>
            <span style="font-weight: bold;">{{ props.price * numberOfGuest }} 원</span>
          </div>
        </div>
      </div>

      <div class="submit">
        <button class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1"
                style="margin-left: var(--gap-2);
                  background-color: var(--color-sub-1);
                  color: var(--color-base-1);
                  width: var(--width-9p);
                  height: 20px;"
                :disabled="!selectedPublishedProgram"
                @click="openMoveReservationModal"
        >결제하기
        </button>
      </div>
    </form>
  </aside>
</template>

<style scoped>
.modal {
  display: none; /* Hidden by default */
  position: fixed;
  z-index: 1000; /* Sit on top */
  left: 50%;
  bottom: 0;
  transform: translate(-50%, 100%); /* 초기에 바닥에 숨어있음 */
  width: 100%;
  min-width: 350px;
  max-width: 500px;
  max-height: 100vh;
  //background-color: white;
  //border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease;
}

.modal.show {
  display: flex;
  transform: translate(-50%, 0); /* 아래에서 등장 */
}

.popup {
  border-radius: 16px 16px 0 0;
  background-color: var(--color-base-1);
  //padding: 16px 24px;
  display: flex;
  flex-direction: column;

  .popup-header {
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    margin-bottom: 10px;

    button {
      position: absolute;
      right: 14px;
      --icon-color: var(--color-base-5);
    }

    button:hover {
      --icon-color: var(--color-base-7);
    }
  }

  .popup-body {
    display: flex;
    flex-direction: column;
    padding: 0 10px;
    overflow-y: auto;

    .submit {
      padding: 20px 0;

      button:disabled {
        cursor: not-allowed;
        opacity: 0.3;
      }
    }
  }
}
</style>