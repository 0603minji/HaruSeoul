<template>
  <main>
    <section class="layout-body">
      <h1 class="d:none">프로그램 목록</h1>

      <div class="layout-main">
        <aside class="layout-main-aside">
          <header class="n-title">
            <h1 class="">Filter</h1>
            <div>
              <button @click.prevent="resetFilters" class="n-icon n-icon:reset"
                      style="--icon-color: var(--color-sub-1)">초기화
              </button>
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
                    <input type="date" v-model="startDate" @change="fetchFilteredPrograms" placeholder="Start Date">
                  </label>
                  <span>~</span>
                  <label>
                    <input type="date" v-model="endDate" @change="fetchFilteredPrograms" placeholder="End Date">
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
                    <input @change="updateAllCheckbox" type="checkbox" v-model="selectedCategoryIds" :value="c.id">
                    {{ c.name }}
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
                  <div class="price-inputs">
                    <input v-model="minPrice" @input="fetchFilteredPrograms" type="number" placeholder="₩0">
                    <span class="tilde">~</span>
                    <input v-model="maxPrice" @input="fetchFilteredPrograms" type="number" placeholder="₩1000000">
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
                    <input id="language-all" type="radio" class="radio" name="language" :value="null" v-model="language"
                           @change="fetchFilteredPrograms">
                    <label for="language-all">All</label>
                  </div>
                  <div>
                    <input id="language-english" type="radio" class="radio" name="language" value="Korean"
                           v-model="language" @change="fetchFilteredPrograms">
                    <label for="language-english">Korean</label>
                  </div>
                  <div>
                    <input id="language-english" type="radio" class="radio" name="language" value="English"
                           v-model="language" @change="fetchFilteredPrograms">
                    <label for="language-english">English</label>
                  </div>
                  <div>
                    <input id="language-japanese" type="radio" class="radio" name="language" value="Japanese"
                           v-model="language" @change="fetchFilteredPrograms">
                    <label for="language-japanese">Japanese</label>
                  </div>
                  <div>
                    <input id="language-chinese" type="radio" class="radio" name="language" value="Chinese"
                           v-model="language" @change="fetchFilteredPrograms">
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
                      <input v-model="groupSizeMin" @change="fetchFilteredPrograms" type="number" min="2" max="5">
                    </div>
                  </div>

                  <span class="tilde">~</span>

                  <div class="max-section">
                    <span>Max</span>
                    <div class="input-group">
                      <input v-model="groupSizeMax" @change="fetchFilteredPrograms" type="number" min="2" max="5">
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
                    <input type="radio" class="radio" name="duration" :value="null" v-model="duration"
                           @change="fetchFilteredPrograms">
                    <label>All</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="duration" value="1" v-model="duration"
                           @change="fetchFilteredPrograms">
                    <label>Under 2 hours</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="duration" value="2" v-model="duration"
                           @change="fetchFilteredPrograms">
                    <label>2 ~ 4 hours</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="duration" value="3" v-model="duration"
                           @change="fetchFilteredPrograms">
                    <label>4 ~ 6 hours</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="duration" value="4" v-model="duration"
                           @change="fetchFilteredPrograms">
                    <label>6 ~ 8 hours</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="duration" value="5" v-model="duration"
                           @change="fetchFilteredPrograms">
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
                    <input type="radio" class="radio" name="startTime" value="1" v-model="startTime"
                           @change="fetchFilteredPrograms">
                    <label>All</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="startTime" value="2" v-model="startTime"
                           @change="fetchFilteredPrograms">
                    <label>Before 12 PM</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="startTime" value="3" v-model="startTime"
                           @change="fetchFilteredPrograms">
                    <label>12 PM to 6 PM</label>
                  </div>
                  <div>
                    <input type="radio" class="radio" name="startTime" value="4" v-model="startTime"
                           @change="fetchFilteredPrograms">
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
                <li>
                  <a href="#" @click.prevent="openModal('period', $event)"
                     :class="['n-btn', 'n-btn-pg-filter', 'n-btn:hover', 'n-icon', 'n-icon:calendar', 'n-icon-size:1', 'n-deco', 'n-deco-gap:1', { active: isPeriodActive }]">
                    기간
                  </a>
                </li>
                <li>
                  <a href="#" @click.prevent="openModal('price', $event)"
                     :class="['n-btn', 'n-btn-pg-filter', 'n-btn:hover', 'n-icon', 'n-icon:price', 'n-icon-size:1', 'n-deco', 'n-deco-gap:1', { active: isPriceActive }]">
                    가격
                  </a>
                </li>
                <li>
                  <a href="#" @click.prevent="openModal('category', $event)"
                     :class="['n-btn', 'n-btn-pg-filter', 'n-btn:hover', 'n-icon', 'n-icon:category', 'n-icon-size:1', 'n-deco', 'n-deco-gap:1',{ active: isCategoryActive }]">
                    카테고리
                  </a>
                </li>
                <li>
                  <a href="#" @click.prevent="openModal('language', $event)"
                     :class="['n-btn', 'n-btn-pg-filter', 'n-btn:hover', 'n-icon', 'n-icon:globe', 'n-icon-size:1', 'n-deco', 'n-deco-gap:1', { active: isLanguageActive }]">
                    언어
                  </a>
                </li>
                <li>
                  <a href="#" @click.prevent="openModal('startTime', $event)"
                     :class="['n-btn', 'n-btn-pg-filter', 'n-btn:hover', 'n-icon', 'n-icon:clock', 'n-icon-size:1', 'n-deco', 'n-deco-gap:1', { active: isStartTimeActive }]">
                    시작시간
                  </a>
                </li>
                <li>
                  <a href="#" @click.prevent="openModal('groupSize', $event)"
                     :class="['n-btn', 'n-btn-pg-filter', 'n-btn:hover', 'n-icon', 'n-icon:group', 'n-icon-size:1', 'n-deco', 'n-deco-gap:1', { active: isGroupSizeActive }]">
                    참여인원
                  </a>
                </li>
                <li>
                  <a href="#" @click.prevent="openModal('duration', $event)"
                     :class="['n-btn', 'n-btn-pg-filter', 'n-btn:hover', 'n-icon', 'n-icon:group', 'n-icon-size:1', 'n-deco', 'n-deco-gap:1',  { active: isDurationActive }]">
                    소요시간
                  </a>
                </li>
              </ul>

            </div>

            <div class="reset-box">
              <div class="gradation"></div>
              <div class="btn-box">
              <span @click.prevent="resetFilters" style="cursor: pointer" class="reset-btn icon-box n-deco1 n-icon n-icon:reset">
                reset
              </span>
              </div>
            </div>


            <!-- 모달 -->
            <transition name="modal">
              <div v-if="selectedModal" class="modal-background" :style="modalPosition">
                <aside class="modal-content" @click.stop>
                  <header>
                    <h2>{{ selectedModal }} 필터</h2>
                    <button @click="closeModal">X</button>
                  </header>
                  <div v-if="selectedModal === 'period'">
                    <label>Start Date:</label>
                    <input type="date" v-model="startDate" @change="fetchFilteredPrograms">
                    <label>End Date:</label>
                    <input type="date" v-model="endDate" @change="fetchFilteredPrograms">
                  </div>

                  <div v-if="selectedModal === 'price'">
                    <label>min price</label>
                    <input v-model="minPrice" @input="fetchFilteredPrograms" type="number" placeholder="₩0">
                    <label>End Date:</label>
                    <input v-model="maxPrice" @input="fetchFilteredPrograms" type="number" placeholder="₩1000000">
                  </div>

                  <div v-if="selectedModal === 'category'">
                    <div class="modal-checkbox">
                      <!-- All 체크박스 -->
                      <label>
                        <input v-model="allCategoriesSelected" @change="toggleAllCategories" type="checkbox"> All
                      </label>
                      <!-- 카테고리 체크박스 목록 -->
                      <label v-for="c in categories" :key="c.id">
                        <input @change="updateAllCheckbox" type="checkbox" v-model="selectedCategoryIds" :value="c.id">
                        {{ c.name }}
                      </label>
                    </div>
                  </div>

                  <div v-if="selectedModal === 'language'">
                    <div>
                      <input id="language-all" type="radio" class="radio" name="language" :value="null"
                             v-model="language" @change="fetchFilteredPrograms">
                      <label for="language-all">All</label>
                    </div>
                    <div>
                      <input id="language-english" type="radio" class="radio" name="language" value="Korean"
                             v-model="language" @change="fetchFilteredPrograms">
                      <label for="language-english">Korean</label>
                    </div>
                    <div>
                      <input id="language-english" type="radio" class="radio" name="language" value="English"
                             v-model="language" @change="fetchFilteredPrograms">
                      <label for="language-english">English</label>
                    </div>
                    <div>
                      <input id="language-japanese" type="radio" class="radio" name="language" value="Japanese"
                             v-model="language" @change="fetchFilteredPrograms">
                      <label for="language-japanese">Japanese</label>
                    </div>
                    <div>
                      <input id="language-chinese" type="radio" class="radio" name="language" value="Chinese"
                             v-model="language" @change="fetchFilteredPrograms">
                      <label for="language-chinese">Chinese</label>
                    </div>
                  </div>

                  <div v-if="selectedModal === 'startTime'">
                    <div>
                      <input type="radio" class="radio" name="startTime" value="1" v-model="startTime"
                             @change="fetchFilteredPrograms">
                      <label>All</label>
                    </div>
                    <div>
                      <input type="radio" class="radio" name="startTime" value="2" v-model="startTime"
                             @change="fetchFilteredPrograms">
                      <label>Before 12 PM</label>
                    </div>
                    <div>
                      <input type="radio" class="radio" name="startTime" value="3" v-model="startTime"
                             @change="fetchFilteredPrograms">
                      <label>12 PM to 6 PM</label>
                    </div>
                    <div>
                      <input type="radio" class="radio" name="startTime" value="4" v-model="startTime"
                             @change="fetchFilteredPrograms">
                      <label>After 6 PM</label>
                    </div>
                  </div>

                  <div v-if="selectedModal === 'groupSize'">
                    <div class="min-section">
                      <span>Min</span>
                      <div class="input-group">
                        <input v-model="groupSizeMin" @change="fetchFilteredPrograms" type="number" min="2" max="5">
                      </div>
                    </div>
                    <div class="max-section">
                      <span>Max</span>
                      <div class="input-group">
                        <input v-model="groupSizeMax" @change="fetchFilteredPrograms" type="number" min="2" max="5">
                      </div>
                    </div>
                  </div>

                  <div v-if="selectedModal === 'duration'">
                    <input type="radio" class="radio" name="duration" :value="null" v-model="duration"
                           @change="fetchFilteredPrograms">
                    <label>All</label>
                    <div>
                      <input type="radio" class="radio" name="duration" value="1" v-model="duration"
                             @change="fetchFilteredPrograms">
                      <label>Under 2 hours</label>
                    </div>
                    <div>
                      <input type="radio" class="radio" name="duration" value="2" v-model="duration"
                             @change="fetchFilteredPrograms">
                      <label>2 ~ 4 hours</label>
                    </div>
                    <div>
                      <input type="radio" class="radio" name="duration" value="3" v-model="duration"
                             @change="fetchFilteredPrograms">
                      <label>4 ~ 6 hours</label>
                    </div>
                    <div>
                      <input type="radio" class="radio" name="duration" value="4" v-model="duration"
                             @change="fetchFilteredPrograms">
                      <label>6 ~ 8 hours</label>
                    </div>
                    <div>
                      <input type="radio" class="radio" name="duration" value="5" v-model="duration"
                             @change="fetchFilteredPrograms">
                      <label>Over 8 hours</label>
                    </div>
                  </div>

                  <!-- 다른 필터 모달들도 필요한 대로 추가 -->
                </aside>
              </div>
            </transition>
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
                <a href="#"
                   class="option sort n-icon n-icon:arrow_swap n-icon-color:base-6 n-deco n-deco-gap:1">
                  정렬
                </a>
              </div>
            </header>
            <div class="search-box d:flex">
              <label class="search-box-title" for="searchInput">검색창</label>
              <input
                  id="searchInput"
                  class="search-box-input"
                  type="text"
                  v-model="title"
                  @keyup.enter="fetchFilteredPrograms"
              placeholder="프로그램 이름을 입력하세요"
              >
              <button class="search-box-button n-icon n-icon:search" @click.prevent="fetchFilteredPrograms"></button>
            </div>
            <!--=== 프로그램 카드 목록 ===================================================-->
            <ul v-if="programs.length > 0" class="n-card-container-column bg-color:base-1">
              <!-- =================================== 예약 카드 1개 =================================== -->
              <li v-for="p in programs" class="n-card n-card-column bg-color:base-1">
                <NuxtLink :to="`/programs/${p.id}`" class="n-link-box"></NuxtLink>
                <h2 class="d:none">프로그램 카드</h2>

                <div class="card-main">
                  <div class="img-wrapper">
                    <img :src="`http://localhost:8083/api/v1/${p.img}`" alt="대표사진">
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
const title = ref('');
const minPrice = ref(null);
const maxPrice = ref(null);
const groupSizeMin = ref(null);
const groupSizeMax = ref(null);
const duration = ref(null);
const startTime = ref(1);
const language = ref(null);

const selectedModal = ref(null);
const modalPosition = ref({top: '0px', left: '0px'});
const isPeriodActive = ref(false);
const isPriceActive = ref(false);
const isCategoryActive = ref(false);
const isLanguageActive = ref(false);
const isStartTimeActive = ref(false);
const isGroupSizeActive = ref(false);
const isDurationActive = ref(false);

let observer;

const fetchCategories = async () => {
  const response = await axios.get("http://localhost:8083/api/v1/categories");
  categories.value = response.data;

};


const fetchFilteredPrograms = async () => {
  if (fetching.value) return;
  fetching.value = true;

  try {
    page.value = 1; // 검색 시 페이지 초기화
    programs.value = []; // 기존 데이터 초기화

    const response = await axios.get('http://localhost:8083/api/v1/programs', {
      params: {
        categoryIds: selectedCategoryIds.value.length === 0 ? null : selectedCategoryIds.value.join(','),
        startDate: startDate.value,
        endDate: endDate.value,
        title: title.value,
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

    programs.value = response.data.programs;
    totalRowCount.value = response.data.totalRowCount;
  } catch (error) {
    console.error('프로그램 목록을 가져오는 데 실패했습니다:', error);
  } finally {
    fetching.value = false;
  }
};

const fetchNextPage = async () => {
  if (fetching.value) return;
  fetching.value = true;

  try {
    page.value += 1; // 다음 페이지 로드
    const response = await axios.get('http://localhost:8083/api/v1/programs', {
      params: {
        categoryIds: selectedCategoryIds.value.length === 0 ? null : selectedCategoryIds.value.join(','),
        startDate: startDate.value,
        endDate: endDate.value,
        title: title.value,
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

    programs.value.push(...response.data.programs);
  } catch (error) {
    console.error('추가 페이지 로드 중 오류 발생:', error);
  } finally {
    fetching.value = false;
  }
};



const resetProgramsAndFetch = () => {
  programs.value = [];
  page.value = 1;
  fetchFilteredPrograms();
};

watch(
    [selectedCategoryIds, startDate, endDate, minPrice, maxPrice, groupSizeMin, groupSizeMax, duration, startTime, language],
    () => {
      disconnectInfiniteScroll(); // 기존 관찰자 해제
      fetchFilteredPrograms(); // 필터 변경된 데이터 로드
      observeInfiniteScroll(); // 새로운 관찰자 등록
    }
);


watch([startDate, endDate], ([newStartDate, newEndDate]) => {
  isPeriodActive.value = !!newStartDate || !!newEndDate;
});
watch([minPrice, maxPrice], ([newMinPrice, newMaxPrice]) => {
  isPriceActive.value = !!newMinPrice || !!newMaxPrice;
});
watch(selectedCategoryIds, (newCategoryIds) => {
  isCategoryActive.value = newCategoryIds.length > 0;
});
watch(language, (newLanguage) => {
  isLanguageActive.value = !!newLanguage;
});
watch(startTime, (newStartTime) => {
  isStartTimeActive.value = newStartTime !== 1;
});
watch([groupSizeMin, groupSizeMax], ([newGroupSizeMin, newGroupSizeMax]) => {
  isGroupSizeActive.value = !!newGroupSizeMin || !!newGroupSizeMax;
});
watch(duration, (newDuration) => {
  isDurationActive.value = !!newDuration;
});

watch(title, (newValue) => {
  if (!newValue || newValue.trim() === '') {
    resetFilters(); // 검색어가 비었을 경우 필터 초기화
  } else {
    fetchFilteredPrograms(); // 검색어가 있을 경우 필터된 데이터를 로드
  }
});



const observeInfiniteScroll = () => {
  const trigger = document.querySelector('.infinite-scroll-trigger');
  if (!trigger) return;

  observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && !fetching.value) {
          fetchNextPage();
        }
      },
      { rootMargin: '0px', threshold: 1.0 }
  );

  observer.observe(trigger);
};

const disconnectInfiniteScroll = () => {
  if (observer) observer.disconnect();
};


// All 체크박스를 클릭했을 때 모든 카테고리를 선택/해제하고 fetchFilteredPrograms 호출
const toggleAllCategories = () => {
  if (allCategoriesSelected.value) {
    selectedCategoryIds.value = [];
  } else {
    selectedCategoryIds.value = categories.value.map(category => category.id); // 개별 카테고리 선택
  }
  fetchFilteredPrograms();
};


// 개별 카테고리 선택 상태가 변경될 때 All 체크박스 상태를 업데이트하고 fetchFilteredPrograms 호출
const updateAllCheckbox = () => {
  if (selectedCategoryIds.value.length === categories.value.length) {
    // 모든 개별 체크박스가 선택된 경우 All 체크박스만 선택
    allCategoriesSelected.value = true;
    selectedCategoryIds.value = [];
  } else {
    // 선택된 개별 카테고리 수가 전체와 다를 경우 All 체크박스 해제
    allCategoriesSelected.value = false;
  }
  fetchFilteredPrograms();
};

//=========필터 리셋============

const resetFilters = () => {
  selectedCategoryIds.value = [];
  startDate.value = null;
  endDate.value = null;
  title.value = '';
  minPrice.value = null;
  maxPrice.value = null;
  groupSizeMin.value = null;
  groupSizeMax.value = null;
  duration.value = null;
  startTime.value = 1;
  language.value = null;

  page.value = 1;
  disconnectInfiniteScroll(); // 기존 관찰자 해제
  fetchFilteredPrograms(); // 초기화 후 새 데이터 로드
  observeInfiniteScroll(); // 무한 스크롤 다시 등록
};


const openModal = async (filterType, event) => {
  if (process.client) {
    selectedModal.value = filterType;
    await nextTick();

    const buttonRect = event.target.getBoundingClientRect();
    const modalWidth = 300;

    let leftPosition = buttonRect.left + window.scrollX;
    if (leftPosition + modalWidth > window.innerWidth) {
      leftPosition = window.innerWidth - modalWidth - 10;
    }

    modalPosition.value = {
      top: `${buttonRect.bottom + window.scrollY - 60}px`,
      left: `${leftPosition}px`,
    };
  }
};

// 모달 닫기 함수
const closeModal = () => {
  selectedModal.value = null;
};



onMounted(() => {
  fetchFilteredPrograms();
  fetchCategories();
  observeInfiniteScroll();
});

onUnmounted(() => {
  disconnectInfiniteScroll(); // 컴포넌트가 제거될 때 관찰자 해제
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

.search-box{
  width: 100%;
  height: 100%;
  align-content: center;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;

  .search-box-title{
    display: none;
  }
  .search-box-input {
    position: relative;
    border: 2px solid var(--color-base-3);
    border-radius: 15px;
    width: 70%;
    height: 40px;
    padding-left: 20px;
  }
  .search-box-button {
    position: relative;
    right: 50px;
    cursor: pointer;
  }

}

.no-results-message {
  padding: 100px;
  display: flex;
  justify-content: center;
}

/* 모달 배경 제거 */
.modal-background {
  position: absolute;
  top: 100%; /* 버튼 바로 아래에 위치하도록 */
  left: 0;
  width: 100%;
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  z-index: 1000;
}

/* 모달 콘텐츠 */
.modal-content {
  background: #fff;
  padding: 1rem;
  border-radius: 8px;
  max-width: 300px;
  width: 100%;
  box-shadow: 0px 8px 16px rgba(0, 0, 0, 0.15);
  position: absolute;
  top: 0;
  left: 0;
  transform: translateY(10px); /* 약간 아래로 위치 조정 */
  animation: fadeIn 0.2s ease-in-out;
  z-index: 1001;
}

/* 모달 삼각형 화살표 추가 */
.modal-content::before {
  content: "";
  position: absolute;
  top: -8px;
  left: 16px; /* 삼각형의 위치 조정 */
  width: 0;
  height: 0;
  border-left: 8px solid transparent;
  border-right: 8px solid transparent;
  border-bottom: 8px solid #fff;
  box-shadow: 0px -1px 3px rgba(0, 0, 0, 0.1); /* 삼각형에 그림자 */
}

/* 모달 제목 스타일 */
.modal-content header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.modal-content h2 {
  font-size: 1.2rem;
  color: #333;
  margin: 0;
}

.modal-content button {
  background: none;
  border: none;
  color: #666;
  font-size: 1.2rem;
  cursor: pointer;
  transition: color 0.3s ease;
}

.modal-content button:hover {
  color: #ff6666;
}

/* 인풋과 라벨 스타일 */
.modal-content label {
  font-size: 0.9rem;
  color: #555;
  margin-bottom: 0.5rem;
  display: inline-block;
}

.modal-content input[type="date"],
.modal-content input[type="number"] {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 0.8rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  color: #333;
  transition: border 0.3s ease;
}

.modal-content input[type="date"]:focus,
.modal-content input[type="number"]:focus {
  border-color: #4a90e2;
  outline: none;
}

/* 버튼 스타일 */
.modal-content .action-button {
  background-color: #4a90e2;
  color: white;
  padding: 0.4rem 1rem;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
  width: 100%;
  margin-top: 0.5rem;
}

.modal-content .action-button:hover {
  background-color: #357abd;
}

/* 부드러운 등장 애니메이션 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

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
        -ms-overflow-style: none; /* IE 및 Edge */
        scrollbar-width: none; /* Firefox */

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