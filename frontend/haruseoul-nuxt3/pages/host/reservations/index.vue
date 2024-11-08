<script setup>

import {watchEffect} from "vue";
import {useRoute} from 'vue-router';

const hostId = 4; // 프론트에서 저장하고 있는 인증정보에 접근해서 얻어와야함
const page = ref(1);

// PublishedProgramResponseDto
const pages = ref([1, 2, 3, 4, 5]);
const totalCount = ref();
let totalPages;
let currentPageRowCount;
let hasNextPage;
let hasPreviousPage;
const publishedPrograms = ref([]);

let query = {
  p: useRoute().query.p,
};

/*// Remove empty parameters (optional)
Object.keys(newQuery).forEach(key => {
  if (!newQuery[key]) delete newQuery[key];
});*/

// router.push({ query: newQuery });

/*// query에 쓸
const dates = ref([]); // 검색할 기간의 시작일, 말일. PublishedProgramFilter에서 emit으로 받아올 것
const statuses = ref([]); // 검색할 상태. 1~6. PublishedProgramFilter에서 emit으로 받아올 것
const pIds = ref([]); // 검색할 프로그램 id들. PublishedProgramFilter에서 emit으로 받아올 것*/
/*const query = computed(() => {
      const result = {mIds: hosId};

      // Add `s` key only if `statuses` is not empty
      if (statuses.value.length) {
        result.s = statuses.value.join(",");
      }

      // Add `d` key only if `dates` is not empty
      if (dates.value.length) {
        result.d = dates.value
            .map((date) =>
                new Intl.DateTimeFormat("ko-KR", {
                  year: "numeric",
                  month: "2-digit",
                  day: "2-digit",
                  timeZone: "Asia/Seoul",
                })
                    .format(date)
                    .replace(/\. /g, "-")
                    .replace(".", "")
            )
            .join(",");
      }

      // Add `pIds` key only if `pIds` is not empty
      if (pIds.value.length) {
        result.pIds = pIds.value.join(",");
      }
      return result;
    }
);
console.log('query initialized. ', query.value);*/
/*// query가 변하면 새로 fetch (refresh) -> publishedPrograms
watch(() => query.value,
    (newQuery) => {
      console.log('query.value watch진입: refresh()')
      refresh();
    }
);*/
/*=== fetch ==========================================================================================================*/
const config = useRuntimeConfig();
// 최초에 예정된 일정만 fetch
const {data, refresh} = useFetch(`host/published-programs?mIds=${hostId}`, {
  baseURL: config.public.apiBase,
  query: query
})

watchEffect(() => {
      console.log('publishedData watchEffect진입')
      if (!data.value) {
        console.log('publishedData watchEffect 종료')
        return;
      }
      console.log('   ->  new publishedData: ', data.value);

      // console.log(useRoute().query)
      // 예정된 일정
      if (useRoute().query.tab === "finished")
        publishedPrograms.value = data.value.publishedPrograms.filter(p => p.statusName === "Finished");
      if (!useRoute().query.hasOwnProperty('tab'))
        publishedPrograms.value = data.value.publishedPrograms.filter(p => p.statusName != "Finished" && p.statusName != "Canceled");
      if (useRoute().query.tab === "canceled")
        publishedPrograms.value = data.value.publishedPrograms.filter(p => p.statusName === "Canceled");



      console.log('   ->  publishedPrograms: ', publishedPrograms.value);

      pages.value = data.value.pages;
      totalCount.value = data.value.totalCount;
      totalPages = data.value.totalPages;
      currentPageRowCount = data.value.currentPageRowCount;
      hasNextPage = data.value.hasNextPage;
      hasPreviousPage = data.value.hasPreviousPage;
      console.log('   ->  pages: ', pages.value);
      console.log('   ->  totalCount: ', totalCount.value);
      console.log('   ->  totalPages: ', totalPages);
      console.log('   ->  currentPageRowCount: ', currentPageRowCount);
      console.log('   ->  hasNextPage: ', hasNextPage);
      console.log('   ->  hasPreviousPage: ', hasPreviousPage);
      console.log('publishedData watchEffect 종료')
    }
)

// watch(() => data.value ,(newData) => {
//       console.log('publishedData watchEffect진입')
//       if (!newData) {
//         console.log('publishedData watchEffect 종료')
//         return;
//       }
//       console.log('   ->  new publishedData: ', newData);
//
//       // console.log(useRoute().query)
//       // 예정된 일정
//       if (useRoute().query.tab === "finished")
//         publishedPrograms.value = newData.publishedPrograms.filter(p => p.statusName === "Finished");
//       if (!useRoute().query.hasOwnProperty('tab'))
//         publishedPrograms.value = newData.publishedPrograms.filter(p => p.statusName != "Finished" && p.statusName != "Canceled");
//       if (useRoute().query.tab === "canceled")
//         publishedPrograms.value = newData.publishedPrograms.filter(p => p.statusName === "Canceled");
//
//
//
//       console.log('   ->  publishedPrograms: ', publishedPrograms.value);
//
//       pages.value = newData.pages;
//       totalCount.value = newData.totalCount;
//       totalPages = newData.totalPages;
//       currentPageRowCount = newData.currentPageRowCount;
//       hasNextPage = newData.hasNextPage;
//       hasPreviousPage = newData.hasPreviousPage;
//       console.log('   ->  pages: ', pages.value);
//       console.log('   ->  totalCount: ', totalCount.value);
//       console.log('   ->  totalPages: ', totalPages);
//       console.log('   ->  currentPageRowCount: ', currentPageRowCount);
//       console.log('   ->  hasNextPage: ', hasNextPage);
//       console.log('   ->  hasPreviousPage: ', hasPreviousPage);
//       console.log('publishedData watchEffect 종료')
//     }
// )
// ;


//=== function =========================================================================================================
// 2024-11-26 -> 24.11.26 Tue
const formatDate = (dateString) => {
  const date = new Date(dateString);

  // Extract date components
  const year = date.getFullYear().toString().slice(2); // Get last two digits of year
  const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-based
  const day = String(date.getDate()).padStart(2, '0');

  // Get weekday in short form (e.g., "Tue")
  const weekday = date.toLocaleString('en-US', {weekday: 'short'});

  return `${year}.${month}.${day} ${weekday}`;
};

// 한국날짜 D-day("2024-11-26") 입력하면 현재 한국시간 기준으로 d-day계산
const calculateKoreanDDay = (enteredDate) => {
  const koreaTimeOffset = 9 * 60; // UTC+9 in minutes

  // Create the entered date (midnight of the entered date in Korean Time)
  const targetDateKST = new Date(enteredDate);
  targetDateKST.setHours(0, 0, 0, 0); // Set to midnight of the entered date in local time

  // Adjust the entered date to Korean Time (add UTC+9)
  const targetDateInKST = new Date(targetDateKST.getTime() - koreaTimeOffset * 60000);

  // Calculate the time difference in milliseconds
  const timeDifference = targetDateInKST.getTime() - Date.now();

  // Convert the time difference to days
  const daysDifference = Math.ceil(timeDifference / (1000 * 3600 * 24));
  /*
    daysDiff = 0 : D-day
    daysDiff > 0 : D-daysDiff
    daysDiff < 0 : D+daysDiff (화면에 출력 안할 것)
  */
  return daysDifference;
}

</script>

<template>
  <main>

    <section class="layout-body"> <!-- main 내 모든 -->
      <!--=== heading ==========================================-->
      <!--    예약 관리           +일정추가-->
      <header class="n-title">
        <h1 class="">예약관리</h1>
        <div>
          <a href="" class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:plus n-deco">일정 추가</a>
        </div>
      </header>

      <!--=== host/reservation/list 예약관리 상단바==========================================-->
      <nav class="n-bar-underline">
        <h1 class="d:none">네비탭</h1>
        <ul class="item-wrapper">
          <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"
              :class="{ active: useRoute().query.tab == null }">
            <RouterLink to="./reservations">예정된 일정</RouterLink>
          </li>
          <li class=" n-btn:hover n-btn n-btn-border:none n-btn-radius:0"
              :class="{ active: useRoute().query.tab === 'finished' }">
            <RouterLink to="./reservations?tab=finished">지난
              일정
            </RouterLink>
          </li>
          <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"
              :class="{ active: useRoute().query.tab === 'canceled' }">
            <RouterLink to="./reservations?tab=canceled">취소된 일정</RouterLink>
          </li>
        </ul>
      </nav>

      <div class="layout-main">
        <div class="layout-main-list">
          <!--=== 필터 .n-filter ==========================================-->
          <!--모집 중, 예약 확정, 폐지 임박, 종료, 폐지, 필터-->
          <section class="n-filter md:d:none bg-color:base-1">
            <h1 class="d:none">필터</h1>

            <div class="overflow-x:auto">
              <ul class="item-wrapper">
                <li><a href=""
                       class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:calendar n-icon-size:1 n-deco n-deco-gap:1">기간</a>
                </li>
                <li><a href=""
                       class="active active:border n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:pending n-icon-size:1 n-deco n-deco-gap:1">프로그램
                  상태</a></li>
                <li><a href=""
                       class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:search n-icon-size:1 n-deco n-deco-gap:1">프로그램</a>
                </li>
              </ul>
            </div>

            <div class="reset-box">
              <div class="gradation"></div>
              <a href="" class="icon-box n-deco1 n-icon n-icon:reset">
                초기화
              </a>
            </div>
          </section>

          <!--=== 목록 ==========================================-->
          <section class="rv-list">
            <!--=== 정렬 .list-header ==========================================-->
            <header class="list-header bg-color:base-1">
              <h1 class="d:none">예약카드 목록</h1>
              <div>
                <span class="list-item-count">{{ totalCount }} Result</span>
                <!--            <button type="button" class="n-icon n-icon:arrow_swap n-deco n-deco-gap:1">-->
                <!--                정렬-->
                <!--            </button>-->
                <a href="?view=calendar"
                   class="option list-view-toggle n-icon n-icon:calendar_month n-icon-color:base-6 n-deco n-deco-gap:1 margin-left:auto">
                  <span class="md:d:inline">캘린더로 보기</span>
                </a>

                <span class="separator"></span>

                <a href=""
                   class="option sort n-icon n-icon:arrow_swap n-icon-color:base-6 n-deco n-deco-gap:1">
                  정렬
                </a>
              </div>
            </header>

            <!--예약 카드 목록. (모집 중) (예약 확정) (폐지 임박)-->
            <ul v-if="publishedPrograms.length" class="n-card-container bg-color:base-1">

              <li v-for="pp in publishedPrograms" :key="pp.id" class="n-card n-card:hover padding:6">
                <a class="n-link-box" href="detail?id=1"></a>
                <h2 class="d:none">예약 카드</h2>

                <div class="card-header">
                  <div class="left">
                    <span class="n-panel-tag" :class="{'on-going': pp.statusName === 'On Going',
                      'urgent': pp.statusName === 'Urgent',
                      'confirmed': pp.statusName === 'Confirmed',
                      'wait-confirm': pp.statusName === 'Wait Confirm',
                      'finished': pp.statusName === 'Finished'}
                    ">
                      {{ pp.statusName }}
                    </span>
                  </div>
                  <div class="right">
                    <a href=""
                       class="n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9">더보기</a>
                  </div>
                </div>

                <div class="card-main">
                  <div class="img-wrapper">
                    <img src="/public/image/program_01.png" alt="대표사진">
                  </div>

                  <div class="card-info-wrapper">
                    <p class="title">{{ pp.programTitle }}</p>
                    <div class="card-info">
                      <span class="n-icon n-icon:calendar n-deco">{{ formatDate(pp.date) }}</span>
                      <span v-if="calculateKoreanDDay(pp.date) === 0" class="n-panel-tag d-day">D-day</span>
                      <span v-if="0 < calculateKoreanDDay(pp.date) && calculateKoreanDDay(pp.date) <= 3"
                            class="n-panel-tag urgent">D-{{ calculateKoreanDDay(pp.date) }}</span>
                      <span v-if="3 < calculateKoreanDDay(pp.date)"
                            class="n-panel-tag">D-{{ calculateKoreanDDay(pp.date) }}</span>
                    </div>
                  </div>

                  <!-- md:footer: card-footer영역에 존재하다가 992px이상에서 card-main의 우측으로 이동 -->
                  <div class="applicant-status lg:show">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span
                        class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">{{
                        pp.groupSizeCurrent
                      }} / {{ pp.groupSizeMax }}</span>
                  </div>
                </div>

                <div class="card-footer">
                  <div class="applicant-status margin-left:auto">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span
                        class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">{{
                        pp.groupSizeCurrent
                      }} / {{ pp.groupSizeMax }}</span>
                  </div>
                </div>
              </li>
            </ul>

          </section>
        </div>

        <aside class="layout-main-aside">

        </aside>
      </div>
    </section>

  </main>
</template>

<style scoped>
.layout-body {
  .n-bar-underline {
    .item-wrapper {
      justify-content: space-between;
    }
  }

  .layout-main {
    display: flex;

    .layout-main-list {
      flex-grow: 1;
      width: 100%;

      .n-filter {
        .n-btn {
          --btn-font-size: 12px;
        }
      }

      /*===============================================================================================*/

      .list-header {
        padding: 8px 20px;

        div {
          .list-view-toggle {
            > span {
              display: none;
            }
          }

          .separator {
            display: flex;
            width: 1px;
            height: 16px;
            background-color: var(--color-base-6);
            margin-left: 4px;
            margin-right: 4px;
          }
        }
      }

      /*===============================================================================================*/

      .n-card-container {
        padding: 8px 16px;

        .n-card {
          position: relative;

          .card-header {
            .left {
              .on-going {
                --tag-border-color: var(--color-sub-1);
                --tag-bg-color: var(--color-sub-1);
                color: var(--color-base-1);
              }

              .urgent {
                --tag-border-color: var(--color-red-1);
                --tag-bg-color: var(--color-red-1);
                color: var(--color-base-1);
              }

              .finished {
                --tag-border-color: var(--color-base-8);
                --tag-bg-color: var(--color-base-8);
                color: var(--color-base-1);
              }

              .wait-confirm {
                --tag-border-color: var(--color-green-1);
                color: var(--color-green-1);
              }

              .confirmed {
                --tag-border-color: var(--color-green-1);
                --tag-bg-color: var(--color-green-1);
                color: var(--color-base-1);
              }
            }

            .right > a {
              position: relative;
              /* a태그는 기본 position이 static. static이면 z-index가 안 먹힘*/
              z-index: 2;
            }
          }

          .card-main {
            .card-info {
              .d-day {
                --tag-border-color: var(--color-red-1);
                --tag-bg-color: var(--color-red-1);
                color: var(--color-base-1);
              }
            }
          }

          .n-link-box {
            display: block;
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            z-index: 1;
          }
        }
      }
    }

    .layout-main-aside {
      display: none;
      flex-shrink: 0;
      width: 250px;
      margin: 0 16px;
      background-color: var(--color-base-3);
    }
  }
}

@media (min-width: 400px) {
  .layout-body {
    .n-bar-underline {
      .item-wrapper {
        --bar-gap: 24px;
        justify-content: flex-start;
      }
    }
  }
}

@media (min-width: 768px) {
  .layout-body {
    margin-left: auto;
    margin-right: auto;
    /*min-width: 992px;*/
    width: 100%;
    max-width: 1092px;

    .n-title {
      > div {
        margin-right: calc(16px + 250px + 16px - 20px);
      }
    }

    .layout-main {
      .layout-main-list {
      }

      .layout-main-aside {
        display: flex;
      }
    }
  }

  .md\:d\:inline {
    display: inline;
  }

  .md\:d\:none {
    display: none;
  }
}
</style>