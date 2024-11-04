<template>
  <main class="container">
    <div class="title">Welcome</div>

    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="id">ID</label>
        <div class="button-container">
          <input type="text" id="id" v-model="userId" placeholder="Enter your ID" required>
        </div>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <div class="button-container">
          <input type="password" id="password" v-model="userPwd" placeholder="Enter your Password" required>
        </div>
      </div>

      <div class="additional-options">
        <div class="remember-me">
          <input type="checkbox" id="remember-me" v-model="rememberMe">
          <label for="remember-me">Remember me</label>
        </div>
        <div class="links">
          <a href="#">Forgot ID</a>
          <a href="#">Forgot Password</a>
        </div>
      </div>

      <button type="submit" class="n-btn n-btn-background-color:main n-btn-size:3">Login</button>

      <div class="divider">
        <span>or</span>
      </div>

      <RouterLink to="/signup" class="n-btn n-btn-background-color:sub n-btn-size:3">Create an account</RouterLink>
    </form>
  </main>
</template>

<script setup>
import {ref} from 'vue';
import axios from 'axios';

const userId = ref('');
const userPwd = ref('');
const rememberMe = ref(false);


const handleLogin = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/v1/signin', {
      userId: userId.value,
      userPwd: userPwd.value,
    }, { withCredentials: true }); // 세션 기반 인증을 위한 옵션

    if (response.status === 200) {
      alert('로그인 성공!');
      navigateTo('/programs');
    }
  } catch (error) {
    console.log(userId.value,userPwd.value);

    alert('로그인 정보가 올바르지 않습니다.');
  }


};
</script>

<style scoped>
.container {
  width: 490px;
  margin: auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  text-align: center;

  .form-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 15px;
    text-align: left;

    input[type="text"],
    input[type="password"] {
      width: 100%;
      padding: 10px;
      font-size: 1em;
      border: 1px solid #ddd;
      border-radius: 5px;
      box-sizing: border-box;
    }
  }

  .additional-options {
    padding: 0 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 14px;
    margin-bottom: 50px;

    .remember-me {
      display: flex;
      align-items: center;

      input[type="checkbox"] {
        margin-right: 5px;
      }
    }

    .links {
      display: flex;
      gap: 15px;

      a {
        color: #888;
        text-decoration: none;
        font-size: 0.9em;
      }
    }
  }

  .divider {
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 20px 0;
    font-size: 0.9em;
    color: #888;

    span {
      padding: 0 10px;
    }

    &::before,
    &::after {
      content: "";
      flex: 1;
      height: 1px;
      background-color: #ddd;
    }
  }
}

.title {
  margin-bottom: 40px;
  font-weight: bold;
  font-size: 2em;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.n-btn-background-color\:main {
  margin: 20px 0;
  box-sizing: border-box;
  width: 100%;
}
.n-btn-background-color\:sub {
  margin: 20px 0;
  box-sizing: border-box;
  width: 100%;
  background-color: white;
  border-color: var(--color-main-1);
  color: var(--color-base-9);

}
</style>
