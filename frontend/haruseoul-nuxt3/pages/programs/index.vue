<template>
  <main>
    <section class="layout-body">
      <h1 class="d:none">프로그램 목록</h1>

      <div class="layout-main">
        <aside class="layout-main-aside">
          <header class="n-title">
            <h1 class="">Filter</h1>
            <div>
              <button @click="resetFilters" class="n-icon n-icon:reset" style="--icon-color: var(--color-sub-1)">초기화</button>
            </div>
          </header>

          <div class="filters">
            <!-- 기간 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">Period</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>
              <form action="" class="form">
                <div class="modal-duration">
                  <label>
                    <input type="date" v-model="startDate" @change="fetchPrograms" placeholder="Start Date">
                  </label>
                  <span>~</span>
                  <label>
                    <input type="date" v-model="endDate" @change="fetchPrograms" placeholder="End Date">
                  </label>
                </div>
              </form>
            </details>

            <!-- 카테고리 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">Category</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">
                <div class="modal-checkbox">
                  <!-- All 체크박스 -->
                  <label>
                    <input v-model="allCategoriesSelected" @change="toggleAllCategories" type="checkbox"> All
                  </label>

                  <!-- 카테고리 체크박스 목록 -->
                  <label v-for="c in categories" :key="c.id">
                    <input @change="updateAllCheckbox" type="checkbox" v-model="selectedCategoryIds" :value="c.id"> {{ c.name }}
                  </label>
                </div>
              </form>
            </details>

            <!-- 가격 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">Price</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">
                <div class="price-container">
                  <!--                  <div class="range-slider">-->
                  <!--                    <input type="range" class="slider">-->
                  <!--                    <input type="range" class="slider">-->
                  <!--                  </div>-->
                  <div class="price-inputs">
                    <input v-model="minPrice" @input="fetchPrograms" type="number" placeholder="₩0">
                    <span class="tilde">~</span>
                    <input v-model="maxPrice" @input="fetchPrograms" type="number" placeholder="₩1000000">
                  </div>
                </div>
              </form>
            </details>

            <!-- 진행언어 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">Language</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">
                <div class="modal-radio">
                  <label class="d:none">Language</label>
                  <div>
                    <input id="language-all" type="radio" class="radio" name="language" :value="null" v-model="language" @change="fetchPrograms">
                    <label for="language-all">All</label>
                  </div>
                  <div>
                    <input id="language-english" type="radio" class="radio" name="language" value="Korean" v-model="language" @change="fetchPrograms">
                    <label for="language-english">Korean</label>
                  </div>
                  <div>
                    <input id="language-english" type="radio" class="radio" name="language" value="English" v-model="language" @change="fetchPrograms">
                    <label for="language-english">English</label>
                  </div>
                  <div>
                    <input id="language-japanese" type="radio" class="radio" name="language" value="Japanese" v-model="language" @change="fetchPrograms">
                    <label for="language-japanese">Japanese</label>
                  </div>
                  <div>
                    <input id="language-chinese" type="radio" class="radio" name="language" value="Chinese" v-model="language" @change="fetchPrograms">
                    <label for="language-chinese">Chinese</label>
                  </div>
                </div>
              </form>
            </details>

            <!-- 참여인원 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">Group Size</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">

                <div class="min-max-container">
                  <div class="min-section">
                    <span>Min</span>
                    <div class="input-group">
                      <button class="n-btn">-</button>
                      <input v-model="groupSizeMin" @change="fetchPrograms" type="number" min="2" max="5">
                      <button class="n-btn">＋</button>
                    </div>
                  </div>

                  <span class="tilde">~</span>

                  <div class="max-section">
                    <span>Max</span>
                    <div class="input-group">
                      <button class="n-btn">-</button>
                      <input v-model="groupSizeMax" @change="fetchPrograms" type="number" min="2" max="5">
                      <button class="n-btn">＋</button>
                    </div>
                  </div>
                </div>

              </form>
            </details>

            <!-- 소요시간 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">Duration</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">
                <div class="modal-radio">
                  <div>
                    <input type="radio" class="radio" name="duration" :value="null" v-model="duration" @change="fetchPrograms">
                    <label>All</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="duration" value="1" v-model="duration" @change="fetchPrograms">
                    <label>Under 2 hours</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="duration" value="2" v-model="duration" @change="fetchPrograms">
                    <label>2 ~ 4 hours</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="duration" value="3" v-model="duration" @change="fetchPrograms">
                    <label>4 ~ 6 hours</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="duration" value="4" v-model="duration" @change="fetchPrograms">
                    <label>6 ~ 8 hours</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="duration" value="5" v-model="duration" @change="fetchPrograms">
                    <label>Over 8 hours</label>
                  </div>
                </div>
              </form>
            </details>

            <!-- 시작시간 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">Start Time</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">
                <div class="modal-radio">
                  <div>
                    <input type="radio" class="radio" name="startTime" value="1" v-model="startTime" @change="fetchPrograms">
                    <label>All</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="startTime" value="2" v-model="startTime" @change="fetchPrograms">
                    <label>Before 12 PM</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="startTime" value="3" v-model="startTime" @change="fetchPrograms">
                    <label>12 PM to 6 PM</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="startTime" value="4" v-model="startTime" @change="fetchPrograms">
                    <label>After 6 PM</label>
                  </div>
                </div>
              </form>
            </details>

          </div>
        </aside>
        <div class="layout-main-list">
          <!--=== 필터 .n-filter ==========================================-->
          <section class="n-filter md:d:none bg-color:base-1">
            <h1 class="d:none">필터</h1>

            <div class="overflow-x:auto">
              <ul class="item-wrapper">
                <li><a href=""
                       class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:calendar n-icon-size:1 n-deco n-deco-gap:1">기간</a>
                </li>
                <li><a href=""
                       class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:price n-icon-size:1 n-deco n-deco-gap:1">가격</a>
                </li>
                <li><a href=""
                       class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:category n-icon-size:1 n-deco n-deco-gap:1">카테고리</a>
                </li>
                <li><a href=""
                       class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:globe n-icon-size:1 n-deco n-deco-gap:1">언어</a>
                </li>
                <li><a href=""
                       class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:clock n-icon-size:1 n-deco n-deco-gap:1">시작시간</a>
                </li>
                <li><a href=""
                       class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:group n-icon-size:1 n-deco n-deco-gap:1 ">참여인원</a>
                </li>
              </ul>
            </div>

            <div class="reset-box">
              <span class="icon-box n-deco1 n-icon n-icon:reset">
                초기화
              </span>
            </div>
          </section>

          <!--=== 프로그램 목록==========================================-->
          <section class="pg-list">
            <!--=== 정렬 .list-header ==========================================-->
            <header class="list-header bg-color:base-1">
              <h1 class="d:none">프로그램 목록</h1>
              <div>
                <span class="list-item-count">{{ totalRowCount }} Result</span>
                <!--            <button type="button" class="n-icon n-icon:arrow_swap n-deco n-deco-gap:1">-->
                <!--                정렬-->
                <!--            </button>-->
                <a href=""
                   class="option sort n-icon n-icon:arrow_swap n-icon-color:base-6 n-deco n-deco-gap:1">
                  정렬
                </a>
              </div>
            </header>

            <!--=== 프로그램 카드 목록 ===================================================-->
            <ul v-if="programs.length > 0" class="n-card-container-column bg-color:base-1">
              <!-- =================================== 예약 카드 1개 =================================== -->
              <li v-for="p in programs" class="n-card n-card-column bg-color:base-1">
                <NuxtLink :to="`/programs/${p.id}`" class="n-link-box"></NuxtLink>
                <h2 class="d:none">프로그램 카드</h2>

                <div class="card-main">
                  <div class="img-wrapper">
                    <img src="/public/image/program_01.png" alt="대표사진">
                  </div>

                  <div class="card-info-wrapper">
                    <div class="card-info gap:1 color:base-7 font-size:5">
                      <span>{{ p.categoryNames.join(' · ') }}</span>
                    </div>
                    <p class="title">{{ p.title }}</p>

                    <div class="card-info">
                                            <span class="n-icon n-icon:star n-deco"
                                                  style="--icon-color: var(--color-yellow-1)"></span>
                      <span>{{ p.rating }} ({{ p.totalRatingCount }})</span>
                    </div>
                    <div class="card-info">
                      <span class="n-icon n-icon:clock_sand n-deco"></span>
                      <span>{{ p.duration }} hours</span>
                    </div>
                  </div>
                </div>

                <div class="card-footer justify-content:flex-end">
                                    <span
                                        class="n-icon n-icon:price n-icon-size:2 n-deco n-deco-gap:1 font-size:8 margin-right:3">{{
                                        p.price ? Number(p.price).toLocaleString() : '가격 없음'
                                      }}</span>
                </div>
              </li>
            </ul>
            <p v-else class="no-results-message">해당 목록이 존재하지 않습니다.</p>
            <div ref="infiniteScrollTrigger" class="infinite-scroll-trigger"></div> <!-- 무한 스크롤 트리거 -->
          </section>
        </div>

        <!--=== 필터 @media (min-width:768px)==========================================-->

      </div>
    </section>

  </main>
</template>


<script setup>
import {ref, onMounted, onUnmounted, computed} from 'vue';
import axios from 'axios';

const programs = ref([]);
const totalRowCount = ref(0);
const categories = ref([]);
const page = ref(1);
const pageSize = ref(15);
const fetching = ref(false);

const allCategoriesSelected = ref(true);
// 필터 값들
const selectedCategoryIds = ref([]);
const startDate = ref(null);
const endDate = ref(null);
const minPrice = ref(null);
const maxPrice = ref(null);
const groupSizeMin = ref(null);
const groupSizeMax = ref(null);
const duration = ref(null);
const startTime = ref(1);
const language = ref(null);



const fetchCategories = async () => {
  const response = await axios.get("http://localhost:8080/api/v1/categories");
  categories.value = response.data;
};


const fetchPrograms = async () => {
  if (fetching.value) return; // 중복 호출 방지
  fetching.value = true;

  try {
    console.log("Fetching page:", page.value); // 디버깅 로그: 현재 페이지 번호
    const response = await axios.get('http://localhost:8080/api/v1/programs', {
      params: {
        categoryIds: selectedCategoryIds.value.length === 0 ? null : selectedCategoryIds.value.join(','),
        startDate: startDate.value,
        endDate: endDate.value,
        minPrice: minPrice.value,
        maxPrice: maxPrice.value,
        groupSizeMin: groupSizeMin.value,
        groupSizeMax: groupSizeMax.value,
        duration: duration.value,
        startTime: startTime.value,
        language: language.value,
        page: page.value,
        pageSize: pageSize.value,
      },
    });

    // 데이터 추가 및 페이지 값 증가
    if (page.value === 1) {
      programs.value = response.data.programs; // 필터 적용 시 데이터를 새로 설정
    } else {
      programs.value.push(...response.data.programs); // 무한 스크롤로 데이터를 추가
    }
    totalRowCount.value = response.data.totalRowCount;
    // page.value += 1; // 다음 페이지로 증가
    // console.log("Next page to fetch:", page.value); // 디버깅 로그: 증가된 페이지 값

  } catch (error) {
    console.error("프로그램 목록을 가져오는 데 실패했습니다:", error);
  } finally {
    fetching.value = false; // 데이터 로드가 완료되면 fetching 상태 해제
    console.log("Fetching completed, fetching set to:", fetching.value); // 디버깅 로그: fetching 상태
  }
};

const resetProgramsAndFetch = () => {
  programs.value = [];
  page.value = 1;
  fetchPrograms();
};

watch([selectedCategoryIds, startDate, endDate, minPrice, maxPrice, groupSizeMin, groupSizeMax, duration, startTime, language], resetProgramsAndFetch);

const observeInfiniteScroll = () => {
  const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && !fetching.value) {
          page.value++;
          fetchPrograms();
        }
      },
      { rootMargin: '0px', threshold: 1.0 }
  );

  observer.observe(document.querySelector('.infinite-scroll-trigger'));
};

// All 체크박스를 클릭했을 때 모든 카테고리를 선택/해제하고 fetchPrograms 호출
const toggleAllCategories = () => {
  if (allCategoriesSelected.value) {
    selectedCategoryIds.value = [];
  } else {
    selectedCategoryIds.value = categories.value.map(category => category.id); // 개별 카테고리 선택
  }
  fetchPrograms(); // 변경 사항 적용
};


// 개별 카테고리 선택 상태가 변경될 때 All 체크박스 상태를 업데이트하고 fetchPrograms 호출
const updateAllCheckbox = () => {
  if (selectedCategoryIds.value.length === categories.value.length) {
    // 모든 개별 체크박스가 선택된 경우 All 체크박스만 선택
    allCategoriesSelected.value = true;
    selectedCategoryIds.value = [];
  } else {
    // 선택된 개별 카테고리 수가 전체와 다를 경우 All 체크박스 해제
    allCategoriesSelected.value = false;
  }
  fetchPrograms(); // 변경 사항 적용
};

//=========필터 리셋============

const resetFilters = () => {
  selectedCategoryIds.value = [];
  startDate.value = null;
  endDate.value = null;
  minPrice.value = null;
  maxPrice.value = null;
  groupSizeMin.value = null;
  groupSizeMax.value = null;
  duration.value = null;
  startTime.value = null;
  language.value = null;
  page.value = 1; // 첫 페이지로 초기화
  fetchPrograms(); // 초기화 후 프로그램 목록 다시 가져오기
};


onMounted(() => {
  fetchPrograms(); // 첫 번째 페이지 데이터 로드
  fetchCategories();
  observeInfiniteScroll();
});


</script>


<style scoped>
.layout-body {
  .layout-main {
    display: flex;

    .layout-main-list {
      flex-grow: 1;
      width: 100%;

      .n-filter {
        .n-btn {
          --btn-font-size: 12px;
        }

      }

      /*===============================================================================================*/

      .list-header {
        padding: 8px 20px;
      }

      /*===============================================================================================*/

      .n-card-container-column {
      }

      /*===============================================================================================*/

      .n-card {
        position: relative;

        .n-link-box {
          display: block;
          width: 100%;
          height: 100%;
          position: absolute;
          top: 0;
          left: 0;
          z-index: 1;
        }
      }
    }

    .layout-main-aside {
      display: none;
      flex-direction: column;
      flex-shrink: 0;
      width: 250px;
      margin: 0 16px;

      .n-title {
        --title-font-size: var(--font-size-9);
        /* 18 */
        --title-font-weight: 600;
        /* semi bold */
        --title-padding: 20px 4px;

        .n-icon {
          --icon-size: var(--icon-size-4);
          cursor: pointer;
        }
      }

      .filters {
        padding: 0 16px;
        border: 1px solid var(--color-base-3);
        border-radius: 12px;
      }
    }
  }
}
.no-results-message{
  padding:100px;
  display: flex;
  justify-content: center;
}

@media (min-width: 768px) {
  .layout-body {
    margin-left: auto;
    margin-right: auto;
    /*min-width: 992px;*/
    width: 100%;
    max-width: 1092px;

    .layout-main {
      .layout-main-list {
      }

      .layout-main-aside {
        display: flex;
        position: sticky;
        top: 0;
        height: 100vh; /* 화면 전체 높이 */
        overflow-y: auto; /* 내용이 넘칠 경우 내부 스크롤 */
        padding-right: 10px; /* 스크롤바와의 여유 공간 */
        box-sizing: border-box;
        -ms-overflow-style: none;  /* IE 및 Edge */
        scrollbar-width: none;  /* Firefox */

        /* Chrome, Safari, Edge에서 스크롤바 숨기기 */
        &::-webkit-scrollbar {
          display: none;
        }
      }
    }
  }

  .md\:d\:inline {
    display: inline;
  }

  .md\:d\:none {
    display: none;
  }
}

.filter {
  border-bottom: 1px solid var(--color-base-3);

  .collapse {
    --collapse-padding: 24px 0 24px 0;
    display: flex;
    align-items: center;
    padding: var(--collapse-padding);
    list-style-type: none;

    .title {
      /*justify-content: space-between;*/
      /*align-items: center;*/
      font-size: 16px;
      font-weight: 600;
      /*flex-grow: 1;*/
      margin-right: auto;
    }

    .n-icon {
      cursor: pointer;
      /*border: none;*/
      /*position: absolute;*/
      /*right: var(--gap-7);*/
    }
  }


  .form {
    margin-bottom: 24px;
  }

  /*기간 필터*/

  .modal-duration {
    display: flex;
    justify-content: center;
    align-items: center;
    border: 1px solid var(--color-base-3);
    padding: 2px 4px;
    gap: 6px;
    border-radius: 4px;
    width: 100%;
    /* 입력 필드가 적절한 크기로 보이게 설정 */

    input {
      border: none;
      outline: none;
      font-size: 12px;
      cursor: pointer;
    }

    input[type="date"]::-webkit-calendar-picker-indicator {
      margin-left: -14px;
      /* 아이콘과 텍스트 사이의 간격 줄이기 */
    }
  }


  /*카테고리 필터*/

  .modal-checkbox {
    width: 100%;
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    background-color: transparent;
    gap: 6px;
    padding: 0 8px;

    label {
      display: flex;
      align-items: center;
      font-size: 14px;
      width: 100%;

      input[type="checkbox"] {
        margin-right: 8px;
        /* 체크박스와 텍스트 사이 간격 */
      }
    }
  }

  /*가격 필터*/

  .price-container {
    margin-top: 8px;
    display: flex;
    flex-direction: column;
    gap: var(--gap-2);
    padding: 0 8px;

    .range-slider {
      display: flex;
      justify-content: center;
      align-items: center;
      margin-bottom: 16px;
      position: relative;

      .slider {
        -webkit-appearance: none;
        width: 100%;
        height: 8px;
        background-color: var(--color-base-3);
        border-radius: 5px;
        outline: none;
        position: relative;
        pointer-events: none; /* 슬라이더 바에서도 클릭이 가능하게 변경 */
      }

      .slider::-webkit-slider-thumb {
        -webkit-appearance: none;
        appearance: none;
        width: 16px;
        height: 16px;
        background-color: var(--color-sub-1);
        border-radius: 50%;
        cursor: pointer;
        position: relative;
        z-index: 2; /* 두 슬라이더 핸들의 z-index를 같은 값으로 설정 */
        pointer-events: auto;
      }

    }

    .price-inputs {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;

      input {
        height: 28px;
        width: 86px;
        font-size: 12px;
        text-align: center;
        border-radius: 4px;
        border: 1px solid var(--color-base-3);
      }

      .tilde {
        margin: 0 8px;
        font-size: 16px;
      }
    }
  }

  /*언어 필터*/
  /*소요시간 필터*/
  /*시작시간 필터*/

  .modal-radio {
    display: flex;
    flex-direction: column;
    flex-wrap: nowrap;
    background-color: transparent;
    padding: 0 8px;
    gap: 6px;

    .radio {
      margin-right: 8px;
    }
  }


  /*인원 필터*/

  .min-max-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    /* 전체 너비를 조정하고 싶으면 값 변경 */

    .min-section,
    .max-section {
      display: flex;
      flex-direction: column;
      align-items: center;
      width: 100%;
      gap: var(--gap-2);
    }

    .input-group {
      display: flex;
      align-items: center;
      gap: 5px;
      /* 버튼과 입력창 사이의 간격 */

      .n-btn {
        --btn-border-color: var(--color-base-4);
        --btn-bg-color: var(--color-base-4);
        --btn-border-radius: 6px;
        --btn-padding: 4px 8px;
        width: 6px;
      }
    }

    input[type="number"] {
      width: 32px;
      height: 28px;
      font-size: 12px;
      text-align: center;
      border-radius: 4px;
      border: 1px solid var(--color-base-3);
    }

    .tilde {
      margin: 0 8px;
      font-size: 16px;
      padding: 16px 0 0 0;
    }
  }
}

.filter[open] {
  .collapse {
    --collapse-padding: 24px 0 12px 0;

    .n-icon\:arrow_up:before {
      --icon: url(/assets/image/icon/arrow_down.svg);
    }
  }
}
</style>