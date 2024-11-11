<script setup>
import SearchableMultiSelect from "~/components/filter/SearchableMultiSelect.vue";

// emit
const emit = defineEmits(['closeModal', 'updateSelectedPrograms']);

// Props
const props = defineProps({
  selectedPrograms: {
    type: Array,
    default: []
  }
});




//=== 변수 ==============================================================================================================
// Selected option
const selectedPrograms = ref(props.selectedPrograms);






// === function ========================================================================================================
// Handle selection change
const updateSelection = (selectedOptions) => {
  selectedPrograms.value = selectedOptions;
  console.log('******* updateSelection called', selectedPrograms.value);
  emit('updateSelectedPrograms', selectedPrograms);
};


</script>

<template>
  <aside class="popup modal">
    <header class="popup-header">
      <h1 class="font-size:9">필터</h1>
      <button @click.prevent="emit('closeModal')" class="n-btn n-btn:hover n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form @submit.prevent="emit('closeModal')" class="popup-body" action="">
      <!--프로그램 선택-->
      <SearchableMultiSelect :selected-options="selectedPrograms"
                             @selection-changed="updateSelection"/>

      <div class="submit">
        <button class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1">확인</button>
      </div>
    </form>
  </aside>
</template>

<style scoped>
.modal {
  display: none; /* Hidden by default */
  position: fixed;
  z-index: 1000; /* Sit on top */
  left: 50%;
  bottom: 0;
  transform: translate(-50%, 100%); /* 초기에 바닥에 숨어있음 */
  width: 100%;
  min-width: 350px;
  max-width: 500px;
  max-height: 100vh;
  //background-color: white;
  //border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease;
}

.modal.show {
  display: flex;
  transform: translate(-50% ,0); /* 아래에서 등장 */
}

.popup {
  border-radius: 16px 16px 0 0;
  background-color: var(--color-base-1);
  //padding: 16px 24px;
  display: flex;
  flex-direction: column;

  .popup-header {
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    margin-bottom: 10px;

    button {
      position: absolute;
      right: 14px;
      --icon-color: var(--color-base-5);
    }

    button:hover {
      --icon-color: var(--color-base-7);
    }
  }

  .popup-body {
    display: flex;
    flex-direction: column;
    padding: 0 10px;
    overflow-y: auto;

    .submit {
      margin-left: auto;
      padding: 20px 0;
    }
  }
}
</style>