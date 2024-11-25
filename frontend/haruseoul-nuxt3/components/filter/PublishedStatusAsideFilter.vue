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
const emit = defineEmits(['closeModal', 'updateSelectedStatuses']);


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
  emit('updateSelectedStatuses', selectedStatuses.value);
}

const onOptionChange = () => {
  emit('updateSelectedStatuses', selectedStatuses.value);
}


const {data} = await useAuthFetch(`host/published-programs/status`);

</script>

<template>
  <!-- 상태 체크박스  -->
  <section class="status-selector">
    <header class="title">
      <h1 class="font-size:8">프로그램 상태</h1>
      <button
          @click.prevent="resetSelectedStatusesHandler"
          class="reset-btn margin-left:auto n-btn n-btn:hover n-btn-bd:none n-icon n-icon-size:2 n-icon:reset n-icon-color:sub-1 color:sub-1"
          :class="{'show': selectedStatuses.length>0 }">
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
                 @change="onOptionChange"
                 :value="status.id">
          <span>{{ translateStatusNameToKorean(status.name) }}</span>
        </label>
      </li>
    </ul>

  </section>
</template>

<style scoped>


/*=== 프로그램 상태 선택박스 =========================================================================================*/

.status-selector {
  --bg-color: #f5f5f5;
  --status-color-disabled: rgba(0, 23, 84, 0.3);

  display: flex;
  flex-direction: column;
  overflow-y: auto;

  .title {
    display: flex;
    height: 32px;
    align-items: center;
    margin-bottom: 8px;

    h1 {
      font-weight: 600;
    }

    .reset-btn {
      --btn-padding: 8px 10px;
      display: none;
    }

    .reset-btn.show {
      display: flex;
    }
  }

  .status-container {
    //max-width: 768px;
    //min-width: 250px;
    width: 100%;
    height: auto;
    display: flex;
    flex-direction: column;
    padding: 0 8px;
    //background-color: var(--bg-color);
    border-radius: 6px;
    /*box-shadow: 0 3px 2px 0 var(--color-base-3);*/

    li {
      padding: 4px;

      label {
        display: flex;
        gap: 6px;
        align-items: center;
      }
      label:has(input:disabled) {
        color: var(--status-color-disabled);
      }
    }
  }
}

/*================================================================================================================*/

.submit {
  margin-left: auto;
  padding: 20px 0;
}


</style>