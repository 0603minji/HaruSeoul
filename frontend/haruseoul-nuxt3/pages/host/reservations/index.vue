<script setup>

import {ref} from "vue";
import {useRoute} from 'vue-router';
import DateRangeFilterModal from "~/components/modal/DateRangeFilterModal.vue";
import PublishProgramModal from "~/components/modal/PublishProgramModal.vue";
import PublishedStatusFilterModal from "~/components/modal/PublishedStatusFilterModal.vue";
import {useAuthFetch} from "~/composables/useAuthFetch.js";
import {useDataFetch} from "~/composables/useDataFetch.js";
import ProgramFilterModal from "~/components/modal/ProgramFilterModal.vue";
import PublishedStatusAsideFilter from "~/components/filter/PublishedStatusAsideFilter.vue";
import ProgramAsideFilter from "~/components/filter/ProgramAsideFilter.vue";
import DateRangeAsideFilter from "~/components/filter/DateRangeAsideFilter.vue";

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

const translateStatusNameToKorean = (statusName) => {
  if (statusName === 'On Going')
    return '모집 중'
  else if (statusName === 'Urgent')
    return '폐지임박'
  else if (statusName === 'Finished')
    return '종료됨'
  else if (statusName === 'Canceled')
    return '취소됨'
  else if (statusName === 'Wait Confirm')
    return '예약확정 대기'
  else if (statusName === 'Confirmed')
    return '예약확정'
}

// 한국날짜 D-day("2024-11-26") 입력하면 현재 한국시간 기준으로 d-day계산
const calculateKoreanDDay = (enteredDate) => {
  const koreaTimeOffset = 9 * 60 * 60 * 1000; // UTC+9 in minutes
  const targetDate = new Date(enteredDate + 'T00:00:00+09:00');
  const today = new Date();

  const timediff = targetDate.getTime() - today.getTime();

  return Math.ceil(timediff / (24 * 3600 * 1000));
}

const createQuery = () => {
  const result = {mIds: hostId};

  if (page.value) result.p = page.value;
  if (pageSize.value) result.pageSize = pageSize.value;

  // tab filtering
  if (tab.value === "finished" || tab.value === "canceled") {
    result.tab = tab.value;
    if (order.value) result.order = order.value;
    if (sortBy.value) result.sortBy = sortBy.value;
  }
  // tab==undefined거나 다른 값이면 예정된 일정 탭으로 간주
  else {
    result.tab = "todo";

    if (order.value) result.order = order.value;
    else result.order = "asc";

    if (sortBy.value) result.sortBy = sortBy.value;
    else result.sortBy = "date";
  }

  // --- 필터 ------------------------------------------------------
  // Add `s` key only if `statuses` is not empty
  if (statuses.value) result.s = statuses.value;

  // Add `d` key only if `dates` is not empty
  if (dates.value) result.d = dates.value;

  // Add `pIds` key only if `pIds` is not empty
  if (pIds.value) result.pIds = pIds.value;
  console.log('createdQuery: ', result);
  return result;
}

// $fetch
const fetchData = async () => {
  console.log('fetchData called.')
  const query = createQuery();
  const data = await useDataFetch(`host/published-programs`, {query: query});

  // api query -> user query
  const keysToExclude = ["mIds", "pageSize"];
  let userQuery = Object.fromEntries(
      Object.entries(query).filter(([key, value]) => {
        // 'mIds'와 'pageSize'를 제외하고 'tab'이 'finished'나 'canceled'가 아닌 경우 'tab'을 제외시킨다
        if (keysToExclude.includes(key)) return false;
        return !(key === 'tab' && value !== 'finished' && value !== 'canceled');
      })
  );

  console.log('     index fetchData called')
  console.log('           >> fetched data :', data)
  console.log('           >> query :', query)
  console.log('           >> userQuery :', userQuery)

  // url에 쿼리스트링 push
  await navigateTo({path: route.path, query: userQuery});

  mapFetchedData(data);
}

// fetch한 데이터로 변수 초기화
const mapFetchedData = (fetchedData) => {
  publishedPrograms.value = fetchedData.publishedPrograms;
  pages.value = fetchedData.pages;
  startNum.value = fetchedData.startNum;
  totalCount.value = fetchedData.totalCount;
  totalPages.value = fetchedData.totalPages;
  currentPageRowCount = fetchedData.currentPageRowCount;
  hasNextPage = fetchedData.hasNextPage;
  hasPreviousPage = fetchedData.hasPreviousPage;
  console.log('mapFetchedData called');
  console.log('   ->  pages: ', pages.value);
  console.log('   -> startNum: ', startNum.value);
  console.log('   ->  totalCount: ', totalCount.value);
  console.log('   ->  totalPages: ', totalPages.value);
  console.log('   ->  currentPageRowCount: ', currentPageRowCount);
  console.log('   ->  hasNextPage: ', hasNextPage);
  console.log('   ->  hasPreviousPage: ', hasPreviousPage);
  console.log('   ->  publishedPrograms: ', publishedPrograms.value);
}

// tab으로 이동 (예정된, 지난, 취소된)
const tabChangeHandler = (newTab) => {
  console.log('tabChangeHandler called')
  tab.value = newTab;
  console.log('tab: ', tab.value);
  page.value = 1; // 탭이동시 무조건 1page로
  fetchData();
}

// 필터모달에서 업데이트한 변수들로 쿼리 만들어서 패치
const updateDateFilterQuery = (SelectedDates) => {
  console.log('updateDateFilterQuery called')
  selectedDates.value = SelectedDates;

  // [date객체, date객체] -> "2024-11-26,2024-11-28"
  dates.value = SelectedDates
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
  console.log('   ->  dates쿼리: ', dates.value);
  page.value = "1";
  fetchData();
}

const updateStatusFilterQuery = (SelectedStatuses) => {
  console.log('updateStatusFilterQuery called');
  selectedStatuses.value = SelectedStatuses;
  console.log('   -> selectedStatuses: ', selectedStatuses.value)
  statuses.value = SelectedStatuses.join(",");
  page.value = "1";
  fetchData();
}

const updateProgramFilterQuery = (selected) => {
  console.log('updateProgramFilterQuery called')
  console.log('selected: ', selected) // [{id: , title: }]
  selectedProgramIds.value = selected.map(program => String(program.id)); // ["1", "2"...]
  pIds.value = selectedProgramIds.value.join(","); // '1,2,3...'
  page.value = "1";
  fetchData();
}

// 모든 필터 초기화
const resetFilterHandler = () => {
  // 쿼리초기화하고 다시 fetch
  dates.value = "";
  statuses.value = "";
  pIds.value = "";
  page.value = "1";
  fetchData();

  // 필터에게 props로 전달해서 필터도 초기화
  selectedDates.value = [];
  selectedProgramIds.value = [];
  selectedStatuses.value = [];

  // 모달창에 저장된 선택값들도 초기화. 필터모달 다시 랜더링
  reRenderTrigger.value = !reRenderTrigger.value;
}

// 페이지네이션 입력받은 페이지의 데이터로 패치
const pageClickHandler = (newPage) => {
  page.value = newPage;
  fetchData();
}

// 일정취소
const CancelHandler = async (pp) => {
  /*// 취소할 publishedProgram의 groupSizeCurrent가 0이면? 그냥 취소
  if (pp.groupSizeCurrent !== 0) {

  }*/

  console.log('   CancelHandler called')
  console.log('          ->  Put host/published-programs');

  // publishedProgramUpdateDto
  const publishedProgramUpdateDto = {
    "id": pp.id,
    "groupSizeCurrent": pp.groupSizeCurrent,
    "statusId": 4
  }

  let response = await useDataFetch("host/published-programs", {
    method: "PUT",
    headers: {
      "Content-type": "application/json"
    },
    body: publishedProgramUpdateDto
  });
  console.log('          PublishedProgram Update result: ', response);

  // 예약한 게스트들의 reservation도 cancel처리
  console.log('          pp.reservationIds: ', pp.reservationIds)
  for (const rId of pp.reservationIds) {
    let rvCancelResponse = await useDataFetch(`host/reservations/${rId}/cancel`, {
      method: "PUT",
      headers: {
        "Content-type": "application/json"
      }
    });
    console.log('          Reservation Cancel result: ', rvCancelResponse);
  }

  // 개설된 프로그램이 더이상 존재하지 않는다면 프로그램 상태르 Published -> Unpublished로 변경
  let statusCheckResponse = await useDataFetch(`host/programs/${pp.programId}/statusCheck`, {
    method: "PUT",
    headers: {
      "Content-type": "application/json"
    }
  });
  console.log('          statusCheck result: ', statusCheckResponse);

  // 일정취소 확인 모달창
  openModal('completeCancelModal');

  // 취소된 pp반영한 목록으로 갱신
  await fetchData();
  // publishProgramModal reRender
  PublishProgramModalKey.value = !PublishProgramModalKey.value;
}

// 예약확정
const confirmHandler = async (pp) => {
  console.log('   confirmHandler called')
  console.log('          ->  Put host/published-programs');

  // publishedProgram 상태 예약확정으로 업데이트-----------------------------------------------------------------------------
  // publishedProgramUpdateDto
  const publishedProgramUpdateDto = {
    "id": pp.id,
    "groupSizeCurrent": pp.groupSizeCurrent,
    "statusId": 6
  }

  let response = await useDataFetch("host/published-programs", {
    method: "PUT",
    headers: {
      "Content-type": "application/json"
    },
    body: publishedProgramUpdateDto
  });
  console.log('          PublishedProgram Update result: ', response);


  // 최대인원 = 현재인원 : reservation엔티티 reservationStatus 2(예약확정)으로 업데이트-----------------------------------------
  if (pp.groupSizeMax === pp.groupSizeCurrent) {
    console.log('          게스트 reservationStatus 1 -> 2로 업데이트');
    console.log('          pp.reservationIds: ', pp.reservationIds);
    for (const rId of pp.reservationIds) {
      // reservationStatusUpdateDto
      const reservationStatusUpdateDto = {
        "id": rId,
        "reservationStatus": 2 // 예약확정
      }

      let rvResponse = await useDataFetch(`host/reservations/${rId}/reservationStatus`, {
        method: "PUT",
        headers: {
          "Content-type": "application/json"
        },
        body: reservationStatusUpdateDto
      });
      console.log('          PublishedProgram Update result: ', rvResponse);
    }
  }


  // 최대인원 > 현재인원 : 게스트들에게 예약확정 동의요청 보내기-----------------------------------------------------------------
  if (pp.groupSizeMax > pp.groupSizeCurrent) {
    await requestGuestApproval();
    openModal('completeRequestGuestApproval');
  }

  // 예약확정 확인 모달창
  else
    openModal('completeConfirmModal');

  // 취소된 pp반영한 목록으로 갱신
  await fetchData();
  // publishProgramModal reRender
  PublishProgramModalKey.value = !PublishProgramModalKey.value;
}

// todo 게스트들에게 참가여부 확인 알림 보내기
const requestGuestApproval = async () => {
  console.log('   requestGuestApproval called')
  // reservation테이블에 guest_consent 1(미응답)로 설정
  for (const rId of pp.value.reservationIds) {
    const guestConsentResponse = await  useDataFetch(`host/reservations/consent`, {
      method: "PUT",
      headers: {
        "Content-type": "application/json"
      },
      body: {
        "id": rId,
        "guestConsent": 1 // 미응답으로 초기화
      }
    });
    console.log('          Reservation Update result: ', guestConsentResponse);
  }
}


// === 모달창 ===========================================================================================================
const modalVisible = ref("");

const OpenDateRangeFilterModalHandler = () => modalVisible.value = "DateRangeFilterModal";
const OpenPublishedStatusFilterModalHandler = () => modalVisible.value = "PublishedStatusFilterModal";
const OpenProgramFilterModalHandler = () => modalVisible.value = "ProgramFilterModal";
const OpenPublishProgramModalHandler = (programId) => {
  pIdToPublish.value = programId;
  modalVisible.value = "PublishProgramModal";
}


// === 팝업 ============================================================================================================
const morePopupStatus = ref({}); // publishedProgram 카드 header영역 right의 더보기 openMorePopup

const toggleMorePopup = (id) => {
  console.log(' toggleMorePopup called')
  // 해당 id가 이미 존재하면 속성을 제거, 없으면 true로 추가
  if (morePopupStatus.value[id]) {
    // id에 해당하는 속성이 있다면 제거
    delete morePopupStatus.value[id];
  } else {
    // id에 해당하는 속성이 없다면 true로 추가
    morePopupStatus.value[id] = true;
  }
  console.log('   -> morePopupStatus: ', morePopupStatus.value);
}

const closeMorePopup = (id) => {
  console.log(' closeMorePopup called')
  // 해당 id에 대한 속성을 삭제
  if (morePopupStatus.value[id])
    delete morePopupStatus.value[id];

  console.log('   -> morePopupStatus: ', morePopupStatus.value);
}

// === 변수 todo: 변수 =============================================================================================================
const route = useRoute();
const router = useRouter();
const userDetails = useUserDetails();
const config = useRuntimeConfig(); // 서버 uploads에서 대표이미지, 게스트이미지 로드용
// 모달창
const { isModalVisible, openModal, closeModal } = useModal();
const confirmPpPost = ref(false); // 개설확인모달창에서 확인 눌렀을 때 PublishProgramModal에 전달하여 post요청
const PublishProgramModalKey = ref(false); // 일정취소 시 리렌더링 유발용


const hostId = userDetails.id.value; // 프론트에서 저장하고 있는 인증정보에 접근해서 얻어와야함
console.log('hostId: ', hostId)
const pIdToPublish = ref(null); // 일정추가, 추가개설할 pubishedprogramId
const ppToCancel = ref(null); // 일정취소할 프로그램 정보를 모달창으로 전달
const ppToConfirm = ref(null); // 예약확정할 프로그램 정보를 모달창으로 전달

// PublishedProgramResponseDto
const pages = ref([1, 2, 3, 4, 5]); // 페이지네이션
const startNum = ref(1); // 페이지네이션
const totalCount = ref();
const totalPages = ref(0);
let currentPageRowCount;
let hasNextPage;
let hasPreviousPage;
const publishedPrograms = ref(false);


// query에 들어가는 변수들
// 최초 페이지 접속 시 query에 쓸 변수 초기화. 이후에는 emit event함수로 초기화 후 fetch후 publishedPrograms갱신
const dates = ref(route.query.d); // 검색할 기간의 시작일, 말일. PublishedProgramFilter에서 emit으로 받아올 것
const statuses = ref(route.query.s); // 검색할 상태. 1~6. PublishedProgramFilter에서 emit으로 받아올 것
const pIds = ref(route.query.pIds); // 검색할 프로그램 id들. PublishedProgramFilter에서 emit으로 받아올 것
const page = ref(route.query.p);
const pageSize = ref(6);
const sortBy = ref(null); // 예정된 일정: date, 지난, 취소된 일정: regDate or null
const order = ref(null); // 예정된 일정: desc or null, 지난, 취소된 일정: asc
/*
** tab에 따른 api쿼리 **
  1. undefined : host/published-programs?tab=todo
  2. finished : ?tab=finished
  3. canceled : ?tab=canceled
*/
const tab = ref(route.query.tab);
watchEffect(() => {
  console.log('index');
  console.log('     -> dates: ', dates.value);
  console.log('     -> statuses: ', statuses.value);
  console.log('     -> pIds: ', pIds.value);
  console.log('     -> page: ', page.value);
  console.log('     -> pageSize: ', pageSize.value);
  console.log('     -> sortBy: ', sortBy.value);
  console.log('     -> order: ', order.value);
  console.log('     -> tab: ', tab.value);
});


// 필터모달에서 보내온 값 저장, 필터모달에게 props로 전달할 변수
// 최초 url접속 시 쿼리스트링에서 받아온 값(statuses등으로) 초기화됨
const selectedStatuses = ref(statuses.value ? statuses.value.split(",") : []);
console.log("index selectedStatues :", selectedStatuses.value);

const selectedDates = ref(dates.value ? dates.value.split(",").map(dateString => new Date(dateString + 'T00:00:00.000+09:00')) : []);
console.log("index selectedDates :", selectedDates.value);

const selectedProgramIds = ref(pIds.value ? pIds.value.split(",") : []);
console.log("index selectedProgramIds :", selectedProgramIds.value);


// 모든 필터 초기화 시 필터모달 다시 렌더링으로 구현
const reRenderTrigger = ref(false);


/*=== fetch ==========================================================================================================*/
const {data} = await useAuthFetch(`host/published-programs`, {query: createQuery()});

// data.value에 PublishedProgramResponseDto가 담겨있음
mapFetchedData(data.value);

watchEffect(() => {
  console.log('selected 확인용===========================================================================')
  console.log('dates', selectedDates.value)
  console.log('programIds', selectedProgramIds.value)
  console.log('statuses', selectedStatuses.value)
  console.log('selected =================================================================================')
})
</script>

<template>
  <main>
    <!-- === 모달 =================================================================================================== -->
    <DateRangeFilterModal :key="reRenderTrigger" :class="{'show': modalVisible === 'DateRangeFilterModal'}"
                          :selectedDates="selectedDates"
                          @close-modal="(selected) => { updateDateFilterQuery(selected); modalVisible = '';}"/>
    <PublishedStatusFilterModal :key="reRenderTrigger"
                                :class="{'show': modalVisible === 'PublishedStatusFilterModal'}"
                                :tab="tab"
                                :selectedStatuses="selectedStatuses"
                                @close-modal="(selected) => { updateStatusFilterQuery(selected); modalVisible = '';}"/>
    <ProgramFilterModal :key="reRenderTrigger"
                        :class="{'show': modalVisible === 'ProgramFilterModal'}"
                        :selectedProgramIds="selectedProgramIds"
                        @close-modal="modalVisible = ''"
                        @updateSelectedPrograms="updateProgramFilterQuery"/>
    <PublishProgramModal :key="PublishProgramModalKey"
                         :class="{'show': modalVisible === 'PublishProgramModal'}"
                         :default-program-id="pIdToPublish"
                         :confirm-pp-post="confirmPpPost"
                         @close-modal="() => { fetchData(); modalVisible = ''; }"
                         @submit="openModal('confirmPpModal')"/>

    <!-- 모달창 떴을 때 배경처리   -->
    <div :class="{'active': modalVisible}" class="backdrop"></div>


    <!--  확인 모달창 ================================================================================================ -->
    <!--  개설확인  -->
    <Modal :isVisible="isModalVisible('confirmPpModal')" @close="closeModal('confirmPpModal')"
           @confirm="() => {confirmPpPost=!confirmPpPost;
             console.log('index. Modal emit confirm and callback func called. confirmPpPost: ', confirmPpPost);
             closeModal('confirmPpModal');}">
      <p>개설하시겠습니까?</p>
    </Modal>

    <!--  일정취소확인  -->
    <Modal :isVisible="isModalVisible('confirmCancelModal')" @close="closeModal('confirmCancelModal')"
           @confirm="() => {CancelHandler(ppToCancel); closeModal('confirmCancelModal');}">
      <p v-if="ppToCancel.groupSizeCurrent > 0" style="color: var(--color-red-1)">프로그램을 예약한 게스트가 존재합니다. ({{ ppToCancel.groupSizeCurrent }} 명)</p>
      <p v-if="ppToCancel.statusName==='Confirmed'" style="color: var(--color-red-1)">예약확정된 프로그램을 취소할 경우 페널티가 있을 수 있습니다.</p>
      <p>일정을 취소하시겠습니까?</p>
    </Modal>
    <Modal class="onlyConfirm" :isVisible="isModalVisible('completeCancelModal')" @confirm="closeModal('completeCancelModal')">
      <p>예약이 취소되었습니다.</p>
    </Modal>

    <!--  예약확정확인  -->
    <Modal :isVisible="isModalVisible('confirmConfirmModal')" @close="closeModal('confirmConfirmModal')"
           @confirm="() => {confirmHandler(ppToConfirm); closeModal('confirmConfirmModal');}">
      <div v-if="ppToConfirm.groupSizeCurrent < ppToConfirm.groupSizeMax">
        <p style="color: var(--color-red-1)">현재 예약인원 ({{ ppToConfirm.groupSizeCurrent }}/{{ ppToConfirm.groupSizeMax }})</p>
        <p>현재 예약인원이 최대 예약인원보다 부족할 경우, 모든 게스트 동의 후 예약확정이 가능합니다.</p>
        <p>게스트에게 예약확정 동의요청을 보내시겠습니까?</p>
      </div>
      <div v-if="ppToConfirm.groupSizeCurrent === ppToConfirm.groupSizeMax">
        <p style="color: var(--color-green-1)">현재 예약인원 ({{ ppToConfirm.groupSizeCurrent }}/{{ ppToConfirm.groupSizeMax }})</p>
        <p>예약을 확정하시겠습니까?</p>
      </div>
    </Modal>
    <Modal class="onlyConfirm" :isVisible="isModalVisible('completeCancelConfirmModal')" @confirm="closeModal('completeCancelConfirmModal')">
      <p>예약이 확정되었습니다.</p>
    </Modal>
    <Modal class="onlyConfirm" :isVisible="isModalVisible('completeRequestGuestApproval')" @confirm="closeModal('completeRequestGuestApproval')">
      <p>게스트들에게 예약확정 동의요청이 전송되었습니다.</p>
    </Modal>

    <!-- ============================================================================================================= -->


    <section class="layout-body"> <!-- main 내 모든 -->
      <!--=== heading ==========================================-->
      <!--    예약 관리           +일정추가-->
      <header class="n-title">
        <h1 class="">예약관리</h1>
        <div>
          <button @click.prevent="OpenPublishProgramModalHandler(null)"
             class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:plus n-deco">일정추가</button>
        </div>
      </header>

      <!--=== 탭 예정된 일정 / 지난 일정 / 취소된 일정 ==========================================-->
      <nav class="n-bar-underline">
        <h1 class="d:none">네비탭</h1>
        <ul class="item-wrapper">
          <li @click.prevent="tabChangeHandler(undefined)"
              class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"
              :class="{ active: !(tab === 'finished' || tab === 'canceled') }">
            <NuxtLink :to="{ path: 'reservations', query: { ...route.query, tab:'todo' } }" class="tab-btn-link">예정된
              일정
            </NuxtLink>
          </li>
          <li @click.prevent="tabChangeHandler('finished')"
              class=" n-btn:hover n-btn n-btn-border:none n-btn-radius:0"
              :class="{ active: tab === 'finished' }">
            <NuxtLink :to="{ path: 'reservations', query: { ...route.query, tab:'finished' } }" class="tab-btn-link">지난
              일정
            </NuxtLink>
          </li>
          <li @click.prevent="tabChangeHandler('canceled')"
              class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"
              :class="{ active: tab === 'canceled' }">
            <NuxtLink :to="{ path: 'reservations', query: { ...route.query, tab:'canceled' } }" class="tab-btn-link">취소된
              일정
            </NuxtLink>
          </li>
        </ul>
      </nav>

      <div class="layout-main">
        <div class="layout-main-list">
          <!--=== 필터 .n-filter ==========================================-->
          <!-- 1.모집 중, 2.폐지임박, 3.종료, 4.취소, 5.예약확정대기, 6.예약확정 -->
          <section class="n-filter md:d:none bg-color:base-1">
            <h1 class="d:none">필터</h1>
            <div class="overflow-x:auto">
              <ul class="item-wrapper">
                <li><a @click.prevent="OpenDateRangeFilterModalHandler" href=""
                       :class="{'active': dates }"
                       class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:calendar n-icon-size:1 n-deco n-deco-gap:1">기간</a>
                </li>
                <li><a @click.prevent="OpenPublishedStatusFilterModalHandler" href=""
                       :class="{'active': statuses}"
                       class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:pending n-icon-size:1 n-deco n-deco-gap:1">프로그램
                  상태</a></li>
                <li><a @click.prevent="OpenProgramFilterModalHandler" href=""
                       :class="{'active': pIds}"
                       class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:search n-icon-size:1 n-deco n-deco-gap:1">프로그램</a>
                </li>
              </ul>
            </div>

            <div class="reset-box">
              <div class="gradation"></div>
              <div class="btn-box">
                <button @click.prevent="resetFilterHandler" :class="{'n-deco': false}"
                        class="reset-btn n-icon n-icon:reset">
                  Reset
                </button>
              </div>
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
            <ul class="n-card-container bg-color:base-1">
              <li v-if="publishedPrograms.length === 0
                  && !(tab==='canceled' || tab==='finished')
                  && !(selectedDates.length>0 || selectedStatuses.length>0 || selectedProgramIds.length>0)" class="padding:8">
                개설된 프로그램이 존재하지 않습니다.
              </li>
              <li v-if="publishedPrograms.length === 0
                  && tab==='finished'
                  && !(selectedDates.length>0 || selectedStatuses.length>0 || selectedProgramIds.length>0)" class="padding:8">
                종료된 프로그램이 존재하지 않습니다.
              </li>
              <li v-if="publishedPrograms.length === 0
                  && tab==='canceled'
                  && !(selectedDates.length>0 || selectedStatuses.length>0 || selectedProgramIds.length>0)" class="padding:8">
                취소된 프로그램이 존재하지 않습니다.
              </li>
              <li v-if="publishedPrograms.length === 0 && (selectedDates.length>0 || selectedStatuses.length>0 || selectedProgramIds.length>0)" class="padding:8">
                일치하는 프로그램이 존재하지 않습니다.
              </li>

              <li v-for="pp in publishedPrograms" :key="pp.id" class="n-card n-card:hover padding:6">
                <h2 class="d:none">예약 카드</h2>

                <div class="card-header">
                  <div class="left">
                    <span class="n-panel-tag" :class="{'on-going': pp.statusName === 'On Going',
                      'urgent': pp.statusName === 'Urgent',
                      'confirmed': pp.statusName === 'Confirmed',
                      'wait-confirm': pp.statusName === 'Wait Confirm',
                      'finished': pp.statusName === 'Finished'}
                    ">
                      {{ translateStatusNameToKorean(pp.statusName) }}
                    </span>
                  </div>
                  <div class="right">
                    <button ref="morePopupBtn"
                        class="morePopup-btn n-btn n-btn:hover n-btn-bd:none n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9"
                        @click.prevent="toggleMorePopup(pp.id)">더보기

                    </button>
                    <!--         더보기 팝업           -->
                    <div class="morePopup" v-if="morePopupStatus[pp.id]">
                      <ul>
                        <li v-if="pp.statusName !== 'Canceled' && pp.statusName !== 'Finished'"
                            :class="{'no-click': pp.groupSizeCurrent === 0 || pp.statusName==='Confirmed', 'disabled': pp.groupSizeCurrent === 0 || pp.statusName==='Confirmed'}"
                            @click.prevent="closeMorePopup(pp.id); ppToConfirm=pp; openModal('confirmConfirmModal')">예약확정</li><!-- groupSizeMin < groupSizeCurrent이면 가능 -->
                        <li v-if="pp.statusName !== 'Canceled' && pp.statusName !== 'Finished'"
                            @click.prevent="closeMorePopup(pp.id); ppToCancel=pp; openModal('confirmCancelModal')">일정취소</li><!-- 취소, 완료된 일정 아닌 나머지 -->
                        <li v-if="pp.statusName !== 'Canceled' && pp.statusName !== 'Finished'"
                            @click.prevent="closeMorePopup(pp.id); OpenPublishProgramModalHandler(pp.programId)">추가개설</li><!-- 취소, 완료된 일정 아닌 나머지 -->
                        <li v-if="pp.statusName==='Canceled' || pp.statusName==='Finished'"
                            @click.prevent="closeMorePopup(pp.id)">내역삭제</li><!-- only 취소3, 완료된 일정4 -->
                      </ul>
                    </div>
                  </div>
                </div>

                <div class="card-main">
                  <NuxtLink class="img-wrapper" :to="`reservations/${pp.id}`">
                    <img v-if="pp.images.length>0" :src="`${config.public.apiBase}${pp.images.at(0).src}`" alt="대표사진">
                    <img v-else src="/assets/image/default-program-image.png" alt="대표사진">
                  </NuxtLink>

                  <NuxtLink class="card-info-wrapper" :to="`reservations/${pp.id}`">
                    <p class="title">{{ pp.programTitle }}</p>
                    <div class="card-info">
                      <span class="n-icon n-icon:calendar n-deco">{{ formatDate(pp.date) }}</span>
                      <span v-if="calculateKoreanDDay(pp.date) === 0" class="n-panel-tag d-day">D-day</span>
                      <span v-if="0 < calculateKoreanDDay(pp.date) && calculateKoreanDDay(pp.date) <= 3"
                            class="n-panel-tag urgent">D-{{ calculateKoreanDDay(pp.date) }}</span>
                      <span v-if="3 < calculateKoreanDDay(pp.date)"
                            class="n-panel-tag">D-{{ calculateKoreanDDay(pp.date) }}</span>
                    </div>
                  </NuxtLink>

                  <!-- md:footer: card-footer영역에 존재하다가 992px이상에서 card-main의 우측으로 이동 -->
                  <div class="applicant-status lg:show">
                    <div class="guest-profile-container">
                      <div v-if="pp.guestProfileImgSrcs.length > 0" v-for="img in pp.guestProfileImgSrcs" class="guest-profile-wrapper">
                        <img :src="`${config.public.apiBase}${img}`" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">
                      {{ pp.groupSizeCurrent }} / {{ pp.groupSizeMax }}
                    </span>
                  </div>
                </div>

                <div class="card-footer">
                  <div class="applicant-status margin-left:auto">
                    <div class="guest-profile-container">
                      <div v-if="pp.guestProfileImgSrcs.length > 0" v-for="img in pp.guestProfileImgSrcs" class="guest-profile-wrapper">
                        <img v-if="img!==null" :src="`${config.public.apiBase}${img}`" alt="게스트 프로필">
                        <img v-else src="/assets/image/default-profile.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">
                      {{ pp.groupSizeCurrent }} / {{ pp.groupSizeMax }}
                    </span>
                  </div>
                </div>
              </li>
            </ul>

            <!-- Pager 부분 --> <!--todo: pager-->
            <Pager2 :page-numbers="pages" :start-num="startNum" :total-pages="totalPages"
                    @page-change="pageClickHandler"/>

          </section>
        </div>

        <!-- @media (min-width: 768px) 필터 aside  -->
        <aside class="layout-main-aside">
          <header class="n-title">
            <h1 class="">Filter</h1>
            <div>
              <button class="n-btn n-btn:hover n-icon n-icon:reset"
                      style="--icon-color: var(--color-sub-1); cursor: pointer;"
                      @click.prevent="resetFilterHandler">
                초기화
              </button>
            </div>
          </header>
          <div class="filters">
            <!-- 기간 필터 -->
            <DateRangeAsideFilter :key="reRenderTrigger"
                                  :selectedDates="selectedDates"
                                  @emit-selected-dates="(selected) => { updateDateFilterQuery(selected); modalVisible = '';}"/>
            <span class="separator"></span>
            <!-- 프로그램 상태 필터 -->
            <PublishedStatusAsideFilter :key="reRenderTrigger"
                                        :tab="tab"
                                        :selectedStatuses="selectedStatuses"
                                        @updateSelectedStatuses="updateStatusFilterQuery"/>
            <span class="separator"></span>
            <!-- 프로그램 필터 -->
            <ProgramAsideFilter :key="reRenderTrigger"
                                :selectedProgramIds="selectedProgramIds"
                                @updateSelectedPrograms="updateProgramFilterQuery"/>
          </div>
        </aside>
      </div>
    </section>
  </main>
</template>

<style scoped>
.no-click {
  pointer-events: none;
}

/* Modal컴포넌트 스타일 */
.modal-content p {
  margin-bottom: 8px;
}

.modal-content p:last-child {
  margin-bottom: 0;
}

.layout-body {
  .n-bar-underline {
    .item-wrapper {
      justify-content: space-between;

      li {
        --btn-padding: 0;

        .tab-btn-link {
          padding: 8px 16px;
        }
      }
    }
  }

  .layout-main {
    display: flex;
    margin-bottom: 30px;
    position: relative;

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
          box-shadow: 5px 5px 10px 0.5px var(--color-base-3);

          .card-header {
            position: relative;

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

            .right {
              position: relative;

              .morePopup-btn {
                position: relative;
                z-index: 10;

                --btn-border-radius: 8px;
                --btn-bg-hover: var(--color-base-4);
              }

              .morePopup {
                width: 120px;
                position: absolute;
                top: 100%; /* 버튼 바로 아래로 위치 */
                right: 0; /* 버튼의 오른쪽 모서리와 평행하도록 맞춤 */
                border-radius: 8px;
                background-color: white;
                padding: 10px 0;
                border: 1px solid #ddd;
                box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
                z-index: 10;

                ul {
                  display: flex;
                  flex-direction: column;

                  li {
                    padding: 6px 16px;
                    cursor: pointer;
                  }

                  li:hover {
                    background-color: var(--color-base-2);
                  }

                  .disabled {
                    color: var(--color-base-6);
                  }
                }
              }
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
        }
      }
    }

    .layout-main-aside {
      display: none;
      flex-direction: column;
      flex-shrink: 0;
      width: 250px;
      margin: 0 16px;
      /*position: sticky; layout-main-list의 카드와 z-index로 오버랩핑이 되지 않는 문제가 발생해 일단 주석처리
    top: 0;*/
      height: 100vh;

      .n-title {
        --title-font-size: var(--font-size-9);
        /* 18 */
        --title-font-weight: 600;
        /* semi bold */
        --title-padding: 6px 4px 14px 4px;

        .n-icon {
          --icon-size: var(--icon-size-4);
          padding: 8px;
        }
      }

      .filters {
        .separator {
          display: flex;
          width: 216px;
          height: 1px;
          background-color: var(--color-base-3);
        }
      }
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
        /* 일정추가 버튼 위치 조정용 */
        /*margin-right: calc(16px + 250px + 16px - 20px);*/
      }
    }

    .layout-main {
      .layout-main-list {
      }

      .layout-main-aside {
        display: flex;

        /* mj host/programs */

        .filters {
          display: flex;
          flex-direction: column;
          gap: 24px;
          padding: 16px 16px 32px 16px;
          border: 1px solid var(--color-base-3);
          border-radius: 12px;
        }
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

/* 모달창 떴을 때 배경처리용 */
.backdrop {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Dark background */
  backdrop-filter: blur(5px); /* Blur effect */
  z-index: 990; /* Behind modal but above other content */
}

.backdrop.active {
  display: block;
}
</style>