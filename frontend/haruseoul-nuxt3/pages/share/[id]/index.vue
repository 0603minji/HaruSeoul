<script setup>
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {useReservationFetch} from "~/composables/useReservationFetch.js";
import CancelReservationModal from "~/components/modal/CancelReservationModal.vue";
import useShare from '~/composables/useShare.js';

definePageMeta({
  layout: false
})

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
const config = useRuntimeConfig();

const {shareToKakao} = useShare();  // useShare 모듈에서 shareToKakao 함수 가져오기

// 버튼 클릭 시 공유 함수 호출
const handleShare = (url) => {
  shareToKakao(url);  // 여기서 원하는 URL을 넣어 공유
};

// 데이터 함수

const fetchreservation = async (rId) => {
  const response = await useReservationFetch(`share/${rId}`,
  );

  reservation.value = response;


  // 날짜 차이를 계산하여 dDay 속성을 추가합니다.
  const currentDate = new Date();
  if (reservationCard.value.date && reservationCard.value) {
    const reservationDate = new Date(reservationCard.value.date);
    reservationCard.value.dDay = Math.floor((reservationDate - currentDate) / (1000 * 60 * 60 * 24));
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
  const year = date.getFullYear();
  const month = date.getMonth();
  // 월이 한 자리일 경우 앞에 0을 추가
  const formattedMonth = String(month).padStart(2, '0');
  const day = date.getDay();
  // 날짜가 한 자리일 경우 앞에 0을 추가
  const formattedDay = String(day).padStart(2, '0');
  const hours = date.getHours();  // 시간
  const minutes = date.getMinutes();  // 분
  return `${year}-${formattedMonth}-${formattedDay} ${hours}:${minutes < 10 ? '0' + minutes : minutes}`;  // 분이 한 자리일 경우 앞에 0을 붙여서 반환
};


// 예약취소--------------------------------------------------------------------------------

const showModal = ref(false); // 모달을 표시할지 여부를 결정하는 변수
const currentReservationId = ref(null); // 취소할 예약의 ID 저장

// 예약 취소 버튼 클릭 핸들러
const handleCancelClick = (reservationId) => {
  console.log("취소 클릭 ID:", reservationId); // ID가 제대로 넘어오는지 확인
  currentReservationId.value = reservationId;  // 클릭한 예약의 ID 설정
  showModal.value = true;  // 모달을 표시
};

// 모달을 열 때
const openModal = () => {
  showModal.value = true;
};

// 모달을 닫을 때
const closeModal = () => {
  showModal.value = false;
};


// 생명주기 함수

onMounted(async () => {
  const rId = route.params.id;
  if (rId) {
    await  fetchreservation(rId);  // rId가 있을 때만 데이터를 가져옵니다.
  }

  const initMaps = () => {
    if (window.kakao && window.kakao.maps) {
      kakao.maps.load(() => {
        loadMap();
      });
    }
  };

  const loadMap = () => {
    const container = document.getElementById('map');
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

  // reservation의 program이 변경될 때 loadMap 실행
  watch(() => program.value.meetingSpotAddress, (newAddress) => {
    if (newAddress) {
      loadMap();
    }
  });
});


</script>

<template>
  <main>
    <section class="reservation-detail" style="margin: 30px 0;">
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

      <NuxtLink :to="`../../programs/${reservation.program.programId}`" class="n-card-container bg-color:base-1" v-for="r in reservation" :key="r.id">
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
            <div v-if="r.src && r.src.startsWith('uploads')" class="img-wrapper">
              <img :src="`http://localhost:8083/api/v1/${r.src}`" alt="대표사진"/>
            </div>
            <div v-else class="img-wrapper">
              <img src="../../../assets/image/default-program-image.png" alt="대표사진"/>
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

              </div>
            </div>
          </div>
        </div>
      </NuxtLink>

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
                          <div class="map-img-wrapper" id="map" style="width: 100%; height: 400px;"></div>
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
        <ReservationCancelModal
            v-if="showModal"
            :showModal="showModal"
            :selectedReservationId="currentReservationId"
            @close="closeModal"
        />
      </section>
    </section>
  </main>
</template>

<style scoped>
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
</style>