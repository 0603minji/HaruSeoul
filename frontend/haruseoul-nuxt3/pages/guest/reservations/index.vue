<script setup>
import { onMounted, ref, watch } from "vue";
import axios from "axios";
import Status from "~/components/filter/Status.vue";
import { useRoute, useRouter } from "vue-router";
import Pager from "~/components/Pager.vue";

let reservations = ref([]);
let selectedStatus = ref(0);

const route = useRoute();
let startNum = ref(1);
let pageNumbers = ref([]);
let totalRowCount = ref(0);
let totalPageCount = ref(0);
let hasPreviousPage = ref(false);
let hasNextPage = ref(false);

// 쿼리 파라미터에서 현재 페이지 가져오기
let currentPage = ref(parseInt(route.query.p) || 1);
// let currentPage = ref(parseInt(Array.isArray(route.query.p) ? route.query.p[0] : route.query.p) || 1);


// 쿼리 스트링을 생성하는 함수
const buildQueryString = () => {
  return { s: selectedStatus.value || '', p: currentPage.value };
};

// 예약 목록을 가져오는 함수
const fetchReservations = async () => {
  try {
    const params = {
      s: Array.isArray(selectedStatus.value) ? selectedStatus.value.join(",") : null,
      p: currentPage.value
    };

    const response = await axios.get("http://localhost:8080/api/v1/guest/reservations", { params });

    console.log("API 응답:", response.data);

    reservations.value = response.data.reservations;
    totalRowCount.value = response.data.totalRowCount;
    totalPageCount.value = response.data.totalPageCount;

    hasPreviousPage.value = currentPage.value > 1;
    hasNextPage.value = currentPage.value < totalPageCount.value;

    pageNumbers.value = Array.from({ length: totalPageCount.value }, (_, i) => i + 1);

  } catch (error) {
    console.error("예약 목록을 가져오는 중 오류 발생:", error);
  }
};

// 상태가 변경될 때 호출되는 함수
const handleStatusChange = (status) => {
  selectedStatus.value = status;
  currentPage.value = 1; // 상태 변경 시 페이지 초기화
  fetchReservations();
};

// 페이지 클릭 핸들러
const pageClickHandler = (page) => {
  if (page < 1) {
    alert("이전 페이지가 없습니다.")
    return;
  }

  if (page > totalPages) {
    alert("다음 페이지가 없습니다.")
    return;
  }

  if (page == null)
    delete query.page;

  query.page = page;

  refresh();
}

// 페이지가 변경될 때 쿼리 문자열을 업데이트
watch(
  () => route.query.p,
  // (newValue) => {
  //   const newPage = parseInt(newValue);
  //   if (!isNaN(newPage) && newPage !== currentPage.value) {
  //     currentPage.value = newPage;
  //     fetchReservations(); // 새로운 페이지에 맞는 예약 목록을 가져옵니다.
  //   }
  // }
);

// 초기 예약 데이터 로드
onMounted(() => {
  fetchReservations();
});
</script>

<template>
  <main>
    <section class="bg-color:base-1 n-layout-mj">
      <header class="n-title">
        <h1>예약 내역 ({{ totalRowCount }})</h1>
      </header>

      <Status @selectStatusIds="handleStatusChange" />

      <div class="n-card-container bg-color:base-1">
        <div class="n-card bg-color:base-1 padding:6" v-for="r in reservations" :key="r.id">
          <RouterLink :to="`/guest/reservations/${r.id}`" class="n-link-box" href="detail?id=1"></RouterLink>
          <div class="card-header">
            <div class="left">
              <span v-if="['On Going', 'Urgent', 'Wait Confirm'].includes(r.statusName)" class="n-panel-tag">
                결제완료
              </span>

              <span v-else-if="r.statusName === 'Finished'" class="n-panel-tag not-submitted">
                이용완료
              </span>

              <span v-else-if="r.statusName === 'Confirmed'" class="n-panel-tag not-submitted">
                예약확정
              </span>

              <span v-else-if="r.statusName === 'Canceled'" class="n-panel-tag not-submitted">
                취소됨
              </span>
            </div>
          </div>

          <div class="card-main">
            <div class="img-wrapper">
              <img src="/public/image/program_01.png" alt="대표사진" />
            </div>

            <div class="card-info-wrapper">
              <div class="card-header-responsive">
                <div class="left">
                  <span v-if="['On Going', 'Urgent', 'Wait Confirm'].includes(r.statusName)" class="n-panel-tag">
                    결제완료
                  </span>

                  <span v-else-if="r.statusName === 'Finished'" class="n-panel-tag not-submitted">
                    이용완료
                  </span>

                  <span v-else-if="r.statusName === 'Confirmed'" class="n-panel-tag not-submitted">
                    예약확정
                  </span>

                  <span v-else-if="r.statusName === 'Canceled'" class="n-panel-tag not-submitted">
                    취소됨
                  </span>
                </div>
              </div>
              <p class="title">
                {{ r.programTitle }}
              </p>
              <div class="card-info-responsive">
                <div class="d:flex flex-direction:column">
                  <div class="card-info">
                    <span class="n-icon n-icon:calendar n-deco">진행일</span>
                    <span>{{ r.date }}</span>
                  </div>
                  <div class="card-info">
                    <span class="n-icon n-icon:group n-deco">예약인원</span>
                    <span>{{ r.groupSize }}</span>
                  </div>
                </div>
                <div class="card-footer-responsive">
                  <a href="aa" class="n-btn bg-color:main-1 color:base-1">Host 문의</a>
                  <a href="aa" class="n-btn">예약 취소</a>
                  <a href="aa"
                    class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
                </div>
              </div>
            </div>
          </div>
          <div class="card-footer margin-top:2">
            <a href="aa" class="n-btn bg-color:main-1 color:base-1">Host 문의</a>
            <a href="aa" class="n-btn">예약 취소</a>
            <a href="aa" class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
          </div>
        </div>
        <!-- <li v-for="r in reservations" :key="r.id" class="n-card bg-color:base-1 padding:6">
          <RouterLink :to="`/guest/reservations/${r.id}`" class="n-link-box"></RouterLink>
          <h1 class="d:none">예약 카드</h1>

          <div class="card-header">
            <span :class="['n-panel-tag', r.status === 'completed' ? 'completed' : 'canceled']">
              {{ r.statusName }}
            </span>
          </div>

          <div class="card-main">
            <div class="img-wrapper">
              <img src="/image/program_02.png" alt="대표사진"/>
            </div>
            <div class="card-info-wrapper">
              <div class="card-header-responsive">
                <div class="left">
                  <span v-if="['On Going', 'Urgent', 'Wait Confirm'].includes(r.statusName)"
                        class="n-panel-tag not-submitted">결제완료</span>
                  <span
                      v-else-if="r.statusName === 'Finished'"
                      class="n-panel-tag not-submitted"
                  >이용완료</span
                  >
                  <span
                      v-else-if="r.statusName === 'Confirmed'"
                      class="n-panel-tag not-submitted"
                  >예약확정</span
                  >
                  <span
                      v-else-if="r.statusName === 'Canceled'"
                      class="n-panel-tag not-submitted"
                  >취소됨</span
                  >
                </div>
              </div>
              <p class="title">{{ r.programTitle }}</p>
              <div class="card-info-responsive">
                <div class="card-info">
                  <span class="n-icon n-icon:calendar n-deco">진행일</span>
                  <span>{{ r.date }}</span>
                </div>
                <div class="card-info">
                  <span class="n-icon n-icon:group n-deco">예약인원</span>
                  <span>{{ r.groupSize }}</span>
                </div>
                <div class="card-footer-responsive">
                  <RouterLink to="#" class="n-btn bg-color:main-1 color:base-1">Host 문의</RouterLink>
                  <RouterLink to="#" class="n-btn">리뷰 작성</RouterLink>
                  <RouterLink to="#" class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">
                    공유하기
                  </RouterLink>
                </div>
              </div>
            </div>
          </div>

          <div class="card-footer margin-top:2">
            <RouterLink to="#" class="n-btn bg-color:main-1 color:base-1">Host 문의</RouterLink>
            <RouterLink to="#" class="n-btn">리뷰 작성</RouterLink>
            <RouterLink to="#" class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기
            </RouterLink>
          </div>
        </li>-->
      </div>
      <!-- Pager 부분 -->
      <Pager class="mb:4" :page-numbers="pageNumbers" :start-number="startNum" :total-page-count="totalPageCount"
        :href="`reservations`" @page-change="pageClickHandler" />
    </section>
  </main>
</template>

<style scoped>
/* 예약 중, 이용완료, 취소 */
.n-panel-tag {
  --tag-border-radius: 14px;
  --tag-padding: 6px 8px;
  font-weight: 600;
  /* semi-bold */
}

.n-panel-tag.completed {
  --tag-border-color: var(--color-base-8);
  --tag-bg-color: var(--color-base-8);
  color: var(--color-base-1);
}

.n-panel-tag.canceled {
  --tag-border-color: var(--color-base-3);
  --tag-bg-color: var(--color-base-3);
  color: var(--color-base-8);
}

/*======================================================*/

.n-card {
  position: relative;

  .n-link-box {
    display: block;
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
  }

  .card-footer>.n-btn {
    z-index: 2;
  }

  .card-main {
    .img-wrapper {
      width: 94px;
      min-width: 94px;
    }
  }
}

/*=========================================================*/
/*반응형 구현*/
.n-layout-mj {
  max-width: 1092px;
  margin: 0 auto;
}

@media (max-width: 768px) {
  .n-card-container {
    max-width: 100%;
    padding: 0 20px 16px 20px;

    .card-header-responsive {
      display: none;
    }

  }

  .card-footer-responsive {
    display: none;
  }
}


@media (min-width: 768px) and (max-width: 1092px) {
  .n-card-container {
    max-width: 100%;
    padding: 0 20px 16px 20px;

    .card-footer {
      display: none;
    }

    .card-header-responsive {
      display: block;
      padding: 10px 0;

      .n-panel-tag {
        --tag-padding: 3px 6px;
      }
    }

    .card-header {
      display: none;
    }

    .card-main {
      .img-wrapper {
        width: 110px;
        min-width: 110px;
      }
    }
  }


  .card-footer-responsive {
    width: 400px;
    display: flex;
    align-items: center;
    justify-content: end;
    gap: 20px;

    .n-btn {
      flex-grow: 0;
      flex-shrink: 1;

      --btn-font-size: 14px;
      --btn-border-color: var(--color-main-1);
      --btn-border-radius: 12px;
      /* --btn-bg-hover: var(--color-base-2); */
      --btn-bg-color: transparent;
      --btn-padding: 12px 16px;
      align-items: center;
      background-color: var(--btn-bg-color);
      border: 1px solid var(--btn-border-color, var(--color-base-3));
      border-radius: var(--btn-border-radius, 8px);
      box-sizing: content-box;
      color: inherit;
      cursor: pointer;
      display: inline-flex;
      font-family: inherit;
      font-size: var(--btn-font-size, 14px);
      font-weight: var(--font-weight-2);
      justify-content: center;
      line-height: 1em;
      padding: var(--btn-padding);
      position: relative;
      transition: border-color .3s, background-color .3s;
      user-select: none;
      white-space: nowrap;
      width: auto;

      z-index: 2;
    }
  }

  .card-info-responsive {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}


@media (min-width: 1092px) {
  .n-card-container {
    max-width: 1092px;
    padding: 0 20px 16px 20px;

    .card-footer {
      display: none;
    }

    .card-header-responsive {
      display: block;
      padding: 10px 0;
    }

    .card-header {
      display: none;
    }

    .card-main {
      .img-wrapper {
        width: 110px;
        min-width: 110px;
      }
    }
  }


  .card-footer-responsive {
    width: 400px;
    display: flex;
    align-items: center;
    justify-content: end;
    gap: 20px;

    .n-btn {
      flex-grow: 0;
      flex-shrink: 1;

      --btn-font-size: 14px;
      --btn-border-color: var(--color-main-1);
      --btn-border-radius: 12px;
      /* --btn-bg-hover: var(--color-base-2); */
      --btn-bg-color: transparent;
      --btn-padding: 12px 16px;
      align-items: center;
      background-color: var(--btn-bg-color);
      border: 1px solid var(--btn-border-color, var(--color-base-3));
      border-radius: var(--btn-border-radius, 8px);
      box-sizing: content-box;
      color: inherit;
      cursor: pointer;
      display: inline-flex;
      font-family: inherit;
      font-size: var(--btn-font-size, 14px);
      font-weight: var(--font-weight-2);
      justify-content: center;
      line-height: 1em;
      padding: var(--btn-padding);
      position: relative;
      transition: border-color .3s, background-color .3s;
      user-select: none;
      white-space: nowrap;
      width: auto;

      z-index: 2;
    }
  }

  .card-info-responsive {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>