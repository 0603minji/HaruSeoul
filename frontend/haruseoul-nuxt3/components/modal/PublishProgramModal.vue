<script setup>
import SearchableSelect from "~/components/filter/SearchableSelect.vue";
// 부모로부터 props로 전달받을 것
// 디폴트 개설할 프로그램 -> SearchableSelect
// 그 외 호스트가 개설가능한 모든 프로그램들 -> SearchableSelect
// 개설가능한 기간 내 이미 개설된 프로그램들 -> Calendar
// submit -> emit 'publishProgram'

// Props
const props = defineProps({
  defaultProgramId: {
    type: Number,
    required: true,
  },
  hostId: {
    type: Number,
    required: true
  }
//   programOptions: {
//     type: Array,
//   },
//   publishedPrograms: {
//     type: Array,
//   }
});

// query ?mIds=props.hostId&s=1,2,5,6&d=개설가능첫날,끝날
const today = new Date();
today.setHours(0, 0, 0, 0);
const startRange = new Date(today);
const endRange = new Date(today);

startRange.setDate(today.getDate() + 3); // today + 3 days
endRange.setDate(today.getDate() + 3 + 21); // startRange + 21 days

let query = {
  mIds: props.hostId,
  s: [1, 2, 5, 6],
  d: [startRange.toISOString().split("T")[0], endRange.toISOString().split("T")[0]]
};

const config = useRuntimeConfig();
const {data, refresh} = useFetch(`host/published-programs`, {
  baseURL: config.public.apiBase,
  params: query
});

/*=======================================================================================================================*/
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
const initial = {id: 4, name: 'Program D'};

// Selected option
const selectedProgram = ref(initial);

// Handle selection change
const updateSelection = (selectedOption) => {
  selectedProgram.value = selectedOption;
};

watchEffect(() => console.log(selectedProgram.value));
</script>

<template>
  <aside class="popup">
    <header class="popup-header">
      <h1 class="font-size:9">개설하기</h1>
      <button class="n-btn n-btn:hover n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form class="popup-body" action="">
      <!--프로그램 선택-->
      <SearchableSelect :options="programs" :initial-option="initial"
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