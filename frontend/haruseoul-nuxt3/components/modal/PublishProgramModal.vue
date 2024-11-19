<script setup>
import {ref, watch} from 'vue';
import SearchableSelect from "~/components/filter/SearchableSelect.vue";
import PublishDatePicker from "~/components/filter/PublishDatePicker.vue";
import {useModal} from "~/composables/useModal.js";
// 부모로부터 props로 전달받을 것
// 1. 디폴트 개설할 프로그램 -> SearchableSelect
// 2. hostId
// submit -> post
/*
PublishedProgramCreateDto(regMemberId, programId, dates)
POST host/published-programs
* */

// emit
const emit = defineEmits(['closeModal', 'submit']);

// Props
const props = defineProps({
  defaultProgramId: {
    type: Number,
    default: null
  },
  confirmPpPost: {
    type: Boolean,
    default: false
  }

});

watch(() => props.defaultProgramId,
    (newVal) => {
      console.log("watch defaultProgramId:", newVal);
    }
);

const reRenderTrigger = ref(false);





/*=== variables ======================================================================================================*/
// Selected option
const selectedProgram = ref(null); // 초기값 = props.defaultProgramId에 해당하는 programFilterDto {id: 16, title: "abdav"}

// Selected dates 프로그램 진행일
let selectedDates = ref([]);

// submit버튼을 누를 수 있는 상태인지? 프로그램, 날짜 모두 선택되어야함
const isPublishable = computed(() => selectedDates.value.length > 0 && selectedProgram.value != null);

const userDetails = useUserDetails();





// === function ========================================================================================================
// Handle selection change
const updateSelectedProgram = (selectedOption) => {
  selectedProgram.value = selectedOption;
  console.log('******* PublishProgramModal: updateSelectedProgram called');
  console.log('          ->  selectedProgram: ', selectedProgram.value);
};

const updateSelectedDates = (selectedOptions) => {
  selectedDates.value = selectedOptions;
  console.log('******* PublishProgramModal: updateSelectedDates called');
  console.log('          ->  selectedDates: ', selectedDates.value);
};

const postPpHandler = async () => {
  console.log('******* PublishedProgramModal: postPpHandler called');
  if (!isPublishable)
    return;

  const publishedProgramCreateDto = {
    regMemberId: userDetails.id.value,
    programId: selectedProgram.value.id,
    dates: selectedDates.value.map((date) =>
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
    let response = await useDataFetch("host/published-programs", {
      method: "POST",
      headers: {
        "Content-type": "application/json"
      },
      body: publishedProgramCreateDto
    });
    console.log('          PublishedProgram post result: ', response);
    // 포스트된 결과를 반영하여 모달창 달력 업데이트

  } catch (error) {
    console.error('         Error creating publishedProgram: ', error);
  }

  openModal('completeModal');
};
watch(()=>props.confirmPpPost, postPpHandler); // 부모인 index의 확인창에 개설확인이 전달되면 post

const closeModal = () => {
  console.log("closeModal")
  emit('closeModal');


  // Delay the re-render trigger to allow closing animation to complete
  setTimeout(() => {
    reRenderTrigger.value = !reRenderTrigger.value;
    console.log('reRenderTrigger: ', reRenderTrigger);
  }, 300); // 0.3 seconds delay (300ms)
}

const { isModalVisible, openModal, closeModal:  closeCompleteModal } = useModal();
</script>

<template>
  <aside class="popup modal">
    <header class="popup-header">
      <h1 class="font-size:9">개설하기</h1>
      <button @click.prevent="closeModal()" class="n-btn n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form @submit.prevent="emit('submit')" class="popup-body" action="">
      <!--프로그램 선택-->
      <SearchableSelect :key="reRenderTrigger" :default-program-id="props.defaultProgramId"
                        @selection-changed="updateSelectedProgram"/>
      <!-- 진행일 선택 -->
      <PublishDatePicker :key="reRenderTrigger" @selection-changed="updateSelectedDates"/>

      <div class="submit">
        <button class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1" :title="'Please select program and dates.'"
                :disabled="!isPublishable">확인
        </button>
      </div>
    </form>

    <button @click.prevent="openModal('completeModal')">테스트</button>
    <Modal class="onlyConfirm" :isVisible="isModalVisible('completeModal')" @confirm="()=> {closeCompleteModal('completeModal'); closeModal();}">
      <p style="text-align: center">개설되었습니다.</p>
    </Modal>
  </aside>
</template>

<style scoped>
.modal {
  display: none; /* Hidden by default */
  position: fixed;
  z-index: 999; /* Sit on top */
  left: 50%;
  bottom: 0;
  transform: translate(-50%, 100%); /* 초기에 바닥에 숨어있음 */
  width: 100%;
  min-width: 300px;
  max-width: 500px;
  max-height: 100vh;
  //background-color: white;
  //border-radius: 8px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease;
}

.modal.show {
  display: flex;
  transform: translate(-50%, 0); /* 아래에서 등장 */
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

      button:disabled {
        cursor: not-allowed;
        opacity: 0.3;
      }
    }
  }
}
</style>