<template>
  <main>
    <section class="container">
      <h1 class="title">Password Check</h1>
      <label class="pwd-input">
        <input
            type="password"
            placeholder="Enter your current password"
            v-model="currentPassword"
        />
      </label>
      <p v-if="errorMessage" class="error-message">
        <span>⚠️</span> {{ errorMessage }}
      </p>
      <div class="button">
        <button class="n-btn n-btn-bg-color:sub" @click="confirmPassword">Verify</button>
      </div>
    </section>
  </main>
</template>

<script setup>
import {ref} from 'vue';
import {useDataFetch} from "~/composables/useDataFetch.js";

const currentPassword = ref('');
const errorMessage = ref('');
const memberId = localStorage.getItem("id");

const confirmPassword = async () => {
  try {
    const response = await useDataFetch('members/checkPwd', {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: {
        id: memberId,
        currentPwd: currentPassword.value
      }
    });

    // 서버가 200 OK를 반환하면 비밀번호가 일치하는 경우
    errorMessage.value = '';
    alert("비밀번호가 일치합니다.");
    return navigateTo("/mypage/change-pwd")

  } catch (error) {
    if (error.response) {
      errorMessage.value = "Incorrect Password";
    }
  }
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
</style>
