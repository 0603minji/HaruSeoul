<script setup>
import {ref, watch} from 'vue';

// props
const props = defineProps({
  selectedDates: {
    type: Array,
    default: []
  },
  resetToggle: {
    type: Boolean,
    default: false
  }
});

watch(()=>props.resetToggle, (newOne) => { resetSelectedDatesHandler();  });

// emit
const emit = defineEmits(['selectionChanged', 'closePopup'])


/*=== 달력 dates 생성 =======================================================================================================*/
// 초기 연도, 월
const initialYear = new Date().getFullYear();
const initialMonth = new Date().getMonth();
// 연도 선택셀렉트박스 옵션
const yearRange = 3; // 현재 연도 +- yearRange까지 옵션으로 제공
const yearOptions = [
  ...Array.from({length: yearRange}, (_, i) => initialYear - (yearRange - i)), // Previous years
  initialYear, // Current year
  ...Array.from({length: yearRange}, (_, i) => initialYear + (i + 1)) // Next years
];

// 검색가능한 최초 날짜, 최후 날짜
const minDate = new Date(yearOptions[0], 0, 1); // 검색가능한 기간 내 첫날 1.1
const maxDate = new Date(yearOptions.at(-1), 11, 31); // 검색가능한 기간 내 마지막날 12.31

// 선택된 연도, 월
const selectedYear = ref(initialYear);
const selectedMonth = ref(initialMonth);
// 선택된 날짜들
const selectedDates = ref(props.selectedDates);
watch(() => props.selectedDates, newOne => selectedDates.value = newOne);

// selectedDates가 변할 때마다 날짜순 정렬 후 emit
watch(selectedDates, (newDates) => {
      console.log('DateRangePicker watch selectedDates run')
      // 선택된 날짜가 2개일 때만 정렬
      if (newDates.length >= 2)
        newDates.sort((a, b) => a - b);
      emit('selectionChanged', newDates);
    },
    {deep: true}
);

// 이전 달의 마지막 날
const prevMonthLastDate = computed(() => new Date(selectedYear.value, selectedMonth.value, 0));
// 이번 달의 마지막 날
const thisMonthLastDate = computed(() => new Date(selectedYear.value, selectedMonth.value + 1, 0));

// selectedYear or selectedMonth가 변하면 update
const dates = computed(() => {
  const datesInMonth = [];
  // 이전 달 마지막 월요일부터 이번달 1일 전까지 dates에 추가
  const prevMonthLastDay = prevMonthLastDate.value.getDay();
  if (prevMonthLastDay !== 6) {
    for (let day = 0; day <= prevMonthLastDay; day++) {
      const date = new Date(prevMonthLastDate.value);
      date.setDate(date.getDate() - prevMonthLastDay + day);
      datesInMonth.push(date);
    }
  }

  // 이번 달 1 ~ 마지막 날까지 추가
  for (let i = 0; i < thisMonthLastDate.value.getDate(); i++) {
    datesInMonth.push(new Date(selectedYear.value, selectedMonth.value, 1 + i));
  }

  // 다음 달 1 ~ 첫번째 토요일까지 추가
  const thisMonthLastDay = thisMonthLastDate.value.getDay();
  if (thisMonthLastDay !== 6) {
    for (let i = 0; i < 6 - thisMonthLastDay; i++) {
      const date = new Date(thisMonthLastDate.value);
      date.setDate(date.getDate() + i + 1);
      datesInMonth.push(date);
    }
  }
  return datesInMonth;
});

/*=== function =======================================================================================================*/
const toNextMonth = () => {
  // console.log("toNextMonth called");
  if (selectedMonth.value === 11) {
    selectedYear.value += 1;
    selectedMonth.value = 0;
  } else {
    selectedMonth.value += 1;
  }
}

const toPrevMonth = () => {
  // console.log("toPrevMonth called");
  if (selectedMonth.value === 0) {
    selectedMonth.value = 11;
    selectedYear.value -= 1;
  } else {
    selectedMonth.value -= 1;
  }
}

const isFirstMonth = () => selectedMonth.value === 0 && selectedYear.value === initialYear - yearRange;

const isLastMonth = () => selectedMonth.value === 11 && selectedYear.value === initialYear + yearRange;

const formatDate = (date) => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are 0-based
  const day = String(date.getDate()).padStart(2, '0');

  return `${year}-${month}-${day}`;
};

// Date객체를 받아서 해당 객체의 년월로 selectedYear, selectedMonth를 초기화
const setSelectedYearMonth = (date) => {
  selectedYear.value = date.getFullYear();
  selectedMonth.value = date.getMonth();
};

const resetSelectedDatesHandler = () => {
  // 캘린터 페이지도 오늘이 표시된 곳으로 설정
  selectedYear.value = initialYear;
  selectedMonth.value = initialMonth;

  selectedDates.value = [];
};

// 2개 날짜가 선택되면 나머지 날짜들 비활성화
const isDisabled = (date) => {
  if (selectedDates.value.length === 2) {
    return !selectedDates.value.some(d => d.getTime() === date.getTime());
  }
  return false;
};

// watchEffect(() => console.log(selectedYear.value));
// watchEffect(() => console.log('prev month last date', prevMonthLastDate.value));
// watchEffect(() => console.log(selectedMonth.value + 1, thisMonthLastDate.value));
// watchEffect(() => console.log('dates: ', dates.value));
// watchEffect(() => console.log('selectedDates: ', selectedDates.value));
</script>

<template>
  <section class="date-picker">
    <header class="title">
      <h1 class="font-size:8">기간 선택</h1>
      <button
          @click.prevent="resetSelectedDatesHandler"
          class="reset-btn margin-right:auto n-btn n-btn:hover n-btn-bd:none n-icon n-icon-size:2 n-icon:reset n-icon-color:sub-1 color:sub-1">
        초기화
      </button>
      <button @click.prevent="emit('closePopup')" class="close-btn n-btn n-btn:hover n-btn-bd:none n-icon n-icon:exit">
        팝업닫기
      </button>
    </header>

    <div class="selected-date-range-display">
      <div class="date-wrapper">
        <!-- Start date display -->
        <span v-if="!selectedDates.at(0)" class="start-date-placeholder">Start date</span>
        <span v-else class="start-date" :title="'Go to calendar page with Start date'"
              @click.prevent="setSelectedYearMonth(selectedDates.at(0))">{{ formatDate(selectedDates.at(0)) }}</span>

        <!-- Clear button for start date -->
        <button v-if="selectedDates.at(0)"
                class="clear-selected-date-icon n-btn n-btn:hover n-btn-bd:none n-icon n-icon:exit"
                @click.prevent="selectedDates.shift()">날짜 선택해제
        </button>
      </div>

      <div>~</div>

      <div class="date-wrapper">
        <!-- End date display -->
        <span v-if="!selectedDates.at(1)" class="end-date-placeholder">End date</span>
        <span v-else class="end-date" :title="'Go to calendar page with End date'"
              @click.prevent="setSelectedYearMonth(selectedDates.at(1))">{{ formatDate(selectedDates.at(1)) }}</span>

        <!-- Clear button for end date -->
        <button v-if="selectedDates.at(1)"
                class="clear-selected-date-icon n-btn n-btn:hover n-btn-bd:none n-icon n-icon:exit"
                @click.prevent="selectedDates.pop()">날짜 선택해제
        </button>
      </div>
    </div>

    <section class="calendar-container">
      <header class="calendar-header">
        <h1 class="d:none">Calendar</h1>
        <button @click.prevent="toPrevMonth"
                :disabled="isFirstMonth()"
                class="to-prev-month n-btn n-btn:hover border-radius:full n-icon n-icon:arrow_left n-icon-size:3"
                type="button">이전
        </button>
        <div class="year-month-wrapper font-size:9">
          <select class="year" v-model="selectedYear">
            <option v-for="year in yearOptions" :value="year">{{ year }}</option>
          </select>

          <select class="month" v-model="selectedMonth">
            <option v-for="(_, index) in 12" :value="index">{{ index + 1 }}</option>
          </select>
        </div>
        <button @click.prevent="toNextMonth"
                :disabled="isLastMonth()"
                class="to-next-month n-btn n-btn:hover border-radius:full n-icon n-icon:arrow_right n-icon-size:3"
                type="button">다음
        </button>
      </header>

      <ul class="weekdays">
        <li>
          <abbr title="Sunday">S</abbr>
        </li>
        <li>
          <abbr title="Monday">M</abbr>
        </li>
        <li>
          <abbr title="Tuesday">T</abbr>
        </li>
        <li>
          <abbr title="Wednesday">W</abbr>
        </li>
        <li>
          <abbr title="Thursday">T</abbr>
        </li>
        <li>
          <abbr title="Friday">F</abbr>
        </li>
        <li>
          <abbr title="Saturday">S</abbr>
        </li>
      </ul>

      <ol class="day-grid">
        <li v-for="date in dates"
            :class="{
            /* 전달, 다음달도 표시되고 선택가능토록 */
            'month-prev': date.getMonth() === (selectedMonth-1<0 ? 11 : selectedMonth-1),
            'month-next': date.getMonth() === (selectedMonth+1>11 ? 0 : selectedMonth+1),
            'out-of-date-range': !(minDate <= date && date <= maxDate),
            'today': date.toDateString() === new Date().toDateString(),
            'selected': selectedDates.find(d => d.getTime() === date.getTime()),
            'date-between': selectedDates.at(0) < date && date < selectedDates.at(1)
          }"
        >
          <label>
            <input type="checkbox"
                   :disabled="isDisabled(date)"
                   v-model="selectedDates"
                   :value="date">
            <span>{{ date.getDate() }}</span>
          </label>
        </li>
      </ol>
    </section>
  </section>
</template>

<style scoped>
.date-picker {
  --calendar-bg-color: #f5f5f5;
  --date-color-disabled: rgba(0, 23, 84, 0.15);

  display: flex;
  flex-direction: column;
  width: 300px;

  .title {
    display: flex;
    height: 32px;
    padding: 0 0 0 10px;
    align-items: center;
    margin-bottom: 8px;

    .reset-btn {
      --btn-padding: 8px 10px;
      margin-left: 4px;
    }

    .close-btn {
      --icon-color: var(--color-base-5);
      --icon-size: 20px;
    }

    .close-btn:hover {
      --icon-color: var(--color-base-7);
    }
  }

  .selected-date-range-display {
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px solid #ccc;
    border-radius: 6px;
    height: 42px;
    padding: 8px;
    margin: 0 10px 30px 10px;

    .date-wrapper {
      display: flex;
      flex-grow: 1;
      flex-basis: 0;
      gap: 0;
      align-items: center;
      justify-content: center;

      .start-date-placeholder, .end-date-placeholder {
        font-size: 14px;
        color: var(--color-base-6);
      }

      .start-date, .end-date {
        font-size: 14px;
        cursor: pointer;
        padding: 6px 6px;
        border-radius: 12px;
      }

      :is(.start-date, .end-date):hover {
        background-color: var(--color-base-2);
      }

      .clear-selected-date-icon {
        --btn-bg-color: var(--color-base-3);
        --btn-bg-hover: var(--color-base-3);
        --icon-size: 10px;
        --icon-color: var(--color-base-6);
      }

      .clear-selected-date-icon:hover {
        --icon-color: var(--color-base-8);
      }
    }
  }

  .calendar-container {
    //max-width: 768px;
    //min-width: 250px;
    width: 100%;
    display: flex;
    flex-direction: column;
    gap: 12px;
    padding: 20px;
    background-color: var(--calendar-bg-color);
    border-radius: 14px;
    box-shadow: 0 1.88px 2px 0 rgba(0, 14, 51, 0.05);

    .calendar-header {
      display: flex;
      justify-content: center;
      align-items: center;
      padding-bottom: 8px;

      .year-month-wrapper {
        display: flex;
        gap: 8px;
        //box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);

        .year, .month {
          font-size: 1rem;
          border-radius: 8px;
          padding: 4px 8px;
        }
      }

      /*--- 다음달 이전달 버튼 -----------------------------------------------------------------------------------------*/

      button {
        background-color: var(--color-base-1);
        --btn-border-color: transparent;
        box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);
      }

      button:hover {
        background-color: var(--color-base-3);
      }

      .to-prev-month {
        margin-left: auto;
        margin-right: 16px;
      }

      .to-next-month {
        margin-right: auto;
        margin-left: 16px;
      }

      /* 이전 다음달이 존재하지 않으면 버튼 disabled */

      :is(.to-prev-month, .to-next-month):disabled {
        --icon-color: var(--color-base-5);
        cursor: not-allowed;
      }
    }

    /*----------------------------------------------------------------------------------------------------------------*/

    .weekdays {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      font-weight: 500;
      font-size: 0.75rem;
      justify-items: center;

      li {
        abbr {
          text-decoration: none;
        }
      }
    }

    .day-grid {
      width: 100%;
      display: grid;
      gap: 8px;
      grid-template-columns: repeat(7, 1fr);


      li {
        min-width: 0;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        height: 42px;
        border-radius: 4px;
        transition: background-color 0.1s ease, border-color 0.1s ease, color 0.1s ease;

        background-color: var(--color-base-1);
        box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);
        /* position x, y, blur, spread, color */

        > label {
          flex-grow: 1;
          width: 100%;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;

          /* 체크박스 숨김 */

          input[type="checkbox"] {
            display: none;
            width: 100%; /* Make input take full width of li */
            height: 100%; /* Make input take full height of li */
            cursor: pointer; /* Change cursor to pointer on hover */
            appearance: none; /* Remove default checkbox appearance (optional) */
          }

          span {
            color: var(--color-base-9);
            font-size: 0.75rem;
            font-weight: 500;
          }
        }
      }

      .today {
        border: 2px solid var(--color-sub-1);

        label span {
          color: var(--color-base-9);
        }
      }

      :is(.month-prev, .month-next) {
        background-color: var(--calendar-bg-color);
        box-shadow: none;

        > label span {
          color: var(--date-color-disabled);
        }
      }

      /* 검색범위 외 month-prev, month-next(ex. 2020-12-31 or 2028-01-01)는 hidden 처리 */

      .out-of-date-range {
        visibility: hidden;
      }

      li:has(input[type="checkbox"]:not(:disabled)):hover {
        background-color: var(--color-base-4);
      }

      /* 선택된 날짜 */

      .selected {
        background-color: var(--color-sub-1);

        > label span {
          color: var(--color-base-1);
        }
      }

      .selected:hover {
        background-color: var(--color-sub-0) !important;
      }

      .date-between {
        background-color: var(--color-sub-2);
      }
    }
  }

  @media (min-width: 768px) {
    .calendar-container {

    }
  }
}
</style>