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


  <form class="popup-body" action="">
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

      <section class="status-container">
        <ul>
          <li v-for="status in data.statusDtos">
            <label>
              <input type="checkbox"
                     v-model="selectedStatuses"
                     :checked="selectedStatuses.includes(status.name)"
                     :disabled="isStatusDisabled(status.name)"
                     @change="onOptionChange"
                     :value="status.id">
              <span>{{ status.name }}</span>
            </label>
          </li>
        </ul>
      </section>
    </section>
  </form>

</template>

<style scoped>

.popup-body {
  display: flex;
  flex-direction: column;
  padding: 0 10px;
  overflow-y: auto;

  /*=== 프로그램 상태 선택박스 =========================================================================================*/

  .status-selector {
    --bg-color: #f5f5f5;
    --status-color-disabled: rgba(0, 23, 84, 0.15);

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
      height: 160px;
      display: flex;
      flex-direction: column;
      gap: 12px;
      padding: 20px;
      background-color: var(--bg-color);
      border-radius: 25px;
      box-shadow: 0 3px 2px 0 var(--color-base-3);
    }
  }

  /*================================================================================================================*/

  .submit {
    margin-left: auto;
    padding: 20px 0;
  }
}

</style>