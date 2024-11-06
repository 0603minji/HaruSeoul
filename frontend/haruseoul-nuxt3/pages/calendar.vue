<script setup>
import { ref } from 'vue';
import CalendarV2 from "~/components/filter/CalendarV2.vue";
import PublishProgramModal from "~/components/modal/PublishProgramModal.vue"
import ProgramFilterModal from "~/components/modal/ProgramFilterModal.vue";

const isPublishProgramModalVisible = ref(false);
const isProgramFilterModalVisible = ref(false);

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
      <PublishProgramModal v-if="isPublishProgramModalVisible" :class="{'show': isPublishProgramModalVisible}" :default-program-id="pIdTobePublished" :host-id="hostId" @close-modal="isPublishProgramModalVisible=false"/>

<!--      <ProgramFilterModal />-->
    </div>
    <div :class="{'active': isPublishProgramModalVisible || isProgramFilterModalVisible}" class="backdrop"></div>
  </main>
</template>
<style scoped>
.backdrop {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Dark background */
  backdrop-filter: blur(5px); /* Blur effect */
  z-index: 999; /* Behind modal but above other content */
}
.backdrop.active {
  display: block;
}

.container {
  display: flex;
  gap: 40px;
  flex-direction: column;
  justify-content: center;
  margin: 0 100px 50px 100px;
}
</style>