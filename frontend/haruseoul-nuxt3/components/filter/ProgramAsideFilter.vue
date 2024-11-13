<script setup>
import SearchableMultiSelect from "~/components/filter/SearchableMultiSelect.vue";

// emit
const emit = defineEmits(['closeModal', 'updateSelectedPrograms']);

// Props
const props = defineProps({
  selectedProgramIds: {
    type: Array,
    default: []
  }
});

console.log('programFilterModal,   props.selectedProgramIds: ', props.selectedProgramIds)


//=== 변수 ==============================================================================================================
// Selected option
const toggleDropdown = ref(false); // 자식인 SearchableMultiSelect의 드롭다운 상태를 props로 초기화. 모달창 닫을 때 드롭다운이 켜져있으면 같이 끔





// === function ========================================================================================================
// Handle selection change
const updateSelection = (selectedOptions) => {
  console.log('******* updateSelection called', selectedOptions);
  emit('updateSelectedPrograms', selectedOptions);
};



</script>

<template>
    <form @submit.prevent="emit('closeModal'); toggleDropdown = !toggleDropdown" class="popup-body" action="">
      <!--프로그램 선택-->
      <SearchableMultiSelect :selected-option-ids="props.selectedProgramIds"
                             :toggle-dropdown="toggleDropdown"
                             @selection-changed="updateSelection"/>

      <div class="submit">
        <button class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1">확인</button>
      </div>
    </form>
</template>

<style scoped>
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
</style>