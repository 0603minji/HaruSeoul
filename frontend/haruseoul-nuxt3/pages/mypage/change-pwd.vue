<template>
  <main>
    <section class="container">
      <h1 class="title">Change Password</h1>
      <div class="caution">
        <p class="caution-title">Caution</p>
        <ul>
          <li>Password must contain English letters, numbers, and special characters.</li>
        </ul>
      </div>
      <label class="pwd-input">
        <input
            type="password"
            v-model="newPassword"
            placeholder="Enter your new password"
            title="Password must contain English letters, numbers, and special characters."
            required
        />
      </label>
      <label class="pwd-input">
        <input
            type="password"
            v-model="confirmPassword"
            placeholder="Confirm new password"
            title="Password must contain English letters, numbers, and special characters."
            required
        />
      </label>
      <p v-if="errorMessage" class="error-message">
        <span>⚠️</span> {{ errorMessage }}
      </p>
      <div class="button">
        <button :disabled="!isPasswordMatch" class="n-btn n-btn-bg-color:sub n-btn-size:3" @click="openConfirmationModal">Verify</button>
      </div>
    </section>
    <transition name="modal">
      <div v-if="showModal" class="modal-background">
        <div class="modal-content">
          <h2>정말 비밀번호를 변경하시겠습니까?</h2>
          <div class="modal-buttons">
            <button @click="confirmPasswordChange" class="confirm-button">확인</button>
            <button @click="closeModal" class="cancel-button">취소</button>
          </div>
        </div>
      </div>
    </transition>
  </main>
</template>

<script setup>

import axios from "axios";

const newPassword = ref('');
const confirmPassword = ref('');
const errorMessage = ref('');
const passwordPattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$/;
const showModal = ref(false);
const memberId = localStorage.getItem("id");
const userDetails = useUserDetails();
const token = localStorage.getItem("token");

const confirmPasswordChange = async () => {
  if (isPasswordMatch.value && isPasswordValid.value) {
    const formData = new FormData();

    const updateMemberData = {
      id: memberId,
      newPwd: newPassword.value,
    };

    formData.append(
        "memberUpdateDto",
        new Blob([JSON.stringify(updateMemberData)], { type: "application/json" })
    );

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

      alert("비밀번호가 성공적으로 변경되었습니다.");
      closeModal();
      userDetails.logout();
      return navigateTo("/signin");
    } catch (error) {
      console.error("비밀번호 변경 중 오류 발생:", error);
      errorMessage.value = "비밀번호 변경에 실패했습니다. 다시 시도해주세요.";
    }
  } else {
    alert("비밀번호가 유효하지 않습니다. 다시 확인해주세요.");
  }
};


const isPasswordMatch = computed(() => {
  if (!newPassword.value || !confirmPassword.value) {
    errorMessage.value = "Please fill in both password fields.";
    return false;
  } else if (newPassword.value !== confirmPassword.value) {
    errorMessage.value = "Passwords do not match.";
    return false;
  } else {
    errorMessage.value = "";
    return true;
  }
});

const isPasswordValid = computed(() => {
  if (!newPassword.value.match(passwordPattern)) {
    errorMessage.value = "Password must contain English letters, numbers, and special characters.";
    return false;
  }
  return true;
});

const openConfirmationModal = () => {
  if (isPasswordMatch.value && isPasswordValid.value) {
    showModal.value = true;
  }
};


// 모달 닫기
const closeModal = () => {
  showModal.value = false;
};



</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  //max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  gap: 15px;
}

.caution {
  display: flex;
  flex-direction: column;
  background-color: var(--color-main-4);
  border-radius: 10px;

  ul{
    list-style: disc;
    padding: 20px;
    align-content: center;
  }
}
.caution-title {
  align-self: center;
  font-size: 1.2rem;
  padding: 0.5rem;
}

.title {
  text-align: center;
  font-weight: bold;
  font-size: 20px;
}
.pwd-input{
  width: 100%;
  max-width: 500px;
}

.pwd-input input {
  width: 100%;
  padding: 12px; /* 패딩을 조금 더 넓게 설정 */
  border: 1px solid #ccc;
  font-size: 16px; /* 폰트 크기를 약간 키움 */
  height: 48px; /* 입력 박스 높이를 명시적으로 설정 */
  text-align: center;
  box-sizing: border-box; /* 패딩이 입력 필드 크기에 포함되도록 설정 */
}

.error-message {
  color: #e74c3c;
  font-size: 12px;
  text-align: center;
  margin-top: 5px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.button {
  width: 200px;
  display: flex;
  justify-content: center;
}


.n-btn:hover {
  background-color: #0056b3;
}
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
  background-color: #4caf50;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.cancel-button {
  background-color: #f44336;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>