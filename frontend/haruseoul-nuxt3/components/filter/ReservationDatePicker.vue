<script setup>
import {ref, watch, watchEffect} from 'vue';
import {useAuthFetch} from "~/composables/useAuthFetch.js";
import {useRoute} from "#vue-router";
//props
const props = defineProps({
  hostId: {
    type: Number,
    default: null
  }
});

// emit
const emit = defineEmits(['selectionChanged'])


/*=== variables =======================================================================================================*/
const route = useRoute();
const programId = route.params.id; // route에서 programId를 가져옴
const numberOfGuest = ref(1); // todo 임시값

const userDetails = useUserDetails();
const today = new Date();
today.setHours(0, 0, 0, 0);
const rangeStart = new Date(today); // 개설가능 시작일
const rangeEnd = new Date(today); // 개설가능 말일

// 초기 연도, 월
const initialYear = new Date().getFullYear();
const initialMonth = new Date().getMonth();

// 선택된 연도, 월
const selectedYear = ref(initialYear);
const selectedMonth = ref(initialMonth);
// 선택된 날짜
const selectedDate = ref(null);

// 이전 달의 마지막 날
const prevMonthLastDate = computed(() => new Date(selectedYear.value, selectedMonth.value, 0));
// 이번 달의 마지막 날
const thisMonthLastDate = computed(() => new Date(selectedYear.value, selectedMonth.value + 1, 0));

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
const datesWithSchedules = ref([]);


/*=== function =======================================================================================================*/
const updateDatesWithSchedules = (dates, publishedPrograms) => {
  // dates로 datesWithPrograms [{date: , schedules: []}] update
  datesWithSchedules.value = dates.map(date => {
    // date와 진행일이 같은 프로그램들만 골라내서 schedules에 담기
    const schedules = publishedPrograms.filter(program => {
      // console.log('date        : ', date.toISOString(), date.toLocaleString());
      // console.log('program date: ', new Date(program.date+'T00:00:00+09:00').toISOString());
      return new Date(program.date + 'T00:00:00+09:00').getTime() === date.getTime();
    });

    // date와 schedules로 dateWithSchedules 생성 후 리턴
    return {date: date, schedules};
  });
}

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

// todo 예약가능한 날짜인지?
const isReservable = (dateSchedule) => {
  /*
    dateSchedule
    [
      {
        "date": Date객체,
        "schedules": [PublishedProgramListDto]
      }
    ]
  */
  // publishedProgramListDto의 statusName이 Wait Confirm이거나 Confirmed면 예약불가능
  dateSchedule.schedules
      .forEach(publishedProgramListDto => {
        // statusName이 Wait Confirm or Confirmed면? 예약 불가
        if (publishedProgramListDto.statusName in ["Wait Confirm", "Confirmed"])
          return false;

        // statusName이 1 or 2
        // current GroupSize, groupSizeMax, numberOfGuest
        return publishedProgramListDto.groupSizeMax >= publishedProgramListDto.groupSizeCurrent + numberOfGuest.value;
      });


};

// const resetSelectedDatesHandler = () => {
//   // 캘린터 페이지도 오늘이 표시된 곳으로 설정
//   selectedYear.value = initialYear;
//   selectedMonth.value = initialMonth;
//
//   selectedDates.value = [];
// };

// todo  Emit selection changed
const emitSelectionChanged = () => {
  emit('selectionChanged', selectedDate.value);
};

// --- 개설가능일 계산용 rangeStart, rangeEnd------------------------------------------------------------------------------
// query ?mIds=4&s=1,2,5,6&d=개설가능첫날,끝날
// today + startOffset부터 publishableRange만큼 개설가능
const reservableRange = 21;

rangeStart.setDate(today.getDate()); // today
rangeEnd.setDate(today.getDate() + reservableRange); //  21 days

/*=== 달력 dates 생성 ==================================================================================================*/
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

/*=== fetch ==========================================================================================================*/
// 캘린더에 예약불가능한 날짜 표시를 위한 개설프로그램 목록 fetch
const {data: publishedData, refresh: publishedRefresh} = await useAuthFetch(`host/published-programs`, {
  params: computed(() => publishedProgramQuery.value)
});

console.log("publishedData:", publishedData.value);
updateDatesWithSchedules(dates.value, publishedData.value.publishedPrograms);

watch(dates, async () => {
  await publishedRefresh();
  updateDatesWithSchedules(dates.value, publishedData.value.publishedPrograms);
});

// Watch the selected dates and emit changes
watch(selectedDate, emitSelectionChanged);

// watchEffect(() => console.log(selectedYear.value));
// watchEffect(() => console.log('prev month last date', prevMonthLastDate.value));
// watchEffect(() => console.log(selectedMonth.value + 1, thisMonthLastDate.value));
// watchEffect(() => console.log('dates: ', dates.value));
// watchEffect(() => console.log('selectedDates: ', selectedDates.value));
// watchEffect(() => console.log('published program query: ', publishedProgramQuery.value));

</script>

<template>
  <section class="date-picker">
    <header class="title">
      <h1 class="font-size:8">진행일 선택</h1>
    </header>
    <section class="calendar-container">
      <header class="calendar-header">
        <h1 class="d:none">May 2024</h1>
        <button @click.prevent="toPrevMonth"
                :disabled="selectedMonth <= new Date().getMonth()"
                class="to-prev-month n-btn n-btn:hover border-radius:full n-icon n-icon:arrow_left n-icon-size:3"
                type="button">이전
        </button>
        <div class="year-month-wrapper font-size:9">
          <span class="year">
            {{ selectedYear }}.
          </span>

          <span class="month">
            {{ selectedMonth + 1 }}.
          </span>
        </div>
        <button @click.prevent="toNextMonth"
                :disabled="rangeEnd.getMonth() <= selectedMonth"
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
        <!--dateSchedule = {date: Date객체, [publishedProgramListDto]}-->
        <li v-for="dateSchedule in datesWithSchedules"
            :class="{
            'month-prev': dateSchedule.date.getMonth() === (selectedMonth-1<0 ? 11 : selectedMonth-1),
            'month-next': dateSchedule.date.getMonth() === (selectedMonth+1>11 ? 0 : selectedMonth+1),
            'today': dateSchedule.date.toDateString() === new Date().toDateString(),
            'available': isReservable(dateSchedule),
            'selected': selectedDate && selectedDate.getTime() === dateSchedule.date.getTime(),
          }"
        >
          <label>
<!--            todo radio로 바꿔서 할수있나 확인해보기 -->
            <input type="radio"
                   :disabled="!isReservable(dateSchedule)"
                   v-model="selectedDate"
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
  --calendar-bg-color: #f5f5f5;
  --date-color-disabled: rgba(0, 23, 84, 0.15);

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

  .calendar-container {
    //max-width: 768px;
    //min-width: 250px;
    width: 100%;
    height: 495px;
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
        //box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);
        border-radius: 8px;
        padding: 4px 8px;
        background-color: white;

        .year, .month {
          font-size: 1.2rem;
          border-radius: 8px;
          padding: 4px 4px;
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

        height: 56px;
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

          input[type="radio"] {
            display: none;
            width: 100%; /* Make input take full width of li */
            height: 100%; /* Make input take full height of li */
            cursor: pointer; /* Change cursor to pointer on hover */
            appearance: none; /* Remove default checkbox appearance (optional) */
          }

          span {
            color: var(--date-color-disabled);
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

      .selected:hover {
        background-color: var(--color-sub-0) !important;
      }

      /* 이미 개설된 프로그램 존재하는 날짜 */

      .occupied.available {
        background-color: var(--color-base-3);
        box-shadow: none;

        > label span {
          color: var(--date-color-disabled);
        }
      }
    }
  }

  @media (min-width: 768px) {
    .calendar-container {

    }
  }
}
</style>