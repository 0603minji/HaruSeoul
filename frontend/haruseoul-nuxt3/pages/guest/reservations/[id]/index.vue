<script setup>
import { onMounted, ref } from "vue";
import axios from "axios";
import { useRoute } from "vue-router";

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


// 데이터 함수

const fetchreservation = async (rId) => {
  const params = { rId: rId };
  const response = await axios.get(`http://localhost:8080/api/v1/guest/reservations/${rId}`, { params: params });

  reservation.value = response.data;
  console.log(reservation.value);
  console.log(guest.value);
  console.log(host.value);
  console.log(requirement.value);
  console.log(reservationCard.value);


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

// 생명주기 함수

onMounted(() => {
  const rId = route.params.id;
  if (rId) {
    fetchreservation(rId);  // rId가 있을 때만 데이터를 가져옵니다.
  }
});
</script>

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
                    <span>
                      {{ r.date }}
                      <span v-if="r.dDay <= 3" style="color: #DB4455;">(D-{{ r.dDay }})</span>
                      <span v-else>(D-{{ r.dDay }})</span>
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

          <div v-else-if="r.statusName === 'Canceled'" class="card-footer margin-top:2" style="justify-content: right;">
            <a href="#" class="n-btn bg-color:main-1 color:base-1" style="max-width: 278px;">호스트 문의</a>
            <a href="#" class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
          </div>
        </div>
      </div>

      <!--  본문  -->
      <section class="content">
        <h1>콘텐츠 정보</h1>

        <section v-if="guest">
          <h1>참가자 정보</h1>
          <div>
            <details open>
              <summary class="collapse">
                <span class="title">참가자 정보</span>
                <span class="n-icon n-icon:arrow_down">펼치기 버튼</span>
              </summary>
              <div class="details">
                <div class="info">
                  <span class="info-form">이름</span>
                  <span class="info-input">{{ guest.memberName }}</span>
                </div>
                <div class="info">
                  <span class="info-form">이메일</span>
                  <span class="info-input">{{ guest.memberEmail }}</span>
                </div>
                <div class="info">
                  <span class="info-form">휴대폰 번호</span>
                  <span class="info-input">+82 01012341234</span>
                </div>
                <div class="info">
                  <span class="info-form">SNS</span>
                  <span class="info-input n-icon n-icon:instagram">@instagram</span>
                </div>
              </div>
            </details>
          </div>
        </section>

        <section v-if="requirement">
          <h1>요청 정보</h1>
          <div>
            <details open>
              <summary class="collapse" style="padding-bottom: 0;">
                <span class="title">요청 정보</span>
                <span class="n-icon n-icon:arrow_down">펼치기 버튼</span>
              </summary>
              <div class="details">
                <div class="info" style="align-items: center;">
                  <span class="info-form" style="width: max-content; color: var(--color-main-3);">호스트 요청 사항</span>
                  <span class="info-input" style="max-width: 500px; color: var(--color-main-3)">{{
                    requirement.hostRequirement }}</span>
                </div>
                <div class="info" style="align-items: center;">
                  <span class="info-form">게스트 요청 사항</span>
                  <span class="info-input" style="max-width: 500px;">{{
                    requirement.guestRequirement }}</span>
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

        <section v-if="host">
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
                  <div class="img-wrapper">
                    <img src="/public/image/profile.png" alt="호스트프사" />
                  </div>
                  <div>
                    <div>{{ host.memberName }}</div>
                    <div class="n-icon n-icon:star n-deco" style="--deco-gap: 3px">
                      <span>{{ host.programRating }} / 5.0</span>
                      <span>({{ host.ratingCount }})</span>
                    </div>
                  </div>
                </div>
                <a href="" class="n-btn n-btn:hover">프로필보기</a>
              </section>
            </details>
          </div>
        </section>

        <section class="program" v-if="program">
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
                            <p>{{ program.programStartTime }} {{ program.meetingSpotTitle }}</p>
                            <div style="
                                display: flex;
                                align-items: center;
                                padding: var(--gap-3) 0;
                                color: var(--color-base-7);
                              ">
                              <span class="n-icon n-icon:placeholder" style="margin-right: 0">위치아이콘</span>
                              <span style="margin-right: var(--gap-1)" id="copyText">
                                {{ program.meetingSpotAddress }}
                              </span>
                              <button @click="copy" class="copy-btn">주소복사</button>
                            </div>
                          </div>
                          <div class="map-img-wrapper">
                            <img class="map-img" src="/public/image/map.png" alt="map" />
                            <!--             나중에 이미지 데이터 바인딩               -->
                          </div>
                        </section>
                      </div>
                    </div>

                    <div id="inclusions" class="id-container">
                      <div class="content-header">
                        <span class="title">포함사항</span>
                      </div>
                      <div class="details">
                        <section>
                          <h1>포함사항</h1>
                          <div class="list-container">
                            <ul style="padding-left: 0">
                              <li class="info-input n-icon n-icon:success-circle-green">
                                {{ program.programInclusion }}
                              </li>
                              <li class="info-input n-icon n-icon:success-circle-green">
                                {{ program.programInclusion }}
                              </li>
                            </ul>
                          </div>
                          <div class="list-container">
                            <ul style="padding-left: 0">
                              <li class="info-input n-icon n-icon:error">
                                {{ program.programExclusion }}
                              </li>
                              <li class="info-input n-icon n-icon:error">
                                {{ program.programExclusion }}
                              </li>
                              <li class="info-input n-icon n-icon:error">
                                {{ program.programExclusion }}
                              </li>
                            </ul>
                          </div>
                        </section>
                      </div>
                    </div>

                    <div id="things-to-know" class="id-container">
                      <div class="content-header">
                        <span class="title">꼭 알아두세요!</span>
                      </div>
                      <div class="details">
                        <section>
                          <h1>꼭 알아두세요!</h1>

                          <div>
                            <h2 class="info-form n-icon n-icon:success-decagon">
                              준비물
                            </h2>
                            <ul>
                              <li class="list-content">
                                {{ program.programPackingList }}
                              </li>
                              <li class="list-content">
                                {{ program.programPackingList }}
                              </li>
                              <li class="list-content">
                                {{ program.programPackingList }}
                              </li>
                            </ul>
                          </div>

                          <div>
                            <h2 class="info-form n-icon n-icon:caution">
                              주의사항
                            </h2>
                            <ul>
                              <li class="list-content">
                                {{ program.programCaution }}
                              </li>
                              <li class="list-content">
                                {{ program.programCaution }}
                              </li>
                              <li class="list-content">
                                {{ program.programCaution }}
                              </li>
                              <li class="list-content">
                                {{ program.programCaution }}
                              </li>
                              <li class="list-content">
                                {{ program.programCaution }}
                              </li>
                              <li class="list-content">
                                {{ program.programCaution }}
                              </li>
                              <li class="list-content">
                                {{ program.programCaution }}
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

        <!--  이용완료, 취소됨 상태만 예약 삭제할 수 있도록 설정     -->
        <div v-if="['Finished', 'Canceled'].includes(reservationCard.statusName)" style="
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

    >section>div {
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