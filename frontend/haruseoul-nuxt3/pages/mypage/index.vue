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
          <span class="value"><NuxtLink class="edit-link" href="/mypage/pwd-check">변경</NuxtLink></span>
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
        <button @click="openDeleteModal" class="delete-account-button">계정 삭제하기</button>
      </section>

      <transition name="modal">
        <div v-if="showDeleteModal" class="modal-background">
          <div class="modal-content">
            <h2>정말 삭제하시겠습니까?</h2>
            <div class="modal-buttons">
              <button @click="confirmDelete" class="confirm-button">확인</button>
              <button @click="closeDeleteModal" class="cancel-button">취소</button>
            </div>
          </div>
        </div>
      </transition>
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

   /* 모달 스타일 */
 .modal-background {
   position: fixed;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   background-color: rgba(0, 0, 0, 0.5);
   display: flex;
   justify-content: center;
   align-items: center;
   z-index: 1000;
 }

.modal-content {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  max-width: 400px;
  width: 100%;
  text-align: center;
}

.modal-buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.confirm-button {
  background-color: #ff4d4f;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.cancel-button {
  background-color: #ccc;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
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
const showDeleteModal = ref(false);
const userDetails = useUserDetails();


const response = await useDataFetch(`members/${memberId}`);
// console.log("응답 데이타", response);
data.value = response;
console.log(data.value);

const openDeleteModal = () => {
  showDeleteModal.value = true;
};

const closeDeleteModal = () => {
  showDeleteModal.value = false;
};

const confirmDelete = async () => {

  const response = await useDataFetch(`members/${memberId}`, {
    method: "DELETE",
  })
  console.log("계정 삭제 확인");
  closeDeleteModal();
  userDetails.logout();
  if (process.client) {
    navigateTo("/signin");
  }
};

</script>