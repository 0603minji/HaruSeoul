<script setup>
import { ref } from 'vue';
import CalendarV2 from "~/components/filter/PublishDatePicker.vue";
import PublishProgramModal from "~/components/modal/PublishProgramModal.vue"
import ProgramFilterModal from "~/components/modal/DateRangeFilterModal.vue";
import DateRangePicker from "~/components/filter/DateRangePicker.vue";
import DateRangeFilterModal from "~/components/modal/DateRangeFilterModal.vue";
import SearchableMultiSelect from "~/components/filter/SearchableMultiSelect.vue";
import SearchableSelect from "~/components/filter/SearchableSelect.vue";

const route = useRoute();

const isPublishProgramModalVisible = ref(false);
const isDateRangeFilterModalVisible = ref(false);

const hostId = 4;
const pIdTobePublished = ref(null);

const OpenPublishProgramModalHandler = (pId) => {
  console.log("OpenPublishProgramModalHandler called");
  pIdTobePublished.value = pId;
  isPublishProgramModalVisible.value = true;
};

watchEffect(()=> {console.log("pIdTobePublished: ",pIdTobePublished.value)})

const OpenDateRangeHandler = () => {
  isDateRangeFilterModalVisible.value = true;
};
</script>
<template>
  <main>

<!--    <SearchableSelect :host-id="4" :default-program-id="null"/>-->

    <!--=== 필터 .n-filter ==========================================-->
    <!--모집 중, 예약 확정, 폐지 임박, 종료, 폐지, 필터-->
    <section class="n-filter md:d:none bg-color:base-1">
      <h1 class="d:none">필터</h1>

      <div class="overflow-x:auto">
        <ul class="item-wrapper">
          <li><a @click.prevent="OpenDateRangeHandler" href=""
                 :class="{active: route.query.dates}"
                 class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:calendar n-icon-size:1 n-deco n-deco-gap:1">기간</a>
          </li>
          <li><a href=""
                 class="active active:border n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:pending n-icon-size:1 n-deco n-deco-gap:1">프로그램
            상태</a></li>
          <li><a href=""
                 class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:search n-icon-size:1 n-deco n-deco-gap:1">프로그램</a>
          </li>
        </ul>
      </div>

      <div class="reset-box">
        <div class="gradation"></div>
        <a href="" class="icon-box n-deco1 n-icon n-icon:reset">
          초기화
        </a>
      </div>
    </section>


    <div class="container">
      <button @click.prevent="OpenPublishProgramModalHandler(null)" class="n-btn n-btn-background-color:sub n-btn:hover">개설하기</button>
    </div>


    <PublishProgramModal :class="{'show': isPublishProgramModalVisible}" :default-program-id="pIdTobePublished" :host-id="hostId" @close-modal="isPublishProgramModalVisible=false"/>
    <DateRangeFilterModal :class="{'show': isDateRangeFilterModalVisible}" :host-id="hostId" @close-modal="isDateRangeFilterModalVisible=false"/>
    <div :class="{'active': isPublishProgramModalVisible || isDateRangeFilterModalVisible}" class="backdrop"></div>
  </main>
</template>
<style scoped>
.backdrop {
  display: none;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Dark background */
  backdrop-filter: blur(5px); /* Blur effect */
  z-index: 999; /* Behind modal but above other content */
}
.backdrop.active {
  display: block;
}

.container {
  display: flex;
  gap: 40px;
  flex-direction: column;
  justify-content: center;
  margin: 0 100px 50px 100px;
}
</style>