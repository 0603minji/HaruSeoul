<script setup>
// 초기 연도, 월
const initialYear = new Date().getFullYear();
const initialMonth = new Date().getMonth();
const yearRange = 3; // 현재 연도 +- yearRange까지 옵션으로 제공

// 선택된 연도, 월
const selectedYear = ref(initialYear);
const selectedMonth = ref(initialMonth);
// 선택된 날짜들
const selectedDates = ref([]);


// 이전 달의 마지막 날
const prevMonthLastDate = computed(() => new Date(selectedYear.value, selectedMonth.value, 0));
// 이번 달의 마지막 날
const thisMonthLastDate = computed(() => new Date(selectedYear.value, selectedMonth.value + 1, 0));

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

/*
[
  {
    date: Date객체,
    schedule: [programs]
  },
  {
    date: Date객체,
    schedule: [programs]
  }
]; */
const datesWithScedule = ref([]);


/*=== function =================================================================================================================*/
// 연도선택 <option>태그 생성용
const getYearOptions = (baseYear, range) => {
  const year = [];
  year.push(baseYear);

  for (let i = 0; i < range; i++)
    year.push(baseYear + i + 1);

  for (let i = 0; i < range; i++)
    year.unshift(baseYear - (i + 1));

  return year;
};

const toNextMonth = () => {
  if (selectedMonth.value === 11) {
    if (selectedYear.value === initialYear + yearRange)
      return;
    selectedYear.value += 1;
    selectedMonth.value = 0;
  } else {
    selectedMonth.value += 1;
  }
}

const toPrevMonth = () => {
  if (selectedMonth.value === 0) {
    if (selectedYear.value === initialYear - yearRange)
      return;
    selectedMonth.value = 11;
    selectedYear.value -= 1;
  } else {
    selectedMonth.value -= 1;
  }
}

// 개설가능한 범위의 날짜인지?
const isDateInRange = (date) => {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const startRange = new Date(today);
  const endRange = new Date(today);

  startRange.setDate(today.getDate() + 3); // today + 3 days
  endRange.setDate(today.getDate() + 3 + 21); // startRange + 21 days

  return startRange <= date && date < endRange;
};

watchEffect(() => console.log(selectedYear.value));
watchEffect(() => console.log(prevMonthLastDate.value));
watchEffect(() => console.log(selectedMonth.value + 1, thisMonthLastDate.value));
watchEffect(() => console.log(dates.value));
watchEffect(() => console.log(selectedDates.value));
</script>
<template>

  <section class="calendar-new">
    <header class="calendar-header">
      <h1 class="d:none">May 2024</h1>
      <button @click.prevent="toPrevMonth"
              class="to-prev-month n-btn border-radius:full n-icon n-icon:arrow_left n-icon-size:3" type="button">이전
      </button>
      <div class="year-month-wrapper font-size:9 font-weight:bold">
        <select class="year" v-model="selectedYear">
          <option v-for="year in getYearOptions(initialYear, yearRange)" :value="year">{{ year }}</option>
        </select>

        <select class="month" v-model="selectedMonth">
          <option v-for="(month, index) in 12" :value="index">{{ index + 1 }}</option>
        </select>
      </div>
      <button @click.prevent="toNextMonth"
              class="to-next-month n-btn border-radius:full n-icon n-icon:arrow_right n-icon-size:3" type="button">다음
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
            'month-prev': date.getMonth() === (selectedMonth-1<0 ? 11 : selectedMonth-1),
            'month-next': date.getMonth() === (selectedMonth+1>11 ? 0 : selectedMonth+1),
            'today': date.toDateString() === new Date().toDateString(),
            'available': isDateInRange(date),
            'selected': selectedDates.includes(date)
            // 'occupied':
          }"
      >
        <label>
          <input type="checkbox"
                 :disabled="!isDateInRange(date)"
                 v-model="selectedDates"
                 :value="date">
          <span>{{ date.getDate() }}</span>
        </label>
      </li>
    </ol>
  </section>

</template>

<style scoped>
.calendar-new {
  //max-width: 768px;
  //min-width: 250px;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 14px;
  box-shadow: 0 1.88px 2px 0 rgba(0, 14, 51, 0.05);


  .calendar-header {
    display: flex;
    justify-content: center;
    align-items: center;
    padding-bottom: 8px;

    .year-month-wrapper {
      display: flex;
      //box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);

      .year, .month {
        font-size: 1rem;
      }
    }

    button {
      background-color: var(--color-base-1);
      --btn-border-color: transparent;
      box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);
    }

    .to-prev-month {
      margin-left: auto;
      margin-right: 16px;
    }

    .to-next-month {
      margin-right: auto;
      margin-left: 16px;
    }
  }

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

      height: 6vh;
      /* background-color: var(--color-base-1); */
      border-radius: 4px;
      transition: background-color 0.3s ease, border-color 0.3s ease, color 0.3s ease;

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
          color: rgba(0, 23, 84, 0.15);
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
      visibility: hidden;
      background-color: transparent;
    }

    /* 개설가능한 날짜 */

    .available {
      background-color: var(--color-base-1);
      box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);
      /* position x, y, blur, spread, color */

      > label span {
        color: var(--color-base-9);
      }
    }

    .available:not(.selected):hover {
      background-color: var(--color-base-4);
    }

    /* 선택된 개설가능한 날짜 */

    .selected {
      background-color: var(--color-sub-1);

      > label span {
        color: var(--color-base-1);
      }
    }

    /* 이미 개설된 프로그램 존재하는 날짜 */

    .occupied {
      background-color: var(--color-base-3);
      box-shadow: none;

      > label span {
        color: rgba(0, 23, 84, 0.15);
      }
    }
  }
}

@media (min-width: 768px) {
  .calendar-new {

  }
}
</style>