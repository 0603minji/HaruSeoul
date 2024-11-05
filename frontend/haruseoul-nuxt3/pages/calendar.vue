<script setup>
import { ref } from 'vue';
import CalendarV2 from "~/components/filter/CalendarV2.vue";
import PublishProgramModal from "~/components/modal/PublishProgramModal.vue"
import ProgramFilterModal from "~/components/modal/ProgramFilterModal.vue";

const isPublishProgramModalVisible = ref(false);
const hostId = 1;
const pIdTobePublished = ref(null);

const publishProgramHandler = (pId) => {
  isPublishProgramModalVisible.value = true;
  pIdTobePublished.value = pId;
};

</script>
<template>
  <main>
    calendar
    <div class="container">
<!--      <CalendarV2 />-->

      <button @click.prevent="publishProgramHandler(16)">개설하기</button>
      <PublishProgramModal v-if="isPublishProgramModalVisible" :default-program-id="pIdTobePublished" :host-id="hostId"/>

<!--      <ProgramFilterModal />-->
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