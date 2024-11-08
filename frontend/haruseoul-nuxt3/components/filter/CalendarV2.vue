<script setup>
import {ref, watch, watchEffect} from 'vue';

// emit
const emit = defineEmits(['selectionChanged'])

// Props
const props = defineProps({
  hostId: {
    type: Number,
    required: true
  }
});

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

// dates가 변하면 update
const publishedProgramQuery = computed(() => (
    {
      mIds: props.hostId,
      s: [1, 2, 5, 6].join(","),
      d: [dates.value.at(0), dates.value.at(-1)] // 달력에 표시된 첫날 ~ 끝날 dates.getFirst getLast 한국시간 자정
          .map((date) =>
              new Intl.DateTimeFormat('ko-KR', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                timeZone: 'Asia/seoul'
              }).format(date)
                  .replace(/\. /g, '-')
                  .replace('.', '')
          ).join(",")
    })
);

// query가 변하면 새로 fetch (refresh) -> publishedPrograms
watch(() => publishedProgramQuery.value,
    (newQuery) => {
      console.log('publishedProgramQuery.value watch진입: pulishedRefresh()')
      publishedRefresh();
    }
);

/*=== fetch ==========================================================================================================*/
const config = useRuntimeConfig();

/*
[
  {
    date: Date객체,
    schedules: [programs]
  },
  {
    date: Date객체,
    schedules: [programs]
  }
]; */
const datesWithScedules = ref([]);

// Published Programs in date range
const publishedPrograms = ref([]);

// 캘린더에 개설불가능한 날짜 표시를 위한 개설프로그램 목록 fetch
const {data: publishedData, refresh: publishedRefresh} = useFetch(`host/published-programs`, {
  baseURL: config.public.apiBase,
  params: computed(() => publishedProgramQuery.value)
});

watch(() => publishedData.value,
    (newPublishedData) => {
      console.log('publishedData watch진입')
      if (newPublishedData) {
        console.log('   ->  new publishedData: ', newPublishedData.publishedPrograms);
        publishedPrograms.value = newPublishedData.publishedPrograms;
        console.log('   ->  publishedData.value fetched. publishedPrograms: ', publishedPrograms.value);
      }
    }
);

watch(() => publishedPrograms.value,
    (newPrograms) => {
      console.log('publishedPrograms watch 진입');
      // dates로 datesWithPrograms [{date: , schedules: []}] update
      datesWithScedules.value = dates.value.map(date => {
        // date와 진행일이 같은 프로그램들만 골라내서 schedules에 담기
        const schedules = newPrograms.filter(program => {
          // console.log('date        : ', date.toISOString(), date.toLocaleString());
          // console.log('program date: ', new Date(program.date+'T00:00:00+09:00').toISOString());
          return new Date(program.date + 'T00:00:00+09:00').getTime() === date.getTime();
        });

        // date와 schedules로 dateWithSchedules 생성
        const dateWithSchedules = {
          date: date,
          schedules
        }
        // console.log(dateWithSchedules);
        return dateWithSchedules;
      });
      console.log('   ->  datesWithSchedules updated', datesWithScedules.value);
    }
);

// --- 개설가능일 계산용 -------------------------------------------------------------------------------------------------
// today + startOffset부터 개설가능
const startOffset = 3;
// today + startOffset부터 publishableRange만큼 개설가능
const publishableRange = 21;

// query ?mIds=props.hostId&s=1,2,5,6&d=개설가능첫날,끝날
const today = new Date();
today.setHours(0, 0, 0, 0);
const rangeStart = new Date(today); // 개설가능 시작일
const rangeEnd = new Date(today); // 개설가능 말일

rangeStart.setDate(today.getDate() + startOffset); // today + 3 days
rangeEnd.setDate(today.getDate() + startOffset + publishableRange); // rangeStart + 21 days

/*=== function =======================================================================================================*/
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
  return rangeStart <= date && date < rangeEnd;
};

const resetSelectedDatesHandler = () => {
  if (selectedDates.value.length === 0)
    return;
  selectedDates.value = [];
};

// Emit selection changed
const emitSelectionChanged = () => {
  emit('selectionChanged', [...selectedDates.value]);
};

// Watch the selected dates and emit changes
watch(selectedDates, emitSelectionChanged);

// watchEffect(() => console.log(selectedYear.value));
// watchEffect(() => console.log('prev month last date', prevMonthLastDate.value));
// watchEffect(() => console.log(selectedMonth.value + 1, thisMonthLastDate.value));
// watchEffect(() => console.log('dates: ', dates.value));
// watchEffect(() => console.log('selectedDates: ', selectedDates.value));
watchEffect(() => console.log('published program query: ', publishedProgramQuery.value));
</script>

<template>
  <section class="date-picker">
    <header class="title">
      <h1 class="font-size:8">진행일 선택</h1>
      <button
          @click.prevent="resetSelectedDatesHandler"
          class="margin-left:auto n-btn n-btn:hover n-btn-bd:none n-icon n-icon-size:2 n-icon:reset n-icon-color:sub-1 n-deco color:sub-1">
        초기화
      </button>
    </header>
    <section class="calendar-new">
      <header class="calendar-header">
        <h1 class="d:none">May 2024</h1>
        <button @click.prevent="toPrevMonth"
                class="to-prev-month n-btn n-btn:hover border-radius:full n-icon n-icon:arrow_left n-icon-size:3" type="button">이전
        </button>
        <div class="year-month-wrapper font-size:9">
          <select class="year" v-model="selectedYear">
            <option v-for="year in getYearOptions(initialYear, yearRange)" :value="year">{{ year }}</option>
          </select>

          <select class="month" v-model="selectedMonth">
            <option v-for="(month, index) in 12" :value="index">{{ index + 1 }}</option>
          </select>
        </div>
        <button @click.prevent="toNextMonth"
                class="to-next-month n-btn n-btn:hover border-radius:full n-icon n-icon:arrow_right n-icon-size:3" type="button">다음
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
        <!--dateSchedule = {date: Date객체, [publishedProgramListDto]}-->
        <li v-for="dateSchedule in datesWithScedules"
            :class="{
            'month-prev': dateSchedule.date.getMonth() === (selectedMonth-1<0 ? 11 : selectedMonth-1),
            'month-next': dateSchedule.date.getMonth() === (selectedMonth+1>11 ? 0 : selectedMonth+1),
            'today': dateSchedule.date.toDateString() === new Date().toDateString(),
            'available': isDateInRange(dateSchedule.date),
            'selected': selectedDates.find(date => date.getTime() === dateSchedule.date.getTime()),
            'occupied': dateSchedule.schedules.length !== 0
          }"
        >
          <label>
            <input type="checkbox"
                   :disabled="!isDateInRange(dateSchedule.date) || dateSchedule.schedules.length!==0"
                   v-model="selectedDates"
                   :value="dateSchedule.date">
            <span>{{ dateSchedule.date.getDate() }}</span>
          </label>
        </li>
      </ol>
    </section>
  </section>
</template>

<style scoped>
.date-picker {
  display: flex;
  flex-direction: column;

  .title {
    display: flex;
    height: 32px;
    padding: 0 0 0 10px;
    align-items: center;
    margin-bottom: 8px;

    button {
      --btn-padding: 8px 10px;
    }
  }

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
        gap: 8px;
        //box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);

        .year, .month {
          font-size: 1rem;
          border-radius: 8px;
          padding: 4px 8px;
        }
      }

      button {
        background-color: var(--color-base-1);
        --btn-border-color: transparent;
        box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);
      }

      button:hover{
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
        transition: background-color 0.1s ease, border-color 0.1s ease, color 0.1s ease;

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

      .occupied.available {
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
}
</style>