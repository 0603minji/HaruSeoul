<script setup>
// emit
const emit = defineEmits(['selectionChanged'])

// Props
const props = defineProps({
  options: {
    type: Array,
    required: true,
  },
  selectedOptions: {
    type: Array,
  }
});

// State
const isDropdownVisible = ref(false);
const searchTerm = ref('');
const selectedOptions = ref([...props.selectedOptions]);
const filteredOptions = ref([...props.options]);

// Methods
const toggleDropdown = () => {
  isDropdownVisible.value = !isDropdownVisible.value;
};

const filterOptions = () => {
  const searchLower = searchTerm.value.toLowerCase();
  filteredOptions.value = props.options.filter(option =>
      option.name.toLowerCase().includes(searchLower)
  );
};

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
};

const clearSearch = () => {
  searchTerm.value = '';
  filterOptions();
};

const programFilterResetHandler = () => {
  selectedOptions.value = [...props.selectedOptions];
};

// Watcher to reset the filtered options when options prop changes
watch(
    () => props.options,
    (newOptions) => {
      filteredOptions.value = newOptions;
    }
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
    <div class="title">
      <p class="font-size:8">프로그램 선택</p>
      <button
          class="margin-left:auto n-btn n-btn:hover n-btn-bd:none n-icon n-icon-size:2 n-icon:reset n-icon-color:sub-1 n-deco color:sub-1"
          @click.prevent="programFilterResetHandler">
        초기화
      </button>
    </div>

    <div class="searchable-select">
      <!-- Select box that toggles the dropdown -->
      <div class="select-box">
        <div @click="toggleDropdown" class="status" v-if="!isDropdownVisible">
          <p class="margin-right:auto" v-if="selectedOptions.length===1">{{ selectedOptions.at(0).name }}</p>
          <p class="margin-right:auto" v-if="selectedOptions.length>1">{{ selectedOptions.length }} selected</p>
          <p class="margin-right:auto" v-if="selectedOptions.length===0">프로그램을 선택하세요.</p>
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
      </div>

      <!-- Dropdown list -->
      <div v-if="isDropdownVisible" class="dropdown">
        <ul>
          <!--selected-->
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
              {{ option.name }}
            </label>
          </li>
        </ul>
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
              {{ option.name }}
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
}
</style>