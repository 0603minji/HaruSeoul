<template>
  <header class="n-global-header n-global-header:host">
    <NuxtLink href="/" class="n-heading:2">
      <img src="/assets/image/icon/logo.svg" alt="">
    </NuxtLink>

    <nav class="header-menu-container ml:auto">
      <h1 class="d:none">헤더메뉴</h1>

      <div class="d:flex align-items:center">
        <NuxtLink class="n-btn n-btn:hover" href="/programs">게스트 되기</NuxtLink>
      </div>

      <ul class="header-menus:text-only lg:show">
        <li class="header-menu">
          <NuxtLink
            class="n-btn n-btn:hover n-btn-bd:transparent host-btn"
            href="/host/programs/new"
            >프로그램 등록</NuxtLink
          >
        </li>
        <span class="separator"></span>
        <li class="header-menu">
          <NuxtLink
            class="n-btn n-btn:hover n-btn-bd:transparent host-btn"
            href="/host/programs"
            >프로그램 관리</NuxtLink
          >
        </li>
        <span class="separator"></span>
        <li class="header-menu">
          <NuxtLink
            class="n-btn n-btn:hover n-btn-bd:transparent host-btn"
            href="/host/reservations"
            >예약 관리</NuxtLink
          >
        </li>
        <span class="separator"></span>
        <li class="header-menu">
          <NuxtLink
            class="n-btn n-btn:hover n-btn-bd:transparent host-btn"
            href="/host/programs"
            >리뷰 모아보기</NuxtLink
          >
        </li>
      </ul>

      <ul class="header-menus:icon-only md:show">
<!--        <li class="header-menu">-->
<!--          <NuxtLink-->
<!--            class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:dashboard"-->
<!--            href="/host/programs"-->
<!--            >대시보드</NuxtLink-->
<!--          >-->
<!--        </li>-->
        <li class="header-menu">
          <NuxtLink
            class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:chat"
            href="/host/programs"
            >Chat</NuxtLink
          >
        </li>
        <li class="header-menu">
          <div v-if="(notifications && notifications.length > 0) || hasNewNotification"
               class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:notification"
               @click.prevent="toggleNotificationModal"
          >
            알림
          </div>
          <div v-else
               class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:alert"
               @click.prevent="toggleNotificationModal"
          >
            알림
          </div>
          <div v-if="showNotificationModal" class="modal-content-notification">
            <header>
              <button class="modal-close" @click="showNotificationModal = false">×</button>
              <div class="modal-header">
                <h2>알림</h2>
              </div>
            </header>
            <div class="modal-body">
              <div style="margin-bottom: 10px">미확인 알림: {{ notificationCount }}개</div>
              <ul>
                <li
                    v-for="notification in notifications"
                    :key="notification.id"
                    class="notification-items"
                    :class="{
                    'bg-color-green': notification.type === 'RESERVE',
                    'bg-color-red': notification.type === 'CANCEL' || notification.type === 'CANCEL FROM HOST'
                     }"
                >
                  <div>
                    <NuxtLink
                        :href="notification.type === 'CANCEL FROM HOST'
                      ? `/guest/reservations/${notification.reservationId}`
                      : `/host/reservations/${notification.programId}`">
                      <span style="font-weight: bold">{{ notification.title }}</span>
                    </NuxtLink>
                    이(가) {{ notification.type === 'RESERVE' ? '예약' : notification.type === 'CANCEL' ? '취소' : '취소' }}
                    되었습니다.
                    <div>{{notification.regDate}}</div>
                  </div>
                  <button
                      class="notification-close"
                      @click.prevent="confirmNotification(notification.notificationId)">
                    ×
                  </button>
                </li>
              </ul>
            </div>
          </div>

        </li>
      </ul>

      <div class="profile-img-container lg:show">
        <div @click.prevent="toggleModal" class="profile-img-wrapper" style="cursor: pointer">
          <img v-if="userDetails.profileImgSrc.value"
            class="profile-img"
            :src="`http://localhost:8083/api/v1/${userDetails.profileImgSrc.value}`"
            alt="게스트 프로필 사진"
          />
          <img v-else
              class="profile-img"
              src="/assets/image/default-profile.png"
              alt="게스트 프로필 사진"
          />
        </div>
        <div v-if="showModal" class="modal-content">
          <!-- 닫기 버튼 -->
          <button class="modal-close" @click="showModal = false">×</button>

          <!-- 프로필 사진과 마이페이지 링크 -->
          <div class="modal-header">
            <img v-if="userDetails.profileImgSrc" class="modal-profile-img" :src="`http://localhost:8083/api/v1/${userDetails.profileImgSrc.value}`" alt="프로필 사진" />
            <img v-else class="modal-profile-img" src="/assets/image/default-profile.png" alt="프로필 사진" />
            <NuxtLink href="/mypage" class="mypage-link">마이페이지</NuxtLink>
          </div>

          <!-- 로그아웃 버튼 -->
          <div class="modal-actions">
            <button @click.prevent="logoutHandler" class="logout-button">로그아웃</button>
          </div>
        </div>
      </div>
    </nav>

    <!-- 어사이드 메뉴 -->
    <section class="menu-icon lg:d:none">
      <h1 class="d:none">어사이드 메뉴</h1>

      <!--햄버거 네비게이터-->
      <label
        for="menu-toggle"
        class="n-icon n-icon:list n-icon-size:6 margin-left:3 cursor:pointer"
      ></label>
      <input id="menu-toggle" class="n-aside-hider" type="checkbox" />

      <div @click="closeAsideHandler" class="bg-darkened"></div>

      <!--어사이드-->
      <aside class="n-aside" style="height: 100vh; overflow-y: auto;">
        <header class="aside-header">
          <h1>게스트 로그인 어사이드</h1>
          <label
              for="menu-toggle"
              class="n-icon n-deco n-icon:exit cursor:pointer"
          ></label>
          <div v-if="!userDetails.isAnonymous()">
            <span class="n-icon n-icon:chat"></span>
            <span v-if="(notifications && notifications.length > 0) || hasNewNotification"
                  @click.prevent="toggleNotificationModalAside"
                  class="n-icon n-icon:notification" style="cursor: pointer"></span>
            <span v-else
                  @click.prevent="toggleNotificationModalAside"
                  class="n-icon n-icon:alert" style="cursor: pointer"></span>
          </div>

          <div v-if="showNotificationModalAside" class="modal-content-notification-aside">
            <header>
              <button class="modal-close" @click="showNotificationModalAside = false">×</button>
              <div class="modal-header">
                <h2>알림</h2>
              </div>
            </header>
            <div class="modal-body d:flex fl-dir:column">
              <div style="margin-bottom: 10px">미확인 알림: {{ notificationCount }}개</div>
              <ul>
                <li
                    v-for="notification in notifications"
                    :key="notification.id"
                    class="notification-items"
                    :class="{
                    'bg-color-green': notification.type === 'RESERVE',
                    'bg-color-red': notification.type === 'CANCEL' || notification.type === 'CANCEL FROM HOST'
                     }"
                >
                  <div class="d:block">
                    <NuxtLink
                        :href="notification.type === 'CANCEL FROM HOST'
                      ? `/guest/reservations/${notification.reservationId}`
                      : `/host/reservations/${notification.programId}`">
                      <span style="font-weight: bold">{{ notification.title }}</span>
                    </NuxtLink>
                    이(가) {{ notification.type === 'RESERVE' ? '예약' : notification.type === 'CANCEL' ? '취소' : '취소' }}
                    되었습니다.
                    <div>{{notification.regDate}}</div>
                  </div>
                  <button
                      class="notification-close"
                      @click.prevent="confirmNotification(notification.notificationId)">
                    ×
                  </button>
                </li>
              </ul>
            </div>
          </div>

        </header>
        <section class="aside-profile">
          <h1>게스트 프로필</h1>
          <div class="profile-img-container">
            <div @click.prevent="toggleModal" class="profile-img-wrapper" style="cursor: pointer">
              <img v-if="userDetails.profileImgSrc.value"
                   class="profile-img"
                   :src="`http://localhost:8083/api/v1/${userDetails.profileImgSrc.value}`"
                   alt="게스트 프로필 사진"
              />
              <img v-else
                   class="profile-img"
                   src="/assets/image/default-profile.png"
                   alt="게스트 프로필 사진"
              />
            </div>
          </div>
          <div class="profile-info">
            <p class="nickname">{{ data.nickname }}</p>
            <div class="account-setting">
              <NuxtLink href="/mypage" class="n-icon n-deco n-icon:setting">내 계정관리</NuxtLink>
            </div>
          </div>
        </section>
        <nav>
          <h1 class="d:none">Guest Menu</h1>
          <details open class="filter">
            <summary class="collapse">
              <span class="title">Guest Menu</span>
              <span class="n-icon n-icon:arrow_down">펼치기 버튼</span>
            </summary>
            <ul class="aside-menu">
              <li>
                <NuxtLink href="/guest/reservations" class="menu n-icon n-deco n-icon:ticket">내 예약</NuxtLink>
              </li>
              <li>
                <a href="#" class="menu n-icon n-deco n-icon:wishlist">찜 목록</a>
              </li>
              <!--            <li><a href="#" class="menu n-icon n-deco n-icon:chat">Chat</a></li>-->
            </ul>
          </details>
          <details open class="filter">
            <summary class="collapse">
              <span class="title">Host Menu</span>
              <span class="n-icon n-icon:arrow_down">펼치기 버튼</span>
            </summary>
            <ul class="aside-menu">
              <li>
                <a href="#" class="menu n-icon n-deco n-icon:dashboard"
                >대시보드</a
                >
              </li>
              <li>
                <NuxtLink href="/host/programs/new" class="menu n-icon n-deco n-icon:create"
                >프로그램 등록</NuxtLink
                >
              </li>
              <li>
                <NuxtLink href="/host/programs" class="menu n-icon n-deco n-icon:programs"
                >프로그램 관리</NuxtLink
                >
              </li>
              <li>
                <NuxtLink href="/host/reservations" class="menu n-icon n-deco n-icon:check_doc"
                >예약 관리</NuxtLink
                >
              </li>
              <li>
                <a href="#" class="menu n-icon n-deco n-icon:review"
                >리뷰 모아보기</a
                >
              </li>
              <!--              <li><a href="#" class="menu n-icon n-deco n-icon:chat">Chat</a></li>-->
            </ul>
          </details>
          <details open class="filter">
            <summary class="collapse">
              <span class="title">Customer Center</span>
              <span class="n-icon n-icon:arrow_down">펼치기 버튼</span>
            </summary>
            <ul class="aside-menu">
              <li>
                <a href="#" class="menu n-icon n-deco n-icon:notice">공지사항</a>
              </li>
              <li>
                <a href="#" class="menu n-icon n-deco n-icon:faq"
                >자주 묻는 질문</a
                >
              </li>
            </ul>
          </details>
          <ul class="aside-menu bd-color:transparent">
            <li>
              <div @click.prevent="logoutHandler" style="cursor: pointer" class="menu n-icon n-deco n-icon:logout">로그아웃</div>
            </li>
          </ul>
        </nav>
        <div class="aside-footer n-icon n-deco n-icon:globe_2 n-icon-color:main-3">
          <select name="language" id="language">
            <option value="Korean">Korean</option>
            <option value="English">English</option>
            <option value="Chinese">Chinese</option>
            <option value="Japanese">Japanese</option>
          </select>
        </div>
      </aside>
    </section>
  </header>
</template>

<script setup>

import {useNotification} from "~/composables/useNotification.js";
import axios from "axios";

const userDetails = useUserDetails();
const data = ref({});
const showModal = ref(false);

const memberId = process.client ? localStorage.getItem("id") : null;
if (process.client && memberId) {
  (async () => {
    const response = await useDataFetch(`members/${memberId}`);
    data.value = response;
  })();
}

const {
  notifications,
  notificationCount,
  hasNewNotification,
  fetchNotifications
} = useNotification(userDetails.id.value);


const showNotificationModal = ref(false);
const showNotificationModalAside = ref(false);

const toggleNotificationModal = () => {
  showNotificationModal.value = !showNotificationModal.value;
};

const toggleNotificationModalAside = () => {
  showNotificationModalAside.value = !showNotificationModalAside.value;
};


const confirmNotification = async (notificationId) => {
  const confirm = await axios.post(`http://localhost:8083/api/v1/notifications/${notificationId}`)
  await fetchNotifications();
}


const toggleModal = () => {
  showModal.value = !showModal.value;
};

const logoutHandler = async () => {
  console.log("logoutHandler");
  userDetails.logout();
  if(process.client) {
    return navigateTo("/signin");
  }
}

const handleClickOutside = (event) => {
  if (
      !event.target.closest('.modal-content') &&
      !event.target.closest('.profile-img-wrapper')
  ) {
    showModal.value = false;
  }
};

const closeAsideHandler = () => {
  console.log('closeAsideHandler called');
  const checkbox = document.getElementById('menu-toggle'); // 체크박스 선택
  if (checkbox.checked) {
    checkbox.checked = false; // 체크 해제
  }
};

onMounted(() => {
  if (process.client) {
    document.addEventListener('click', handleClickOutside);
    fetchNotifications();
  }
});

onBeforeUnmount(() => {
  if (process.client) {
    document.removeEventListener('click', handleClickOutside);
  }
});

watch(notifications, (newNotifications) => {
  console.log("새 알림:", newNotifications);
});

</script>

<style scoped>
.profile-img-container {
  position: relative;
}

.modal-content {
  position: absolute;
  top: calc(100% + 8px); /* 프로필 이미지 바로 아래 */
  left: -150%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff;
  color: #333;
  padding: 20px;
  border-radius: 8px;
  width: 220px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
  z-index: 600;
  font-size: 14px;
}

.modal-content-notification {
  position: fixed;
  top: 8%; /* 모달 위치는 고정 */
  left: 80%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff;
  color: #333;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
  z-index: 999;
  max-height: 60vh; /* 모달 전체의 최대 높이 */
  font-size: 14px;
  overflow: hidden; /* 모달 외부로 내용이 넘치지 않도록 설정 */
  border: 1px solid #ccc;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

.modal-body {
  max-height: calc(3 * 72px + 20px); /* li가 최대 3개 높이까지만 보이도록 설정 */
  overflow-y: auto; /* 초과되는 내용은 스크롤 가능 */
  width: 100%; /* 스크롤 시 너비 고정 */
}

.notification-items {
  display: flex;
  align-items: center; /* 세로 정렬 */
  justify-content: space-between; /* 텍스트와 버튼 분리 */
  gap: 20px;
  background-color: #f9f9f9; /* 카드 배경색 */
  border: 1px solid #ddd; /* 카드 테두리 */
  border-radius: 8px; /* 둥근 모서리 */
  padding: 15px; /* 내부 여백 */
  margin-bottom: 10px; /* 카드 간 간격 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  transition: transform 0.2s, box-shadow 0.2s; /* 호버 시 애니메이션 */
}

.notification-items:last-child {
  margin-bottom: 0; /* 마지막 아이템의 간격 제거 */
}

.bg-color-green {
  background-color: #E7F4E8; /* 초록색 배경 (예약) */
}

.bg-color-red {
  background-color: #FFE2E5; /* 빨간색 배경 (취소) */
}


.notification-close {
  background: none;
  border: none;
  color: #888;
  font-size: 16px;
  cursor: pointer;
}

.notification-close:hover {
  color: #333;
}
.n-icon\:notification::before{
  background-image: var(--icon);
}

.modal-content-notification-aside {
  position: fixed;
  top: 6%; /* 모달 위치는 고정 */
  left: 70%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #fff;
  color: #333;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
  z-index: 600;
  max-height: 60vh; /* 모달 전체의 최대 높이 */
  font-size: 14px;
  overflow: hidden; /* 모달 외부로 내용이 넘치지 않도록 설정 */
  border: 1px solid #ddd;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2);
}

/* 닫기 버튼 스타일 */
.modal-close {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 16px;
  cursor: pointer;
  color: #999;
}

/* 프로필 사진 스타일 */
.modal-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.modal-profile-img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

/* 마이페이지 링크 스타일 */
.mypage-link {
  color: #333;
  font-weight: bold;
  text-decoration: none;
}

/* 로그아웃 버튼 스타일 */
.modal-actions {
  width: 100%;
  display: flex;
  justify-content: center;
}

.logout-button {
  background-color: #e74c3c;
  color: #fff;
  border: none;
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  width: 100%;
  text-align: center;
}

.logout-button:hover {
  background-color: #c0392b;
}


.filter[open] {
  .collapse {
    --collapse-padding: 24px 0 12px 0;

    .n-icon\:arrow_up:before {
      --icon: url(/assets/image/icon/arrow_down.svg);
    }
  }
}

</style>
