<script setup>
import SearchableMultiSelect from "~/components/filter/SearchableMultiSelect.vue";
import SearchableMultiSelectAsideFilter from "~/components/filter/SearchableMultiSelectAsideFilter.vue";

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
      <!--프로그램 선택-->
      <SearchableMultiSelectAsideFilter :selected-option-ids="props.selectedProgramIds"
                             :toggle-dropdown="toggleDropdown"
                             @selection-changed="updateSelection"/>
</template>

<style scoped>
</style>