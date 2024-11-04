<script setup>
import SearchableMultiSelect from "~/components/filter/SearchableMultiSelect.vue";
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

// Handle selection change
const updateSelection = (selectedOptions) => {
  selectedPrograms.value = selectedOptions;
  console.log('updateSelection called', selectedPrograms.value);
};

</script>

<template>
  <aside class="popup">
    <header class="popup-header">
      <h1 class="font-size:9">필터</h1>
      <button class="n-btn n-btn:hover n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form class="popup-body" action="">
      <!--프로그램 선택-->
      <SearchableMultiSelect :options="programs" :initial-options="initial"
                             @selection-changed="updateSelection"/>

      <FilterCalendarV2/>

      <div class="submit">
        <button class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1">확인</button>
      </div>
    </form>
  </aside>
</template>

<style scoped>
.popup {
  border: 1px solid black;
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

    button {
      position: absolute;
      right: 14px;
    }
  }

  .popup-body {
    display: flex;
    flex-direction: column;
    padding: 0 10px;

    .submit {
      margin-left: auto;
      padding: 20px 0;
    }
  }
}
</style>