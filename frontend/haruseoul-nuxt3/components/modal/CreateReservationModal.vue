<script setup>
import {ref, watch, watchEffect} from 'vue';
import axios from "axios";
import ReservationDatePicker from "~/components/filter/ReservationDatePicker.vue";

// Props
const props = defineProps({
  pIds: {
    type: Number,
    default: null
  },
  hostId: {
    type: Number,
    default: null
  }
});

// emit
const emit = defineEmits(['closeModal']);

const reRenderTrigger = ref(false);

/*=== variables ======================================================================================================*/
// Selected dates 프로그램 진행일
let selectedDate = ref(null);

// submit버튼을 누를 수 있는 상태인지? 프로그램, 날짜 모두 선택되어야함
// const isReservable = computed(() => selectedDate.value
//     // &&
// );

const userDetails = useUserDetails();

// === function ========================================================================================================
// Handle selection change
const updateSelectedProgram = (selectedOption) => {
  selectedProgram.value = selectedOption;
  console.log('******* PublishProgramModal: updateSelectedProgram called');
  console.log('          ->  selectedProgram: ', selectedProgram.value);
};

const updateSelectedDate = (selectedOptions) => {
  selectedDate.value = selectedOptions;
  console.log('******* PublishProgramModal: updateSelectedDate called');
  console.log('          ->  selectedDate: ', selectedDate.value);
};

const submitHandler = async () => {
  console.log('******* PublishedProgramModal: submitHandler called');
  if (!isReservable)
    return;

  // const publishedProgramCreateDto = {
  //   regMemberId: userDetails.id.value,
  //   programId: selectedProgram.value.id,
  //   dates: selectedDate.value.map((date) =>
  //       new Intl.DateTimeFormat('ko-KR', {
  //         year: 'numeric',
  //         month: '2-digit',
  //         day: '2-digit',
  //         timeZone: 'Asia/seoul'
  //       }).format(date)
  //           .replace(/\. /g, '-')
  //           .replace('.', '')
  //   )
  // };
  const reservationCreateDto = {
    // private Long publishedProgramId;
    //
    // private Long guestId;
    //
    // private Integer numberOfGuest;
    //
    // private String reservationRequirement;
  }

  try {
    console.log('          ->  POST host/published-programs');
    const response = await axios.post(`${config.public.apiBase}host/published-programs`, publishedProgramCreateDto);
    console.log('          PublishedProgram post result: ', response.data);
    // 포스트된 결과를 반영하여 모달창 달력 업데이트

  } catch (error) {
    console.error('         Error creating publishedProgram: ', error);
  }

  // 모달창 닫기
  closeModal();
};

const closeModal = () => {
  console.log("closeModal")
  emit('closeModal');

  // // Delay the re-render trigger to allow closing animation to complete
  // setTimeout(() => {
  //   reRenderTrigger.value = !reRenderTrigger.value;
  //   console.log('reRenderTrigger: ', reRenderTrigger);
  // }, 300); // 0.3 seconds delay (300ms)
}
</script>

<template>
  <aside class="popup modal">
    <header class="popup-header">
      <h1 class="font-size:9">예약하기</h1>
      <button @click.prevent="closeModal()" class="n-btn n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form @submit.prevent="submitHandler" class="popup-body" action="">
      <!-- 진행일 선택 -->
      <ReservationDatePicker :key="reRenderTrigger" :hostId="props.hostId" @selection-changed="updateSelectedDate"/>

      <div class="submit">
        <NuxtLink to="../guest/reservations" class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1"
                :disabled="!isReservable">결제하기
        </NuxtLink>
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
      margin-left: auto;
      padding: 20px 0;

      button:disabled {
        cursor: not-allowed;
        opacity: 0.3;
      }
    }
  }
}
</style>