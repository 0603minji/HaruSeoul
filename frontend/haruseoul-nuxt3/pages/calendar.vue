<script setup>
</script>
<template>
  <main>
    calendar
    <div class="container">
      <FilterCalendar/>
    </div>
    <br>
    <br>
  </main>
</template>
<style scoped>
.container {
  display: flex;
  justify-content: center;

  margin: 0 100px;

  > section {
    //flex-grow: 1;
  }
}
</style>