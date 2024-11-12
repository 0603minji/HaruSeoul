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

      <ul class="header-menus:icon-only md:show">
        <li class="header-menu">
          <NuxtLink
            class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:ticket"
            href="/guest/reservations"
            >내 예약</NuxtLink
          >
        </li>
        <li class="header-menu">
          <NuxtLink
            class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:wishlist"
            href="/guest/reservations"
            >찜 목록</NuxtLink
          >
        </li>
        <li class="header-menu">
          <NuxtLink
            class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:chat"
            href="/guest/reservations"
            >Chat</NuxtLink
          >
        </li>
        <li class="header-menu">
          <NuxtLink
            class="n-btn n-btn:hover n-btn-bd:transparent n-icon n-icon:alert"
            href="/guest/reservations"
            >알림</NuxtLink
          >
        </li>
      </ul>

      <div class="profile-img-container md:show">
        <NuxtLink href="/mypage" class="profile-img-wrapper">
          <img
            class="profile-img"
            src="/image/profile_cat.png"
            alt="게스트 프로필 사진"
          />
        </NuxtLink>
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

      <div class="bg-darkened"></div>

      <!--어사이드-->
      <aside class="n-aside">
        <header class="aside-header">
          <h1>게스트 로그인 어사이드</h1>
          <label
            for="menu-toggle"
            class="n-icon n-deco n-icon:exit cursor:pointer"
          ></label>
          <a href="#" class="n-icon n-deco n-icon:alert">
            <span
              class="n-icon n-deco n-icon:circle n-icon-color:accent-1 position:absolute z-index:1"
            ></span>
          </a>
        </header>
        <section class="aside-profile">
          <h1>게스트 프로필</h1>
          <div class="profile-img-container">
            <img
              class="profile-img"
              src="/image/profile_cat.png"
              alt="게스트 프로필 사진"
            />
          </div>
          <div class="profile-info">
            <p class="nickname">닉네임</p>
            <div class="account-setting">
              <NuxtLink href="/mypage" class="n-icon n-deco n-icon:setting">내 계정관리</NuxtLink>
            </div>
          </div>
        </section>
        <nav>
          <h1 class="d:none">게스트 로그인 어사이드 메뉴</h1>
          <ul class="aside-menu">
            <li>
              <NuxtLink href="/guest/reservations" class="menu n-icon n-deco n-icon:ticket">내 예약</NuxtLink>
            </li>
            <li>
              <a href="#" class="menu n-icon n-deco n-icon:wishlist">찜 목록</a>
            </li>
            <li><a href="#" class="menu n-icon n-deco n-icon:chat">Chat</a></li>
          </ul>
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
          <ul class="aside-menu bd-color:transparent">
            <li>
              <NuxtLink style="cursor: pointer" class="menu n-icon n-deco n-icon:logout" href="/signin">로그인</NuxtLink>
            </li>
            <li>
              <div @click.prevent="logoutHandler" style="cursor: pointer" class="menu n-icon n-deco n-icon:logout">로그아웃</div>
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

const userDetails = useUserDetails();


// const memberId = localStorage.getItem("id");
// const data = ref({});
//
// const response = await useDataFetch(`members/${memberId}`);
// data.value = response;


const logoutHandler = async () => {
  console.log("logoutHandler");
  userDetails.logout();
  if(process.client) {
    return navigateTo("/signin");
  }
}
</script>

<style>

</style>
