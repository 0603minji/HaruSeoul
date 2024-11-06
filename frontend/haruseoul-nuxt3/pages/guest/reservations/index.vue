<script setup>
import { onMounted, ref, watch } from "vue";
import axios from "axios";
import Status from "~/components/filter/Status.vue";
import { useRoute } from "vue-router";
import Pager from "~/components/Pager.vue";

const reservations = ref([]);
const selectedStatus = ref(0);

const route = useRoute();

const startNum = ref(1); // 시작 페이지
const totalRowCount = ref(0); // 총 개수
const totalPageCount = ref(0); // 총 페이지 개수
const pageNumbers = ref([]); // 5개 페이징에 담길 번호들
const hasPreviousPage = ref(false); // 이전 페이지가 있는지
const hasNextPage = ref(false); // 다음 페이지가 있는지
const currentPage = ref(1); // 현제 페이지 초기값은 1


// 예약 목록을 가져오는 함수
const fetchReservations = async (pageNum) => {
  try {
    const params = {
      s: Array.isArray(selectedStatus.value) ? selectedStatus.value.join(",") : null,
      pageNum: currentPage.value,
      // m: memberId.value
    };

    const response = await axios.get("http://localhost:8080/api/v1/guest/reservations", { params:params });

    console.log("API 응답:", response.data);

    reservations.value = response.data.reservations;
    totalRowCount.value = response.data.totalRowCount;
    totalPageCount.value = response.data.totalPageCount;
    
    hasPreviousPage.value = currentPage.value > 1;
    hasNextPage.value = currentPage.value < totalPageCount.value;
    startNum.value = pageNumbers[0];
    pageNumbers.value = Array.from({ length: 5 }, (_, startNum) => startNum + 1);
    
    // startNum.value = response.data.startNum;

    // 각 예약의 날짜 차이를 계산하여 dDay 속성을 추가합니다.
    const currentDate = new Date();
    reservations.value = reservations.value.map(r => {
      // 예약 날짜가 유효한 경우에만 dDay 계산
      if (r.date) {
        const reservationDate = new Date(r.date);
        r.dDay = Math.floor((reservationDate - currentDate) / (1000 * 60 * 60 * 24)); // dDay 계산
        // 1초 x 60(분) x 60(시) x 24(일)
      } else {
        r.dDay = null; // 날짜가 없을 경우 null 처리
      }
      return r; // 수정된 예약 객체 반환
    });

  } catch (error) {
    console.error("예약 목록을 가져오는 중 오류 발생:", error);
  }
};

// 상태가 변경될 때 호출되는 함수
const StatusChangeHandler = async (status) => {
  selectedStatus.value = status;
  currentPage.value = 1; // 상태 변경 시 페이지 초기화
  fetchReservations();
};

// 페이지 클릭 핸들러
const pageClickHandler = async (newPage) => {
  if (newPage < 1 || newPage > totalPageCount.value) {
    alert("마지막 페이지 입니다.");
    return;
  }

  currentPage.value = route.query.p;
} 

// 페이지가 변경될 때 쿼리 문자열을 업데이트
watch(
  () => route.query.p,
  (newPage) => {
    currentPage.value = parseInt(newPage) || 1; // 현재 페이지를 선택한 페이지로 업데이트
    fetchReservations(); // 새로운 페이지에 따라 예약 목록 갱신
  }
);

// 초기 예약 데이터 로드
onMounted(() => {
  currentPage.value = route.query.p || 1;
  fetchReservations();
});
</script>




<template>
  <main>
    <section class="bg-color:base-1 n-layout-mj">
      <header class="n-title">
        <h1>예약 내역 ({{ totalRowCount }})</h1>
      </header>

      <Status @selectStatusIds="StatusChangeHandler" />

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

              <span v-else-if="r.statusName === 'Canceled'" class="n-panel-tag not-submitted"
              style="border-color: #DB4455; color: #DB4455;">
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

                  <span v-else-if="r.statusName === 'Canceled'" class="n-panel-tag not-submitted" 
                  style="border-color: #DB4455; color: #DB4455;">
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
                    <span v-if="r.dDay > 1">

                      {{ r.date }}
                      <span v-if="['On Going', 'Urgent', 'Wait Confirm', 'Confirmed'].includes(r.statusName) && (r.dDay <= 7)" style="color: #DB4455;">
                        (D-{{ r.dDay }})
                      </span>

                      <span v-else-if="['On Going', 'Urgent', 'Wait Confirm', 'Confirmed'].includes(r.statusName) && (r.dDay > 7)">
                        (D-{{ r.dDay }})
                      </span>
                      
                    </span>
                    
                  </div>
                  <div class="card-info">
                    <span class="n-icon n-icon:group n-deco">예약인원</span>
                    <span>{{ r.groupSize }}</span>
                  </div>
                </div>

                <div v-if="['On Going', 'Urgent', 'Wait Confirm', 'Confirmed'].includes(r.statusName)"
                  class="card-footer-responsive">
                  <a href="#" class="n-btn bg-color:main-1 color:base-1">호스트 문의</a>
                  <a href="#" class="n-btn" style="color: #DB4455; --btn-border-color:#DB4455;">예약 취소</a>
                  <a href="#"
                    class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
                </div>

                <div v-else-if="r.statusName === 'Finished'" class="card-footer-responsive">
                  <a href="#" class="n-btn bg-color:main-1 color:base-1">호스트 문의</a>
                  <a href="#" class="n-btn">리뷰 작성</a>
                  <a href="#"
                    class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
                </div>

                <div v-else-if="r.statusName === 'Canceled'" class="card-footer-responsive">
                  <a href="#" class="n-btn bg-color:main-1 color:base-1">호스트 문의</a>
                  <a href="#"
                    class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
                </div>

              </div>
            </div>
          </div>

          <div v-if="['On Going', 'Urgent', 'Wait Confirm', 'Confirmed'].includes(r.statusName)"
            class="card-footer margin-top:2">
            <a href="#" class="n-btn bg-color:main-1 color:base-1">호스트 문의</a>
            <a href="#" class="n-btn" style="color: #DB4455; --btn-border-color:#DB4455;">예약 취소</a>
            <a href="#" class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
          </div>

          <div v-else-if="r.statusName === 'Finished'" class="card-footer margin-top:2">
            <a href="#" class="n-btn bg-color:main-1 color:base-1">호스트 문의</a>
            <a href="#" class="n-btn">리뷰 작성</a>
            <a href="#" class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
          </div>

          <div v-else-if="r.statusName === 'Canceled'" class="card-footer margin-top:2" style="justify-content: space-between;">
            <a href="#" class="n-btn bg-color:main-1 color:base-1" style="max-width: 278px;">호스트 문의</a>
            <a href="#" class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
          </div>

        </div>
      </div>

      <!-- Pager 부분 -->
      <Pager class="mb:4" :page-numbers="pageNumbers" :start-number="startNum" :total-page-count="totalPageCount"
      :href="`reservations`" @pageChange="pageClickHandler" />

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

  .card-footer {
    /* max-width: 432px; */

    >.n-btn {
      z-index: 2;
    }
  }

  .card-main {
    .img-wrapper {
      width: 94px;
      min-width: 94px;
    }

    .card-info {
      width: max-content;
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


@media (min-width: 768px) {
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
      --btn-bg-hover: var(--color-base-2);
      --btn-bg-color: transparent;
      --btn-padding: 13px 26px;
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