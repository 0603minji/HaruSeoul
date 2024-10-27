<script setup>

import {onMounted, ref} from "vue";
import axios from "axios";
//=======================변수영역==========
const statuses = ref([]);


//================Fetch Function==========
const fetchStatuses = async () => {
  const response = await axios.get("http://localhost:8080/api/v1/statuses");

  statuses.value = response.data;

}

//=======================LifeCycle Functions==========
onMounted(() => {
  fetchStatuses();
})

</script>

<template>
  <section class="n-filter n-filter-rv bg-color:base-1 padding-x:6">
    <h1 class="d:none">필터</h1>

    <div class="overflow-x:auto">
      <ul class="item-wrapper padding-y:6">
        <li>
          <a href="" class="active n-btn n-btn-rv-filter n-btn:hover"
          >전체</a
          >
        </li>
        <li>
          <a href="" class="n-btn n-btn n-btn-rv-filter n-btn:hover"
          >예약 중</a
          >
        </li>
        <li>
          <a href="" class="n-btn n-btn n-btn-rv-filter n-btn:hover"
          >이용완료</a
          >
        </li>
        <li>
          <a href="" class="n-btn n-btn n-btn-rv-filter n-btn:hover"
          >취소됨</a
          >
        </li>
      </ul>
    </div>
  </section>
</template>