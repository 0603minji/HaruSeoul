<template>
  <main class="container">
    <div class="title">Create an account</div>

    <form @submit.prevent="submitHandler">
      <div class="form-group">
        <label for="name">Name</label>
        <input v-model="name" type="text" id="name" placeholder="Name" pattern="[A-Za-z]+"
               title="Please enter English letters only." required>
      </div>

      <div class="form-group">
        <label for="id">ID</label>
        <div class="button-container">
          <input v-model="userId" type="text" id="id" placeholder="ID" pattern="[A-Za-z0-9]+"
                 title="Please enter English letters and numbers only." required>
          <button @click="validHandler" type="button" class="n-btn n-btn-pg-filter active">Verify</button>
        </div>
        <p v-if="isChecked" class="success-message">중복확인 완료</p>
        <p v-if="!(isChecked)" class="fail-message">중복확인을 해주세요.</p>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input
            type="password"
            id="password"
            placeholder="Password"
            v-model="password"
            pattern="(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+"
            title="Password must contain English letters, numbers, and special characters."
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
        <input v-model="nickname" type="text" id="nickname" placeholder="Nickname" pattern="[A-Za-z0-9]+"
               title="Please enter English letters and numbers only." required>
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input v-model="email" type="email" id="email" placeholder="Your Email" required>
      </div>

      <div class="form-group">
        <label for="birth">Birth</label>
        <input v-model="birth" type="date" id="birth" placeholder="birth" required>
      </div>

      <button type="submit" class="n-btn n-btn-background-color:main n-btn-size:3">Register</button>
    </form>
  </main>

</template>

<script setup>
import {ref, computed, watch} from "vue";
import axios from "axios";

// 리액티브 변수 선언

const password = ref("");
const confirmPassword = ref("");
const name = ref("");
const userId = ref("");
const nickname = ref("");
const email = ref("");
const birth = ref("");
const isChecked = ref(false);


watch(userId, () => {
  isChecked.value = false;
});


// 비밀번호 일치 여부 계산 속성
const isMatch = computed(() => password.value === confirmPassword.value);

// 메시지 계산 속성
const message = computed(() => {
  if (!confirmPassword.value) return "";
  return isMatch.value ? "password correct " : "password incorrect";
});

const submitHandler = async () => {
  if (!isMatch.value || !isChecked.value) { // 중복 확인을 완료하지 않았다면 진행 불가
    alert("아이디 중복 확인을 해주세요.");
    return;
  }

  const memberData = {
    name: name.value,
    userId: userId.value,
    userPwd: password.value,
    nickname: nickname.value,
    email: email.value,
    birth: birth.value,
  };

  try {
    const response = await axios.post('http://localhost:8083/api/v1/members/signup', memberData);
    // 회원가입 성공 시 http://localhost:5173/success 페이지로 리디렉션
    navigateTo('/signup-success');
  } catch (error) {
    alert("입력값을 확인해주세요")
    // 실패 시 처리 로직 추가 가능
  }
};

const validHandler = async () => {
  const userIdData = {userId: userId.value};

  try {
    const response = await axios.post('http://localhost:8083/api/v1/members/idvalid', userIdData);
    alert("중복확인 완료");
    isChecked.value = true;
  } catch (error) {
    if (error.response && error.response.status === 409) { // 상태 코드 409이면 중복된 ID임을 표시
      alert("이미 존재하는 ID입니다.");
      isChecked.value = false;
    } else {
      alert("오류가 발생했습니다. 다시 시도해주세요.");
    }
  }
};


</script>
<style scoped>
.container {
  width: 100%;
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
    input[type="password"],
    input[type="email"],
    input[type="tel"],
    input[type="date"]
    {
      width:100%;
      padding: 10px;
      font-size: 1em;
      border: 1px solid #ddd;
      border-radius: 5px;
      box-sizing: border-box;
    }

    .button-container {
      gap: 1rem;
      display: flex;
    }

  }

}


.title{
  margin-bottom: 20px;
  font-weight: bold;
  font-size: 1.5em;
}


label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.error-message {
  padding: 5px;
  font-size: 0.85em;
}

.error-message.error {
  color: red; /* 빨간색 */
}

.error-message.success {
  color: #2ecc71; /* 초록색 */
}

.n-btn{
  border:none;
}

.n-btn-background-color\:main{
  margin: 20px 0;
  box-sizing: border-box;
  width: 100%;
}

.success-message {
  color: blue;
  font-size: 1em;
  margin: 5px;
}

.fail-message {
  color: red;
  font-size: 1em;
  margin: 5px;
}


@media (min-width: 768px) {
  .container {
    max-width: 600px;
    margin: 0 auto; /* 가운데 정렬 */
  }
}
</style>