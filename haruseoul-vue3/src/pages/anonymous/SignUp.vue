<template>
  <main class="container">
    <div class="title">Create an account</div>
    <form>
      <div class="form-group">
        <label for="name">Name</label>
        <input type="text" id="name" placeholder="Your Last Name" required>
      </div>

      <div class="form-group">
        <label for="id">ID</label>
        <input type="text" id="id" placeholder="ID" required>
        <button type="button" class="n-btn n-btn-background-color:sub">Verify</button>
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input type="password" id="password" placeholder="Password" required>
        <div class="error-message">8 characters minimum</div>
      </div>

      <div class="form-group">
        <label for="confirm-password">Confirm Password</label>
        <input type="password" id="confirm-password" placeholder="Password" required>
      </div>

      <div class="form-group">
        <label for="nickname">Nickname</label>
        <input type="text" id="nickname" placeholder="Nickname">
      </div>
      <div class="form-group">
        <label for="email">Email</label>
        <input type="email" id="email" placeholder="Your Email" required>
      </div>

      <div class="form-group">
        <label for="phone">Phone Number</label>
        <input type="tel" id="phone" placeholder="(82) 010-1111-2222">
      </div>

      <button type="submit" class="n-btn n-btn-background-color:main">Register</button>
    </form>
  </main>

</template>
<script>
import axios from "axios";

async function signUp(name,userId,userPwd,nickname,email,birthday){
  const res = await axios.post('http://localhost:8080/api/v1/members/signup',
  name,userId,userPwd,nickname,email,birthday);
}
</script>


<style scoped>
@import url("/css/program/signup.css");
</style>