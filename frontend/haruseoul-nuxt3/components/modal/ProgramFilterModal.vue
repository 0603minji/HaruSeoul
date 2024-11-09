<script setup>
// emit
const emit = defineEmits(['closeModal']);

// Props
const props = defineProps({
  hostId: {
    type: Number,
    required: true
  }
});

import SearchableMultiSelect from "~/components/filter/SearchableMultiSelect.vue";
import DateRangePicker from "~/components/filter/DateRangePicker.vue";
// Sample options for the select box
const programs = ref([
  {id: 1, name: 'Program A'},
  {id: 2, name: 'Program B'},
  {id: 3, name: 'Program C'},
  {id: 4, name: 'Program D'},
  {id: 5, name: 'Program E'},
  {id: 6, name: 'Program F'},
  {id: 7, name: 'Program G'},
  {id: 8, name: 'Program H'},
  {id: 9, name: 'Program I'},
  {id: 10, name: 'Program J'},
  {id: 11, name: 'Program K'},
  {id: 12, name: 'Program L'},
  {id: 13, name: 'Program M'},
  {id: 14, name: 'Program N'},
  {id: 15, name: 'Program O'},
  {id: 16, name: 'Program P'},
  {id: 17, name: 'Program Q'},
  {id: 18, name: 'Program R'},
  {id: 19, name: 'Program S'},
  {id: 20, name: 'Program T'},
  {id: 21, name: 'Program U'},
  {id: 22, name: 'Program V'},
  {id: 23, name: 'Program W'},
  {id: 24, name: 'Program X'},
  {id: 25, name: 'Program Y'},
  {id: 26, name: 'Program Z'},
]);

// initial
const initial = [{id: 4, name: 'Program D'}, {id: 9, name: 'Program I'}];

// Selected option
const selectedPrograms = ref([...initial]);

// Selected dates 프로그램 검색기간 [시작일 , 끝] or [날짜]
let selectedDates = [];

// Handle selection change
const updateSelection = (selectedOptions) => {
  selectedPrograms.value = selectedOptions;
  console.log('******* updateSelection called', selectedPrograms.value);
};

const updateSelectedDates = (selectedOptions) => {
  selectedDates = selectedOptions;
  console.log('******* ProgramFilterModal: updateSelectedDates called');
  console.log('         ->  selectedDates: ', selectedDates);
};

</script>

<template>
  <aside class="popup modal">
    <header class="popup-header">
      <h1 class="font-size:9">필터</h1>
      <button @click.prevent="emit('closeModal')" class="n-btn n-btn:hover n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form class="popup-body" action="">
      <!--프로그램 선택-->
      <SearchableMultiSelect :options="programs" :initial-options="initial"
                             @selection-changed="updateSelection"/>

      <DateRangePicker :host-id="props.hostId" @selection-changed="updateSelectedDates"/>

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