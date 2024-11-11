<template>
  <main>
    <div class="container">
      <section class="profile-header">
        <img class="profile-image" src="/assets/image/main1.jpg" alt="프로필 이미지"/>
        <div class="profile-name">
          <p>{{ data.nickname }}</p>
          <span class="n-icon">️</span>
        </div>
      </section>
      <section class="profile-info">
        <div class="info-row">
          <span class="label">아이디</span>
          <span class="value">{{data.userId}}</span>
        </div>
        <div class="info-row">
          <span class="label">비밀번호</span>
          <span class="value"><NuxtLink class="edit-link" href="/change-password">변경</NuxtLink></span>
        </div>
        <div class="info-row">
          <span class="label">이름</span>
          <span class="value">{{data.name}}</span>
        </div>
        <div class="info-row">
          <span class="label">연락처</span>
          <span class="value">+82 01012345678</span>
        </div>
        <div class="info-row">
          <span class="label">이메일</span>
          <span class="value">{{data.email}}</span>
        </div>
      </section>

      <section class="profile-footer">
        <button class="delete-account-button">계정 삭제하기</button>
      </section>
    </div>
  </main>
</template>
<style scoped>
.profile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #e0e0e0;
}

.profile-image {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
}

.profile-name {
  display: flex;
  margin-top: 10px;
  font-size: 16px;
  font-weight: bold;
  align-items: center;
  gap: 5px;
}

.n-icon {
  mask-image: url("assets/image/icon/edit.svg");
  align-content: center;
  align-items: center;
}

.profile-info {
  padding: 20px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  font-size: 14px;
  border-bottom: 1px solid #f0f0f0;
}

.label {
  color: #666;
}

.value {
  color: #333;
}

.edit-link {
  color: #4a90e2; /* 파란색 */
  font-size: 12px;
  text-decoration: none;
}

.profile-footer {
  display: flex;
  justify-content: center;
  padding: 20px;
  border-top: 1px solid #e0e0e0;
  background-color: #f9f9f9;
}

.delete-account-button {
  font-size: 14px;
  border-bottom: 1px solid #e0e0e0;
  cursor: pointer;
}

@media (min-width: 768px) {
  .container {
    max-width: 768px;
    margin: 0 auto; /* 가운데 정렬 */
  }
}
</style>
<script setup>

import {useDataFetch} from "~/composables/useDataFetch.js";

const memberId = localStorage.getItem("id");
const data = ref({});

const response = await useDataFetch(`members/${memberId}`);
// console.log("응답 데이타", response);
data.value = response;
console.log(data.value);

</script>