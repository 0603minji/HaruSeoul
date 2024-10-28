<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import Category from "@/components/filter/Category.vue";

//============= 변수 영역 ====================
const programs = ref([]);     //  서버에서 가져온 프로그램 목록 저장 배열
const totalRowCount = ref(0); // 서버에서 가져온 총 프로그램 개수 저장
const totalPageCount = ref(0);  //  서버에서 가져온 총 페이지 개수 저장
const programTitles = ref([]);  //  프로그램 id, title들을 저장할 배열
const selectedPrograms = ref([]); // 프로그램 필터에서 선택된 프로그램의 id를 저장할 배열
  //  ref : 반응형 변수 (데이터 변경시 ui도 자동 업데이트
  //  const : javascript에서 상수값 선언시 사용하는 키워드
  //          변수의 값이 변하지 않을때 사용하는 키워드
  //          재선언, 재할당 불가능
  //          블록 스코프
  //  let : 재선언 불가능, 재할당 가능
  //        블록 스코프


//============= Lifecycle Functions ================
onMounted(() => {
  //  컴포넌트가 화면에 마운트(렌더링)된 후에 실행
  fetchPrograms();  //  프로그램 목록을 가져오는 함수
  fetchProgramIds();  //  프로그램의 제목과 ID 목록을 가져오는 함수
});

//============= Data Functions =======================
//  서버에서 프로그터를 가져오는 비동기 함수
const fetchPrograms = async (cIds, pIds, statuses, pageNum) => {
  const params = {};
  //  params 객체 생성
  //  조건(cIds, pIds, statuses, pageNum)이 존재하는 경우에만 해당 값을 params에 추가
  if (cIds) {
    params.c = cIds;
  }
  if (pIds) {
    params.pg = pIds;
  }
  if (statuses) {
    params.s = statuses;
  }
  if (pageNum) {
    params.pageNum = pageNum;
  }
  const response = await axios.get(
      //  axios.get : 비동기적으로 서버의 API로 GET 요청 보냄
      "http://localhost:8080/api/v1/host/programs",{
        params: params
        //  이 URL에 params 객체를 함께 전송하여 필터링된 결과를
        //  response 변수에 받기
      }
  );

  //  서버로 부터 받은 응답 response
  programs.value = response.data.programs;  // response에서 프로그램 목록을 추출해서 저장
  totalRowCount.value = response.data.totalRowCount;  //  response에서 총 프로그램수 추출해서 저장
  totalPageCount.value = response.data.totalPageCount;  //  response에서 총 페이지 갯수 추출해서 저장
};

//  서버에서 프로그램 id와 title을 가져와서 programTitles에 저장
const fetchProgramIds = async () => {
  const response = await axios.get(
      "http://localhost:8080/api/v1/host/programs/titles"
      //  이 url로 서버에 get 요청 보냄
      //  응답 결과를 response 변수에 담기
  );
  programTitles.value = response.data.map(p => ({id: p.id, title: p.title}));
  //response.data에서 id와 title만 추출하여 programTitles 배열 객체에 저장
};

//  선택된 모든 체크박스를 초기화 (All 선택시)
const selectProgramAll = async () => {
  selectedPrograms.value = [];
  // 프로그램 필터에서 선택된 프로그램 id들을 담은 selectedPrograms 배열 객체를 비움
  const checkboxes = document.querySelectorAll('.programids');
  checkboxes.forEach((checkbox) => {
    checkbox.checked = false;
  });
  //  .programids 클래스를 가진 모든 체크박스를 찾아서 선택 해제
  try {
    await fetchPrograms(null, null, null, null);
    //  fetchPrograms를 호출하여 모든 프로그램을 서버에서 다시 가져오기
    //  파라미터를 모두 null로 전달하여 필터 없이 전체 목록 가져오기
  } catch (error) {
    console.error("Error fetching all programs:", error);
  }
};

//  선택된 프로그램들만 서버에서 가져와 목록을 업데이트하는 함수
const selectProgram = async () => {
  const checkboxes = document.querySelectorAll('.programidAll');
  checkboxes.forEach((checkbox) => {
    checkbox.checked = false;
  });
  //  .programidAll 클래스를 가진 모든 체크박스를 찾아 선택을 해제

  // 선택된 체크박스의 id를 배열로 담기
  const selectedIds = selectedPrograms.value;

  //  선택된 체크박스의 id가 존재하는 경우(프로그램 필터에 선택된 체크박스가 있는 경우)
  if (selectedIds.length > 0) {
    try {
      // 선택된 id들을 ','로 연결해서 쿼리 파라미터로 전송 (pg=1,2,3)
      await fetchPrograms(null, selectedIds.join(','), null, null);
    } catch (error) {
      console.error("Error fetching selected programs:", error);
    }
  } else {
    console.log("No programs selected.");
  }
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
                    <img src="/image/guest/program_01.png" alt="대표사진"/>
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
                          <span class="category" v-for="(c,index) in p.categoryNames" :key="index">
                            {{ c }}<span v-if="index<p.categoryNames.length-1"> ·</span>
                          </span>
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
          <Category/>
            <!-- 프로그램 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">프로그램</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">
                <div class="modal-checkbox">
                  <label><input class="programidAll" type="checkbox" @change="selectProgramAll"/>All</label>
                  <label v-for="p in programTitles" :key="p.id">
                    <input class="programids" type="checkbox" @change="selectProgram" :value="p.id" v-model="selectedPrograms">{{ p.title }}
                  </label>
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
                  <label><input type="checkbox"/>전체</label>
                  <label><input type="checkbox"/>작성중</label>
                  <label><input type="checkbox"/>작성완료</label>
                  <label><input type="checkbox"/>모집중</label>
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
