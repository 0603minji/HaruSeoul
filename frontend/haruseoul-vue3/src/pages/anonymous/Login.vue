<template>
  <main>
    <h1>로그인</h1>
    <form class="login-form">

      <fieldset class="input-group">
        <legend>호스트 로그인</legend>
        <label>
          아이디
          <input type="text" name="hostId" placeholder="아이디 입력">
        </label>

        <label>
          비밀번호
          <input type="password" name="hostPwd" placeholder="비밀번호 입력">
        </label>
      </fieldset>

      <div class="options">
        <label>
          <input type="checkbox" name="rememberMe">
          로그인 유지
        </label>
        <a href="#">아이디 찾기</a>
        <a href="#">비밀번호 찾기</a>
      </div>

      <button>호스트 로그인</button>

    </form>
    <div class="separator">또는</div>

    <nav>
      <h1>소셜 로그인</h1>
      <ul class="social-login">
        <li><a href="" class="social-btn google"><img src="" alt="google"/></a></li>
        <li><a href="" class="social-btn kakao"><img src="" alt="kakao"/></a></li>
        <li><a href="" class="social-btn naver"><img src="" alt="naver"/></a></li>
      </ul>
    </nav>

    <div><a href="" class="signup-button">호스트로 가입하기</a></div>
  </main>
</template>

<style scoped>
body {
  font-family: Arial, sans-serif;
  background-color: #f9f9f9;
  margin: 0;
  padding: 0;
}

h1 {
  font-size: 24px;
  margin: 0;
}

.login-form {
  margin-top: 20px;
}

.input-group {
  margin-bottom: 15px;
}

.input-group label {
  display: block;
  font-size: 14px;
  margin-bottom: 5px;
}

.input-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.options {
  display: flex;
}
</style>