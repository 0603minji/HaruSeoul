<template>
  <main>
    <section class="n-layout-mj">
      <!--=== 헤더 ==========================================-->
      <header class="n-title">
        <h1 class="">프로그램 관리</h1>
        <div>
          <nuxt-link to="/host/programs/new"
                     class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:plus n-deco">프로그램 등록
          </nuxt-link>
        </div>
      </header>

      <section class="d:flex">
        <section class="layout-list">
          <!--=== 필터 ==========================================-->
          <section class="n-filter bg-color:base-1 padding-x:6">
            <h1 class="d:none">필터</h1>

              <div v-show="activeDropdown === 'program'" class="dropdown-menu program" :style="dropdownPosition">
                <div class="d:flex jc:space-between">
                  <h3>프로그램 필터</h3>
                  <span @click.prevent="toggleDropdown" class="n-icon n-icon:exit cursor:pointer"></span>
                </div>
                <div class="custom-dropdown">
                  <input
                      type="text"
                      id="program-select"
                      class="dropdown-search"
                      placeholder="프로그램 검색"
                      v-model="searchQuery"
                      @focus="openDropdown"
                      @input="filterProgramsBySearch"
                  />

                  <div v-if="isDropdownOpen" class="dropdown-list">
                    <div class="dropdown-header">
                      <span>프로그램 선택</span>
                      <button @click="closeDropdown" class="n-icon n-icon:arrow_up cursor:pointer"></button>
                    </div>
                    <label v-for="pt in filteredPrograms" :key="pt.id" class="dropdown-item">
                      <input
                          type="checkbox"
                          :value="pt.id"
                          v-model="selectedProgramIds"
                          @change="applyProgramFilter"
                      />
                      {{ pt.title }}
                    </label>
                  </div>

                </div>
              </div>

              <div v-show="activeDropdown === 'status'" class="dropdown-menu status" :style="dropdownPosition">
                <div class="d:flex jc:space-between">
                  <h3>프로그램 상태</h3>
                  <span @click.prevent="toggleDropdown" class="n-icon n-icon:exit cursor:pointer"></span>
                </div>
                <label><input class="statusCheckboxAll" type="checkbox"
                              @change="selectStatusAll"/>전체</label>
                <label>
                  <input
                      type="checkbox"
                      :value="'In Progress'"
                      v-model="selectedStatus"
                      @change="selectStatus"
                  />
                  작성중
                </label>
                <label>
                  <input
                      type="checkbox"
                      :value="'Unpublished'"
                      v-model="selectedStatus"
                      @change="selectStatus"
                  />
                  작성완료
                </label>
                <label>
                  <input
                      type="checkbox"
                      :value="'Published'"
                      v-model="selectedStatus"
                      @change="selectStatus"
                  />
                  모집중
                </label>
              </div>

              <div v-show="activeDropdown === 'category'" class="dropdown-menu category" :style="dropdownPosition">
                <div class="d:flex jc:space-between">
                  <h3>카테고리 필터</h3>
                  <span @click.prevent="toggleDropdown" class="n-icon n-icon:exit cursor:pointer"></span>
                </div>
                <label>
                  <input
                      class="categoryAll"
                      type="checkbox"
                      @change="selectCategoryAll"
                  />
                  All
                </label>
                <label v-for="c in categories" :key="c.id">
                  <input
                      type="checkbox"
                      :value="c.id"
                      v-model="selectedCategories"
                      @change="selectCategory"
                  />
                  {{ c.name }}
                </label>
              </div>

            <div class="overflow-x:auto">
              <ul class="item-wrapper padding-y:5">
                <!-- 프로그램 필터 버튼 -->
                <li class="filter-item">
                  <div
                      :class="['n-btn', 'n-btn-pg-filter', 'n-btn:hover', 'n-icon', 'n-icon:search', 'n-icon-size:1', 'n-deco', 'n-deco-gap:1', { active: isProgramFilterActive }]"
                      @click="toggleDropdown($event, 'program')"
                  >
                    프로그램
                  </div>
                </li>

                <!-- 프로그램 상태 필터 버튼 -->
                <li class="filter-item">
                  <div
                      :class="['n-btn', 'n-btn-pg-filter', 'n-btn:hover', 'n-icon', 'n-icon:pending', 'n-icon-size:1', 'n-deco', 'n-deco-gap:1', { active: isStatusFilterActive }]"
                      @click="toggleDropdown($event,'status')"
                  >
                    프로그램 상태
                  </div>
                </li>

                <!-- 카테고리 필터 버튼 -->
                <li class="filter-item">
                  <div
                      :class="['n-btn', 'n-btn-pg-filter', 'n-btn:hover', 'n-icon', 'n-icon:category', 'n-icon-size:1', 'n-deco', 'n-deco-gap:1', { active: isCategoryFilterActive }]"
                      @click="toggleDropdown($event,'category')"
                  >
                    카테고리
                  </div>
                </li>
              </ul>
            </div>

            <!-- 모달 창 -->
            <div v-if="isModalOpen" class="filter-modal">
              <div class="modal-content">
                <button class="close-btn" @click="closeModal">✖</button>

                <!-- 프로그램 필터 모달 -->
                <div v-if="activeFilter === 'program'">
                  <h3>프로그램 필터</h3>
                  <div>
                    <label v-for="pt in filteredPrograms" :key="pt.id">
                      <input
                          type="checkbox"
                          :value="pt.id"
                          v-model="selectedProgramIds"
                          @change="applyProgramFilter"
                      />
                      {{ pt.title }}
                    </label>
                  </div>
                </div>

                <!-- 프로그램 상태 필터 모달 -->
                <div v-if="activeFilter === 'status'">
                  <h3>프로그램 상태 필터</h3>
                  <div>
                    <label>
                      <input
                          type="checkbox"
                          :value="'In Progress'"
                          v-model="selectedStatus"
                          @change="selectStatus"
                      />
                      작성중
                    </label>
                    <label>
                      <input
                          type="checkbox"
                          :value="'Unpublished'"
                          v-model="selectedStatus"
                          @change="selectStatus"
                      />
                      작성완료
                    </label>
                    <label>
                      <input
                          type="checkbox"
                          :value="'Published'"
                          v-model="selectedStatus"
                          @change="selectStatus"
                      />
                      모집중
                    </label>
                  </div>
                </div>

                <!-- 카테고리 필터 모달 -->
                <div v-if="activeFilter === 'category'">
                  <h3>카테고리 필터</h3>
                  <div>
                    <label>
                      <input
                          class="categoryAll"
                          type="checkbox"
                          @change="selectCategoryAll"
                      />
                      All
                    </label>
                    <label v-for="c in categories" :key="c.id">
                      <input
                          type="checkbox"
                          :value="c.id"
                          v-model="selectedCategories"
                          @change="selectCategory"
                      />
                      {{ c.name }}
                    </label>
                  </div>
                </div>
              </div>
            </div>
            <div class="reset-box">
              <div class="gradation"></div>
              <div class="btn-box">
              <span @click.prevent="resetFilters" style="cursor: pointer"
                    class="reset-btn icon-box n-deco1 n-icon n-icon:reset">
                reset
              </span>
              </div>
            </div>
          </section>

          <!--=== 프로그램 카드 목록 + 정렬 ==========================================-->
          <section class="n-layout-mj-cards">
            <!--정렬-->
            <header class="list-header bg-color:base-1">
              <h1 class="d:none">프로그램 목록</h1>
              <div>
                <span>{{ totalRowCount }} Result</span>
                <a href="#" class="n-icon n-icon:arrow_swap n-deco n-deco-gap:1">
                  정렬
                </a>
              </div>
            </header>

            <!--프로그램 카드 목록 (작성 중) (작성 완료) (모집 중)-->
            <ul class="n-card-container bg-color:base-1 padding:7">
              <li v-for="(p,index) in programs" :key="p.id" class="n-card bg-color:base-1 padding:6">
                <h2 class="d:none">프로그램 카드</h2>

                <div class="card-header">
                  <div class="left">
                    <span v-if="p.status === 'In Progress'"
                          class="n-panel-tag not-submitted">작성중</span>
                    <span v-else-if="p.status === 'Published'"
                          class="n-panel-tag not-submitted">모집중</span>
                    <span v-else-if="p.status === 'Unpublished'"
                          class="n-panel-tag not-submitted">작성완료</span>
                  </div>
                  <div class="right">
                    <div class="n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9" style="cursor: pointer"
                         @click.prevent="toggleMenu(index)"
                    >더보기
                    </div>
                  </div>
                  <div v-if="activeMenuIndex[index]" class="menu-dropdown">
                    <ul>
                      <NuxtLink v-if="!(p.status === 'Published')" class="edit" :href="`/host/programs/${p.id}/edit`">수정</NuxtLink>
                      <li @click.prevent="deleteProgram(p, index)">삭제</li>
                    </ul>
                  </div>
                </div>
                <div class="card-main">
                  <NuxtLink v-if="p.images && p.images.length > 0" :href="`/host/programs/${p.id}`" class="img-wrapper">
                    <img :src="getImageSrc(p)" alt="대표사진"/>
                  </NuxtLink>
                  <NuxtLink v-else :href="`/host/programs/${p.id}`" class="img-wrapper">
                    <img src="/assets/image/default-program-image.png" alt="대표사진"/>
                  </NuxtLink>
                  <div class="card-info-wrapper">
                    <NuxtLink :href="`/host/programs/${p.id}`" style="cursor: pointer" class="title">
                      {{ p.title }}
                    </NuxtLink>
                    <div class="card-info-responsive">
                      <div>
                        <div class="card-info">
                          <span class="n-icon n-icon:star n-deco"></span>
                          <span>0.0 (0)</span>
                        </div>
                        <div class="card-info gap:1">
                          <span class="category" v-for="(c, index) in p.categoryNames" :key="index">{{ c }}<span
                              v-if="index < p.categoryNames.length - 1">·</span></span>
                        </div>
                      </div>

                      <div class="card-footer-responsive">
                        <NuxtLink
                            v-if="p.status === 'In Progress'"
                            :href="`/host/programs/${p.id}/edit`"
                            class="n-btn create"
                        >작성하기
                        </NuxtLink>
                        <button
                            v-else-if="p.status === 'Published'"
                            class="n-btn open"
                            @click.prevent="OpenPublishProgramModalHandler(p.id)"
                        >추가 개설
                        </button>
                        <button
                            v-else-if="p.status === 'Unpublished'"
                            class="n-btn open"
                            @click.prevent="OpenPublishProgramModalHandler(p.id)"
                        >개설하기
                        </button>
                      </div>

                    </div>
                  </div>
                </div>

                <div class="card-footer">
                  <NuxtLink
                      v-if="p.status === 'In Progress'"
                      :href="`/host/programs/${p.id}/edit`"
                      class="n-btn create"
                  >작성하기
                  </NuxtLink>
                  <button
                      v-if="p.status === 'Published'"
                      class="n-btn manage"
                      @click.prevent="OpenPublishProgramModalHandler(p.id)"
                  >추가 개설
                  </button>
                  <button
                      v-else-if="p.status === 'Unpublished'"
                      class="n-btn open"
                      @click.prevent="OpenPublishProgramModalHandler(p.id)"
                  >개설하기
                  </button>
                </div>
              </li>
            </ul>

            <div class="pagination">
              <!-- 이전 버튼 -->
              <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 1">
                〈
              </button>

              <!-- 페이지 번호 버튼 -->
              <button
                  v-for="page in visiblePages"
                  :key="page"
                  @click="goToPage(page)"
                  :class="{ active: page === currentPage }">
                {{ page }}
              </button>

              <!-- 다음 버튼 -->
              <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPageCount || totalPageCount <= 1">
                〉
              </button>
            </div>

          </section>
        </section>


        <!--=== 필터 반응형 ==========================================-->
        <aside class="n-filter-aside">
          <header class="n-title">
            <h1 class="">Filter</h1>
            <div>
              <button class="n-icon n-icon:reset" @click.prevent="resetFilters"
                      style="--icon-color: var(--color-sub-1); cursor: pointer;">초기화
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
                  <label>
                    <input
                        class="categoryAll"
                        type="checkbox"
                        @change="selectCategoryAll"
                    />
                    All
                  </label>

                  <label v-for="c in categories" :key="c.id">
                    <input
                        class="categoryIds"
                        type="checkbox"
                        @change="selectCategory"
                        :value="c.id"
                        v-model="selectedCategories"
                    />
                    {{ c.name }}
                  </label>
                </div>
              </form>
            </details>
            <!-- 프로그램 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">프로그램</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>
              <div class="custom-dropdown">
                <input
                    type="text"
                    id="program-select"
                    class="dropdown-search"
                    placeholder="프로그램 검색"
                    v-model="searchQuery"
                    @focus="openDropdown"
                    @input="filterProgramsBySearch"
                />

                <div v-if="isDropdownOpen" class="dropdown-list">
                  <div class="dropdown-header">
                    <span>프로그램 선택</span>
                    <button @click="closeDropdown" class="n-icon n-icon:arrow_up cursor:pointer"></button>
                  </div>
                  <label v-for="pt in filteredPrograms" :key="pt.id" class="dropdown-item">
                    <input
                        type="checkbox"
                        :value="pt.id"
                        v-model="selectedProgramIds"
                        @change="applyProgramFilter"
                    />
                    {{ pt.title }}
                  </label>
                </div>

              </div>
            </details>


            <!-- 프로그램 상태 필터 -->
            <details open class="filter">
              <summary class="collapse">
                <span class="title">프로그램 상태</span>
                <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
              </summary>

              <form action="" class="form">
                <div class="modal-checkbox">
                  <label><input class="statusCheckboxAll" type="checkbox"
                                @change="selectStatusAll"/>전체</label>
                  <label><input class="statusCheckbox" type="checkbox" @change="selectStatus"
                                :value="'In Progress'" v-model="selectedStatus"/>작성중</label>
                  <label><input class="statusCheckbox" type="checkbox" @change="selectStatus"
                                :value="'Unpublished'" v-model="selectedStatus"/>작성완료</label>
                  <label><input class="statusCheckbox" type="checkbox" @change="selectStatus"
                                :value="'Published'" v-model="selectedStatus"/>모집중</label>
                </div>
              </form>
            </details>
          </div>
        </aside>

      </section>
    </section>
    <div :class="{'active': modalVisible}" class="backdrop"></div>

    <!--  확인 모달창 ================================================================================================ -->
    <!--  개설확인  -->
    <Modal :isVisible="isModalVisible('confirmPpModal')" @close="closePublishModal('confirmPpModal')"
           @confirm="() => {confirmPpPost=!confirmPpPost;
             console.log('index. Modal emit confirm and callback func called. confirmPpPost: ', confirmPpPost);
             closePublishModal('confirmPpModal');}">
      <p>개설하시겠습니까?</p>
    </Modal>
    <PublishProgramModal :key="PublishProgramModalKey"
                         :class="{'show': modalVisible === 'PublishProgramModal'}"
                         :default-program-id="pIdToPublish"
                         :confirm-pp-post="confirmPpPost"
                         @close-modal="() => { fetchPrograms(); modalVisible = ''; }"
                         @submit="openPublishModal('confirmPpModal')"/>

  </main>
</template>

<script setup>
import {onMounted, ref, computed} from "vue";
import axios from "axios";
import {useRouter} from 'vue-router';
import PublishProgramModal from "~/components/modal/PublishProgramModal.vue";

//============= 변수 영역 ====================
const programs = ref([]); //  서버에서 가져온 프로그램 목록 저장 배열
const totalRowCount = ref(0); // 서버에서 가져온 총 프로그램 개수 저장
const totalPageCount = ref(0); //  서버에서 가져온 총 페이지 개수 저장
const selectedProgramIds = ref([]); // 프로그램 필터에서 선택된 프로그램의 id를 저장할 배열
const categories = ref([]);
const selectedCategories = ref([]);
const selectedStatus = ref([]);
const currentPage = ref(1); //  현재 페이지 번호
const cardsPerPage = 6; //  한페이지당 표시할 프로그램 카드 수
const router = useRouter();
const activeMenuIndex = ref({});
const programTitles = ref({});
const searchQuery = ref(""); // 검색어 상태
const filteredPrograms = ref([]); // 검색 결과 상태
const isDropdownOpen = ref(false);
const isModalOpen = ref(false); // 모달 열림 상태
const activeFilter = ref("");

const activeDropdown = ref(""); // 현재 활성화된 드롭다운 ('program', 'status', 'category')
const dropdownPosition = ref({}); // 드롭다운의 위치


// 드롭다운 열기/닫기
const toggleDropdown = (event, filter) => {
  if (activeDropdown.value === filter)
    activeDropdown.value = "";
  else {
    activeDropdown.value = filter;
    calculateDropdownPosition(event.target, filter);
  }
};

const calculateDropdownPosition = (buttonElement, filter) => {
  const buttonRect = buttonElement.getBoundingClientRect(); // 버튼의 위치
  const container = document.querySelector('.n-filter'); // 기준이 되는 요소
  const containerRect = container.getBoundingClientRect(); // 기준 요소의 위치

  console.log('buttonRect: ', buttonRect)
  console.log('containerRect: ', containerRect)

  let dropdownWidth = 200;
  // 드롭다운의 width 값을 정확하게 가져옴
  if (filter === 'program')
    dropdownWidth = 200;
  else if (filter === 'status')
    dropdownWidth = 200;
  else if (filter === 'category')
    dropdownWidth = 200;

  let leftOffset = buttonRect.left - containerRect.left;
  console.log('dropdownWidth: ', dropdownWidth)
  console.log('leftOffset: ', leftOffset)


  // 드롭다운의 왼쪽 경계가 화면을 벗어나는 경우 처리
  if (leftOffset < 0) {
    leftOffset = 16; // 왼쪽 여백을 16px로 설정
  }

  // 드롭다운의 오른쪽 경계가 화면을 벗어나는 경우 처리
  if (leftOffset + dropdownWidth > window.innerWidth) {
    leftOffset = window.innerWidth - (34 + dropdownWidth); // 화면 오른쪽 끝에 맞추기
  }

  dropdownPosition.value = {
    position: 'absolute',
    top: `${buttonRect.bottom - containerRect.top + 8}px`, // 버튼 아래로 드롭다운 배치
    left: `${leftOffset}px`, // 버튼의 x 좌표에 맞춤
    zIndex: 1000,
  };
};

//============= Lifecycle Functions ================
onMounted(() => {
  //  컴포넌트가 화면에 마운트(렌더링)된 후에 실행
  fetchPrograms(); //  프로그램 목록을 가져오는 함수
  fetchCategories();
  fetchProgramsTitle();

  const allCheckbox = document.querySelector(".categoryAll");
  if (allCheckbox) {
    allCheckbox.checked = true; // All 기본 선택
  }


  filteredPrograms.value = programTitles.value;
});

//============= Data Functions =======================
const fetchCategories = async () => {
  const response = await axios.get("http://localhost:8083/api/v1/categories");
  categories.value = response.data;
};

const fetchProgramsTitle = async () => {
  const userId = localStorage.getItem("id");
  const token = localStorage.getItem("token");
  const response = await axios.get(`http://localhost:8083/api/v1/host/programs/user/${userId}`,
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
  programTitles.value = response.data;
}

//  서버에서 프로그램 데이터를 가져오는 비동기 함수
const fetchPrograms = async (
    cIds,
    pIds,
    statuses,
    pageNum = currentPage.value
) => {
  const id = localStorage.getItem("id");
  const params = {};
  //  params 객체 생성
  //  조건(cIds, pIds, statuses, pageNum)이 존재하는 경우에만 해당 값을 params에 추가
  params.mid = id;

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
  if (cardsPerPage) {
    params.cardsPerPage = cardsPerPage;
  }

  const token = localStorage.getItem("token");

  const response = await axios.get(
      //  axios.get : 비동기적으로 서버의 API로 GET 요청 보냄
      "http://localhost:8083/api/v1/host/programs",
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        params: params,
        //  이 URL에 params 객체를 함께 전송하여 필터링된 결과를
        //  response 변수에 받기
      }
  );

  //  서버로 부터 받은 응답 response
  programs.value = response.data.programs; // response에서 프로그램 목록을 추출해서 저장
  console.log(programs.value);
  totalRowCount.value = response.data.totalRowCount; //  response에서 총 프로그램수 추출해서 저장
  totalPageCount.value = Math.ceil(totalRowCount.value / cardsPerPage); //response.data.totalPageCount; //  response에서 총 페이지 갯수 추출해서 저장
};

const deleteProgram = async (program, index) => {
  const token = localStorage.getItem("token");
  console.log('deleteProgram called. program.status: ', program.status);
  if(program.status === 'Published') {
    alert("모집중인 프로그램은 삭제할 수 없습니다.")
    return;
  }
  try {
    await axios.put(`http://localhost:8083/api/v1/host/programs/${program.id}/softDelete`, {}, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    });
    alert("프로그램이 성공적으로 삭제되었습니다.");

    // 프로그램 목록에서 삭제된 항목 제거
    programs.value = programs.value.filter(p => p.id !== program.id);
    activeMenuIndex.value = {[index]: false};


  } catch (error) {
      console.log(error)
      alert("공개된 프로그램이라 삭제할 수 없습니다.");

  }
};


const toggleMenu = (index) => {
  // 다른 카드의 메뉴는 닫고, 해당 카드만 토글
  activeMenuIndex.value = {[index]: !activeMenuIndex.value[index]};
};


const getImageSrc = (program) => {
  const BASE_URL = 'http://localhost:8083/api/v1/'
  const image = program.images.find(img => img.order === 1)
  // 이미지가 있을 경우, 경로 앞에 BASE_URL을 추가
  return image ? `${BASE_URL}${image.src.startsWith('uploads') ? image.src : image.src}` : `${BASE_URL}uploads/default.jpg`
}


const selectCategory = async () => {
  const allCheckbox = document.querySelector(".categoryAll");
  if (allCheckbox) {
    allCheckbox.checked = false; // All 체크박스를 해제
  }

  try {
    await fetchPrograms(
        selectedCategories.value.join(","), // 선택된 카테고리
        selectedProgramIds.value.join(","),
        selectedStatus.value.join(",")
    );
  } catch (error) {
    console.error("Error filtering programs by category:", error);
  }
};


const selectCategoryAll = async () => {
  selectedCategories.value = []; // 다른 선택된 카테고리 해제

  const checkboxes = document.querySelectorAll(".categoryIds");
  checkboxes.forEach((checkbox) => {
    checkbox.checked = false; // 모든 체크박스를 해제
  });

  try {
    await fetchPrograms(
        null, // 선택된 카테고리 없음
        selectedProgramIds.value.join(","),
        selectedStatus.value.join(",")
    );
  } catch (error) {
    console.error("Error resetting to all categories:", error);
  }
};

const filterProgramsBySearch = () => {
  // 검색어를 포함하는 프로그램만 필터링
  const searchFiltered = programTitles.value.filter((program) =>
      program.title.toLowerCase().includes(searchQuery.value.toLowerCase())
  );

  // 선택된 항목을 맨 위로 정렬
  filteredPrograms.value = searchFiltered.sort((a, b) => {
    const isSelectedA = selectedProgramIds.value.includes(a.id);
    const isSelectedB = selectedProgramIds.value.includes(b.id);

    // 선택된 항목은 맨 위로, 나머지는 그대로
    if (isSelectedA && !isSelectedB) return -1;
    if (!isSelectedA && isSelectedB) return 1;
    return 0;
  });
};

const applyProgramFilter = async () => {
  try {
    // 선택된 항목을 포함하여 필터링된 프로그램 목록 재정렬
    filterProgramsBySearch();

    // 서버 호출
    await fetchPrograms(
        selectedCategories.value.join(","),
        selectedProgramIds.value.join(","),
        selectedStatus.value.join(",")
    );
  } catch (error) {
    console.error("Error applying program filter:", error);
  }
};

const openDropdown = () => {
  isDropdownOpen.value = true;
};

// 드롭다운 닫기
const closeDropdown = () => {
  isDropdownOpen.value = false;
};

const openModal = (filter) => {
  activeFilter.value = filter; // 현재 활성화된 필터 설정
  isModalOpen.value = true; // 모달 열기
};

// 모달 닫기
const closeModal = () => {
  isModalOpen.value = false; // 모달 닫기
  activeFilter.value = ""; // 활성화된 필터 초기화
};


const selectStatusAll = async () => {
  selectedStatus.value = [];
  const checkboxes = document.querySelectorAll(".statusCheckbox");
  checkboxes.forEach((checkbox) => {
    // 상태 조건 필터링 모두를 unCheck 한다.
    checkbox.checked = false;
  });

  try {
    await fetchPrograms(
        selectedCategories.value.join(","),
        selectedProgramIds.value.join(","),
        null,
        null
    );
    //  fetchPrograms를 호출하여 모든 프로그램을 서버에서 다시 가져오기
    //  파라미터를 모두 null로 전달하여 필터 없이 전체 목록 가져오기
  } catch (error) {
    console.error("Error fetching all programs:", error);
  }
};

const selectStatus = async () => {
  const checkboxes = document.querySelector(".statusCheckboxAll");
  if (checkboxes) {
    checkboxes.checked = false;
  }
  console.log(`Selected status: ${selectedStatus.value}`);
  try {
    await fetchPrograms(
        selectedCategories.value.join(","),
        selectedProgramIds.value.join(","),
        selectedStatus.value.join(","),
        null
    );
    //  fetchPrograms를 호출하여 모든 프로그램을 서버에서 다시 가져오기
    //  파라미터를 모두 null로 전달하여 필터 없이 전체 목록 가져오기
  } catch (error) {
    console.error("Error fetching all programs:", error);
  }
};

const resetFilters = async () => {
  // 모든 필터 초기화
  searchQuery.value = ""; // 검색어 초기화
  selectedProgramIds.value = []; // 선택된 프로그램 ID 초기화
  selectedCategories.value = []; // 선택된 카테고리 초기화
  selectedStatus.value = []; // 선택된 상태 초기화

  // 모든 체크박스 해제
  const categoryCheckboxes = document.querySelectorAll(".categoryIds, .categoryAll");
  categoryCheckboxes.forEach((checkbox) => (checkbox.checked = false));

  const statusCheckboxes = document.querySelectorAll(".statusCheckbox, .statusCheckboxAll");
  statusCheckboxes.forEach((checkbox) => (checkbox.checked = false));

  // 프로그램 목록 초기화
  filteredPrograms.value = programTitles.value;

  try {
    // 전체 필터를 비운 상태로 서버에서 프로그램 다시 로드
    await fetchPrograms(null, null, null);
  } catch (error) {
    console.error("Error resetting filters:", error);
  }
};

// 프로그램 필터 활성화 여부
const isProgramFilterActive = computed(() => selectedProgramIds.value.length > 0);

// 프로그램 상태 필터 활성화 여부
const isStatusFilterActive = computed(() => selectedStatus.value.length > 0);

// 카테고리 필터 활성화 여부
const isCategoryFilterActive = computed(() => selectedCategories.value.length > 0);


watchEffect(() => {
  if (programs.value && programs.value.images && programs.value.images.length > 0) {
    const mainImage = programs.value.images.find(image => image.order === 1);
    mainImageSrc.value = mainImage ? `http://localhost:8083/api/v1/${mainImage.src}` : '';
  }
});


const goToPage = async (page) => {
  // 페이지 번호가 유효한 경우에만 이동
  if (page >= 1 && page <= totalPageCount.value) {
    currentPage.value = page; // 현재 페이지 업데이트
    try {
      await fetchPrograms(
          selectedCategories.value.join(","),
          selectedProgramIds.value.join(","),
          selectedStatus.value.join(","),
          page // 요청할 페이지 번호
      );
    } catch (error) {
      console.error("Error while fetching programs for page:", error);
    }
  }
};


const visiblePages = computed(() => {
  const pages = [];
  const total = totalPageCount.value; // 총 페이지 수
  const current = currentPage.value; // 현재 페이지 번호
  let startPage;
  let endPage;

  // 시작 페이지와 끝 페이지 계산
  if (total <= 5) {
    // 페이지가 5개 이하인 경우 모두 표시
    startPage = 1;
    endPage = total;
  } else if (current <= 3) {
    // 현재 페이지가 1~3인 경우 첫 5개 페이지 표시
    startPage = 1;
    endPage = 5;
  } else if (current > total - 3) {
    // 현재 페이지가 마지막 3개 중 하나인 경우 마지막 5개 페이지 표시
    startPage = total - 4;
    endPage = total;
  } else {
    // 그 외의 경우 현재 페이지를 기준으로 앞뒤 2개씩 표시
    startPage = current - 2;
    endPage = current + 2;
  }

  // 페이지 번호 배열 생성
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }
  return pages;
});


//==============개설 모달창 영역==========================
const modalVisible = ref("");
const pIdToPublish = ref(null); // 일정추가, 추가개설할 pubishedprogramId
const confirmPpPost = ref(false);
const PublishProgramModalKey = ref(false); // 예약취소 시 리렌더링 유발용
const { isModalVisible, openModal:openPublishModal, closeModal:closePublishModal } = useModal();

const OpenPublishProgramModalHandler = (programId) => {
  pIdToPublish.value = programId;
  modalVisible.value = "PublishProgramModal";
}


</script>

<style scoped>
.filter-item {
  position: relative;
  .n-btn {
    --btn-font-size: 12px;
  }
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 10;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 10px;
  width: 200px;
}

.dropdown-menu h3 {
  font-size: 12px;
  margin-bottom: 10px;
}

.dropdown-menu label {
  display: block;
  margin-bottom: 5px;
}

.dropdown-menu input {
  margin-right: 5px;
}

.n-btn:hover + .dropdown-menu {
  display: block;
}


.filter-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 90%;
  max-width: 400px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
}


.menu-dropdown {
  position: absolute;
  right: 10px;
  top: 40px;
  background-color: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 10px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.menu-dropdown ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.custom-dropdown {
  position: relative;
  width: 100%;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.dropdown-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  border-bottom: 1px solid #ccc;
  background-color: #f9f9f9;
}

.dropdown-search {
  width: 100%;
  padding: 8px;
  border: none;
  outline: none;
  border-bottom: 1px solid #ccc;
}

.dropdown-list {
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid #ccc;
  border-radius: 0 0 4px 4px;
  background-color: #fff;
}

.dropdown-item {
  display: flex;
  align-items: center;
  padding: 8px 10px;
  font-size: 14px;
}

.dropdown-item:hover {
  background-color: #f0f0f0;
  cursor: pointer;
}

.dropdown-item input {
  margin-right: 10px;
}


.menu-dropdown li {
  padding: 8px 12px;
  cursor: pointer;
}

.menu-dropdown .edit {
  padding: 8px 12px;
  cursor: pointer;
}

.menu-dropdown li:hover {
  background-color: #f5f5f5;
}


.n-card {
  /* 해당영역 클릭 시 링크로 이동 */
  position: relative;
  box-shadow: 5px 5px 10px 0.5px var(--color-base-3);

  .n-link-box {
    display: block;
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
  }

  /* card header 태그 */

  .card-header {
    .n-panel-tag {
      --tag-border-radius: 10px;
      --tag-padding: 6px 8px;
      font-weight: 400;
      /* regular */
    }

    .n-panel-tag.not-submitted {
      --tag-border-color: var(--color-base-8);
      --tag-bg-color: var(--color-base-8);
      color: var(--color-base-1);
    }

    .n-panel-tag.submitted {
      --tag-border-color: var(--color-sub-1);
      --tag-bg-color: var(--color-base-1);
      color: var(--color-sub-1);
    }

    .n-panel-tag.open {
      --tag-border-color: var(--color-sub-1);
      --tag-bg-color: var(--color-sub-1);
      color: var(--color-base-1);
    }
  }

  /* card-main card-info 카테고리 태그*/

  .card-main {
    .card-info-wrapper {
      .card-info {
        .n-icon\:star {
          --icon-color: var(--color-base-4);
        }

        .n-icon\:star.rated {
          --icon-color: var(--color-yellow-1);
        }

        .n-panel-tag.category {
          --tag-border-radius: 14px;
          --tag-padding: 3px 4px;
          font-weight: 400;
          /* regular */
        }

        .category {
          color: var(--color-base-7);
          font-size: var(--font-size-6);
          /* 14 */
        }

        gap: var(--gap-2);
      }
    }
  }

  /* card footer 버튼 */

  .card-footer {
    justify-content: flex-end;

    .n-btn {
      z-index: 2;
      /* n-link-box보다 높게 설정 */

      flex-grow: 0;
      flex-shrink: 1;

      /* --btn-font-size: 14px; */
      --btn-border-color: var(--color-main-1);
      --btn-border-radius: 12px;
      /* --btn-bg-hover: var(--color-base-2); */
      --btn-bg-color: transparent;
      --btn-padding: 8px 12px;
      align-items: center;
      background-color: var(--btn-bg-color);
      border: 1.5px solid var(--btn-border-color, var(--color-base-3));
      border-radius: var(--btn-border-radius, 8px);
      box-sizing: content-box;
      color: inherit;
      cursor: pointer;
      display: inline-flex;
      font-family: inherit;
      font-size: var(--btn-font-size, 14px);
      font-weight: var(--font-weight-2);
      justify-content: center;
      line-height: 1em;
      padding: var(--btn-padding);
      position: relative;
      transition: border-color 0.3s, background-color 0.3s;
      user-select: none;
      white-space: nowrap;
      width: auto;
    }

    .n-btn.create {
      --btn-border-color: var(--color-sub-1);
      color: var(--color-sub-1);
    }

    .n-btn.open {
      --btn-border-color: var(--color-sub-1);
      --btn-bg-color: var(--color-sub-1);
      color: var(--color-base-1);
    }

    .n-btn.manage {
      --btn-border-color: var(--color-sub-3);
      --btn-bg-color: var(--color-sub-3);
      color: var(--color-sub-1);
    }
  }
}

/* ============== 페이지 네이션 ================ */

.pagination {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin: 20px 0;
}

.pagination button {
  padding: 4px 10px;
  border: none;
  border-radius: 4px;
  background-color: var(--color-base-3);
  cursor: pointer;
}

.pagination button.active {
  color: var(--color-base-1);
  background-color: var(--color-sub-1);
}

.pagination button:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

/*===================== 반응형 구현 ====================*/

/*필터 어사이드*/
.filters {
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

    /*카테고리, 프로그램, 프로그램 상태 필터*/

    .modal-checkbox {
      width: 100%;
      display: flex;
      flex-direction: column;
      flex-wrap: nowrap;
      background-color: transparent;
      gap: 6px;
      padding: 0 8px;
      overflow-y: auto;
      max-height: 250px;

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

/*컨텐츠 + 필터어사이드 묶은 카테고리*/
.n-layout-mj {
  display: flex;
  flex-direction: column;
  min-width: 300px;
  max-width: 1092px;

  .layout-list {
    flex-grow: 1;
    width: 100%;
  }

  .n-filter-aside {
    display: none;
    width: 250px;
    height: 100%;
    margin-right: 16px;

    .n-title {
      --title-font-size: var(--font-size-9);
      /* 18 */
      --title-font-weight: 600;
      /* semi bold */
      --title-padding: 16px 4px;

      .n-icon {
        --icon-size: var(--icon-size-4);
      }
    }
  }
}

/*너비 768px 이하*/
@media (max-width: 768px) {
  .n-card-container {
    max-width: 100%;
    flex-shrink: 1;
    flex-grow: 1;

    .card-footer-responsive {
      display: none;
    }
  }
}

/*너비 768px 이상*/
@media (min-width: 768px) {
  .n-layout-mj {
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 8px;
    padding-bottom: 16px;

    .n-filter-aside {
      display: flex;
      flex-direction: column;
      flex-shrink: 0;

      .filters {
        padding: 0 16px;
        border: 1px solid var(--color-base-3);
        border-radius: 12px;
      }
    }

    .n-card-container {
      max-width: 100%;
      flex-shrink: 1;
      flex-grow: 1;

      .card-footer {
        display: none;
      }
    }
  }

  .card-info-responsive {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .card-footer-responsive {
    display: flex;
    justify-content: end;

    .n-btn {
      z-index: 2;
      /* n-link-box보다 높게 설정 */

      flex-grow: 0;
      flex-shrink: 1;

      /* --btn-font-size: 14px; */
      --btn-border-color: var(--color-main-1);
      --btn-border-radius: 12px;
      /* --btn-bg-hover: var(--color-base-2); */
      --btn-bg-color: transparent;
      --btn-padding: 8px 12px;
      align-items: center;
      background-color: var(--btn-bg-color);
      border: 1.5px solid var(--btn-border-color, var(--color-base-3));
      border-radius: var(--btn-border-radius, 8px);
      box-sizing: content-box;
      color: inherit;
      cursor: pointer;
      display: inline-flex;
      font-family: inherit;
      font-size: var(--btn-font-size, 14px);
      font-weight: var(--font-weight-2);
      justify-content: center;
      line-height: 1em;
      padding: var(--btn-padding);
      position: relative;
      transition: border-color 0.3s, background-color 0.3s;
      user-select: none;
      white-space: nowrap;
      width: auto;
    }

    .n-btn.create {
      --btn-border-color: var(--color-sub-1);
      color: var(--color-sub-1);
    }

    .n-btn.open {
      --btn-border-color: var(--color-sub-1);
      --btn-bg-color: var(--color-sub-1);
      color: var(--color-base-1);
    }

    .n-btn.manage {
      --btn-border-color: var(--color-sub-3);
      --btn-bg-color: var(--color-sub-3);
      color: var(--color-sub-1);
    }
  }

  .n-filter {
    display: none;
  }
}


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

@media (min-width: 1092px) {
  .n-layout-mj {
    display: flex;
    flex-direction: column;
    justify-content: center;
    max-width: 1092px;
    margin: 0 auto;
    gap: 16px;
    padding-bottom: 16px;
  }

  .n-filter-aside {
    display: flex;
    flex-direction: column;
    flex-shrink: 0;
    width: 250px;
    /* 필터 어사이드의 고정 너비 */
    margin-right: 16px;
    /* 카드와 필터 사이의 간격 */
  }

  .n-card-container {
    max-width: 100%;
    flex-shrink: 1;
    flex-grow: 1;
  }
}

.more {
  display: flex;
  flex-direction: column;
  background: var(--color-base-1);
  border-radius: 10px;
  padding: 8px;

  .more-close {
    display: flex;
    justify-content: right;
  }

  .more-content {
    .n-btn {
      --btn-border-color: var(--color-base-7);
      color: var(--color-base-7);
    }

    display: flex;
    flex-direction: column;
    padding: 20px 30px;
    gap: 16px;
    width: 300px;
  }
}

.more-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 7;
}
</style>
