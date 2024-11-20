<template>
  <header class="n-global-header n-global-header:guest">
    <NuxtLink href="/" class="n-heading:2">
      <img src="/assets/image/icon/logo.svg" alt="">
    </NuxtLink>

    <nav class="header-menu-container ml:auto">
      <h1 class="d:none">헤더메뉴</h1>

      <div class="d:flex align-items:center">
        <NuxtLink class="n-btn n-btn:hover" href="/host/programs">호스트 되기</NuxtLink>
      </div>

      <ul v-if="!userDetails.isAnonymous()" class="header-menus:icon-only md:show">
        <li class="header-menu">
          <NuxtLink
              class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:ticket"
              href="/guest/reservations"
          >내 예약
          </NuxtLink
          >
        </li>
        <li class="header-menu">
          <NuxtLink
              class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:wishlist"
              href="/guest/reservations"
          >찜 목록
          </NuxtLink
          >
        </li>
        <li class="header-menu">
          <NuxtLink
              class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:chat"
              href="/guest/reservations"
          >Chat
          </NuxtLink
          >
        </li>
<!--        <li v-if="notifications.length" v-for="notification in notifications" :key="notification.id">-->
<!--          {{notification.type}} - {{notification.regDate}}-->
<!--        </li>-->
        <li class="header-menu">
          <div class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:alert">
            알림
          </div>
<!--          <span v-if="hasNewNotification">-->
<!--            새알람이 있습니다!!!!-->
<!--          </span>-->
        </li>
      </ul>

      <div v-if="userDetails.isAnonymous()" class="profile-img-container md:show">
        <NuxtLink href="/signin" class="profile-img-wrapper">
          <img
              class="profile-img"
              src="/assets/image/default-profile.png"
              alt="게스트 프로필 사진"
          />
        </NuxtLink>
      </div>
      <div v-if="!userDetails.isAnonymous()" class="profile-img-container md:show">
        <div @click.prevent="toggleModal" style="cursor: pointer" class="profile-img-wrapper">
          <img v-if="userDetails.profileImgSrc"
               class="profile-img"
               :src="`http://localhost:8080/api/v1/${userDetails.profileImgSrc.value}`"
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
            <img v-if="userDetails.profileImgSrc" class="modal-profile-img"
                 :src="`http://localhost:8080/api/v1/${userDetails.profileImgSrc.value}`" alt="프로필 사진"/>
            <img v-else class="modal-profile-img" src="/assets/image/default-profile.png" alt="프로필 사진"/>
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
    <section class="menu-icon md:d:none">
      <h1 class="d:none">어사이드 메뉴</h1>

      <!--햄버거 네비게이터-->
      <label
          for="menu-toggle"
          class="n-icon n-icon:list n-icon-size:6 margin-left:3 cursor:pointer"
      ></label>
      <input id="menu-toggle" class="n-aside-hider" type="checkbox"/>

      <div @click="closeAsideHandler" class="bg-darkened"></div>

      <!--어사이드-->
      <aside class="n-aside" style="height: 100vh; overflow-y: auto;">
        <header class="aside-header">
          <h1>게스트 로그인 어사이드</h1>
          <label
              for="menu-toggle"
              class="n-icon n-deco n-icon:exit cursor:pointer"
          ></label>
          <div>
            <a href="#" class="n-icon n-icon:chat"></a>
            <a href="#" class="n-icon n-icon:alert"></a>
          </div>
        </header>
        <section v-if="!userDetails.isAnonymous()" class="aside-profile">
          <h1>게스트 프로필</h1>
          <div class="profile-img-container">
            <img v-if="userDetails.profileImgSrc"
                 class="profile-img"
                 :src="`http://localhost:8080/api/v1/${userDetails.profileImgSrc.value}`"
                 alt="게스트 프로필 사진"
            />
            <img v-else
                 class="profile-img"
                 src="/assets/image/default-profile.png"
                 alt="게스트 프로필 사진"
            />
          </div>
          <div class="profile-info">
            <p class="nickname">{{ data.nickname }}</p>
            <div class="account-setting">
              <NuxtLink href="/mypage" class="n-icon n-deco n-icon:setting">내 계정관리</NuxtLink>
            </div>
          </div>
        </section>
        <section v-else class="aside-logout">
          <h1 class="aside-main-title">Haru Seoul</h1>
          <NuxtLink href="/signin" class="n-btn n-btn-background-color:main">로그인 및 회원가입</NuxtLink>
        </section>
        <nav>
          <h1 class="d:none">Guest Menu</h1>
          <details v-if="!userDetails.isAnonymous()" open class="filter">
            <summary class="collapse">
              <span class="title">Guest Menu</span>
              <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
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
          <details v-if="!userDetails.isAnonymous()" open class="filter">
            <summary class="collapse">
              <span class="title">Host Menu</span>
              <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
            </summary>
            <ul class="aside-menu">
              <li>
                <a href="#" class="menu n-icon n-deco n-icon:dashboard"
                >대시보드</a
                >
              </li>
              <li>
                <NuxtLink href="/host/programs/new" class="menu n-icon n-deco n-icon:create"
                >프로그램 등록
                </NuxtLink
                >
              </li>
              <li>
                <NuxtLink href="/host/programs" class="menu n-icon n-deco n-icon:programs"
                >프로그램 관리
                </NuxtLink
                >
              </li>
              <li>
                <NuxtLink href="/host/reservations" class="menu n-icon n-deco n-icon:check_doc"
                >예약 관리
                </NuxtLink
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
              <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
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
              <div v-if="!userDetails.isAnonymous()" @click.prevent="logoutHandler" style="cursor: pointer"
                   class="menu n-icon n-deco n-icon:logout">로그아웃
              </div>
            </li>
          </ul>
        </nav>
        <div
            class="aside-footer n-icon n-deco n-icon:globe_2 n-icon-color:main-3"
        >
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
import {useDataFetch} from "~/composables/useDataFetch.js";
import {useNotification} from "~/composables/useNotification.js";

const userDetails = useUserDetails();
const data = ref({});
const memberId = process.client ? localStorage.getItem("id") : null;

const {notifications, hasNewNotification} = useNotification(memberId);


const showModal = ref(false);

if (process.client && memberId) {
  (async () => {
    const response = await useDataFetch(`members/${memberId}`);
    data.value = response;
  })();
}


const logoutHandler = async () => {
  console.log("logoutHandler");
  userDetails.logout();
  if (process.client) {
    return navigateTo("/signin");
  }
}
const toggleModal = () => {
  showModal.value = !showModal.value;
};

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
  }
});

onBeforeUnmount(() => {
  if (process.client) {
    document.removeEventListener('click', handleClickOutside);
  }
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
  flex-direction: row;
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

.aside-logout {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  gap: 30px;

  h1 {
    padding-top: 10px;
    font-size: 2em;
    font-weight: bold;
    color: var(--color-main-1);
  }

  .n-btn {
    border-radius: 5px;
    width: 160px;
    height: 20px;
    margin-bottom: 20px;
  }
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
