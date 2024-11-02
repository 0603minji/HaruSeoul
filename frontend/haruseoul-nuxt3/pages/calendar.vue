<script setup>
import CalendarV2 from "~/components/filter/CalendarV2.vue";
</script>
<template>
  <main>
    calendar
    <div class="container">
      <FilterCalendar/>
      <CalendarV2 />
    </div>
    <br>
    <br>
  </main>
</template>
<style scoped>
.container {
  display: flex;
  gap: 40px;
  flex-direction: column;
  justify-content: center;

  margin: 0 100px;

  > section {
    //flex-grow: 1;
  }
}
</style>