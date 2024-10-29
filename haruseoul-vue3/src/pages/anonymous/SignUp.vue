<template>
  <main class="container">
    <div class="title">Create an account</div>

    <form @submit.prevent="submitHandler">
      <div class="form-group">
        <label for="name">Name</label>
        <input v-model="name" type="text" id="name" placeholder="Name" required>
      </div>

      <div class="form-group">
        <label for="id">ID</label>
        <div class="button-container">
        <input v-model="userId" type="text" id="id" placeholder="ID" required>
        <button type="button" class="n-btn n-btn-pg-filter active">Verify</button>
        </div>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input
            type="password"
            id="password"
            placeholder="Password"
            v-model="password"
            required
        />
      </div>

      <div class="form-group">
        <label for="confirm-password">Confirm Password</label>
        <input
            type="password"
            id="confirm-password"
            placeholder="Password"
            v-model="confirmPassword"
            required
        />
        <div
            class="error-message"
            :class="{ success: isMatch, error: !isMatch && confirmPassword }"
        >
          {{ message }}
        </div>
      </div>

      <div class="form-group">
        <label for="nickname">Nickname</label>
        <input v-model="nickname" type="text" id="nickname" placeholder="Nickname" required>
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input v-model="email" type="email" id="email" placeholder="Your Email" required>
      </div>

      <div class="form-group">
        <label for="birth">Birth</label>
        <input v-model="birthday" type="date" id="birth" placeholder="birth" required>
      </div>

      <button type="submit" class="n-btn n-btn-background-color:main n-btn-size:3" :disabled="!isMatch">Register</button>
    </form>
  </main>

</template>

<script setup>
import {ref, computed} from "vue";
import axios from "axios";

// 리액티브 변수 선언

const password = ref("");
const confirmPassword = ref("");
const name = ref("");
const userId = ref("");
const nickname = ref("");
const email = ref("");
const birthday = ref("");


// 비밀번호 일치 여부 계산 속성
const isMatch = computed(() => password.value === confirmPassword.value);

// 메시지 계산 속성
const message = computed(() => {
  if (!confirmPassword.value) return "";
  return isMatch.value ? "password correct " : "password incorrect";
});

const submitHandler = async () => {
  if (!isMatch.value) return;

  const memberData = {
    name: name.value,
    userId: userId.value,
    userPwd: password.value,
    nickname: nickname.value,
    email: email.value,
    birthday: birthday.value,
  };

  try {
    const response = await axios.post('http://localhost:8080/api/v1/members/signup', memberData);
    // 회원가입 성공 시 http://localhost:5173/success 페이지로 리디렉션
    window.location.href = "http://localhost:5173/success";
  } catch (error) {
    alert("입력값을 확인해주세요")
    // 실패 시 처리 로직 추가 가능
  }
};
</script>


<style scoped>
@import url("/css/program/signup.css");
</style>