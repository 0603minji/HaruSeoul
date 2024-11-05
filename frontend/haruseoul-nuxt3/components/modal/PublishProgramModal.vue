<script setup>
import {ref, watch, watchEffect} from 'vue';
import SearchableSelect from "~/components/filter/SearchableSelect.vue";
import axios from "axios";
// 부모로부터 props로 전달받을 것
// 디폴트 개설할 프로그램 -> SearchableSelect
// 그 외 호스트가 개설가능한 모든 프로그램들 -> SearchableSelect
// 개설가능한 기간 내 이미 개설된 프로그램들 -> Calendar
// submit -> post
/*
PublishedProgramCreateDto(regMemberId, programId, dates)
POST host/published-programs
* */

// emit
const emit = defineEmits(['closeModal']);

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
let selectedDates = [];

// Every Possible options for the select box
const programOptions = ref([]);

// === fetch ===========================================================================================================
const programQuery = {
  s: ["Published", "In Progress"].join(",")
};

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
      // console.log('PublishProgramModal: programDate.value watch 진입');
      // console.log('   ->  programData.value fetched: ', programOptions.value);
      // selectedProgram 초기화
      selectedProgram.value = newOne.find(program => program.id === props.defaultProgramId);
      // console.log('   -> selectedProgram: ', selectedProgram.value);
    }
);

// === function ========================================================================================================
// Handle selection change
const updateSelectedProgram = (selectedOption) => {
  selectedProgram.value = selectedOption;
  console.log('PublishProgramModal: updateSelectedProgram called');
  console.log('   ->  selectedProgram: ', selectedProgram.value);
};

const updateSelectedDates = (selectedOptions) => {
  selectedDates = selectedOptions;
  console.log('PublishProgramModal: updateSelectedDates called');
  console.log('   ->  selectedDates: ', selectedDates);
};

const submitHandler = async () => {
  console.log('PublishedProgramModal: submitHandler called');
  if (selectedDates.length===0) {
    console.log('   ->  selectedDates is empty. Please select date.');
    return;
  }
  const publishedProgramCreateDto = {
    regMemberId: props.hostId,
    programId: selectedProgram.value.id,
    dates: selectedDates.map((date) =>
        new Intl.DateTimeFormat('ko-KR', {
          year: 'numeric',
          month: '2-digit',
          day: '2-digit',
          timeZone: 'Asia/seoul'
        }).format(date)
            .replace(/\. /g, '-')
            .replace('.', '')
    )
  };

  try {
    console.log('   ->  POST host/published-programs');
    const response = await axios.post(`${config.public.apiBase}host/published-programs`, publishedProgramCreateDto);
    console.log('   PublishedProgram created successfully: ', response.data);
  } catch (error) {
    console.error('Error creating publishedProgram: ', error);
  }

  // 모달창 닫기
  emit('closeModal');
};

</script>

<template>
  <aside class="popup">
    <header class="popup-header">
      <h1 class="font-size:9">개설하기</h1>
      <button @click.prevent="emit('closeModal')" class="n-btn n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form class="popup-body" action="">
      <!--프로그램 선택-->
      <SearchableSelect :options="programOptions" :initial-option="selectedProgram"
                        @selection-changed="updateSelectedProgram"/>
      <!-- 진행일 선택 -->
      <FilterCalendarV2 :host-id="props.hostId" @selection-changed="updateSelectedDates"/>

      <div class="submit">
        <button @click.prevent="submitHandler" class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1">확인</button>
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

    .submit {
      margin-left: auto;
      padding: 20px 0;
    }
  }
}
</style>