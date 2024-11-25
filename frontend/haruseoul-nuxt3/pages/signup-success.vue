<template>
  <main class="container">
    <img src="/assets/image/icon/checked.png" alt="">
    <div class="message">
      Your account was successfully created!
    </div>
    <RouterLink to="/signin" class="n-btn n-btn-background-color:main">
      Back to Login
    </RouterLink>
  </main>
</template>


<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
  height: 70vh;
  background-color: #ffffff;
}

.message {
  padding: 10px;
  font-size: 40px;
  font-weight: bold;
  color: #333;
  margin-bottom: 40px;
}
</style>