<script setup>
const props = defineProps({
  isVisible: { type: Boolean, required: true },

});
const emit = defineEmits(['close', 'confirm']);

const closeModal = () => {
  emit('close');
};

const confirmModal = () => {
  console.log('       Modal confirmModal called. emit confirm');
  emit('confirm');
};
</script>

<template>
  <div v-if="isVisible" class="modal-backdrop" @click.self="closeModal">
    <div class="modal-content">
      <slot></slot>
      <div class="buttons">
        <button class="close-button n-btn n-btn:hover" @click="closeModal">닫기</button>
        <button class="confirm-button n-btn n-btn:hover n-btn-bg-color:main" @click="confirmModal">확인</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  position: relative;
  z-index: 100;
  min-width: 300px;
  max-width: 360px;
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 24px;

  .close-button {
    flex-grow: 1;
    padding: 16px 30px;
  }
  .confirm-button {
    flex-grow: 1;
    padding: 16px 30px;
  }
}

.modal-backdrop.onlyConfirm {
  .close-button {
    display: none;
  }
}

</style>
