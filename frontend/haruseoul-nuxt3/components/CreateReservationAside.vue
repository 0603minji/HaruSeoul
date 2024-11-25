<script setup>
import {ref, watch, watchEffect} from 'vue';
import axios from "axios";
import ReservationDatePicker from "~/components/filter/ReservationDatePicker.vue";
import CreateReservationModal from "~/components/modal/CreateReservationModal.vue";

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
  }
});

// emit
const emit = defineEmits(['closeModal']);

const reRenderTrigger = ref(false);

/*=== variables ======================================================================================================*/
// Selected dates 프로그램 진행일
let selectedPublishedProgram = ref(null);

const numberOfGuest = ref(1);
const userDetails = useUserDetails();

// === function ========================================================================================================
// Handle selection change
const updateNumberOfGuest = (selectedOption) => {
  numberOfGuest.value = selectedOption;
  console.log('******* CreateReservationModal: updateNumberOfGuest called');
  console.log('          ->  numberOfGuest: ', numberOfGuest.value);
};

const updateSelectedPublishedProgram = (selectedOptions) => {
  if (selectedOptions)
  {selectedPublishedProgram.value = selectedOptions;}
  else selectedPublishedProgram.value = null;
  console.log('******* CreateReservationModal: updateSelectedPublishedProgram called');
  console.log('          ->  selectedPublishedProgram: ', selectedPublishedProgram.value);
};

const submitHandler = async () => {
  console.log('******* CreateReservationModal: submitHandler called');

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
  } catch (error) {
    console.error('         Error creating Reservations: ', error);
  }

  // 모달창 닫기
  closeModal();
};

const closeModal = () => {
  emit('closeModal');

  // Delay the re-render trigger to allow closing animation to complete
  setTimeout(() => {
    reRenderTrigger.value = !reRenderTrigger.value;
    // console.log('reRenderTrigger: ', reRenderTrigger);
  }, 300); // 0.3 seconds delay (300ms)
}
</script>


<template>
  <aside class="popup modal">
    <form @submit.prevent="submitHandler" class="popup-body" action="">
      <!-- 진행일 선택 -->
      <ReservationDatePicker
          :key="reRenderTrigger"
          :hostId="props.hostId"
          :groupSizeMax="props.groupSizeMax"
          @update-number-of-guest="updateNumberOfGuest"
          @update-selected-published-program="updateSelectedPublishedProgram"/>


      <div class="submit">
        <button class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1"
                style="margin-left: var(--gap-2);
                  background-color: var(--color-sub-1);
                  color: var(--color-base-1);
                  width: var(--width-9p);
                  height: 20px;"
                :disabled="!selectedPublishedProgram"
        >결제하기</button>
      </div>
    </form>
  </aside>
</template>

<style scoped>
.min-max-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%; /* 전체 너비를 조정하고 싶으면 값 변경 */

  > .min-section, .max-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 10px;
  }

  > .input-group {
    display: flex;
    align-items: center;
    gap: 5px; /* 버튼과 입력창 사이의 간격 */
  }

  input[type="number"] {
    width: 50px;
    height: 30px;
    text-align: center;
    border-radius: 4px;
    border: 1px solid #ccc;
    font-size: 16px;
  }
}

/* 크롬, 사파리, 엣지 */
input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.count-btn {
  --btn-bg-color: #352F36;
  border-radius: 6px;
  --btn-bg-hover: var(--color-base-2);
  color: white;
  --btn-padding: 4px 8px;
  font-size: 20px;
}

.modal {
  display: none; /* Hidden by default */
  position: sticky;
  //z-index: 1000; /* Sit on top */
  left: 50%;
  bottom: 0;
  //transform: translate(-50%, 100%); /* 초기에 바닥에 숨어있음 */
  width: 100%;
  min-width: 340px;
  max-width: 500px;
  max-height: 100vh;
  //background-color: white;
  //border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease;
}

.popup {
  border-radius: 16px 16px 0 0;
  background-color: var(--color-base-1);
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