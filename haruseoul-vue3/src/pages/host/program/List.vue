<script setup>
import { onMounted, ref } from "vue";
import axios from "axios";

//============= 변수 영역 ====================
const programs = ref([]);
const totalRowCount = ref(0);
const totalPageCount = ref(0);

//============= Lifecycle Functions ================
onMounted(() => {
  fetchPrograms();
});

//============= Data Functions =======================
const fetchPrograms = async () => {
  const response = await axios.get(
    "http://localhost:8080/api/v1/host/programs"
  );

  programs.value = response.data.programs;
  totalRowCount.value = response.data.totalRowCount;
  totalPageCount.value = response.data.totalPageCount;

  console.log(programs.value);
};
</script>

<template>
  <main>
    <section class="n-layout-mj">
      <!--=== heading ==========================================-->
      <!--    프로그램 관리           +프로그램 등록-->
      <header class="n-title">
        <h1 class="">프로그램 관리</h1>
        <div>
          <a
            href=""
            class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:plus n-deco"
            >프로그램 등록</a
          >
        </div>
      </header>

      <section class="d:flex">
        <section class="layout-list">
          <!--=== 필터 .filter ==========================================-->
          <!--프로그램, 카테고리, 프로그램 상태 필터-->
          <section class="n-filter bg-color:base-1 padding-x:6">
            <h1 class="d:none">필터</h1>

            <div class="overflow-x:auto">
              <ul class="item-wrapper padding-y:5">
                <li>
                  <a
                    href=""
                    class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:search n-icon-size:1 n-deco n-deco-gap:1"
                    >프로그램</a
                  >
                </li>
                <li>
                  <a
                    href=""
                    class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:pending n-icon-size:1 n-deco n-deco-gap:1"
                    >프로그램 상태</a
                  >
                </li>
                <li>
                  <a
                    href=""
                    class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:category n-icon-size:1 n-deco n-deco-gap:1"
                    >카테고리</a
                  >
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

          <!--=== 목록 ==========================================-->
          <section>
            <!--=== 정렬 .list-header ==========================================-->
            <header class="list-header bg-color:base-1">
              <h1 class="d:none">프로그램 목록</h1>
              <div>
                <span>{{ totalRowCount }} Result</span>
                <!--            <button type="button" class="n-icon n-icon:arrow_swap n-deco n-deco-gap:1">-->
                <!--                정렬-->
                <!--            </button>-->
                <a href="" class="n-icon n-icon:arrow_swap n-deco n-deco-gap:1">
                  정렬
                </a>
              </div>
            </header>

            <!--프로그램 카드 목록. (작성 중) (작성 완료) (모집 중)-->
            <ul class="n-card-container bg-color:base-1 padding:7">
              <!-- =================================== 작성 중 예약 카드 1개 =================================== -->
              <li
                class="n-card bg-color:base-1 padding:6"
                v-for="p in programs"
              >
                <h2 class="d:none">프로그램 카드</h2>

                <div class="card-header">
                  <div class="left">
                    <span class="n-panel-tag not-submitted">{{
                      p.status
                    }}</span>
                  </div>
                  <div class="right">
                    <a
                      href=""
                      class="n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9"
                      >더보기</a
                    >
                  </div>
                </div>

                <div class="card-main">
                  <div class="img-wrapper">
                    <img src="/image/guest/program_01.png" alt="대표사진" />
                  </div>

                  <div class="card-info-wrapper">
                    <p class="title">
                      {{ p.title }}
                    </p>
                    <div class="card-info-responsive">
                      <div>
                        <div class="card-info">
                          <span class="n-icon n-icon:star n-deco"></span>
                          <span>0.0 (0)</span>
                        </div>
                        <div class="card-info gap:1">
                          <span class="category">{{ p.categoryIds }}</span>
                          <span>·</span>
                          <span class="category"></span>
                        </div>
                      </div>
                      <div class="card-footer-responsive">
                        <a href="aa" class="n-btn create">작성하기</a>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="card-footer">
                  <a href="aa" class="n-btn create">작성하기</a>
                </div>
              </li>
            </ul>
          </section>
        </section>
        <!--=== 필터 반응형 ==========================================-->
        <aside class="n-filter-aside">
          <header class="n-title">
            <h1 class="">Filter</h1>
            <div>
              <button
                class="n-icon n-icon:reset"
                style="--icon-color: var(--color-sub-1)"
              >
                초기화
              </button>
            </div>
          </header>

          <div class="filters">
            <!-- 카테고리 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">카테고리</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">
                <div class="modal-checkbox">
                  <label><input type="checkbox" />All</label>
                  <label><input type="checkbox" />Activity</label>
                  <label><input type="checkbox" />Culture</label>
                  <label><input type="checkbox" />Shopping</label>
                  <label><input type="checkbox" />Food</label>
                  <label><input type="checkbox" />Nature</label>
                </div>
              </form>
            </details>

            <!-- 프로그램 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">프로그램</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">
                <div class="modal-checkbox">
                  <label><input type="checkbox" />All</label>
                  <label
                    ><input type="checkbox" />Gyeong-bok-gung Palace Tour With
                    Wearing Han-bok</label
                  >
                  <label
                    ><input type="checkbox" />Gyeong-bok-gung Palace Tour With
                    Wearing Han-bok</label
                  >
                  <label
                    ><input type="checkbox" />Gyeong-bok-gung Palace Tour With
                    Wearing Han-bok</label
                  >
                  <label
                    ><input type="checkbox" />Gyeong-bok-gung Palace Tour With
                    Wearing Han-bok</label
                  >
                  <label
                    ><input type="checkbox" />Gyeong-bok-gung Palace Tour With
                    Wearing Han-bok</label
                  >
                  <label
                    ><input type="checkbox" />Gyeong-bok-gung Palace Tour With
                    Wearing Han-bok</label
                  >
                  <label
                    ><input type="checkbox" />Gyeong-bok-gung Palace Tour With
                    Wearing Han-bok</label
                  >
                  <label
                    ><input type="checkbox" />Gyeong-bok-gung Palace Tour With
                    Wearing Han-bok</label
                  >
                </div>
              </form>
            </details>

            <!-- 프로그램 상태 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">프로그램 상태</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">
                <div class="modal-checkbox">
                  <label><input type="checkbox" />전체</label>
                  <label><input type="checkbox" />작성중</label>
                  <label><input type="checkbox" />작성완료</label>
                  <label><input type="checkbox" />모집중</label>
                </div>
              </form>
            </details>
          </div>
        </aside>
      </section>
    </section>
  </main>
</template>

<style scoped>
@import url("/css/host/program/list.css");
</style>
