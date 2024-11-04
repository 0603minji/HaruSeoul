<script setup>
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
  }
});

// State
const isDropdownVisible = ref(false);
const searchTerm = ref('');
const selectedOption = ref(props.initialOption);
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

const clearSearch = () => {
  searchTerm.value = '';
  filterOptions();
};

const onOptionChange = (option) => {
  selectedOption.value = option; // Update the local selected option
  emit('selectionChanged', option); // Emit the selected option
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
    </div>

    <div class="searchable-select">
      <!-- Select box that toggles the dropdown -->
      <div class="select-box">
        <div @click="toggleDropdown" class="status" v-if="!isDropdownVisible">
          <p class="margin-right:auto" v-if="selectedOption!=null">{{ selectedOption.name }}</p>
          <p class="margin-right:auto" v-if="selectedOption==null">프로그램을 선택하세요.</p>
          <span class="n-icon n-icon:arrow_down margin-left:auto">드롭다운 보기</span>
        </div>

        <div class="search-input" v-if="isDropdownVisible">

          <input
              type="text"
              v-model="searchTerm"
              placeholder="Search..."
              @input="filterOptions"
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
          <li v-if="filteredOptions.length===0" class="padding:2">검색결과가 없습니다.</li>

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