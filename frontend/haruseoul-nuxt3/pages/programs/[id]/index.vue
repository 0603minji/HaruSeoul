<template>
  <main>
    <section class="layout-body">
      <h1 class="d:none">프로그램과 어사이드</h1>
      <section class="layout-main">
        <section v-if="data" class="program-detail">
          <h1 class="d:none">program-detail 페이지</h1>
          <nav class="n-bar-underline">
            <h1 class="d:none">네비탭</h1>
            <ul class="item-wrapper padding-x:6">
              <li :class="{ active: currentHash === '#intro' }"
                  class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                <a href="#intro">개요</a>
              </li>
              <li :class="{ active: currentHash === '#program-overview' }"
                  class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                <a href="#program-overview">프로그램 소개</a>
              </li>
              <li :class="{ active: currentHash === '#course-information' }"
                  class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                <a href="#course-information">코스 안내</a>
              </li>
              <li :class="{ active: currentHash === '#meeting-location' }"
                  class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                <a href="#meeting-location">만나는 장소</a>
              </li>
              <li :class="{ active: currentHash === '#inclusions' }"
                  class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                <a href="#inclusions">포함사항</a>
              </li>
              <li :class="{ active: currentHash === '#things-to-know' }"
                  class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                <a href="#things-to-know">꼭 알아두세요!</a>
              </li>
              <li :class="{ active: currentHash === '#review' }"
                  class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                <a href="#review">리뷰</a>
              </li>
            </ul>
          </nav>

          <!--  프로그램 소개  -->
          <section class="reservation-card bg-color:base-1 width:10p">
            <h1 class="d:none">예약 카드</h1>

            <div class="card-main">
              <div class="thumbnail-wrapper">
                <!-- 이전 이미지 버튼 -->
                <button
                    class="n-btn thumbnail-btn thumbnail-btn:left"
                    @click="prevImage"
                    :disabled="totalImages === 0"
                >
                  <span class="n-icon n-icon:arrow_left"></span>
                </button>

                <!-- 현재 이미지 -->
                <img
                    :src="currentImage"
                    alt="대표사진"
                    class="thumbnail-img"
                    v-if="totalImages > 0"
                />

                <!-- 다음 이미지 버튼 -->
                <button
                    class="n-btn thumbnail-btn thumbnail-btn:right"
                    @click="nextImage"
                    :disabled="totalImages === 0"
                >
                  <span class="n-icon n-icon:arrow_right"></span>
                </button>

                <!-- 이미지 순서 표시 -->
                <div class="thumbnail-btn thumbnail-btn:num n-deco">
                  {{ currentImageIndex + 1 }}/{{ totalImages }}
                </div>
              </div>


              <div
                  style="display: flex; padding: var(--gap-7) var(--gap-5); padding-bottom: 0;"
              >
                <div v-for="(category, index) in data.programDetailCategoryDto.categoryNames"
                     class="n-panel-tag"
                     style="
                  margin-right: var(--gap-3);
                  border-radius: var(--border-radius-3);
                  --btn-border-color: var(--color-base-9);
                  --btn-padding: 5px 12px;
                "
                >
                  {{ category }}
                </div>
              </div>


              <div class="card-info-wrapper">
                <p style="color: var(--color-base-9);
              font-size: var(--font-size-11);
              font-weight: bold;
              padding: var(--gap-3) var(--gap-6);">{{ data.programDetailProgramDto.title }}</p>
                <div
                    style="display:flex;  flex-grow: 1; padding: var(--gap-3) var(--gap-7) var(--gap-8) var(--gap-7); gap: var(--gap-2); align-items: center;">
                <span class="card-info n-icon n-icon:star n-deco"
                      style="gap: var(--gap-1);">{{ data.programDetailProgramDto.rating }}</span>
                  <span>/</span>
                  <span>5.0</span>
                  <span>리뷰 {{ data.programDetailReviewDto.ratingCount }}개</span>
                  <div
                      style="display: flex; justify-content: center; align-items: center; height: 25px; margin-left: auto; ">
                    <div class="n-icon n-icon:price n-deco"
                         style="display: flex; justify-content: center; align-items: center; height: inherit; font-size: var(--font-size-10); gap: 0;">
                      {{ data.programDetailProgramDto.price }}
                    </div>
                    <div
                        style="display: flex; justify-content: center; align-items: center; height: inherit; margin-left: var(--gap-1); font-size: var(--font-size-6);">
                      1인
                    </div>
                  </div>
                </div>
              </div>

              <div class="cross-line"></div>
            </div>
          </section>

          <section class="content">
            <!-- 개요 -->
            <section id="intro" class="intro" style="margin-bottom: 20px;">
              <div class="background-color:base-1" style="padding: 0 var(--gap-6); border-bottom:0;">
                <div class="id-container" style="margin-top: 0;">
                  <div class="details">
                    <section>
                      <h1>개요</h1>
                      <div class="list-container">
                        <ul>
                          <li class="list-content"><span
                              class="n-icon n-icon:globe n-deco">{{ data.programDetailProgramDto.language }}</span>
                          </li>
                          <li class="list-content">
                          <span
                              class="n-icon n-icon:people n-deco">{{
                              data.programDetailProgramDto.groupSizeMin
                            }}/{{ data.programDetailProgramDto.groupSizeMax }}</span>
                            <span>(min/max)</span>
                          </li>
                          <li class="list-content">
                            <span class="n-icon n-icon:clock n-deco">{{ data.programDetailProgramDto.duration }}</span>
                            <span>hours</span>
                          </li>
                          <li class="list-content"><span
                              class="n-icon n-icon:placeholder n-deco">{{ addressWithOrderOne }}</span>
                          </li>
                        </ul>
                      </div>
                    </section>
                  </div>
                </div>
              </div>
            </section>

            <!--  호스트 프로필 카드  -->
            <section style="margin-bottom: 20px;">
              <h1>호스트정보</h1>
              <div class="background-color:base-1" style="padding: 0 var(--gap-6);">
                <div class="id-container">
                  <!-- 프로필 카드 -->
                  <section class="profile-card">
                    <h1>프로필 카드</h1>
                    <div class="overview">
                      <div class="img-wrapper">
                        <img v-if="data.programDetailMemberDto.hostProfileImg"
                             :src="`http://localhost:8083/api/v1/${data.programDetailMemberDto.hostProfileImg}`"
                             alt="호스트프사">
                        <img v-else src="/assets/image/default-profile.png" alt="호스트프사">
                      </div>
                      <div>
                        <div>{{ data.programDetailMemberDto.hostNickname }}</div>
                        <div class="n-icon n-icon:star n-deco">
                          <span>{{ data.programDetailReviewDto.hostRating }} / 5.0</span>
                          <span>({{ data.programDetailReviewDto.hostRatingCount }})</span>
                        </div>
                      </div>
                    </div>
                    <a href="" class="n-btn n-btn:hover">프로필보기</a>
                  </section>
                </div>
              </div>
            </section>

            <!--  프로그램 소개  -->
            <section id="program-overview" style="margin-bottom: 20px;">
              <h1>프로그램 소개</h1>
              <div class="background-color:base-1" style="padding:var(--gap-6);">
                <div class="program-container">
                  <div class="content-header">
                    <span class="title">프로그램 소개</span>
                  </div>

                  <div class="text">
                    <!-- 텍스트 미리보기 (3줄 표시) -->
                    <pre class="p-summary" v-show="!isExpanded">{{ data.programDetailProgramDto.detail }}</pre>

                    <!-- 텍스트 전체 (전체 내용 표시) -->
                    <pre class="p-full" v-show="isExpanded">{{ data.programDetailProgramDto.detail }}</pre>

                    <!-- 펼치기 버튼 -->
                    <button v-if="!isExpanded" @click="toggleExpand"
                            class="n-icon n-icon:arrow_down n-deco-pos:right n-deco">
                      펼치기
                    </button>

                    <!-- 닫기 버튼 -->
                    <button v-if="isExpanded" @click="toggleExpand"
                            class="n-icon n-icon:arrow_up n-deco-pos:right n-deco">
                      닫기
                    </button>
                  </div>
                </div>
              </div>
            </section>

            <!--  코스 안내  -->
            <section id="course-information" class="program">
              <h1>코스안내</h1>
              <div class="background-color:base-1" style="padding: 0 var(--gap-6);">
                <div class="id-container">
                  <div class="content-header">
                    <span class="title">코스 안내</span>
                  </div>
                  <div class="details">
                    <div class="map-img-wrapper" id="map1"></div>

                    <section class="n-course-flow">
                      <!-- 출발지 -->
                      <div v-if="departure" class="point">
                        <div class="icon-wrapper">
                          <span class="n-icon n-icon:rectangle">막대기</span>
                          <span class="n-icon n-icon:placeholder">출발 아이콘</span>
                        </div>
                        <div class="point-detail">
                          <div class="n-panel-tag n-panel-tag:time">
                            <span class="n-icon n-icon:clock n-deco"> {{
                                data.programDetailProgramDto.startTime ? data.programDetailProgramDto.startTime.split(':').slice(0, 2).join(':') : '시간 없음'
                              }}</span>
                          </div>
                          <div>
                            <span class="point-name">{{ departure.title }}</span>
                            <span>({{ formatDuration(departure.durationHour, departure.durationMinute) }})</span>
                            <div class="point-info">{{ departure.description || '설명 없음' }}</div>
                          </div>
                        </div>
                      </div>

                      <!-- 경유지 -->
                      <div v-for="(stop, index) in stops" :key="index" class="point drop-by">
                        <div class="icon-wrapper">
                          <span class="n-icon n-icon:rectangle">막대기</span>
                          <span :class="`n-icon n-icon:number${stop.order}`">경유 아이콘</span>
                        </div>
                        <div class="point-detail">
                          <div class="n-panel-tag n-panel-tag:time">
                            <span class="n-icon n-icon:clock n-deco">{{ stop.startTime || '시간 없음' }}</span>
                          </div>
                          <div>
                            <span class="point-name">{{ stop.title }}</span>
                            <span>({{ formatDuration(stop.durationHour, stop.durationMinute) }})</span>
                            <div class="point-info">{{ stop.description || '설명 없음' }}</div>
                          </div>
                        </div>
                      </div>

                      <!-- 도착지 -->
                      <div v-if="destination" class="point">
                        <div class="icon-wrapper">
                          <span class="n-icon n-icon:rectangle">막대기</span>
                          <span class="n-icon n-icon:placeholder">도착 아이콘</span>
                        </div>
                        <div class="point-detail">
                          <div class="n-panel-tag n-panel-tag:time">
                            <span class="n-icon n-icon:clock n-deco">{{ destination.startTime || '시간 없음' }}</span>
                          </div>
                          <div>
                            <span class="point-name">{{ destination.title }}</span>
                            <span>({{ formatDuration(destination.durationHour, destination.durationMinute) }})</span>
                            <div class="point-info">{{ destination.description || '설명 없음' }}</div>
                          </div>
                        </div>
                      </div>
                    </section>


                  </div>
                </div>
              </div>
            </section>

            <!--  만나는 장소  -->
            <section id="meeting-location" class="program">
              <h1>프로그램 정보 및 정책</h1>
              <div class="background-color:base-1" style="padding: 0 var(--gap-6);">
                <div class="id-container" style="border-bottom:0;">
                  <div class="content-header">
                    <span class="title">만나는 장소</span>
                  </div>
                  <div class="details">
                    <section style="padding-top: 0;">
                      <h1>만나는장소</h1>
                      <div class="info-container">
                        <p>{{
                            data.programDetailProgramDto.startTime ? data.programDetailProgramDto.startTime.split(':').slice(0, 2).join(':') : '시간 없음'
                          }} {{ titleWithOrderOne }}</p>
                        <div
                            style="display:flex; align-items: center; padding: var(--gap-3) 0; color: var(--color-base-7);">
                                                <span class="n-icon n-icon:placeholder"
                                                      style="margin-right: var(--gap-1);">위치아이콘</span>
                          <span style="margin-right: var(--gap-1);">{{ addressWithOrderOne }}</span>
                          <button @click.prevent="copyAddress" class="copy-btn">주소복사</button>
                        </div>
                      </div>
                      <div class="map-container" id="map2" style="width: 100%; height: 400px;"></div>
                    </section>
                  </div>
                </div>

                <div v-if="data.programDetailProgramDto.inclusion || data.programDetailProgramDto.exclusion"
                     id="inclusions" class="id-container" style="border-bottom:0;">
                  <div class="content-header">
                    <span class="title">포함사항</span>
                  </div>
                  <div class="details">
                    <section v-if="data.programDetailProgramDto.inclusion">
                      <h1>포함사항</h1>
                      <div class="list-container">
                        <ul class="d:flex fl-dir:column">
                          <li v-for="(item, index) in data.programDetailProgramDto.inclusion.split('\n')"
                              :key="index"
                              class="list-content n-icon n-icon:success-circle-green">
                            {{ item }}
                          </li>
                        </ul>

                      </div>
                      <div class="list-container">
                        <ul v-if="data.programDetailProgramDto.exclusion" class="d:flex fl-dir:column">
                          <li v-for="(item, index) in data.programDetailProgramDto.exclusion.split('\n')"
                              :key="index"
                              class="list-content n-icon n-icon:error">
                            {{ item }}
                          </li>
                        </ul>
                      </div>
                    </section>
                  </div>
                </div>

                <div id="things-to-know" class="id-container" style="margin-top: 0; border-bottom:0;">
                  <div class="content-header">
                    <span class="title">꼭 알아두세요!</span>
                  </div>
                  <div class="details">

                    <section>
                      <h1>꼭 알아두세요!</h1>

                      <div v-if="data.programDetailProgramDto.packingList" style="padding: 0 var(--gap-6);">
                        <h2 class="info-form n-icon n-icon:success-decagon">준비물</h2>
                        <ul>
                          <li v-for="(item, index) in data.programDetailProgramDto.packingList.split('\n')"
                              :key="index"
                              class="list-content">
                            {{ item }}
                          </li>
                        </ul>

                      </div>

                      <div v-if="data.programDetailProgramDto.caution"
                           style="padding: var(--gap-6); padding-bottom: 0;">
                        <h2 class="info-form n-icon n-icon:caution">주의사항</h2>
                        <ul>
                          <li v-for="(item, index) in data.programDetailProgramDto.caution.split('\n')"
                              :key="index"
                              class="list-content">
                            {{ item }}
                          </li>
                        </ul>

                      </div>

                      <div style="padding: var(--gap-6)">
                        <h2 class="info-form n-icon n-icon:reset">취소 및 환불정책</h2>
                        <ul>
                          <li class="list-content">여행일 기준 24시간 전까지 통보 시: 여행 요금 100% 전액 환불됩니다.</li>
                          <li class="list-content">여행일 기준 24시간 이내 통보 시: 취소/환불 불가합니다.</li>
                          <li class="list-content">취소 요청 날짜와 시간은 여행 상품이 진행되는 현지 시간을 따릅니다.</li>
                          <li class="list-content">여행일은 여행 상품이 진행되는 현지 날짜의 오전 9시를 기준으로 합니다.</li>
                        </ul>
                      </div>
                    </section>
                  </div>
                </div>


              </div>
            </section>

            <!--  리뷰  -->
            <div id="review" class="id-container" style="border-bottom:0;">
              <div class="background-color:base-1" style="padding: 0 var(--gap-6);">
                <div class="content-header">
                  <span class="title" style="padding: 0 var(--gap-6);">리뷰</span>
                </div>
                <div class="details">
                  <section>
                    <h1>리뷰</h1>
                    <div
                        style="display:flex; justify-content: space-between; align-items: center; padding: 0 var(--gap-6);">
                                        <span
                                            style="display:flex; align-items: center; width: auto; height: 30px;">{{
                                            data.programDetailReviewDto.ratingCount
                                          }}개
                                            리뷰</span>
                      <div style="display: flex;">
                        <a href="" class="n-icon n-icon:arrow_swap">정렬 아이콘</a>
                        <span>최신순</span>
                      </div>
                    </div>

                    <!--   리뷰 게스트 댓글   -->
                    <section class="review-container">
                      <h1>리뷰 댓글과 답글</h1>
                      <div v-for="(review, index) in data.programDetailReviewDto.reviewDetailDtos" :key="index"
                           class="review">
                        <div class="star-wrapper">
                          <span v-for="star in review.rating" :key="star" class="n-icon n-icon:star">별</span>
                        </div>
                        <div class="guest-info">
                          <img class="profile-img" src="/public/image/face.jpg" alt="프로필이미지">
                          <div style="flex-grow: 1;">
                            <div class="meatball-icon">
                              <span class="name">{{ review.regMemberName }}</span>
                              <label class="n-icon n-icon:meatball"><span>똥똥똥</span><input
                                  type="checkbox"></label>
                            </div>
                            <span class="date">{{ review.regDate }}</span>
                          </div>
                        </div>
                        <div class="program-container">
                          <div class="text">
                            <!-- 텍스트 미리보기 (3줄 표시) -->
                            <pre class="p-summary"
                                 v-show="!isExpanded2">{{ review.content }}</pre>

                            <!-- 텍스트 전체 (전체 내용 표시) -->
                            <pre class="p-full"
                                 v-show="isExpanded2">{{ review.content }}</pre>

                            <!-- 펼치기 버튼 -->
                            <button v-if="!isExpanded2" @click="toggleExpand2"
                                    class="n-icon n-icon:arrow_down n-deco-pos:right n-deco">
                              펼치기
                            </button>

                            <!-- 닫기 버튼 -->
                            <button v-if="isExpanded2" @click="toggleExpand2"
                                    class="n-icon n-icon:arrow_up n-deco-pos:right n-deco">
                              닫기
                            </button>
                          </div>
                        </div>
                      </div>
                    </section>
                  </section>
                  <div
                      style="display: flex; justify-content: center; align-items: center; margin-bottom: var(--gap-6);">
                    <a href="#" class="n-btn n-btn:hover"
                       style="padding: var(--gap-3) var(--height-4p); border: 1px solid var(--color-base-8);">리뷰
                      더
                      보기</a>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <!-- 찜 -->
          <div class="floating-bar" style="z-index: 10;"><!--   지도에 잡아먹혀서 z-index     -->
            <button class="n-btn picked"><span class="n-icon n-icon:bookmark_simple">찜</span></button>
            <a class="n-btn book" @click.prevent="OpenCreateReservationModalHandler"><span>예약하기</span><input
                type="checkbox" id="bookCheckbox"/></a>
            <!--    로그인 안된 사람이 누르면, 로그인페이지로 이동시켜야함 / 로그인한 사람이 누르면 모달창 띄움   -->
          </div>

          <!-- === 모달 =================================================================================================== -->
          <CreateReservationModal class="CreateReservationModal"
                                  :class="{'show': isModalVisible === 'CreateReservationModal'}"
                                  :pId="programId"
                                  :hostId="data.programDetailMemberDto.hostId"
                                  :group-size-max="data.programDetailProgramDto.groupSizeMax"
                                  :price="data.programDetailProgramDto.price"
                                  @close-modal="() => { isModalVisible = ''; }"
                                  @open-move-modal="handleShowMoveReservationModal"/>

          <!-- 모달창 떴을 때 배경처리   -->
          <div :class="{'active': isModalVisible}" class="backdrop"></div>
          <!-- ============================================================================================================= -->
        </section>
        <MoveReservationModal v-if="showMoveReservationModal" @close="showMoveReservationModal = false"/>
      </section>
    </section>
  </main>
</template>

<script setup>

import {ref, onMounted} from 'vue';
import {useFetch} from '#app';
import {useRoute} from 'vue-router';
import CreateReservationModal from "~/components/modal/CreateReservationModal.vue";
import MoveReservationModal from "~/components/modal/MoveReservationModal.vue";

const route = useRoute();
const programId = ref(route.params.id); // route에서 programId를 가져옴
const currentHash = computed(() => route.hash || '');
const isModalVisible = ref("");
const isExpanded = ref(false);
const isExpanded2 = ref(false);

const toggleExpand = () => {
  isExpanded.value = !isExpanded.value;
};
const toggleExpand2 = () => {
  isExpanded2.value = !isExpanded2.value;
};

const OpenCreateReservationModalHandler = () => isModalVisible.value = "CreateReservationModal";

const currentImageIndex = ref(0);
const totalImages = computed(() => data.value?.programDetailImageDto?.imgSrc?.length || 0);

const currentImage = computed(() => {
  return totalImages.value > 0
      ? `http://localhost:8083/api/v1/${data.value.programDetailImageDto.imgSrc[currentImageIndex.value]}`
      : '';
});

// 슬라이더 제어 함수
const prevImage = () => {
  currentImageIndex.value =
      currentImageIndex.value === 0 ? totalImages.value - 1 : currentImageIndex.value - 1;
};

const nextImage = () => {
  currentImageIndex.value =
      currentImageIndex.value === totalImages.value - 1 ? 0 : currentImageIndex.value + 1;
};


onMounted(() => {
  const initMaps = () => {
    if (window.kakao && window.kakao.maps) {
      kakao.maps.load(() => {
        loadMap1();
        loadMap2();
      });
    }
  };

  const loadMap1 = () => {
    const container = document.getElementById('map1');
    if (!container) {
      console.warn("Map1 container not found. Skipping map initialization.");
      return;
    }

    const options = {
      center: new kakao.maps.LatLng(33.450701, 126.570667),
      level: 5
    };
    const map = new kakao.maps.Map(container, options);

    const geocoder = new kakao.maps.services.Geocoder();
    const locations = [
      departure.value,
      ...stops.value,
      destination.value
    ];

    const coordsPromises = locations.map(location => {
      return new Promise((resolve, reject) => {
        if (location && location.address) {
          geocoder.addressSearch(location.address, (result, status) => {
            if (status === kakao.maps.services.Status.OK) {
              resolve(new kakao.maps.LatLng(result[0].y, result[0].x));
            } else {
              console.error(`주소를 찾을 수 없습니다: ${location.address}`);
              resolve(null);
            }
          });
        } else {
          resolve(null);
        }
      });
    });

    Promise.all(coordsPromises).then(coords => {
      const filteredCoords = coords.filter(coord => coord !== null);

      const polyline = new kakao.maps.Polyline({
        map: map,
        path: filteredCoords,
        strokeWeight: 5,
        strokeColor: '#FF0000',
        strokeOpacity: 0.8,
        strokeStyle: 'solid'
      });

      filteredCoords.forEach((coord, index) => {
        const markerContent = `<div style="width: 24px; height: 24px; background-color: #FF6347; color: white; border-radius: 50%; display: flex; align-items: center; justify-content: center;">${index + 1}</div>`;

        new kakao.maps.CustomOverlay({
          map: map,
          position: coord,
          content: markerContent,
          yAnchor: 1
        });
      });

      const bounds = new kakao.maps.LatLngBounds();
      filteredCoords.forEach(coord => bounds.extend(coord));
      map.setBounds(bounds);
    });
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

    if (addressWithOrderOne.value) {
      const geocoder = new kakao.maps.services.Geocoder();

      geocoder.addressSearch(addressWithOrderOne.value, (result, status) => {
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


const {data, error} = await useFetch(`http://localhost:8083/api/v1/programs/${programId.value}`);

const addressWithOrderOne = computed(() => {
  const item = data.value.programDetailRouteDto.find(route => route.order === 1);
  return item ? item.address : "주소 없음"; // 조건에 맞는 객체가 없을 경우 "주소 없음" 반환
});

const titleWithOrderOne = computed(() => {
  const item = data.value.programDetailRouteDto.find(route => route.order === 1);
  return item ? item.title : "주소 없음"; // 조건에 맞는 객체가 없을 경우 "주소 없음" 반환
});

const meetingTimeWithOrderOne = computed(() => {
  const item = data.value.programDetailRouteDto.find(route => route.order === 1);
  if (item && item.startTime) {
    return item.startTime
  }
});

console.log("이미지소스입니당", data.value.programDetailImageDto);


const departure = computed(() =>
    data.value && data.value.programDetailRouteDto
        ? data.value.programDetailRouteDto.find(route => route.order === 1)
        : null
);

const destination = computed(() =>
    data.value && data.value.programDetailRouteDto
        ? data.value.programDetailRouteDto.some(route => route.order !== 1) // order가 1이 아닌 값이 있는지 확인
            ? data.value.programDetailRouteDto.reduce(
                (max, route) => (route.order > max.order ? route : max),
                data.value.programDetailRouteDto[0]
            )
            : null // 모두 order가 1인 경우 null 반환
        : null
);

const stops = computed(() =>
    data.value && data.value.programDetailRouteDto
        ? data.value.programDetailRouteDto.filter(route => route.order !== 1 && route.order !== destination.value.order)
        : []
);


const formatDuration = (hours, minutes) => {
  return `${hours}시간 ${minutes}분`;
};

const copyAddress = async () => {
  try {
    if (navigator.clipboard && navigator.clipboard.writeText) {
      // navigator.clipboard가 지원될 경우
      await navigator.clipboard.writeText(addressWithOrderOne.value);
    } else {
      // 지원되지 않는 경우 execCommand 사용
      const textarea = document.createElement("textarea");
      textarea.value = addressWithOrderOne.value;
      document.body.appendChild(textarea);
      textarea.select();
      document.execCommand("copy");
      document.body.removeChild(textarea);
    }
    alert('주소가 복사되었습니다.');
  } catch (err) {
    console.error('복사 실패:', err);
  }
};

console.log("리뷰 내용:", data.value.programDetailReviewDto.reviewDetailDtos.content)

// 모달창
const showMoveReservationModal = ref(false); // MoveReservationModal 표시 상태

// CreateReservationModal에서 발생하는 이벤트를 처리
const handleShowMoveReservationModal = () => {
  showMoveReservationModal.value = true; // MoveReservationModal 표시 상태 true로 설정
};
// -----------------------


watchEffect(() => {
  console.log("Fetched data:", data.value);
  console.log("Error:", error.value);
});
</script>

<style scoped>
.program-detail {
  font-size: var(--font-size-7);
  max-width: 700px;
}

/* n-icon */

.n-icon {
  --icon-size: var(--icon-size-4);
  margin-right: 3px;
}

.n-icon:before {
  width: var(--icon-size);
  background-color: var(--icon-color);
  margin-right: var(--gap-1);
}

.n-icon\:star:before {
  background-color: var(--color-yellow-1);
}

.n-icon\:credit_card:before {
  width: var(--icon-size);
}

.n-icon\:instagram:before {
  width: var(--icon-size);
}

.n-icon\:success-circle-green:before {
  background-color: var(--color-green-1);
}

.n-icon\:error:before {
  background-color: var(--color-red-1);
}

.n-icon\:placeholder:before {
  background-size: var(--icon-size);
}

.n-icon\:rectangle:after,
.n-icon\:rectangle:before {
  background-color: var(--color-base-5);
}

.n-icon\:clock:after,
.n-icon\:clock:before {
  background-color: var(--color-base-7);
}

/* n-deco */
.n-deco {
  --deco-gap: var(--gap-2);
}

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
  /*color: var(--color-main-1); !* 마우스 오버 시 배경색 변경 *!*/
}

/* */

.cross-line {
  background-color: var(--color-base-2);
  height: 20px;
}

#intro,
#program-overview,
#course-information,
#meeting-location,
#inclusions,
#things-to-know,
#review {
  scroll-margin-top: 39px;
}

.n-bar-underline {
  position: sticky;
  top: 0;
  z-index: 20; /* 다른 콘텐츠보다 위에 오도록 설정 */
  background-color: white; /* 배경색 지정 (필요시 조정) */
  padding: 0;
}

.item-wrapper {
  display: flex;
  overflow-x: auto;
  white-space: nowrap;
  -ms-overflow-style: none; /* IE, Edge에서 스크롤바 숨기기 */
  scrollbar-width: none; /* Firefox에서 스크롤바 숨기기 */
}

.item-wrapper::-webkit-scrollbar {
  display: none; /* Chrome, Safari에서 스크롤바 숨기기 */
}


/* thumbnail */
.thumbnail-wrapper {
  display: flex;
  position: relative;
  justify-content: center;
  align-items: center;
  gap: 16px;
  overflow: hidden;
  width: 100%;
  max-width: 700px; /* 최대 크기 제한 */
  aspect-ratio: 16 / 9; /* 가로 세로 비율 고정 */
  background-color: #F2EAE1; /* 기본 배경색 */
  border-radius: 8px;

  .thumbnail-img {
    width: 100%;
    height: 100%;
    object-fit: contain;
    transition: opacity 0.5s ease-in-out;
  }

  .thumbnail-btn {
    cursor: pointer;
    --btn-bg-hover: var(--color-base-2);
    --btn-padding: 2px;
    align-items: center;
    border-radius: var(--border-radius-4, 8px);
    display: inline-flex;
    padding: var(--btn-padding);
    transition: border-color .3s, background-color .3s;
    user-select: none;
    white-space: nowrap;

    background-color: rgba(0, 0, 0, 0.3);
    /* 반투명 배경 */
    position: absolute;
    top: 50%;
    margin: 0;
    gap: 0;
    width: var(--icon-size);

    .n-icon {
      --icon-size: var(--icon-size-5);
      margin: 0;
    }

    .n-icon:before {
      background-color: var(--color-base-1);
    }
  }

  .thumbnail-btn\:left {
    left: 3%;
  }

  .thumbnail-btn\:right {
    right: 3%;
  }

  .thumbnail-btn\:num {
    color: var(--color-base-1);
    cursor: none;
    top: auto;
    bottom: 10px;
    /* 아래에서 10px */
    right: 10px;
    /* 오른쪽에서 10px */
  }
}


.intro {

  .title {
    color: var(--color-base-9);
  }

  .list-container {
    display: block;
    color: var(--color-base-9);
    padding: var(--gap-4) 0;

    li {
      margin-bottom: var(--gap-1);
    }
  }

  .list-content {
    display: flex;
    font-size: var(--font-size-6);
    align-items: center;
    width: auto;
    height: 43px;
    justify-content: left;
    list-style: none;
  }

  li {
    color: var(--color-base-9);
    padding: var(--gap-4) 0;
  }

  .n-icon {
    color: var(--color-base-9);
  }

  .n-icon:before {
    width: var(--icon-size);
    background-color: var(--icon-color);
  }
}


.content {
  background-color: var(--color-base-2);
  font-size: var(--font-size-6);

  .content-header {
    width: max-content;
    padding: var(--gap-6) var(--gap-3);
    list-style-type: none;

    .title {
      justify-content: space-between;
      align-items: center;
      font-size: var(--font-size-8);
      font-weight: bold;
      flex-grow: 1;
    }

    .n-icon {
      cursor: pointer;
      border: none;
      position: absolute;
      right: var(--gap-7);
    }
  }

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

  .id-container {
    margin-top: var(--gap-6);
  }

  .program-container {

    /* 글 내용 */

    .text {
      padding: 0 var(--gap-6);

      .p-summary {
        display: -webkit-box;
        padding: 0 var(--gap-1);
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        text-overflow: ellipsis;
        line-height: 1.5;
        white-space: pre-line;
      }

      .p-full {
        display: block;
        padding: 0 var(--gap-1);
        white-space: pre-line;
        line-height: 1.5;
      }


      /* 더보기 버튼 */

      button {
        width: var(--width-10p);
        background-color: var(--color-base-1);
        justify-content: center;
        padding: 25px 0;
        cursor: pointer;
        text-decoration: underline;
        text-decoration-color: var(--color-base-9);
        /* 밑줄 색상 */
        border-radius: 5px;
      }

      button:hover {
        font-weight: bold;
      }

    }
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


.program {
  .title {
    color: var(--color-base-9);
  }

  section {
    .title {
      color: var(--color-base-9);
      font-size: var(--font-size-8);
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
      padding-bottom: var(--gap-5);

      li {
        margin-bottom: var(--gap-3);
      }
    }

    ul {
      list-style: none;
      /* 기본 점을 없앰 */
      padding-left: var(--gap-3);
    }

    .list-content {
      font-size: var(--font-size-6);
      align-items: center;
      width: auto;
    }

    .list-content::before {
      content: "•";
      /* 기본 점 없애고 커스텀 점 */
      display: inline-block;
      width: 1em;
      /* 점과 텍스트 사이의 간격 */
      padding-left: var(--gap-3);
      /* 점을 왼쪽으로 이동 */
    }
  }


  h2 {
    font-size: var(--font-size-6);
    font-weight: bold;
    width: auto;
    height: 30px;
    gap: 4px;
  }

  li {
    color: var(--color-main-2);
    padding: var(--gap-3);
  }
}


.info-container {
  display: flex;
  margin-bottom: var(--gap-6);
  justify-content: space-between;
}

.info-input {
  font-size: var(--font-size-6);
  display: flex;
  align-items: center;
  width: auto;
  justify-content: left;
}

/* folding */

.details {
  margin: 0;
  padding: var(--gap-4);

  .map-img {
    max-width: 700px;
    width: 100%;
    height: auto;
  }
}

/* 하단 플로팅 바 */
.floating-bar {
  display: flex;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 60px;
  justify-content: center;
  align-items: center;
  gap: var(--gap-3);
  padding: var(--gap-6) var(--gap-3);
  background-color: var(--color-base-1);
  z-index: var(--modal-z-index);

  .picked {
    background-color: var(--color-base-2);
    height: 65%;

    .n-icon {
      margin-right: 0;
    }

    .n-icon\:bookmark_simple:before {
      --icon-size: var(--icon-size-4);
    }
  }


  .book {
    margin-left: var(--gap-2);
    background-color: var(--color-sub-1);
    color: var(--color-base-1);
    width: var(--width-9p);
    height: 65%;

    span {
      z-index: 1;
    }
  }

  input[type="checkbox"] {
    opacity: 0;
    position: absolute;
  }
}


/* 반응형 */

@media (min-width: 768px) {
  .layout-body {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .book {
    max-width: 580px;
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
  background-color: rgba(0, 0, 0, 0.1); /* Dark background */
  backdrop-filter: blur(5px); /* Blur effect */
  z-index: 990; /* Behind modal but above other content */
}

.backdrop.active {
  display: block;
}
</style>