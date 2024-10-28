<script setup>

import {onMounted, ref} from "vue";
import axios from "axios";
//=======================변수영역==========
const categories = ref([]);


//================Fetch Function==========
const fetchCategories = async () => {
  const response = await axios.get("http://localhost:8080/api/v1/categories");

  categories.value = response.data;

  // console.log(categories.value);
}

//=======================LifeCycle Functions==========
onMounted(() => {
  fetchCategories();
})

</script>
<template>
  <details open class="filter">
    <summary class="collapse">
      <span class="title">카테고리</span>
      <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
    </summary>

    <form action="" class="form">
      <div class="modal-checkbox">
        <label v-for="c in categories" :key="c.id">
          <input type="checkbox"/>{{ c.name }}
        </label>
      </div>
    </form>
  </details>

</template>
<style scoped>
@import url("/css/host/program/list.css");
</style>