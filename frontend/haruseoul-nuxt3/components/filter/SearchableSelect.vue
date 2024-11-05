<script setup>
import { ref } from 'vue';

// emit
const emit = defineEmits(['selectionChanged'])

// Props
const props = defineProps({
  options: {
    type: Array,
    required: true,
  },
  initialOption: {
    type: Object,
    default: () => {}
  }
});
console.log('searchableSelect rendered');
console.log('searchableSelect props: ', props.options, props.initialOption);
// State
const isDropdownVisible = ref(false);
const searchTerm = ref('');
const selectedOption = ref(props.initialOption);
const filteredOptions = ref([...props.options]);

watch(
    () => props.initialOption,
    (newInitialOption) => {
      selectedOption.value = newInitialOption;
    }
);

watch(
    () => props.options,
    (newOptions) => {
      filteredOptions.value = [...newOptions];
      filterOptions(); // 검색어가 존재할 경우 새 옵션을 필터링
    },
    { deep: true }
);

// Methods
const toggleDropdown = () => {
  isDropdownVisible.value = !isDropdownVisible.value;
};

const filterOptions = () => {
  if (props.options.length===0)
    return;
  const searchLower = searchTerm.value.toLowerCase();
  filteredOptions.value = props.options.filter(option =>
      option.title.toLowerCase().includes(searchLower)
  );
  console.log("filterOptions called")
};

const clearSearch = () => {
  searchTerm.value = '';
  filterOptions();
};

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
          <span class="n-icon n-icon:arrow_down margin-left:auto">드롭다운 보기</span>
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
          <span @click="toggleDropdown" class="n-icon n-icon:arrow_up">드롭다운 닫기</span>
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
                  :checked="option.id === selectedOption.id"
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
        gap: 12px;
        padding: 8px;
        border: 1px solid #ccc;
        border-radius: 6px 6px 0 0;
        cursor: pointer;

        input {
          flex-grow: 1;
        }
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

        .filtered-option:hover {
          background-color: #f0f0f0;
        }
      }
    }
  }
}
</style>