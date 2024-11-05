<script setup>
import {ref, watch, watchEffect} from 'vue';
import SearchableSelect from "~/components/filter/SearchableSelect.vue";
// 부모로부터 props로 전달받을 것
// 디폴트 개설할 프로그램 -> SearchableSelect
// 그 외 호스트가 개설가능한 모든 프로그램들 -> SearchableSelect
// 개설가능한 기간 내 이미 개설된 프로그램들 -> Calendar
// submit -> post

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
});

/*=== variables ======================================================================================================*/
// Selected option
const selectedProgram = ref(null); // 초기값 = props.defaultProgramId에 해당하는 programFilterDto {id: 16, title: "abdav"}

// Selected dates 프로그램 진행일
const selectedDates = [];

// Every Possible options for the select box
const programOptions = ref([]);

// ---------------------------------------------------------------------------------------------------------------------

const programQuery = {
  s: ["Published", "In Progress"].join(",")
};

// === fetch ===========================================================================================================
const config = useRuntimeConfig();

// 프로그램 선택창에 개설가능한 모든 프로그램 옵션을 표시하기 위한 개설가능프로그램목록 fetch
// default 선택값은 props.defaultProgramId
const {data: programData} = useFetch(`host/programs/user/${props.hostId}`, {
  baseURL: config.public.apiBase,
  params: programQuery
});

watch(() => programData.value,
    (newOne) => {
      programOptions.value = newOne;
      // console.log('programData.value fetched', programOptions.value);
      // selectedProgram 초기화
      selectedProgram.value = newOne.find(program => program.id === props.defaultProgramId);
      // console.log(selectedProgram.value);
    }
);

// === function ========================================================================================================
// Handle selection change
const updateSelectedProgram = (selectedOption) => {
  selectedProgram.value = selectedOption;
};

const updateSelectedDates = () => {

};

</script>

<template>
  <aside class="popup">
    <header class="popup-header">
      <h1 class="font-size:9">개설하기</h1>
      <button class="n-btn n-btn:hover n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form class="popup-body" action="">
      <!--프로그램 선택-->
      <SearchableSelect :options="programOptions" :initial-option="selectedProgram"
                        @selection-changed="updateSelectedProgram"/>
      <!-- 진행일 선택 -->
      <FilterCalendarV2 :host-id="props.hostId" @selection-changed="updateSelectedDates"/>

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