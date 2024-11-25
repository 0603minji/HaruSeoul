<script setup>

// props
import {useAuthFetch} from "~/composables/useAuthFetch.js";

const props = defineProps({
  tab: {
    type: String,
    default: null
  },
  selectedStatuses: {
    type: Array,
    default: []
  }
});

// emit
const emit = defineEmits(['closeModal']);


// Selected Statuses 개설된 프로그램 상태 1.모집중 2.폐지임박 3.종료 4.취소 5.획정대기 6.확정
// props로 초기화
const selectedStatuses = ref([]);
watchEffect(() => {
  selectedStatuses.value = props.selectedStatuses;
})

const translateStatusNameToKorean = (statusName) => {
  if (statusName === 'On Going')
    return '모집 중'
  else if (statusName === 'Urgent')
    return '폐지임박'
  else if (statusName === 'Finished')
    return '종료됨'
  else if (statusName === 'Canceled')
    return '취소됨'
  else if (statusName === 'Wait Confirm')
    return '예약확정 대기'
  else if (statusName === 'Confirmed')
    return '예약확정'
}

// Computed property to check if a status should be disabled based on the tab
const isStatusDisabled = (StatusName) => {
  // console.log('isStatusDisabled called. tab: ', props.tab);
  // console.log('   statusName : ', StatusName.toLowerCase())
  const statusName = StatusName.toLowerCase();
  if (props.tab === 'canceled') {
    // console.log('     isStatusDisabled: ', statusName !== 'canceled')
    return statusName !== 'canceled';
  } else if (props.tab === 'finished') {
    // console.log('     isStatusDisabled: ', statusName !== 'finished')
    return statusName !== 'finished';
  } else {
    // console.log('     isStatusDisabled: ', statusName === 'finished' || statusName === 'canceled')
    return statusName === 'finished' || statusName === 'canceled';
  }
};


// Handle selection change
// const updateSelectedStatuses = (selectedOptions) => {
//   selectedStatuses.value = selectedOptions;
//   console.log('******* PublishedStatusesFilterModal: updateSelectedStatuses called');
//   console.log('         ->  selectedStatuses: ', selectedStatuses);
// };

const closeModal = () => {
  emit('closeModal', selectedStatuses.value);
}

//=== status-selector =======================================================================
const resetSelectedStatusesHandler = () => {
  selectedStatuses.value = [];
}

const {data} = await useAuthFetch(`host/published-programs/status`);

</script>

<template>
  <aside class="popup modal">
    <header class="popup-header">
      <h1 class="font-size:9">필터</h1>
      <button @click.prevent="closeModal" class="n-btn n-btn:hover n-btn-border:transparent n-icon n-icon:exit">닫기
      </button>
    </header>

    <form @submit.prevent="closeModal" class="popup-body" action="">
      <!-- 상태 체크박스  -->
      <section class="status-selector">
        <header class="title">
          <h1 class="font-size:8">프로그램 상태</h1>
          <button
              @click.prevent="resetSelectedStatusesHandler"
              class="margin-left:auto n-btn n-btn:hover n-btn-bd:none n-icon n-icon-size:2 n-icon:reset n-icon-color:sub-1 n-deco color:sub-1">
            초기화
          </button>
        </header>

        <ul class="status-container">
          <li v-for="status in data.statusDtos">
            <label>
              <input type="checkbox"
                     v-model="selectedStatuses"
                     :checked="selectedStatuses.includes(status.name)"
                     :disabled="isStatusDisabled(status.name)"
                     :value="status.id">
              <span>{{ translateStatusNameToKorean(status.name) }}</span>
            </label>
          </li>
        </ul>

      </section>

      <div class="submit">
        <button class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1">확인</button>
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
  min-width: 300px;
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

    /*=== 프로그램 상태 선택박스 =========================================================================================*/

    .status-selector {
      --bg-color: #f5f5f5;
      --status-color-disabled: rgba(0, 23, 84, 0.3);

      display: flex;
      flex-direction: column;

      .title {
        display: flex;
        height: 32px;
        padding: 0 0 0 10px;
        align-items: center;
        margin-bottom: 12px;

        button {
          --btn-padding: 8px 10px;
        }
      }

      .status-container {
        //max-width: 768px;
        //min-width: 250px;
        width: 100%;
        height: auto;
        display: grid;
        grid-template-columns: repeat(2, 1fr); /* 2열로 배치 */
        gap: 12px;
        padding: 30px;
        background-color: var(--bg-color);
        border-radius: 25px;
        box-shadow: 0 3px 2px 0 var(--color-base-3);

        li > label {
          display: flex;
          gap: 6px;
          align-items: center;

        }

        li > label:has(input:disabled) {
          color: var(--status-color-disabled);
        }
      }
    }

    /*================================================================================================================*/

    .submit {
      margin-left: auto;
      padding: 20px 0;
    }
  }
}
</style>