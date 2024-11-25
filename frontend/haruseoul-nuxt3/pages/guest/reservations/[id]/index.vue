<template>
  <main>
    <section class="reservation-detail">
      <h1 class="d:none">guest-reservation-detail 페이지</h1>

      <div style="
          width: 100%;
          padding: var(--gap-1) var(--gap-6);
          font-size: var(--font-size-8);
          font-weight: bold;">
        예약 상세보기
      </div>
      <div style="width: 100%; padding: var(--gap-3) var(--gap-6)">
        예약번호 {{ reservation.reservationId }}
      </div>

      <div class="n-card-container bg-color:base-1" v-for="r in reservation" :key="r.id">
        <div class="n-card bg-color:base-1 padding:6" v-if="r.id">
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
            > [취소일자: {{ formatDeleteDate(reservationCard.deleteDate) }}]</span>
          </div>

          <div class="card-main">
            <NuxtLink :to="`../../programs/${reservation.program.programId}`"
                      v-if="r.src && r.src.startsWith('uploads')" class="img-wrapper">
              <img :src="`http://localhost:8083/api/v1/${r.src}`" alt="대표사진"/>
            </NuxtLink>
            <div v-else class="img-wrapper">
              <img src="assets/image/default-program-image.png" alt="대표사진"/>
            </div>

            <div class="card-info-wrapper">
              <div class="card-header-responsive">
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

                  <span v-else-if="r.reservationStatus === 3"
                        class="n-panel-tag not-submitted"
                        style="border-color: #DB4455; color: #DB4455;">
                    취소됨
                  </span>
                  <span v-if="r.reservationStatus === 3"
                        style="font-weight: bold; padding-left: 2px;"
                  > [취소일자: {{ formatDeleteDate(reservationCard.deleteDate) }}]</span>
                </div>
              </div>
              <NuxtLink :to="`../../programs/${reservation.program.programId}`" class="title">
                {{ r.programTitle }}
              </NuxtLink>
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
                  <a v-if="guestConsent !==1" href="#" class="n-btn"
                     @click="modalOpenHandler(r.id, `cancelReservationModal`, reservation.program.programId);"
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
            <a v-if="guestConsent !==1" href="#" class="n-btn"
               @click="modalOpenHandler(r.id, `cancelReservationModal`, reservation.program.programId)"
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

        </div>
      </div>

      <!--   todo   -->

      <section v-if="guestConsent===1" class="guest-consent">
        <header>
          <img src="/assets/image/icon/warning.png" alt="warning icon">
          <h1>프로그램 진행 안내</h1>
        </header>
        <p>해당 프로그램은 예약 인원이 최소 기준에 미달되더라도 예정대로 진행될 예정입니다.</p>
        <p>동의 여부를 선택해 주세요.</p>
        <ul>
          <li>참가 동의: 프로그램에 참여합니다.</li>
          <li>예약취소: 예약을 취소하고 전액 환불받습니다.</li>
          <li class="notice">프로그램 진행일 전날까지 미응답 시 자동으로 예약취소처리됩니다.</li>
        </ul>
        <div>
          <button @click.prevent="openModal('acceptReservationModal')">참가동의</button>
          <button @click.prevent="modalOpenHandler(reservationCard.id, `cancelReservationModal2`)">예약취소</button>
        </div>
      </section>

      <section v-if="guestConsent===2" class="guest-consent-confirmed">
        <img src="/assets/image/icon/success.png" alt="success icon"/>
        <p>예약 인원이 최소 기준에 미달되더라도 프로그램에 참가하는 것에 동의하셨습니다.</p>
      </section>

      <section v-if="guestConsent===3" class="guest-consent-rejected">
        <img src="/assets/image/icon/error2.png" alt="error2 icon"/>
        <p>예약인원이 최소 기준에 미달된 채로 프로그램을 진행하는 것에 동의하지 않아 예약이 취소되었습니다.</p>
      </section>

      <!--  본문  -->
      <section class="content">
        <h1>콘텐츠 정보</h1>

        <section v-if="guest && Object.keys(guest).length > 0">
          <h1>참가자 정보</h1>
          <div>
            <details open>
              <summary class="collapse">
                <span class="title">참가자 정보</span>
                <span class="n-icon n-icon:arrow_down">펼치기 버튼</span>
              </summary>
              <div class="details">
                <div class="info" v-if="guest.memberName">
                  <span class="info-form">이름</span>
                  <span class="info-input">{{ guest.memberName }}</span>
                </div>
                <div class="info" v-if="guest.memberEmail">
                  <span class="info-form">이메일</span>
                  <span class="info-input">{{ guest.memberEmail }}</span>
                </div>
                <div class="info">
                  <span class="info-form">휴대폰 번호</span>
                  <span class="info-input">(+82)10-5678-1234</span>
                </div>
                <div class="info">
                  <span class="info-form">SNS</span>
                  <span class="info-input n-icon n-icon:instagram">@idididid</span>
                </div>
              </div>
            </details>
          </div>
        </section>

        <section v-if="host && Object.keys(host).length > 0">
          <h1>호스트정보</h1>
          <div>
            <details open>
              <summary class="collapse">
                <span class="title">호스트 정보</span>
                <span class="n-icon n-icon:arrow_down">펼치기 버튼</span>
              </summary>
              <!-- 프로필 카드 -->
              <section class="profile-card">
                <h1>프로필 카드</h1>
                <div class="overview">
                  <div v-if="host.memberImg && host.memberImg.startsWith('uploads')" class="img-wrapper">
                    <img :src="`http://localhost:8083/api/v1/${host.memberImg}`" alt="대표사진"/>
                  </div>
                  <div v-else class="img-wrapper">
                    <img src="/assets/image/default-profile.png" alt="호스트프사">
                  </div>
                  <div>
                    <div v-if="host.memberName">{{ host.memberName }}</div>
                    <div class="n-icon n-icon:star n-deco" style="--deco-gap: 3px">
                      <span v-if="host.programRating">{{ host.programRating }} / 5.0</span>
                      <span v-if="host.ratingCount">({{ host.ratingCount }})</span>
                    </div>
                  </div>
                </div>
                <a href="" class="n-btn n-btn:hover">프로필보기</a>
              </section>
            </details>
          </div>
        </section>

        <section class="program" v-if="program && Object.keys(program).length > 0">
          <h1>프로그램 정보 및 정책</h1>
          <div>
            <details open>
              <summary class="collapse">
                <span class="title">프로그램 정보 및 정책</span>
                <span class="n-icon n-icon:arrow_down">펼치기 버튼</span>
              </summary>
              <div class="details">
                <!--  만나는 장소  -->
                <section id="meeting-location">
                  <h1>프로그램 정보 및 정책</h1>
                  <div class="background-color:base-1">
                    <div class="id-container">
                      <div class="content-header">
                        <span class="title">만나는 장소</span>
                      </div>
                      <div class="details">
                        <section style="padding-top: 0">
                          <h1>만나는장소</h1>
                          <div class="info-container">
                            <p v-if="program.programStartTime || program.meetingSpotTitle">
                              {{ program.programStartTime }} {{ program.meetingSpotTitle }}
                            </p>
                            <div style="
                                display: flex;
                                align-items: center;
                                padding: var(--gap-3) 0;
                                color: var(--color-base-7);
                              ">
                              <span class="n-icon n-icon:placeholder" style="margin-right: 0">위치아이콘</span>
                              <span v-if="program.meetingSpotAddress" style="margin-right: var(--gap-1)" id="copyText">
                                {{ program.meetingSpotAddress }}
                              </span>
                              <button @click="copy" class="copy-btn">주소복사</button>
                            </div>
                          </div>
                          <div class="map-img-wrapper" id="map2" style="width: 100%; height: 400px;"></div>
                        </section>
                      </div>
                    </div>

                    <div v-if="program.programInclusion || program.programExclusion" id="inclusions"
                         class="id-container">
                      <div class="content-header" style="padding-top: 10px;">
                        <span class="title">포함사항</span>
                      </div>
                      <div class="details">
                        <section>
                          <h1>포함사항</h1>
                          <div v-if="program.programInclusion" class="list-container">
                            <ul style="padding-left: 0">
                              <li v-for="(item, index) in program.programInclusion.split('\n')" :key="index"
                                  class="info-input n-icon n-icon:success-circle-green">
                                {{ item }}
                              </li>
                            </ul>
                          </div>
                          <div v-if="program.programExclusion" class="list-container">
                            <ul style="padding-left: 0">
                              <li v-for="(item, index) in program.programExclusion.split('\n')" :key="index"
                                  class="info-input n-icon n-icon:error">
                                {{ item }}
                              </li>
                            </ul>
                          </div>
                        </section>
                      </div>
                    </div>

                    <div class="id-container">
                      <div class="content-header">
                        <span class="title">꼭 알아두세요!</span>
                      </div>
                      <div class="details">
                        <section>
                          <h1>꼭 알아두세요!</h1>

                          <div v-if="program.programPackingList">
                            <h2 class="info-form n-icon n-icon:success-decagon">
                              준비물
                            </h2>
                            <ul>
                              <li v-for="(item, index) in program.programPackingList.split('\n')"
                                  :key="index" class="list-content">
                                {{ item }}
                              </li>
                            </ul>
                          </div>

                          <div v-if="program.programCaution">
                            <h2 class="info-form n-icon n-icon:caution">
                              주의사항
                            </h2>
                            <ul>
                              <li v-for="(item, index) in program.programCaution.split('\n')"
                                  :key="index" class="list-content">
                                {{ item }}
                              </li>
                            </ul>
                          </div>

                          <div>
                            <h2 class="info-form n-icon n-icon:reset">
                              취소 및 환불정책
                            </h2>
                            <ul>
                              <li class="list-content">
                                여행일 기준 24시간 전까지 통보 시: 여행 요금
                                100% 전액 환불됩니다.
                              </li>
                              <li class="list-content">
                                여행일 기준 24시간 이내 통보 시: 취소/환불
                                불가합니다.
                              </li>
                              <li class="list-content">
                                취소 요청 날짜와 시간은 여행 상품이 진행되는
                                현지 시간을 따릅니다.
                              </li>
                              <li class="list-content">
                                여행일은 여행 상품이 진행되는 현지 날짜의 오전
                                9시를 기준으로 합니다.
                              </li>
                            </ul>
                          </div>
                        </section>
                      </div>
                    </div>
                  </div>
                </section>
              </div>
            </details>
          </div>
        </section>

        <section v-if="requirement && Object.keys(requirement).length > 2">
          <h1>요청사항 정보</h1>
          <div>
            <details open>
              <summary class="collapse">
                <span class="title">요청 정보</span>
                <span class="n-icon n-icon:arrow_down">펼치기 버튼</span>
              </summary>
              <div class="details" style="margin-top: 0">
                <div v-if="requirement.hostRequirement" class="info" style="flex-direction: column; padding: 0 14px;">
                  <span>
                    <span class="info-form n-icon n-icon:request"
                          style="width: max-content;
                          font-size: 15px;
                          transform: scaleX(-1);
                          --icon-size: var(--icon-size-5);
                    "></span>
                    <span>Host</span>
                  </span>
                  <span class="info-input"
                        style="
                        border: 1px solid black;
                        padding: 10px;
                        border-radius: 4px;">{{
                      requirement.hostRequirement
                    }}</span>
                </div>
                <div v-if="requirement.guestRequirement" class="info" style="flex-direction: column; padding: 0 14px;">
                  <span class="info-form n-icon n-icon:request n-deco-pos:right"
                        style="
                        width: 100%;
                        font-size: 15px;
                        --icon-size: var(--icon-size-5);
                        justify-content: end;"
                  >Guest</span>
                  <span class="info-input"
                        style="
                        border: 1px solid black;
                        padding: 10px;
                        border-radius: 4px;">{{
                      requirement.guestRequirement
                    }}</span>
                </div>
              </div>
            </details>
          </div>
        </section>

        <section>
          <h1>결제정보</h1>
          <div>
            <details open>
              <summary class="collapse">
                <span class="title">결제 정보</span>
                <span class="n-icon n-icon:arrow_down">펼치기 버튼</span>
              </summary>
              <div class="details">
                <div class="info">
                  <span class="info-form">결제일</span>
                  <span class="info-input">2024.9.10 19:24GMT</span>
                </div>
                <div class="info">
                  <span class="info-form">결제수단</span>
                  <div style="padding-right: 0">
                    <span class="info-input" style="justify-content: right">Credit card</span>
                    <span class="info-input n-icon n-icon:credit_card">1234 1245 1234 1234</span>
                  </div>
                </div>
                <div class="info" style="margin-bottom: 13px; align-items: center">
                  <span class="info-form">결제금액</span>
                  <span class="info-input">30,000 (KRW)</span>
                </div>
                <div style="display: flex; justify-content: end">
                  <a class="n-btn n-btn:hover" href="#">영수증 보기</a>
                </div>
              </div>
            </details>
          </div>
        </section>

        <!--  이용완료, 취소됨 상태만 예약 삭제할 수 있도록 설정     -->
        <div v-if="[3,4].includes(reservationCard.reservationStatus)" style="
            display: flex;
            justify-content: center;
            padding: var(--gap-8) 0;
            margin: 0 var(--gap-6);
          ">
          <a href="#" class="n-btn n-btn:hover" style="
              width: var(--width-9p);
              height: var(--height-percent-4);
              --btn-font-color: var(--color-red-1);
              --btn-border-color: var(--color-base-5);
            ">예약 내역 삭제</a>
        </div>

        <!-- 예약 취소 모달 -->
        <CancelReservationModal
            v-if="showModal"
            :showModal="showModal"
            :selectedReservationId="currentReservationId"
            :fetchReservation="fetchReservation"
            :publishedProgramId="program.publishedProgramId"
            :reservationId="reservationCard.id"
            :programId="program.programId"
            @close="closeModal"
        />

        <CancelReservationModal2
            v-if="showModal"
            :showModal="showModal"
            :selectedReservationId="currentReservationId"
            :fetchReservation="fetchReservation"
            :reservation="reservation"
            :programId="program.programId"
            @close="closeModal"
            @update-guest-consent="updateGuestConsent"
        />

        <div v-show="showModal ===`acceptReservationModal`" class="modal">
          <div class="modal-content" style="display: flex; flex-direction: column; align-items: center">
            <p style="font-size: 15px; font-weight: bold">참가에 동의하시겠습니까?</p>
            <p>(예약이 확정됩니다.)</p>
            <div
                style="width: 180px; padding-left: 20px; padding-top: 15px; display: flex; justify-content: space-between">
              <button class="n-btn n-btn:hover" style="color:#DB4455" @click.prevent="confirmParticipationHandler">확인
              </button>
              <button class="n-btn n-btn:hover" @click="closeModal">닫기</button>
            </div>
          </div>
        </div>
      </section>
    </section>
  </main>
</template>

<script setup>
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {useReservationFetch} from "~/composables/useReservationFetch.js";
import useShare from '~/composables/useShare';
import CancelReservationModal from "~/components/modal/CancelReservationModal.vue";
import CancelReservationModal2 from "~/components/modal/CancelReservationModal2.vue";

const reservation = ref({
  guest: {},
  host: {},
  program: {},
  requirement: {},
  reservationCard: {}
});

const route = useRoute();

const guest = computed(() => reservation.value.guest);
const host = computed(() => reservation.value.host);
const program = computed(() => reservation.value.program);
const requirement = computed(() => reservation.value.requirement);
const reservationCard = computed(() => reservation.value.reservationCard);
const guestConsent = ref(null); // 호스트가 풀방아닌 상태에서 예약확정하려고 했을 때 게스트 동의여부를 저장
const config = useRuntimeConfig();

const {shareToKakao} = useShare();  // useShare 모듈에서 shareToKakao 함수 가져오기

// 버튼 클릭 시 공유 함수 호출
const handleShare = (url) => {
  shareToKakao(url);  // 여기서 원하는 URL을 넣어 공유
};

// 데이터 함수

const fetchReservation = async (rId) => {
  const token = localStorage.getItem("token");
  const response = await useReservationFetch(`guest/reservations/${rId}`,
      {
        headers: {
          Authorization: `Bearer ${token.value}`,
        }
      }
  );

  reservation.value = response;
  console.log('fetchreservation called. reservation: ', reservation.value);
  guestConsent.value = reservation.value.reservationCard.guestConsent;

  // 날짜 차이를 계산하여 dDay 속성을 추가합니다.
  const currentDate = new Date();
  if (reservationCard.value.date && reservationCard.value) {
    const reservationDate = new Date(reservationCard.value.date + `T00:00:00+09:00`);
    reservationCard.value.dDay = Math.ceil((reservationDate - currentDate) / (1000 * 60 * 60 * 24));
  } else {
    reservationCard.value.dDay = null;
  }
};

function copy() {
  const copyText = document.getElementById("copyText");
  const textToCopy = copyText.innerText; // 텍스트를 가져옴

  // clipboard (API) 에 textToCopy 에 담긴 텍스트를 이동시키고, 알람 띄움
  navigator.clipboard.writeText(textToCopy).then(() => {
    alert("주소가 복사되었습니다.");
  }).catch((err) => {
    console.error("복사 실패:", err);
  });
}

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


// 예약취소--------------------------------------------------------------------------------

const showModal = ref(null); // 모달을 표시할지 여부를 결정하는 변수
const currentReservationId = ref(null); // 취소할 예약의 ID 저장
const selectedProgramId = ref(null); // 취소할 예약의 ID 저장

// 예약 취소 버튼 클릭 핸들러
const modalOpenHandler = (reservationId, modalName, programId) => {
  console.log("취소 클릭 ID:", reservationId); // ID가 제대로 넘어오는지 확인
  currentReservationId.value = reservationId;  // 클릭한 예약의 ID 설정
  selectedProgramId.value = programId;
  showModal.value = modalName;  // 모달을 표시
};

const openModal = (modalName) => {
  showModal.value = modalName;
}

// 모달을 닫을 때
const closeModal = () => {
  showModal.value = "";
};

const updateGuestConsent = (consent) => {
  guestConsent.value = consent;
  showModal.value = "";
  fetchReservation(route.params.id)
}

// 생명주기 함수

onMounted(async () => {
  const rId = route.params.id;
  if (rId) {
    await fetchReservation(rId);  // rId가 있을 때만 데이터를 가져옵니다.
  }

  const initMaps = () => {
    if (window.kakao && window.kakao.maps) {
      kakao.maps.load(() => {

        loadMap2();
      });
    }
  };

  const loadMap2 = () => {
    const container = document.getElementById('map2');
    if (!container) {
      console.warn("Map container not found. Skipping map initialization.");
      return;
    }

    const options = {
      center: new kakao.maps.LatLng(33.450701, 126.570667),
      level: 3
    };
    const map = new kakao.maps.Map(container, options);

    if (program.value.meetingSpotAddress) {
      const geocoder = new kakao.maps.services.Geocoder();

      geocoder.addressSearch(program.value.meetingSpotAddress, (result, status) => {
        if (status === kakao.maps.services.Status.OK) {
          const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
          map.setCenter(coords);

          new kakao.maps.Marker({
            map: map,
            position: coords
          });
        } else {
          console.error('주소를 찾을 수 없습니다.');
        }
      });
    } else {
      console.log("주소가 없어 기본 좌표를 표시합니다.");
    }
  };

  if (!window.kakao) {
    const script = document.createElement("script");
    script.src = "//dapi.kakao.com/v2/maps/sdk.js?appkey=9fcf4fe8de11c60a0d6702af06ca856e&autoload=false&libraries=services";
    script.onload = initMaps;
    document.head.appendChild(script);
  } else {
    initMaps();
  }

});


const confirmParticipationHandler = async () => {
  console.log('          confirmParticipationHandler called.');
  const guestConsentResponse = await useDataFetch(`host/reservations/consent`, {
    method: "PUT",
    headers: {
      "Content-type": "application/json"
    },
    body: {
      "id": reservationCard.value.id,
      "guestConsent": 2,// 참가동의로 초기화
      "reservationStatus": 2
    }
  });
  console.log('          Reservation Update result: ', guestConsentResponse);
  guestConsent.value = 2;
  closeModal();
  await fetchReservation(reservationCard.value.id);
}
</script>

<style scoped>
.guest-consent {
  max-width: 600px; /* 콘텐츠 폭 제한 */
  margin: 20px auto; /* 가운데 정렬 */
  padding: 20px; /* 내부 여백 */
  border-radius: 12px; /* 둥근 모서리 */
  background-color: #FFF4E4; /* 연한 배경색 */
  /*
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); !* 약간의 그림자 *!
  */
  width: calc(100% - 32px); /* 최소 좌우 여백 16px 확보 */

  /* 헤더 스타일 */

  header {
    width: 100%;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    margin-bottom: 15px;

    h1 {
      font-size: 18px;
      font-weight: 600;
      color: #333;
      text-align: center; /* 가운데 정렬 */
    }
  }

  /* 본문 텍스트 */

  p {
    font-size: 14px;
    color: #555;
    line-height: 1.5;
    margin: 4px 0;
  }

  .notice {
    font-size: 12px;
    color: #888;
    list-style: none;
  }

  /* 리스트 스타일 */

  ul {
    list-style-type: disc;
    margin: 20px 0 24px 0; /* 안쪽 여백 추가 */
    padding-left: 20px; /* 목록 들여쓰기 */

    li {
      margin-bottom: 8px; /* 항목 간 간격 */
      color: #555;
    }
  }

  /* 버튼 컨테이너 */

  div {
    display: flex;
    justify-content: space-between; /* 버튼을 양쪽에 배치 */
    gap: 16px; /* 버튼 간 간격 */
    margin-top: 20px;

    /* 버튼 스타일 */

    button {
      flex: 1; /* 버튼 크기 균등화 */
      padding: 10px 15px;
      font-size: 1rem;
      border: 1px solid transparent; /* 기본 투명한 테두리 */
      border-radius: 10px;
      cursor: pointer;
      transition: background-color 0.3s, color 0.3s; /* 전환 효과 */
    }

    button:first-child {
      background-color: #fff;
      color: var(--color-sub-1);
      border: 1px solid var(--color-sub-1);
    }

    button:first-child:hover {
      color: #fff; /* 텍스트 흰색 */
      background-color: var(--color-sub-1); /* 호버 시 더 어두운 색상 */
    }

    button:last-child {
      background-color: #fff; /* 예약 취소 버튼 색 */
      color: #dc3545; /* 빨간 텍스트 */
      border: 1px solid #dc3545; /* 빨간 테두리 */
    }

    button:last-child:hover {
      background-color: var(--color-red-1); /* 호버 시 빨간 배경 */
      color: #fff; /* 텍스트 흰색 */
    }
  }
}

.guest-consent-confirmed, .guest-consent-rejected {
  display: flex;
  align-items: center;
  gap: 20px;
  max-width: 600px; /* 콘텐츠 폭 제한 */
  margin: 20px auto; /* 가운데 정렬 */
  padding: 20px; /* 내부 여백 */
  border-radius: 12px; /* 둥근 모서리 */
  background-color: #f9f9f9; /* 연한 배경색 */
  width: calc(100% - 32px); /* 최소 좌우 여백 16px 확보 */

  p {
    line-height: 1.5;
    margin: 4px 0;
  }
}

.guest-consent-confirmed {
  background-color: var(--color-green-3);
}

.guest-consent-rejected {
  background-color: var(--color-red-3);
}

/* n-icon */

.n-icon {
  --icon-size: 20px;
  color: var(--color-base-9);
}

.n-icon:before {
  width: var(--icon-size);
  background-color: var(--icon-color);
  margin-right: var(--gap-1);
}

.n-icon\:star:before {
  --icon-size: 14px;
  background-color: var(--color-yellow-1);
}

.n-icon\:credit_card:before {
  --icon-size: var(--icon-size-4);
  width: var(--icon-size);
}

.n-icon\:instagram:before {
  --icon-size: var(--icon-size-4);
  width: var(--icon-size);
}

.n-icon\:success-circle-green:before {
  background-color: var(--color-green-1);
}

.n-icon\:error:before {
  background-color: var(--color-red-1);
}

.n-icon\:placeholder:before {
  --icon-size: 16px;
  background-size: var(--icon-size);
}

.n-icon\:arrow_down:before {
  --icon-size: var(--icon-size-4);
}

/* --- */

/* btn */

.copy-btn {
  color: var(--color-sub-1);
  /* 글씨 색을 파란색으로 설정 */
  background-color: transparent;
  /* 배경색 투명 */
  cursor: pointer;
  /* 커서가 손 모양으로 변경 */
  font-size: var(--font-size-1) px;
  /* 글자 크기 설정 */
}

.copy-btn:hover {
  font-weight: bolder;
}

/* --- */
.n-card {
  margin: 0 var(--gap-6);

  .card-main {
    .img-wrapper {
      width: 94px;
      min-width: 94px;
    }
  }
}

/*    */

.reservation-detail {
  font-size: 14px;

  .n-panel-tag {
    --tag-border-radius: 14px;
    --tag-padding: 3px 6px;
    font-weight: 600;
  }

  .card-header-responsive {
    display: none;
  }

  .card-footer-responsive {
    display: none;
  }

  .content {

    body {
      color: var(--color-base-9);
      display: block;
      align-items: center;
      align-content: center;
      padding: var(--gap-6);
    }

    h1 {
      display: none;
    }

    details {
      border-bottom: var(--border-width-2) solid var(--color-base-3);
    }

    > section > div {
      padding: 0 var(--gap-6);
    }
  }


  .program {
    .title {
      color: var(--color-base-9);
    }

    section {
      .title {
        color: var(--color-base-9);
        font-size: var(--font-size-7);
        font-weight: bold;
        padding: var(--gap-2);
      }

      .info-container {
        display: block;
        color: var(--color-base-9);
        padding: 0;
      }

      .list-container {
        display: block;
        color: var(--color-base-9);

        li {
          margin-bottom: var(--gap-3);
        }
      }

      ul {
        list-style: none;
        /* 기본 점을 없앰 */
        padding: var(--gap-3);
        padding-top: 0;
      }

      .list-content {
        width: auto;
      }

      .list-content::before {
        content: "•";
        /* 기본 점 없애고 커스텀 점 */
        display: inline-block;
        width: 1em;
        /* 점과 텍스트 사이의 간격 */
        padding-left: var(--gap-3);
        margin-left: -1.5em;
        /* 점을 왼쪽으로 이동 */
      }
    }


    h2 {
      font-size: var(--font-size-7);
      font-weight: bold;
      width: auto;
      height: 30px;
    }

    li {
      /*padding: var(--gap-4); --pc버전*/
      padding: var(--gap-2) var(--gap-1);
    }

    .map-img-wrapper {
      display: flex;
      position: relative;
      flex-direction: row;
      justify-content: center;
      align-items: center;
      gap: 16px;
      flex-shrink: 0;
      overflow: hidden;
      min-width: 85px;
      aspect-ratio: 2 / 1;

      .map-img {
        width: 100%;
      }
    }
  }

  .info {
    display: flex;
    margin-bottom: var(--gap-6);
    justify-content: space-between;
  }

  .info-input {
    display: flex;
    align-items: center;
    width: auto;
    justify-content: left;
  }
}

.modal {
  background-color: rgba(0, 0, 0, 0.5);
}


/* --- */

@media (min-width: 768px) {
  main {
    display: flex;
    align-items: center;
    flex-direction: column;

    .reservation-detail {
      max-width: 1092px;
      width: 100%;
      padding: 0 16px;
    }

    .content {
      width: 100%;
      min-width: 300px;
      max-width: 1096px;

      .n-btn {
        --btn-padding: 10px 14px;
      }

      .details {
        margin: var(--gap-4) var(--gap-6);
      }

    }

  }

  /*list.css 카드 부분 반응형 추가*/
  .n-card {
    .card-header {
      display: none;
    }

    .card-info {
      width: max-content;
    }

    .card-header-responsive {
      display: block;
      padding: 10px 0;
    }

    .card-main {
      .img-wrapper {
        width: 110px;
        min-width: 110px;
      }
    }

    .n-panel-tag {
      --tag-border-radius: 14px;
      --tag-padding: 3px 6px;
      font-weight: 600;
    }

    .card-main {
      .card-info-wrapper {
        .card-header-responsive {
          display: block;

          .n-panel-tag {
            --tag-padding: 3px 6px;
          }
        }
      }

      .card-info-responsive {
        display: flex;
        justify-content: space-between;
        align-items: center;


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
      }
    }

    .card-footer {
      display: none;
    }
  }
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.2);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 3;
}

.modal-content {
  background-color: white;
  padding: 35px;
  border-radius: 10px;
}
</style>