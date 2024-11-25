<script setup>
import {onMounted, ref, watch} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useReservationFetch} from "~/composables/useReservationFetch.js";
import Pager from "~/components/Pager.vue";
import CancelReservationModal from "~/components/modal/CancelReservationModal.vue";
import useShare from '~/composables/useShare';

const reservations = ref([]);

const route = useRoute();
const router = useRouter(); // router 인스턴스 가져오기
const userDetails = useUserDetails();

const startNum = ref(1); // 시작 페이지
const totalRowCount = ref(0); // 총 개수
const totalPageCount = ref(0); // 총 페이지 개수
const pageNumbers = ref([]); // 5개 페이징에 담길 번호들
const hasPreviousPage = ref(false); // 이전 페이지가 있는지
const hasNextPage = ref(false); // 다음 페이지가 있는지
const currentPage = ref(1); // 현제 페이지 초기값은 1
const guestId = userDetails.id.value;
const reservationStatus = ref(0);

const getPublishedProgramIdByReservationId = (reservationId) => {
  const reservation = reservations.value.find(r => r.id === reservationId);
  return reservation ? reservation.publishedProgramId : null;
};

const getProgramIdByReservationId = (reservationId) => {
  const reservation = reservations.value.find(r => r.id === reservationId);
  return reservation ? reservation.programId : null;
};

// 카카오톡 공유하기 ------------------------------------------------
const {shareToKakao} = useShare();  // useShare 모듈에서 shareToKakao 함수 가져오기

// 버튼 클릭 시 공유 함수 호출
const handleShare = (url) => {
  shareToKakao(url);  // 여기서 원하는 URL을 넣어 공유
};


// 예약 목록 -------------------------------------------------------

// 상태 필터링을 위한 선택된 상태 ID
const selectedStatusId = ref([1, 2, 3, 4]); // 초기에는 모든 상태 표시

// 상태 필터 함수
const filterByStatus = (statusIds) => {
  selectedStatusId.value = statusIds;  // 선택된 상태 값 저장
  router.replace({query: {...route.query, p: 1}});
  fetchReservations();
};

// 예약 목록 불러오기
const fetchReservations = async () => {
      try {
        const token = localStorage.getItem("token");

        const response = await useReservationFetch("guest/reservations",
            {
              headers: {Authorization: `Bearer ${token}`},
              params: {
                s: selectedStatusId.value,
                pageNum: currentPage.value || 1,
                m: guestId,
              }
            }
        );

        console.log("리스폰",response)

        reservations.value = response.reservations;
        totalRowCount.value = response.totalRowCount;
        totalPageCount.value = response.totalPageCount;

        hasPreviousPage.value = currentPage.value > 1;
        hasNextPage.value = currentPage.value < totalPageCount.value;

        // 5개씩 페이지 번호를 나누기
        startNum.value = Math.floor((currentPage.value - 1) / 5) * 5 + 1;
        pageNumbers.value = Array.from({length: Math.min(5, totalPageCount.value - startNum.value + 1)}, (_, index) => startNum.value + index);

        // 각 예약의 날짜 차이를 계산하여 dDay 속성을 추가합니다.
        const currentDate = new Date();

        reservations.value = reservations.value.map(r => {
          if (r.date) {
            const reservationDate = new Date(r.date + `T00:00:00+09:00`);
            r.dDay = Math.ceil((reservationDate - currentDate) / (1000 * 60 * 60 * 24));
          } else {
            r.dDay = null;
          }
          return r;
        });

      } catch
          (error) {
        if (error.response) {
          console.error("예약 목록을 가져오는 중 오류 발생:", error.response.data);
        } else {
          console.error("예약 목록을 가져오는 중 알 수 없는 오류 발생:", error.message);
        }
      }
    }
;

const formatDeleteDate = (deleteDate) => {
  const date = new Date(deleteDate);  // UTC 시간을 로컬 시간으로 변환

  // 한국 시간으로 변환 (UTC+9)
  const koreaTimeOffset = 15 * 60; // 9 hours in minutes
  const localDate = new Date(date.getTime() + koreaTimeOffset * 60 * 1000);

  const year = localDate.getFullYear();
  const month = localDate.getMonth();
  const formattedMonth = String(month + 1).padStart(2, '0');  // 월은 0부터 시작하므로 +1
  const day = localDate.getDate();  // getDay()는 요일을 반환하므로 getDate()로 날짜를 가져옴
  const formattedDay = String(day).padStart(2, '0');
  const hours = localDate.getHours();
  const minutes = localDate.getMinutes();

  return `${year}-${formattedMonth}-${formattedDay} ${hours}:${minutes < 10 ? '0' + minutes : minutes}`;
};


// 페이지 클릭 핸들러
const pageClickHandler = (newPage) => {
  if (newPage < 1 || newPage > totalPageCount.value) {
    alert("마지막 페이지 입니다.");
    return;
  }
}

// 예약취소--------------------------------------------------------------------------------

const showModal = ref(null); // 모달을 표시할지 여부를 결정하는 변수
const selectedReservationId = ref(null); // 취소할 예약의 ID 저장
const selectedProgramId = ref(null); // 취소할 예약의 ID 저장

// 예약 취소 버튼 클릭 핸들러
const handleCancelClick = (reservationId,modalName,programId) => {
  console.log("취소 클릭 ID:", reservationId); // ID가 제대로 넘어오는지 확인
  selectedReservationId.value = reservationId;  // 클릭한 예약의 ID 설정
  selectedProgramId.value = programId;
  showModal.value = modalName;  // 누르면 모달을 표시하게끔
};

// 모달을 닫을 때
const closeModal = () => {
  showModal.value = "";
  fetchReservations();
};

// 페이지가 변경될 때 쿼리 문자열을 업데이트 ----------------------------------------------------
watch(
    () => route.query.p,
    (newPage) => {
      currentPage.value = parseInt(newPage) || 1; // 쿼리 값이 없을 때 기본값을 1로 설정
      fetchReservations();
    }
);

// 생명주기 함수 ----------------------------------------------------------------------------
onMounted(() => {
  // 쿼리 파라미터 p가 없거나 유효하지 않으면 p=1로 설정
  if (!route.query.p || parseInt(route.query.p) > totalPageCount.value) {
    currentPage.value = 1;
    // router.replace로 쿼리 문자열을 p=1로 설정
    router.replace({query: {...route.query, p: 1}});
  } else {
    currentPage.value = parseInt(route.query.p); // 쿼리 파라미터 값을 currentPage에 설정
  }
  fetchReservations(); // 데이터를 패치
});

</script>

<template>
  <main>
    <section class="bg-color:base-1 n-layout-mj">
      <header class="n-title">
        <h1>예약 내역 ({{ totalRowCount }})</h1>
      </header>

      <div class="n-filter n-filter-rv bg-color:base-1 padding-x:6">
        <div class="overflow-x:auto">
          <ul class="item-wrapper padding-y:6">
            <li>
              <NuxtLink
                  href=""
                  class="n-btn n-btn-rv-filter n-btn:hover"
                  @click.prevent="filterByStatus([1,2,3,4])"
                  :class="{ active: selectedStatusId.join(',') === '1,2,3,4' }"
              >전체
              </NuxtLink>
            </li>

            <!-- 결제완료 버튼을 1개로 고정 -->
            <li>
              <NuxtLink
                  href=""
                  class="n-btn n-btn n-btn-rv-filter n-btn:hover"
                  @click.prevent="filterByStatus([1])"
                  :class="{ active: selectedStatusId.join(',') === '1' }"
              >결제완료
              </NuxtLink>
            </li>

            <!-- 예약확정, 이용완료, 취소됨 버튼 -->
            <li>
              <NuxtLink
                  href=""
                  class="n-btn n-btn-rv-filter n-btn:hover"
                  @click.prevent="filterByStatus([2])"
                  :class="{ active: selectedStatusId.join(',') === '2' }"
              >
                예약확정
              </NuxtLink>
            </li>
            <li>
              <NuxtLink
                  href=""
                  class="n-btn n-btn-rv-filter n-btn:hover"
                  @click.prevent="filterByStatus([3])"
                  :class="{ active: selectedStatusId.join(',') === '3' }"
              >
                취소됨
              </NuxtLink>
            </li>
            <li>
              <NuxtLink
                  href=""
                  class="n-btn n-btn-rv-filter n-btn:hover"
                  @click.prevent="filterByStatus([4])"
                  :class="{ active: selectedStatusId.join(',') === '4' }"
              >
                이용완료
              </NuxtLink>
            </li>
          </ul>
        </div>
      </div>

      <div class="n-card-container bg-color:base-1">
        <div class="n-card bg-color:base-1 padding:6" v-for="r in reservations" :key="r.id">
          <RouterLink :to="`/guest/reservations/${r.id}`" class="n-link-box" href="detail?id=1"></RouterLink>
          <div class="card-header">
            <div class="left">
              <span v-if="r.reservationStatus === 1"
                    class="n-panel-tag">
                결제완료
              </span>

              <span v-else-if="r.reservationStatus === 4" class="n-panel-tag not-submitted">
                이용완료
              </span>

              <span v-else-if="r.reservationStatus === 2" class="n-panel-tag not-submitted">
                예약확정
              </span>

              <span v-else-if="r.reservationStatus === 3" class="n-panel-tag not-submitted"
                    style="border-color: #DB4455; color: #DB4455;">
                취소됨
              </span>
            </div>
            <span v-if="r.reservationStatus === 3"
                  style="font-weight: bold; height: 20px; padding-left: 8px;"
            > [취소일자: {{ formatDeleteDate(r.deleteDate) }}]</span>
          </div>

          <div class="card-main">
            <div v-if="r.src && r.src.startsWith('uploads')" class="img-wrapper">
              <img :src="`http://localhost:8083/api/v1/${r.src}`" alt="대표사진"/>
            </div>

            <div class="card-info-wrapper">
              <div class="card-header-responsive">
                <div class="left" style="height: 24px;">
                  <span v-if="r.reservationStatus === 1"
                        class="n-panel-tag">
                    결제완료
                  </span>

                  <span v-else-if="r.reservationStatus === 4" class="n-panel-tag not-submitted">
                    이용완료
                  </span>

                  <span v-else-if="r.reservationStatus === 2" class="n-panel-tag not-submitted">
                    예약확정
                  </span>

                  <span v-else-if="r.reservationStatus === 3"
                        class="n-panel-tag not-submitted"
                        style="border-color: #DB4455; color: #DB4455;">
                    취소됨
                  </span>
                  <span v-if="r.reservationStatus === 3"
                        style="font-weight: bold; padding-left: 2px;"
                  > [취소일자: {{ formatDeleteDate(r.deleteDate) }}]</span>
                </div>
              </div>
              <p class="title">
                {{ r.programTitle }}
              </p>
              <div class="card-info-responsive">
                <div class="d:flex flex-direction:column">
                  <div class="card-info">
                    <span class="n-icon n-icon:calendar n-deco">진행일</span>
                    <span v-if="r.dDay >= 0">

                      {{ r.date }}
                      <span
                          v-if="[1, 2].includes(r.reservationStatus) && (r.dDay <= 3) && (r.dDay > 0)"
                          style="color: #DB4455;">
                        (D-{{ r.dDay }})
                      </span>

                      <span
                          v-else-if="[1, 2].includes(r.reservationStatus) && (r.dDay > 3)">
                        (D-{{ r.dDay }})
                      </span>

                      <span
                          v-else-if="[1, 2].includes(r.reservationStatus) && (r.dDay === 0)"
                          style="color: #DB4455;">
                        (D-day)
                      </span>

                    </span>

                  </div>
                  <div class="card-info">
                    <span class="n-icon n-icon:group n-deco">예약인원</span>
                    <span>{{ r.numberOfGuest }}</span>
                  </div>
                </div>

                <div v-if="[1, 2].includes(r.reservationStatus)"
                     class="card-footer-responsive">
                  <a href="#" class="n-btn bg-color:main-1 color:base-1">호스트 문의</a>
                  <a href="#" class="n-btn" @click="handleCancelClick(r.id, 'cancelReservationModal', r.programId)"
                     style="color: #DB4455; --btn-border-color:#DB4455;">
                    예약 취소
                  </a>
                  <a href="#"
                     class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding:0"
                     @click="handleShare(`http://localhost:3003/share/${r.id}`)">공유하기</a>
                </div>

                <div v-else-if="r.reservationStatus === 4" class="card-footer-responsive">
                  <a href="#" class="n-btn bg-color:main-1 color:base-1">호스트 문의</a>
                  <a href="#" class="n-btn">리뷰 작성</a>
                  <a href="#"
                     class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding:0"
                     @click="handleShare(`http://localhost:3003/share/${r.id}`)">공유하기</a>
                </div>

                <div v-else-if="r.reservationStatus === 3" class="card-footer-responsive">
                  <a href="#" class="n-btn bg-color:main-1 color:base-1">호스트 문의</a>
                  <a href="#"
                     class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding:0"
                     @click="handleShare(`http://localhost:3003/share/${r.id}`)">공유하기</a>
                </div>

              </div>
            </div>
          </div>

          <div v-if="[1, 2].includes(r.reservationStatus)"
               class="card-footer margin-top:2">
            <a href="#" class="n-btn bg-color:main-1 color:base-1">호스트 문의</a>
            <a href="#" class="n-btn" @click="handleCancelClick(r.id, 'cancelReservationModal', r.programId)"
               style="color: #DB4455; --btn-border-color:#DB4455;">
              예약 취소
            </a>
            <a href="#" class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding:0"
               @click="handleShare(`http://localhost:3003/share/${r.id}`)">공유하기</a>
          </div>

          <div v-else-if="r.reservationStatus === 4" class="card-footer margin-top:2">
            <a href="#" class="n-btn bg-color:main-1 color:base-1">호스트 문의</a>
            <a href="#" class="n-btn">리뷰 작성</a>
            <a href="#" class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding:0"
               @click="handleShare(`http://localhost:3003/share/${r.id}`)">공유하기</a>
          </div>

          <div v-else-if="r.reservationStatus === 3" class="card-footer margin-top:2"
               style="padding-left: 10px; justify-content: space-between;">
            <a href="#" class="n-btn bg-color:main-1 color:base-1" style="max-width: 478px;">호스트 문의</a>
            <a href="#" class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding:0"
               @click="handleShare(`http://localhost:3003/share/${r.id}`)">공유하기</a>
          </div>

          <!-- 예약 취소 모달 -->
          <CancelReservationModal
              v-if="showModal"
              :showModal="showModal"
              :selectedReservationId="selectedReservationId"
              :publishedProgramId="getPublishedProgramIdByReservationId(selectedReservationId)"
              :reservationId="selectedReservationId"
              :programId="getProgramIdByReservationId(selectedReservationId)"
              @close="closeModal"
          />

        </div>
      </div>

      <!-- Pager 부분 -->
      <Pager class="mb:4" :page-numbers="pageNumbers" :start-number="startNum" :total-page-count="totalPageCount"
             :href="`reservations`" @pageChange="pageClickHandler"/>

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

    > .n-btn {
      position: relative;
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