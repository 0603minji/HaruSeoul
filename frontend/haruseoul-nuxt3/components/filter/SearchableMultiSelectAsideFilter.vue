<script setup>
// emit
import {useAuthFetch} from "~/composables/useAuthFetch.js";

const emit = defineEmits(['selectionChanged'])

// Props
const props = defineProps({
  selectedOptionIds: {
    type: Array,
    default: []
  },
  toggleDropdown: {
    type: Boolean,
    default: false
  }
});


console.log('SearchableMultiSelect,    props.selectedOptionIds: ', props.selectedOptionIds);


const userDetails = useUserDetails();
const hostId = userDetails.id.value;

// State
const isDropdownVisible = ref(false);
const searchTerm = ref(''); // 검색어
const selectedOptions = ref([]); // 아래에서 부모컴포넌트에서 전달된 값으로 초기화. 부모에게 emit할 배열
// todo: 개설되면 새로 options fetch해야함
const options = ref([]); // 선택가능한 모든 옵션
const filteredOptions = ref([]); // 초기값: 선택가능한 모든 옵션. options기반으로 filterOptons()로 생성 후 드롭다운에 표시





// === functions =======================================================================================================
const toggleDropdown = () => isDropdownVisible.value = !isDropdownVisible.value;
const closeDropdown = () => isDropdownVisible.value = false;

// 검색어를 적용해서 filteredOptions를 업데이트
const filterOptions = () => {
  console.log('       filterOptions called,')
  const searchLower = searchTerm.value.toLowerCase();
  filteredOptions.value = options.value.filter(option =>
      option.title.toLowerCase().includes(searchLower)
  );
  console.log('           -> filteredOptions: ', filteredOptions.value)
};

// 옵션이 체크되거나 체크해제되면 selectedOptions에 추가, 삭제, 정렬 후 emit
const toggleOption = (option) => {
  const index = selectedOptions.value.findIndex((selected) => selected.id === option.id);
  if (index > -1) {
    selectedOptions.value.splice(index, 1);
  } else {
    selectedOptions.value.push(option);
  }
  // Order by Id
  selectedOptions.value.sort((option1, option2) => {
    return option1.id - option2.id;
  });
  emit('selectionChanged', selectedOptions.value);
};

const clearSearch = () => {
  searchTerm.value = '';
  filterOptions();
};

const programFilterResetHandler = () => {
  selectedOptions.value = [];
  emit('selectionChanged', selectedOptions.value);
};


/*
  options 초기화
  1. PublishedProgram테이블에서 mIds로 모든 distinct pIds를 얻어온다. host/published-programs/distinct?hostId=?
  2. Program테이블에서 pIds로 검색해서 {id: 1, title: '제목'}객체 배열을 생성 후 초기화
*/

const {data} = await useAuthFetch(`host/published-programs/distinct?mId=${hostId}`);
options.value = data.value.programFilterListDtos; // [{id: 1, title: 'daf'},...]
console.log('       프로그램필터 모든 옵션. options: ', options.value);

// selectedOptions 초기화. props.selectedOptionIds -> options.filter
selectedOptions.value = options.value.filter(option => props.selectedOptionIds.includes(String(option.id)));
console.log('SearchableMultiSelect,    selectedOptions: ', selectedOptions.value)


// filteredOptions 초기화
filterOptions();





// 모달창을 때, 드롭다운 collapse
watch(()=>props.toggleDropdown, closeDropdown);

// Watcher to reset the filtered options when options prop changes
watch(
    () => props.selectedOptionIds,
    (newOptions) => {
      console.log('SearchableMultiSelect watch,    props.selectedOptionIds: ', newOptions)
      console.log('SearchableMultiSelect watch,    options: ', options.value)
      selectedOptions.value = options.value.filter(option => newOptions.includes(String(option.id)));
      console.log('SearchableMultiSelect watch,    selectedOptions: ', selectedOptions.value)
      filterOptions();
    },
    {deep: true}
);

// watch(
//     () => props.selectedOptions,
//     (newSelectedOptions, oldSelectedOptions) => {
//       selectedOptions.value = newSelectedOptions;
//     },
//     {deep: true}
// );

// watchEffect(() => {
//   console.log(props.selectedOptions);
// });
</script>
<template>
  <div class="program-selector">
    <header class="title">
      <h1 class="font-size:8">프로그램 선택</h1>
      <button
          class="reset-btn margin-left:auto n-btn n-btn:hover n-btn-bd:none n-icon n-icon-size:2 n-icon:reset n-icon-color:sub-1 color:sub-1"
          :class="{'show': selectedOptions.length>0 }"
          @click.prevent="programFilterResetHandler">
        초기화
      </button>
    </header>

    <div class="searchable-multi-select">
      <!-- Select box that toggles the dropdown -->
      <div class="select-box">
        <div @click="toggleDropdown" class="status" v-if="!isDropdownVisible">
          <p class="margin-right:auto" v-if="selectedOptions.length===1">{{ selectedOptions.at(0).title }}</p>
          <p class="margin-right:auto" v-if="selectedOptions.length>1">{{ selectedOptions.length }} selected</p>
          <p class="margin-right:auto" v-if="selectedOptions.length===0">Select programs.</p>
          <span class="n-icon n-icon:arrow_down margin-left:auto">드롭다운 보기</span>
        </div>

        <div class="search-input" v-if="isDropdownVisible">

          <input
              type="text"
              v-model="searchTerm"
              placeholder="Search..."
              @input="filterOptions"
          />
          <span v-if="searchTerm!==''" @click="clearSearch"
                class="clear-search n-icon n-icon:exit n-icon-color:base-6 n-icon-size:4">검색어 초기화</span>
          <span @click="toggleDropdown" class="n-icon n-icon:arrow_up">드롭다운 닫기</span>
        </div>

        <!-- Dropdown list -->
        <div v-if="isDropdownVisible" class="dropdown">
          <!--selected 드롭다운에서 선택된 옵션들 따로 표시해주는 목록박스-->
          <!--
          <ul>
            <li class="select-count" v-if="searchTerm===''&&selectedOptions.length!==0">
              {{ selectedOptions.length }} selected
            </li>
            <li class="selected-option"
                v-if="searchTerm===''"
                v-for="(option, index) in selectedOptions"
                :key="option.id"
                :class="{'last-selected-option': index === selectedOptions.length-1}">
              <label>
                <input
                    type="checkbox"
                    :value="option"
                    checked
                    @change="toggleOption(option)"
                />
                {{ option.title }}
              </label>
            </li>
          </ul>-->
          <ul>
            <li v-if="filteredOptions.length===0" class="padding:2">검색결과가 없습니다.</li>

            <!--not selected & filtered-->
            <li class="filtered-option" v-for="option in filteredOptions" :key="option.id">
              <label>
                <input
                    type="checkbox"
                    :value="option"
                    :checked="selectedOptions.some(selected => selected.id === option.id)"
                    @change="toggleOption(option)"
                />
                {{ option.title }}
              </label>
            </li>
          </ul>
        </div>
      </div>

      <div class="selected-list-container" v-if="selectedOptions.length>0">
        <ul>
          <li  v-for="(option, index) in selectedOptions"
               :key="option.id">
            <label>
              <input
                  type="checkbox"
                  :value="option"
                  checked
                  @change="toggleOption(option)"
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

  .title {
    display: flex;
    height: 32px;
    align-items: center;
    margin-bottom: 8px;

    h1 {
      font-weight: 600;
    }

    .reset-btn {
      --btn-padding: 8px 10px;
      display: none;
    }
    .reset-btn.show {
      display: flex;
    }
  }

  .searchable-multi-select {
    position: relative;
    //width: 200px;
    //padding: 0 10px;

    .select-box {
      //padding: 8px;
      position: relative;
      margin-bottom: 16px;

      .status {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 4px 8px;
        border: 1px solid #ccc;
        border-radius: 6px;
        cursor: pointer;
      }

      .search-input {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 4px 8px;
        border: 1px solid #ccc;
        border-radius: 6px 6px 0 0;
        cursor: pointer;

        input {
          min-width: 2ch;
          flex-grow: 1;
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
        max-height: 400px;
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

          .filtered-option:hover {
            background-color: #f0f0f0;
          }

          .select-count {
            padding: 8px;
          }

          .selected-option, .select-count {
            background-color: #e1e0e0;
          }

          .last-selected-option {
            padding-bottom: 8px;
          }
        }
      }
    }


    .selected-list-container {
      --bg-color: #f5f5f5;

      //max-width: 768px;
      //min-width: 250px;
      width: 100%;
      height: auto;
      max-height: 200px;
      overflow-y: auto;
      display: flex;
      flex-direction: column;
      gap: 12px;
      padding: 8px 8px;
      background-color: var(--bg-color);
      border-radius: 6px;
      box-shadow: 0 3px 2px 0 var(--color-base-3);

      li {
        padding: 4px;
      }
    }

  }
}
</style>