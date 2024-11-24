<script setup>
import {useAuthFetch} from "~/composables/useAuthFetch.js";
import {ref} from "vue";
import PublishProgramModal from "~/components/modal/PublishProgramModal.vue";

const config = useRuntimeConfig(); // 서버 uploads에서 대표이미지 로드용
const route = useRoute();
const publishedProgramId = route.params.id;
const {data:publishedProgramData} = await useAuthFetch(`host/published-programs/${publishedProgramId}`);
const pp = ref(publishedProgramData.value); // publishedProgram
console.log('pp: ', pp.value);
const {data:rvListDtosData} = await useAuthFetch(`host/reservations`, {query: {ppId: publishedProgramId}});
const applicants = ref(rvListDtosData); // 해당 publishedProgram에 참가한 참가자들
console.log('applicants: ', applicants.value);
const applicantToDismiss = ref(null); // 추방할 참가자. 추방확인모달에서 사용


// 모달창
const { isModalVisible, openModal, closeModal } = useModal();
const confirmPpPost = ref(false); // 개설확인모달창에서 확인 눌렀을 때 PublishProgramModal에 전달하여 post요청
const PublishProgramModalKey = ref(false); // 일정취소 시 리렌더링 유발용

const pIdToPublish = ref(null); // 일정추가, 추가개설할 pubishedprogramId
const ppToCancel = ref(null); // 일정취소할 프로그램 정보를 모달창으로 전달
const ppToConfirm = ref(null); // 예약확정할 프로그램 정보를 모달창으로 전달

const modalVisible = ref("");
const OpenPublishProgramModalHandler = (programId) => {
  pIdToPublish.value = programId;
  modalVisible.value = "PublishProgramModal";
}

// === 유틸 =============================================================================================================
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


// === 팝업 =============================================================================================================
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


// === 더보기 일정취소 확정 ===============================================================================================
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
    "groupSizeCurrent": pp.groupSizeCurrent, // 폐지 당시 예약된 참가자수 박제
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
      },
      body: {
        "cancelMethod": 3, // 3:호스트가 pp취소 or 정원미달로 자동폐지
        "cancelReason": "호스트가 일정을 취소함",
        "reservationStatus": 3
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

// 예약확정===============================================================================================================
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

// todo 호스트가 게스트 추방
const dismissHandler = async (reservationId) => {
  console.log('   dismissHandler called')
  console.log(`          ->  Put host/reservations/${reservationId}`);

  // reservation테이블에서 해당 예약을 삭제처리(delete_date 업데이트)----------------------------------------------------------
  const rvCancelResponse = await useDataFetch(`host/reservations/${reservationId}/cancel`, {
    method: "PUT",
    headers: {
      "Content-type": "application/json"
    },
    body: {
      "cancelMethod": 2, // 2:kick
      "cancelReason": "호스트에 의해 추방됨. 추방사유 입력받기는 아직 미구현",
      "reservationStatus": 3
    }
  });
  console.log('          Reservation Cancel result: ', rvCancelResponse);


  // publishedProgram테이블에서도 변동사항 처리-----------------------------------------------------------------------------
    console.log('          publishedProgram에 추방사항 적용');
  let updatedStatusId = pp.value.statusId; // statusId=5가 아닐 때 변경없음
  // statusId=5(예약확정대기, 풀방)일 때는 groupSizeCurrent-- 후에 모집중 or 폐지임박으로 변경
  // 현재시간 기준 d-1이면 폐지임박 그외엔 모집중
  if (pp.value.statusId === 5) {
    const Dday = calculateKoreanDDay(pp.value.date);
    console.log('             예약확정대기상태일 경우, pp status변경을 위한 Dday계산, Dday: ', Dday);
    if (Dday <= 2) updatedStatusId = 2;
    else updatedStatusId = 1;
  }

  const publishedProgramUpdateDto = {
    id: pp.value.id,
    groupSizeCurrent: pp.value.groupSizeCurrent - applicantToDismiss.value.numberOfGuest,
    // 예약대기 상태(풀방)에서 추방하면 모집중 or 폐지임박으로 변경
    statusId: updatedStatusId
  }

  const ppResponse = await useDataFetch(`host/published-programs`, {
    method: "PUT",
    headers: {
      "Content-type": "application/json"
    },
    body: publishedProgramUpdateDto
  })
  console.log('          PublishProgram update result: ', ppResponse);


  // 일정취소 확인 모달창
  openModal('completeDismissModal');

  // 취소된 pp반영한 목록으로 갱신
  await fetchData();
  // publishProgramModal reRender
  PublishProgramModalKey.value = !PublishProgramModalKey.value;
}


// === $fetch ==========================================================================================================
const fetchData = async () => {
  console.log('fetchData called.')
  const dataPp = await useDataFetch(`host/published-programs/${publishedProgramId}`);
  const dataApplicants = await useDataFetch(`host/reservations`, {query: {ppId: publishedProgramId}});

  console.log('           >> fetched dataPp :', dataPp);
  console.log('           >> fetched dataApplicants :', dataApplicants);
  pp.value = dataPp;
  applicants.value = dataApplicants;
}
</script>
<template>
  <main>
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
    <Modal class="confirmModal"
           :isVisible="isModalVisible('confirmConfirmModal')" @close="closeModal('confirmConfirmModal')"
           @confirm="() => {confirmHandler(ppToConfirm); closeModal('confirmConfirmModal');}">
      <div v-if="ppToConfirm.groupSizeCurrent < ppToConfirm.groupSizeMax">

          <p class="font-weight:bold margin-bottom:6">[예약 확정 안내] <span class="font-weight:400" style="color: var(--color-red-1)">현재 예약인원 ({{ ppToConfirm.groupSizeCurrent }}/{{ ppToConfirm.groupSizeMax }})</span></p>
          <p>인원 미달 상태로 프로그램을 확정하시겠습니까?</p>
        <ul>
          <li class="margin-top:7">예약 확정 시, 참가자에게 참가 동의 여부를 요청하는 알림이 발송됩니다.</li>
          <li>동의하지 않은 참가자의 예약은 자동으로 취소 및 환불 처리됩니다.</li>
        </ul>
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
      <p>참가자들에게 참가 동의요청이 발송되었습니다.</p>
    </Modal>

    <!--  참가자추방확인  -->
    <Modal :isVisible="isModalVisible('confirmDismissModal')" @close="closeModal('confirmDismissModal')"
           @confirm="() => {dismissHandler(applicantToDismiss.reservationId); closeModal('confirmDismissModal');}">
      <p>{{ applicantToDismiss.applicantName }} 참가자를 추방하시겠습니까?</p>
    </Modal>
    <Modal class="onlyConfirm" :isVisible="isModalVisible('completeDismissModal')" @confirm="closeModal('completeDismissModal')">
      <p>{{ applicantToDismiss.applicantName }} 참가자가 추방되었습니다.</p>
    </Modal>

    <!-- ============================================================================================================= -->


    <section class="layout-body"> <!-- main 내 모든 -->
      <header class="n-title">
        <h1 class="">예약 상세보기</h1>
      </header>

      <div class="layout-main">
        <div class="layout-main-list">

            <div class="n-card-container">
              <div class="n-card padding:6">
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
                  <div class="img-wrapper">
                    <img v-if="pp.images.length>0" :src="`${config.public.apiBase}${pp.images.at(0).src}`" alt="대표사진">
                    <img v-else src="/assets/image/default-program-image.png" alt="대표사진">
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
                  <div class="btn-box">
                    <button v-if="pp.statusName !== 'Canceled' && pp.statusName !== 'Finished'"
                            class="n-btn n-btn:hover n-btn-bg-color:main"
                            :class="{'no-click': pp.groupSizeCurrent === 0 || pp.statusName==='Confirmed', 'disabled': pp.groupSizeCurrent === 0 || pp.statusName==='Confirmed'}"
                            @click.prevent="ppToConfirm=pp; openModal('confirmConfirmModal')">예약확정</button>
                    <button v-if="pp.statusName !== 'Canceled' && pp.statusName !== 'Finished'"
                            class="n-btn n-btn:hover n-btn-bd:main" style="--btn-bg-hover: var(--color-base-2);"
                            @click.prevent="ppToCancel=pp; openModal('confirmCancelModal')">일정취소</button>
                    <button v-if="pp.statusName !== 'Canceled' && pp.statusName !== 'Finished'"
                            class="n-btn n-btn:hover"
                            @click.prevent="OpenPublishProgramModalHandler(pp.programId)">추가개설</button>
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
              </div>
            </div>

            <section class="applicant-container">
              <!--    예약자 관리           +전체 chat-->
              <header class="n-title">
                <h1 class="" style="font-size: 20px">예약자 관리</h1>
                <div>
                  <button @click.prevent=""
                          class="active n-btn n-btn-pg-filter n-btn:hover" style="--btn-padding: 8px 12px">전체 Chat</button>
                </div>
              </header>

              <section class="applicants">
                <h1 class="d:none">참가자 정보</h1>
                <details class="applicant" v-for="applicant in applicants" :key="applicant.reservationId">
                  <summary class="summary">
                    <div class="applicant-profile">
                      <div class="guest-profile-wrapper">
                        <img v-if="applicant.guestProfileImgSrc!==null" :src="`${config.public.apiBase}${applicant.guestProfileImgSrc}`" alt="게스트 프로필">
                        <img v-else src="/assets/image/default-profile.png" alt="게스트 프로필">
                      </div>
                      <span class="nickname">{{applicant.memberNickname}}</span>
                      <span v-if="applicant.guestConsent===1" class="n-panel-tag">미응답</span>
                      <span v-if="applicant.guestConsent===2" class="n-panel-tag confirmed">참가동의</span>
                    </div>
                    <button class="chat-btn n-btn n-btn:hover n-btn-bg-color:sub">Chat</button>
                    <span class="n-icon n-icon:arrow_up margin-left:auto">펼치기 버튼</span>
                  </summary>
                  <section class="applicant-info">
                    <h1>참가자 정보</h1>
                    <div class="info-row">
                      <span class="label">이름</span>
                      <span class="value">{{ applicant.applicantName }}</span>
                    </div>
                    <div class="info-row">
                      <span class="label">예약인원</span>
                      <span class="value">{{ applicant.numberOfGuest }}</span>
                    </div>
                    <div class="info-row">
                      <span class="label">e-mail</span>
                      <span class="value">{{ applicant.email }}</span>
                    </div>
                    <div class="info-row">
                      <span class="label">전화번호</span>
                      <span class="value">{{ applicant.phone }}</span>
                    </div>
                  </section>
                  <section v-if="applicant.hostRequirement" class="requirement">
                    <h1>요청사항</h1>
                    <p class="hostRequirement">
                      {{ applicant.hostRequirement }}
                    </p>
                    <p class="guestRequirement">
                      {{ (applicant.requirement)? applicant.requirement : `요청사항이 존재하지 않습니다.` }}
                    </p>
                  </section>
                  <div class="footer">
                    <button @click.prevent="applicantToDismiss=applicant; openModal('confirmDismissModal');" class="n-btn n-btn:hover n-btn-bd:none"
                            v-if="!(pp.statusId === 3 || pp.statusId === 4)">
                      추방하기
                    </button>
                  </div>
                </details>
              </section>

            </section>

        </div>
      </div>

    </section>
  </main>
</template>
<style scoped>
.no-click {
  pointer-events: none;
}

.confirmModal {
  ul {
    list-style-type: disc; /* 또는 'circle', 'square', 'none' 등 */
    padding-left: 20px; /* 왼쪽 들여쓰기 */
  }

  ul li {
    margin-bottom: 10px; /* 각 항목 간 간격 */
  }

  ul li strong {
    font-weight: bold;
  }
}

.layout-body {
  .layout-main {
    display: flex;
    margin-bottom: 30px;
    position: relative;

    .layout-main-list {
      flex-grow: 1;
      width: 100%;

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

              @media (min-width: 768px) {
                .morePopup-btn {
                  display: none;
                }
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

            .btn-box {
              display: none;
              gap: 16px;

              .n-btn {
                --btn-padding: 12px 20px;
              }

              .disabled {
                opacity: 0.6;
              }
            }

            @media (min-width: 768px) {
              .btn-box {
                display: flex;
              }
            }
          }

          .card-footer {
            display: flex;
          }
        }
      }

      .applicant-container {
        margin-top: 30px;

        .n-title {
          margin-bottom: 12px;
        }

        .applicants {
          display: flex;
          flex-direction: column;
          gap: 16px;
          padding: 0 20px;

          .applicant {
            padding: 12px 14px;
            border-radius: 10px;
            border: 2px solid var(--color-base-3);
            box-shadow: 0 2px 5px 0.5px var(--color-base-3);


            .summary {
              display: flex;
              align-items: center;
              width: auto;
              padding: 12px 14px;
              /*
              list-style-type: none;
              pointer-events: none;
              */

              .applicant-profile {
                display: flex;
                align-items: center;
                gap: 16px;

                .nickname {
                  font-size: 18px;
                }

                .guest-profile-wrapper {
                  flex-shrink: 0;
                  flex-grow: 0;
                  margin-left: -12px;
                  aspect-ratio: 1 / 1;
                  overflow: hidden;
                  width: 44px;
                  border-radius: 50%;
                  border: 1px solid var(--color-base-2);

                  img {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                    border-radius: var(--border-radius-4);
                  }
                }

                .n-panel-tag.confirmed{
                  --tag-border-color: var(--color-green-1);
                  --tag-bg-color: var(--color-green-1);
                  color: var(--color-base-1);
                }
              }

              .chat-btn {
                margin-left: 20px;
              }
            }

            .applicant-info, .requirement {
              padding: 16px 8px;

              h1 {
                font-size: 16px;
                font-weight: 600;
                margin-bottom: 10px;
              }
            }

            .applicant-info {
              padding-top: 24px;
              margin-top: 8px;
              margin-bottom: 8px;
              border-top: 1.5px solid var(--color-base-3);

              .info-row {
                display: flex;
                justify-content: space-between;
                padding: 10px 0;
                font-size: 14px;
                border-bottom: 1px solid #f0f0f0;

                .label {
                  color: #666;
                }

                .value {
                  color: #333;
                }
              }
            }

            .requirement {
              h1 {
                margin-bottom: 4px;
              }
              .hostRequirement {
                color: var(--color-base-7);
                padding: 8px 0;
                margin-bottom: 8px;
              }
              .guestRequirement {
                padding: 10px 10px;
                border: 1px solid #ccc;
                border-radius: 8px;
                background-color: #f9f9f9;
              }
            }

            .footer {
              margin-top: 6px;
              margin-bottom: 10px;
              display: flex;
              justify-content: center;
              align-items: center;

              button { /* 추방하기 */
                font-size: 16px;
                text-decoration: underline;
                text-underline-offset: 3px; /* 밑줄 간격 */
              }
            }
          }

          .applicant[open] .summary .n-icon\:arrow_up:before {
            --icon: url(/assets/image/icon/arrow_down.svg);
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