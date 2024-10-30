<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import Status from "@/components/filter/Status.vue";

//============= 변수 영역 ====================
const reservations = ref([]);
const totalRowCount = ref(0);
const totalPageCount = ref(0);

//============= Lifecycle Functions ================
onMounted(() => {
  fetchReservations();
});

//============= Data Functions =======================
const fetchReservations = async () => {
  const response = await axios.get(
      "http://localhost:8080/api/v1/guest/reservations"
  );

  reservations.value = response.data.reservations;
  totalRowCount.value = response.data.totalRowCount;
  totalPageCount.value = response.data.totalPageCount;
};

</script>



<template>
  <main>
    <section class="bg-color:base-1 n-layout-mj">
      <!--=== heading ==========================================-->
      <!--    예약내역           -->
      <header class="n-title">
        <h1 class="">예약내역</h1>
      </header>

      <!--=== 필터 .n-filter .n-filter-rv==========================================-->
      <!-- 전체, 예약 중, 이용완료, 취소됨 필터-->
      <Status/>
      <!--=== 예약 카드 목록 ===================================================-->
      <ul class="n-card-container bg-color:base-1">
        <!-- =================================== 카드 1개 =================================== -->
        <li
            v-for="r in reservations"
            class="n-card bg-color:base-1 padding:6"
        >
          <a class="n-link-box" href="detail?id=1"></a>
          <h1 class="d:none">예약 카드</h1>

          <div class="card-header">
            <div class="left">
              <span class="n-panel-tag completed">{{}}</span>
            </div>
          </div>

          <div class="card-main">
            <div class="img-wrapper">
              <img src="/image/guest/program_02.png" alt="대표사진" />
            </div>
            <div class="card-info-wrapper">
              <div class="card-header-responsive">
                <div class="left">
                  <span class="n-panel-tag completed">{{ r.status }}</span>
                </div>
              </div>
              <p class="title">
                Gyeong-bok-gung Palace Tour With Wearing Han-bok
              </p>
              <div class="card-info-responsive">
                <div class="d:flex flex-direction:column">
                  <div class="card-info">
                    <span class="n-icon n-icon:calendar n-deco">진행일</span>
                    <span>24.09.26 Thu</span>
                  </div>
                  <div class="card-info">
                    <span class="n-icon n-icon:group n-deco">예약인원</span>
                    <span>3</span>
                  </div>
                </div>
                <div class="card-footer-responsive">
                  <a href="aa" class="n-btn bg-color:main-1 color:base-1"
                    >Host 문의</a
                  >
                  <a href="aa" class="n-btn">리뷰 작성</a>
                  <a
                    href="aa"
                    class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0"
                    >공유하기</a
                  >
                </div>
              </div>
            </div>
          </div>

          <div class="card-footer margin-top:2">
            <a href="aa" class="n-btn bg-color:main-1 color:base-1"
              >Host 문의</a
            >
            <a href="aa" class="n-btn">리뷰 작성</a>
            <a
              href="aa"
              class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0"
              >공유하기</a
            >
          </div>
        </li>
      </ul>
    </section>
  </main>
</template>

<style scoped>
@import url("/css/guest/reservation/list.css");
</style>
