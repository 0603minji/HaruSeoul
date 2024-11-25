<template>
  <main>
    <div class="container">
      <section class="profile-header">
        <div class="profile-image-container" :key="profileImageKey">
          <img
              v-if="userDetails.profileImgSrc.value"
              class="profile-image"
              :src="`http://localhost:8083/api/v1/${userDetails.profileImgSrc.value}`"
              alt="프로필 이미지"/>
          <img v-else class="profile-image" src="/assets/image/default-profile.png" alt="프로필 이미지"/>
          <input
          type="file"
          accept="image/*"
          class="n-icon n-icon:edit"
          @change="handleProfileImageChange"
          />
        </div>
        <div class="profile-name">
          <!-- 읽기 모드 -->
          <template v-if="!editingNickname">
            <p>{{ data.nickname }}</p>
            <span class="n-icon n-icon:edit" style="cursor: pointer" @click.prevent="enableEdit">✏️</span>
          </template>

          <!-- 편집 모드 -->
          <template v-else>
            <div>
            <input
                type="text"
                v-model="editedNickname"
                placeholder="Enter new nickname"
                style="border: 1px solid; position: relative"
            />
            <button style="cursor: pointer; position: relative; right: 20px" @click.prevent="cancelEdit">x</button>
            </div>
            <button class="n-btn n-btn-background-color:sub" @click.prevent="updateNickname">확인</button>
          </template>
        </div>
      </section>
      <section class="profile-info">
        <div class="info-row">
          <span class="label">아이디</span>
          <span class="value">{{ data.userId }}</span>
        </div>
        <div class="info-row">
          <span class="label">비밀번호</span>
          <span class="value"><NuxtLink class="edit-link" href="/mypage/pwd-check">변경</NuxtLink></span>
        </div>
        <div class="info-row">
          <span class="label">이름</span>
          <span class="value">{{ data.name }}</span>
        </div>
        <div class="info-row">
          <span class="label">연락처</span>
          <span class="value">+82 01012345678</span>
        </div>
        <div class="info-row">
          <span class="label">이메일</span>
          <span class="value">{{ data.email }}</span>
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

<script setup>

import {useDataFetch} from "~/composables/useDataFetch.js";
import axios from "axios";

const memberId = localStorage.getItem("id");
const token = localStorage.getItem("token");
const data = ref({});
const showDeleteModal = ref(false);
const userDetails = useUserDetails();
const nickname = ref(); // 기존 닉네임
const editedNickname = ref(nickname.value); // 수정할 닉네임 값
const editingNickname = ref(false);
const profileImageKey = ref(0);


const response = await useDataFetch(`members/${memberId}`);
// console.log("응답 데이타", response);
data.value = response;


const enableEdit = () => {
  editingNickname.value = true;
  editedNickname.value = nickname.value; // 기존 닉네임 복사
};

const cancelEdit = () => {
  editingNickname.value = false; // 편집 모드 종료
  editedNickname.value = nickname.value; // 변경된 닉네임 초기화
};


// 닉네임 업데이트
const updateNickname = async () => {
  const formData = new FormData();

  const updateNickname = {
    id:memberId,
    nickname: editedNickname.value,
  }

  formData.append("memberUpdateDto",
      new Blob([JSON.stringify(updateNickname)], {type: "application/json"}));

  if (editedNickname.value.trim() === "") {
    alert("닉네임을 입력해주세요.");
    return;
  }
  try {
    await axios.put(
        `http://localhost:8083/api/v1/members/${memberId}`,formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
    );
    data.value.nickname = editedNickname.value;
    editingNickname.value = false; // 편집 모드 종료
    alert("닉네임이 성공적으로 변경되었습니다.");
  } catch (error) {
    console.error("닉네임 변경 중 오류 발생:", error);
    alert("닉네임 변경에 실패했습니다. 다시 시도해주세요.");
  }
};
// 프사 변경
const handleProfileImageChange = async (event) => {
  const file = event.target.files[0];
  if (!file) {
    alert("파일을 선택해주세요.");
    return;
  }

  const formData = new FormData();
  const updateProfileImg = {
    id:memberId,
  }

  formData.append(
      "memberUpdateDto",
      new Blob([JSON.stringify(updateProfileImg)], { type: "application/json" })
  );

  formData.append("profileImgSrc", file);

  try {
    const response = await axios.put(
        `http://localhost:8083/api/v1/members/${memberId}`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
    );

    // 새 데이터 가져오기
    const updatedData = await useDataFetch(`members/${memberId}`);
    console.log("업데이트된 데이터:", updatedData);

    // 전역 상태 업데이트 및 Vue 강제 갱신
    userDetails.profileImgSrc.value = ''; // 초기화
    setTimeout(() => {
      userDetails.profileImgSrc.value = `${updatedData.profileImgSrc}?timestamp=${new Date().getTime()}`;
    }, 0);


    console.log("전역 상태 profileImgSrc:", userDetails.profileImgSrc);
    alert("프로필 이미지가 성공적으로 변경되었습니다.");
  } catch (error) {
    console.error("프로필 이미지 변경 중 오류 발생:", error);
    alert("프로필 이미지 변경에 실패했습니다. 다시 시도해주세요.");
  }
};



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