<script setup>
import { ref } from 'vue';
import CalendarV2 from "~/components/filter/CalendarV2.vue";
import PublishProgramModal from "~/components/modal/PublishProgramModal.vue"
import ProgramFilterModal from "~/components/modal/ProgramFilterModal.vue";

const isPublishProgramModalVisible = ref(false);
const hostId = 1;
const pIdTobePublished = ref(null);

const OpenpublishProgramModalHandler = (pId) => {
  isPublishProgramModalVisible.value = true;
  pIdTobePublished.value = pId;
};

</script>
<template>
  <main>
    calendar
    <div class="container">
<!--      <CalendarV2 />-->

      <button @click.prevent="OpenpublishProgramModalHandler(16)" class="n-btn n-btn-background-color:sub n-btn:hover">개설하기</button>
      <PublishProgramModal v-if="isPublishProgramModalVisible" :default-program-id="pIdTobePublished" :host-id="hostId" @close-modal="isPublishProgramModalVisible=false"/>

<!--      <ProgramFilterModal />-->
    </div>
  </main>
</template>
<style scoped>
.container {
  display: flex;
  gap: 40px;
  flex-direction: column;
  justify-content: center;
  margin: 0 100px 50px 100px;
}
</style>