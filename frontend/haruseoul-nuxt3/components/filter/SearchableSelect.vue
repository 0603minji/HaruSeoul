<script setup>
import { ref } from 'vue';
import {useAuthFetch} from "~/composables/useAuthFetch.js";

// emit
const emit = defineEmits(['selectionChanged'])

// Props
const props = defineProps({
  defaultProgramId: {
    type: Number,
    default: null
  }
});





// === variables =======================================================================================================
const isDropdownVisible = ref(false);
const searchTerm = ref(''); // 옵션리스트에서 필터링할 검색어
const selectedOption = ref(props.defaultProgramId); // 옵션리스트에서 선택된 옵션
watch(()=>props.defaultProgramId, (newOne) => {
  // selectedOption 초기화
  selectedOption.value = newOne === null? null : programOptions.value.find(program => program.id === newOne);
  emit('selectionChanged', selectedOption.value); // Emit the default selected option
});
const programOptions = ref([]); // 선택가능한 모든 프로그램 옵션리스트
const filteredOptions = ref([]); // 옵션리스트에서 검색어로 필터링된 리스트
const userDetails = useUserDetails();




/*=== function =======================================================================================================*/
const toggleDropdown = () => {
  isDropdownVisible.value = !isDropdownVisible.value;
};

// 검색어로 옵션리스트 필터
const filterOptions = () => {
  if (programOptions.value.length===0) // 검색할 옵션이 없을 때
    return;
  const searchLower = searchTerm.value.toLowerCase();
  filteredOptions.value = programOptions.value.filter(option =>
       option.title && option.title.toLowerCase().includes(searchLower)
  );
  // console.log("filterOptions called");
};

// 검색어 초기화
const clearSearch = () => {
  searchTerm.value = '';
  filterOptions();
};

// 옵션 선택 시, selectedOption update, 부모에게 emit
const onOptionChange = (option) => {
  selectedOption.value = option; // Update the local selected option
  emit('selectionChanged', option); // Emit the selected option
};

// input에 @input이벤트가 발생할 때마다 filterOptions실행
// (이렇게 해줘야 한글과 같은 조합형문자의 조합이 완료되지 않은 상태에서 바로바로 필터링 가능)
const onInput = (event) => {
  searchTerm.value = event.target.value; // 직접 값을 할당
  filterOptions();
};





// === fetch ===========================================================================================================
// Every Possible options for the select box
const programQuery = {
  s: ["Published", "Unpublished"].join(",")
};

const config = useRuntimeConfig();

// 프로그램 선택창에 개설가능한 모든 프로그램 옵션을 표시하기 위한 개설가능프로그램목록 fetch
// default 선택값은 props.defaultProgramId
const { data: programData } = await useAuthFetch(`host/programs/user/${userDetails.id.value}`, { params: programQuery });

programOptions.value = programData.value;
console.log('programOptions: ', programOptions.value);
// filteredOptions 초기화, 검색어가 입력되어있을 경우, 필터링
filterOptions();
console.log('filteredOptions: ', filteredOptions.value)
// selectedOption 초기화
selectedOption.value = props.defaultProgramId === null? null : programOptions.value.find(program => program.id === props.defaultProgramId);
emit('selectionChanged', selectedOption.value); // Emit the default selected option

</script>

<template>
  <div class="program-selector">
    <div class="title">
      <p class="font-size:8">프로그램 선택</p>
    </div>

    <div class="searchable-select">
      <!-- Select box that toggles the dropdown -->
      <div class="select-box">
        <div @click="toggleDropdown" class="status" v-if="!isDropdownVisible">
          <p class="margin-right:auto" v-if="selectedOption!=null">{{ selectedOption.title }}</p>
          <p class="margin-right:auto" v-if="selectedOption==null">프로그램을 선택하세요.</p>
          <span class="dropdown-toogle n-icon n-icon:arrow_down margin-left:auto">드롭다운 보기</span>
        </div>

        <div class="search-input" v-if="isDropdownVisible">
          <input
              type="text"
              v-model="searchTerm"
              placeholder="Search..."
              @input="onInput"
              @keydown.enter.prevent
          />
          <span v-if="searchTerm!==''" @click="clearSearch"
                class="clear-search n-icon n-icon:exit n-icon-color:base-6 n-icon-size:4">검색어 초기화</span>
          <span @click="toggleDropdown" class="dropdown-toogle n-icon n-icon:arrow_up">드롭다운 닫기</span>
        </div>
      </div>

      <!-- Dropdown list -->
      <div v-if="isDropdownVisible" class="dropdown">
        <ul>
          <li v-if="searchTerm && filteredOptions.length===0" class="padding:2">검색결과가 없습니다.</li>
          <li v-if="!searchTerm && filteredOptions.length===0" class="padding:2">선택 가능한 프로그램이 존재하지 않습니다.</li>

          <!--not selected & filtered-->
          <li class="filtered-option" v-for="option in filteredOptions" :key="option.id">
            <label>
              <input
                  v-model="selectedOption"
                  name="program"
                  type="radio"
                  :value="option"
                  :checked="selectedOption!=null && option.id === selectedOption.id"
                  @change="() => {onOptionChange(option); toggleDropdown();}"
              />
              {{ option.title }}
            </label>
          </li>
        </ul>
      </div>
    </div>
  </div>

</template>

<style scoped>
.program-selector {
  display: flex;
  flex-direction: column;
  margin-bottom: 30px;

  .title {
    display: flex;
    height: 32px;
    padding: 0 0 0 10px;
    align-items: center;
    margin-bottom: 8px;

    button {
      --btn-padding: 8px 10px;
    }
  }

  .searchable-select {
    position: relative;
    //width: 200px;
    //padding: 0 10px;
    margin: 0 10px;

    .select-box {
      //padding: 8px;

      .status {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 6px;
        cursor: pointer;
      }

      .search-input {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 6px 6px 0 0;
        cursor: pointer;

        input {
          flex-grow: 1;
        }

        .clear-search {
          border-radius: 50%;
          --icon-color: var(--color-base-7);
        }

        .clear-search:hover {
          background-color: var(--color-base-3);
        }
      }

      .dropdown-toogle {
        border-radius: 5px;
        --icon-color: var(--color-base-7);
      }

      .dropdown-toogle:hover {
        background-color: #f8f8f8;
      }
    }

    .dropdown {
      position: absolute;
      top: 100%;
      left: 0;
      right: 0;
      border: 1px solid #ccc;
      border-radius: 0 0 6px 6px;
      background-color: #fff;
      max-height: 350px;
      overflow-y: auto;
      z-index: 1000;

      ul {
        list-style-type: none;
        padding: 0;
        margin: 0;

        li {
          label {
            padding: 8px;
            display: flex;
            align-items: center;
            gap: 6px;
            cursor: pointer;
          }
        }

        li:has(input:checked) {
          background-color: #e1e0e0;
        }

        .filtered-option:hover {
          background-color: #f0f0f0;
        }
      }
    }
  }
}
</style>