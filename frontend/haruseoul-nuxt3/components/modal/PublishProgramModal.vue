<script setup>
import {ref, watch, watchEffect} from 'vue';
import SearchableSelect from "~/components/filter/SearchableSelect.vue";
import PublishDatePicker from "~/components/filter/PublishDatePicker.vue";
import axios from "axios";
// 부모로부터 props로 전달받을 것
// 1. 디폴트 개설할 프로그램 -> SearchableSelect
// 2. hostId
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

const config = useRuntimeConfig();

// === function ========================================================================================================
// Handle selection change
const updateSelectedProgram = (selectedOption) => {
  selectedProgram.value = selectedOption;
  console.log('******* PublishProgramModal: updateSelectedProgram called');
  console.log('          ->  selectedProgram: ', selectedProgram.value);
};

const updateSelectedDates = (selectedOptions) => {
  selectedDates = selectedOptions;
  console.log('******* PublishProgramModal: updateSelectedDates called');
  console.log('          ->  selectedDates: ', selectedDates);
};

const submitHandler = async () => {
  console.log('******* PublishedProgramModal: submitHandler called');
  if (selectedDates.length===0) {
    console.log('          ->  selectedDates is empty. Please select date.');
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
    console.log('          ->  POST host/published-programs');
    const response = await axios.post(`${config.public.apiBase}host/published-programs`, publishedProgramCreateDto);
    console.log('          PublishedProgram created successfully: ', response.data);
  } catch (error) {
    console.error('         Error creating publishedProgram: ', error);
  }

  // 모달창 닫기
  emit('closeModal');
};
</script>

<template>
  <aside class="popup modal">
    <header class="popup-header">
      <h1 class="font-size:9">개설하기</h1>
      <button @click.prevent="emit('closeModal')" class="n-btn n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form @submit.prevent="submitHandler" class="popup-body" action="">
      <!--프로그램 선택-->
      <SearchableSelect :host-id="props.hostId" :default-program-id="defaultProgramId"
                        @selection-changed="updateSelectedProgram"/>
      <!-- 진행일 선택 -->
      <PublishDatePicker :host-id="props.hostId" @selection-changed="updateSelectedDates"/>

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