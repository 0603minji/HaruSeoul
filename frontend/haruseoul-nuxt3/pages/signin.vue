<template>
  <main class="container">
    <div class="title">Welcome</div>

    <form @submit.prevent="signinHandler">
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
<!--        <div class="remember-me">-->
<!--          <input type="checkbox" id="remember-me" v-model="rememberMe">-->
<!--          <label for="remember-me">Remember me</label>-->
<!--        </div>-->
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
import {jwtDecode} from "jwt-decode";


const userId = ref('');
const userPwd = ref('');
const route = useRoute();
const userDetails = useUserDetails();
// const rememberMe = ref(false);


const signinHandler = async () => {

  
  console.log("로그인 버튼 작동 중");

  try {
    let response = await useDataFetch("auth/signin", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: {
        userId: userId.value,
        userPwd: userPwd.value
      }
    });

    let userInfo = jwtDecode(response.token);
    userDetails.login({
      id: userInfo.id,
      userId: userInfo.userId,
      username: userInfo.name,
      birth: userInfo.birth,
      email: userInfo.email,
      profileImgSrc: userInfo.profileImgSrc,
      token: response.token
    });

    console.log("회원 정보:", userInfo);
    // 온전한 returnURL 복구
  console.log('조각난 리턴url: ', route.query);
  let originalReturnURL = route.query.returnURL; // host/reservations?p=1
  Object.entries(route.query)
      .filter(([key, value]) => key !== 'returnURL')
      .forEach(([key, value]) => {
        originalReturnURL += `&${key}=${value}`;
      });
  console.log('originalReturnUrl: ', originalReturnURL);

  return navigateTo(originalReturnURL);

  } catch (error) {
    if (error.response && error.response.status === 401) {
      alert("존재하지 않는 아이디입니다.");
    } else {
      console.error("로그인 중 오류 발생:", error);
      alert("로그인에 실패했습니다. 다시 시도해주세요.");
    }
  }
};

</script>

<style scoped>
.container {
  max-width: 490px;
  min-width: 400px;
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
