<script setup>
import DateRangePicker from "~/components/filter/DateRangePicker.vue";

// props
const props = defineProps({
  selectedDates: {
    type: Array,
    default: []
  }
});

// emit
const emit = defineEmits(['closeModal']);

// Selected dates 프로그램 검색기간 [시작일 , 끝] or [날짜]
const selectedDates = ref(props.selectedDates);
watch(() => props.selectedDates, (newOne) => selectedDates.value = newOne);

// 자식인 DateRangePicker의 resetSelectedDatesHandler를 호출시키기 위한 토글
const resetToggle = ref(false);

// Handle selection change
const updateSelectedDates = (selectedOptions) => {
  selectedDates.value = selectedOptions;
  console.log('******* ProgramFilterModal: updateSelectedDates called');
  console.log('         ->  selectedDates: ', selectedDates.value);
};

const closeModal = () => emit('closeModal', selectedDates.value);

const resetDateRangePicker = () => resetToggle.value = !resetToggle.value;

</script>

<template>
  <aside class="popup">
    <!-- selectedDates를 표시. 눌렀을 때 아래 form태그 visible   -->
    <header class="title">
      <h1 class="font-size:8">기간 선택</h1>
      <button
          @click.prevent="resetDateRangePicker"
          class="margin-left:auto n-btn n-btn:hover n-btn-bd:none n-icon n-icon-size:2 n-icon:reset n-icon-color:sub-1 n-deco color:sub-1">
        초기화
      </button>
    </header>

    <form @submit.prevent="closeModal" class="popup-body" action="">
      <DateRangePicker :selectedDates="selectedDates"
                       :reset-toggle="resetToggle"
                       @selection-changed="updateSelectedDates"/>

      <div class="submit">
        <button class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1">확인</button>
      </div>
    </form>
  </aside>
</template>

<style scoped>
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
    }
  }
}
</style>