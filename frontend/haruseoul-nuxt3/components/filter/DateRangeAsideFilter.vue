<script setup>
import DateRangePicker from "~/components/filter/DateRangePicker.vue";
import DateRangePickerPopup from "~/components/filter/DateRangePickerPopup.vue";

// props
const props = defineProps({
  selectedDates: {
    type: Array,
    default: []
  }
});

// emit
const emit = defineEmits(['emitSelectedDates']);

// Selected dates 프로그램 검색기간 [시작일 , 끝] or [날짜]
const selectedDates = ref(props.selectedDates);
watch(() => props.selectedDates, (newOne) => selectedDates.value = newOne);

// 자식인 DateRangePicker의 resetSelectedDatesHandler를 호출시키기 위한 토글
const resetToggle = ref(false);

const isDateRangePickerVisible = ref(false);





// Handle selection change
const updateSelectedDates = (selectedOptions) => {
  selectedDates.value = selectedOptions;
  console.log('******* ProgramFilterModal: updateSelectedDates called');
  console.log('         ->  selectedDates: ', selectedDates.value);
  emit('emitSelectedDates', selectedDates.value);
};

// 자식인 DateRangePicker의 resetSelectedDatesHandler를 호출
const resetDateRangePicker = () => { resetToggle.value = !resetToggle.value; emit('emitSelectedDates', []);}

const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
  const day = String(date.getDate()).padStart(2, '0');

  return `${year}-${month}-${day}`;
};

const toggleDateRangePicker = () => {
  console.log("toggleDateRangePicker called")
  isDateRangePickerVisible.value = !isDateRangePickerVisible.value;
  console.log('       isDateRangePickerVisible: ', isDateRangePickerVisible.value);
}

</script>

<template>
  <section class="filter">
    <!-- selectedDates를 표시. 눌렀을 때 아래 form태그 visible   -->
    <header class="title">
      <h1 class="font-size:8">기간</h1>
      <button
          @click.prevent="resetDateRangePicker"
          class="reset-btn margin-left:auto n-btn n-btn:hover n-btn-bd:none n-icon n-icon-size:2 n-icon:reset n-icon-color:sub-1 color:sub-1"
          :class="{'show': selectedDates.length>0 }">
        초기화
      </button>
    </header>

    <!-- selectedDate를 표시 -->
    <div @click.prevent="toggleDateRangePicker" class="selected-date-range-display" :class="{'active': selectedDates.length}">
      <div class="date-wrapper">
        <!-- Start date display -->
        <span v-if="!selectedDates.at(0)" class="start-date-placeholder">Start date</span>
        <span v-else class="start-date" :title="'Go to calendar page with Start date'">{{ formatDate(selectedDates.at(0)) }}</span>
      </div>

      <div>~</div>

      <div class="date-wrapper">
        <!-- End date display -->
        <span v-if="!selectedDates.at(1)" class="end-date-placeholder">End date</span>
        <span v-else class="end-date" :title="'Go to calendar page with End date'">{{ formatDate(selectedDates.at(1)) }}</span>
      </div>
    </div>

    <!--  캘린더 팝업  -->
    <div class="calendar-popup" :class="{'show': isDateRangePickerVisible}">
      <DateRangePickerPopup :selectedDates="selectedDates"
                       :reset-toggle="resetToggle"
                       @selection-changed="updateSelectedDates"
                       @close-popup="()=>{isDateRangePickerVisible=false}"/>
    </div>
  </section>
</template>

<style scoped>
.filter {
  border-radius: 16px 16px 0 0;
  background-color: var(--color-base-1);
  //padding: 16px 24px;
  display: flex;
  flex-direction: column;
  position: relative; /* filter-form(캘린더) position: absolute용 */

  .title {
    display: flex;
    height: 32px;
    align-items: center;
    margin-bottom: 12px;

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

  .selected-date-range-display {
    display: flex;
    align-items: center;
    justify-content: center;
    //border: 1px solid #ccc;
    border-radius: 6px;
    /*height: 42px;*/
    padding: 8px;
    cursor: pointer;
    background-color: var(--color-base-2);

    .date-wrapper {
      display: flex;
      flex-grow: 1;
      flex-basis: 0;
      gap: 4px;
      align-items: center;
      justify-content: center;

      .start-date-placeholder, .end-date-placeholder {
        font-size: 14px;
        color: var(--color-base-6);
      }

      .start-date, .end-date {
        font-size: 14px;
        font-weight: 500;
        padding: 0 4px;
        border-radius: 12px;
      }
    }
  }

  .selected-date-range-display:hover {
    background-color: var(--color-sub-3);
  }
  .selected-date-range-display.active {
    color: var(--color-sub-1);
    //background-color: var(--color-sub-3);
    border-color: var(--color-sub-1);
  }
  .selected-date-range-display.active:hover {
    background-color: var(--color-sub-3);
  }

  .calendar-popup {
    display: none;
    flex-direction: column;
    padding: 20px;
    overflow-y: auto;
    position: absolute;
    top: 102px;
    left: -108px;
    z-index: 100;
    background-color: var(--color-base-1);
    box-shadow: 0 8px 16px 0 rgba(33, 37, 41, 0.15), 0 0 0 1px #e9ecef, 0 1px 4px 0 rgba(0, 0, 0, 0.15);
  }

  .calendar-popup.show {
    display: flex;
  }
}
</style>