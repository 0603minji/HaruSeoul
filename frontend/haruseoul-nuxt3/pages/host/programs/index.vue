<!--1. 페이지 네이션 구현-->
<!--2. programs/new에서 값 입력받으면 새로운 프로그램 조회 가능 구현-->
<!--3. 프로그램 상태에 따라 버튼 변경 : 작성하기 / 개설하기 / 예약관리 -->
<!--4. 로그인 되어있는 해당 호스트에 대한 프로그램만 조회 -->

<script setup>
import { onMounted, ref, computed } from "vue";
import axios from "axios";

//============= 변수 영역 ====================
const programs = ref([]); //  서버에서 가져온 프로그램 목록 저장 배열
const totalRowCount = ref(0); // 서버에서 가져온 총 프로그램 개수 저장
const totalPageCount = ref(0); //  서버에서 가져온 총 페이지 개수 저장
const programTitles = ref([]); //  프로그램 id, title들을 저장할 배열
const selectedProgramIds = ref([]); // 프로그램 필터에서 선택된 프로그램의 id를 저장할 배열
const categories = ref([]);
const selectedCategories = ref([]);
const selectedStatus = ref([]);
const currentPage = ref(1); //  현재 페이지 번호
const cardsPerPage = 6; //  한페이지당 표시할 프로그램 카드 수
// 모달 관련 상태
const moreIsOpen = ref(false);

//============= Lifecycle Functions ================
onMounted(() => {
    //  컴포넌트가 화면에 마운트(렌더링)된 후에 실행
    fetchPrograms(); //  프로그램 목록을 가져오는 함수
    fetchProgramIds(); //  프로그램의 제목과 ID 목록을 가져오는 함수
    fetchCategories();

    const CategoryCheckbox = document.querySelector(".categoryAll");
    CategoryCheckbox.checked = true;
    const programCheckbox = document.querySelector(".programidAll");
    programCheckbox.checked = true;
    const statusCheckbox = document.querySelector(".statusCheckboxAll");
    statusCheckbox.checked = true;
});

//============= Data Functions =======================
const fetchCategories = async () => {
    const response = await axios.get("http://localhost:8080/api/v1/categories");
    categories.value = response.data;
};

//  서버에서 프로그램 id와 title을 가져와서 programTitles에 저장
const fetchProgramIds = async () => {
    const response = await axios.get(
        "http://localhost:8080/api/v1/titles"
        //  이 url로 서버에 get 요청 보냄
        //  응답 결과를 response 변수에 담기
    );
    programTitles.value = response.data.map((p) => ({
        id: p.id,
        title: p.title,
    }));
    //response.data에서 id와 title만 추출하여 programTitles 배열 객체에 저장
};

//  서버에서 프로그램 데이터를 가져오는 비동기 함수
const fetchPrograms = async (
    cIds,
    pIds,
    statuses,
    pageNum = currentPage.value
) => {
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
    if (cardsPerPage) {
        params.cardsPerPage = cardsPerPage;
    }

    const response = await axios.get(
        //  axios.get : 비동기적으로 서버의 API로 GET 요청 보냄
        "http://localhost:8080/api/v1/host/programs",
        {
            params: params,
            //  이 URL에 params 객체를 함께 전송하여 필터링된 결과를
            //  response 변수에 받기
        }
    );

    //  서버로 부터 받은 응답 response
    programs.value = response.data.programs; // response에서 프로그램 목록을 추출해서 저장
    totalRowCount.value = response.data.totalRowCount; //  response에서 총 프로그램수 추출해서 저장
    totalPageCount.value = Math.ceil(totalRowCount.value / cardsPerPage); //response.data.totalPageCount; //  response에서 총 페이지 갯수 추출해서 저장
};

const selectCategoryAll = async () => {
    selectedCategories.value = [];
    const checkboxes = document.querySelectorAll(".categoryIds");
    checkboxes.forEach((checkbox) => {
        checkbox.checked = false;
    });

    try {
        await fetchPrograms(
            null,
            selectedProgramIds.value.join(","),
            selectedStatus.value.join(","),
            page
        );
    } catch (error) {
        console.error("Error fetching all categories:", error);
    }
};

const selectCategory = async () => {
    const allCheckbox = document.querySelector(".categoryAll");
    if (allCheckbox) {
        allCheckbox.checked = false;
    }

    if (selectedCategories.value.length > 0) {
        try {
            await fetchPrograms(
                selectedCategories.value.join(","),
                selectedProgramIds.value.join(","),
                selectedStatus.value.join(","),
                null
            );
        } catch (error) {
            console.error("Error fetching selected categories:", error);
        }
    } else {
        console.log("No categories selected.");
    }
};

//  선택된 모든 체크박스를 초기화 (All 선택시)
const selectProgramAll = async () => {
    selectedProgramIds.value = [];
    // 프로그램 필터에서 선택된 프로그램 id들을 담은 selectedPrograms 배열 객체를 비움
    const checkboxes = document.querySelectorAll(".programids");
    checkboxes.forEach((checkbox) => {
        checkbox.checked = false;
    });
    //  .programids 클래스를 가진 모든 체크박스를 찾아서 선택 해제
    try {
        await fetchPrograms(
            selectedCategories.value.join(","),
            null,
            selectedStatus.value.join(","),
            null
        );
        //  fetchPrograms를 호출하여 모든 프로그램을 서버에서 다시 가져오기
        //  파라미터를 모두 null로 전달하여 필터 없이 전체 목록 가져오기
    } catch (error) {
        console.error("Error fetching all programs:", error);
    }
};

//  선택된 프로그램들만 서버에서 가져와 목록을 업데이트하는 함수
const selectProgram = async () => {
    const allCheckbox = document.querySelector(".programidAll");
    if (allCheckbox) {
        allCheckbox.checked = false;
    }
    //  .programidAll 클래스를 가진 모든 체크박스를 찾아 선택을 해제

    //  선택된 체크박스의 id가 존재하는 경우(프로그램 필터에 선택된 체크박스가 있는 경우)
    if (selectedProgramIds.value.length > 0) {
        try {
            // 선택된 id들을 ','로 연결해서 쿼리 파라미터로 전송 (pg=1,2,3)
            await fetchPrograms(
                selectedCategories.value.join(","),
                selectedProgramIds.value.join(","),
                selectedStatus.value.join(","),
                null
            );
        } catch (error) {
            console.error("Error fetching selected programs:", error);
        }
    } else {
        console.log("No programs selected.");
    }
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

const goToPage = async (page) => {
    if (page >= 1 && page <= totalPageCount.value) {
        currentPage.value = page;
        await fetchPrograms(
            selectedCategories.value.join(","),
            selectedProgramIds.value.join(","),
            selectedStatus.value.join(","),
            page
        );
    }
};


const visiblePages = computed(() => {
    const pages = [];
    let startPage;
    let endPage;

    if (currentPage.value <= 3) {
        // 1, 2, 또는 3 페이지일 경우 항상 1부터 5까지 표시
        startPage = 1;
        endPage = Math.min(totalPageCount.value, 5);
    } else if (currentPage.value > totalPageCount.value - 3) {
        // 마지막 3개 페이지에 가까울 때는 끝에서 5개 페이지 표시
        startPage = Math.max(1, totalPageCount.value - 4);
        endPage = totalPageCount.value;
    } else {
        // 그 외의 경우 현재 페이지를 중심으로 앞뒤로 2페이지씩 표시
        startPage = currentPage.value - 2;
        endPage = currentPage.value + 2;
    }

    for (let page = startPage; page <= endPage; page++) {
        pages.push(page);
    }
    return pages;
});


// 모달 열기
const openMore = () => {
    moreIsOpen.value = true;
};

// 모달 닫기
const closeMore = () => {
    moreIsOpen.value = false;
};

// 필터 초기화
const filterInit = () => {
    selectCategoryAll();
    selectProgramAll();
    selectStatusAll();
    const CategoryCheckbox = document.querySelector(".categoryAll");
    CategoryCheckbox.checked = true;
    const programCheckbox = document.querySelector(".programidAll");
    programCheckbox.checked = true;
    const statusCheckbox = document.querySelector(".statusCheckboxAll");
    statusCheckbox.checked = true;
}
</script>

<template>
    <main>
        <section class="n-layout-mj">
            <!--=== 헤더 ==========================================-->
            <header class="n-title">
                <h1 class="">프로그램 관리</h1>
                <div>
                    <nuxt-link to="/host/programs/new"
                        class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:plus n-deco">프로그램 등록</nuxt-link>
                </div>
            </header>

            <section class="d:flex">
                <section class="layout-list">
                    <!--=== 필터 ==========================================-->
                    <section class="n-filter bg-color:base-1 padding-x:6">
                        <h1 class="d:none">필터</h1>

                        <div class="overflow-x:auto">
                            <ul class="item-wrapper padding-y:5">
                                <li>
                                    <a href=""
                                        class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:search n-icon-size:1 n-deco n-deco-gap:1">프로그램</a>
                                </li>
                                <li>
                                    <a href=""
                                        class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:pending n-icon-size:1 n-deco n-deco-gap:1">프로그램
                                        상태</a>
                                </li>
                                <li>
                                    <a href=""
                                        class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:category n-icon-size:1 n-deco n-deco-gap:1">카테고리</a>
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

                    <!--=== 프로그램 카드 목록 + 정렬 ==========================================-->
                    <section class="n-layout-mj-cards">
                        <!--정렬-->
                        <header class="list-header bg-color:base-1">
                            <h1 class="d:none">프로그램 목록</h1>
                            <div>
                                <span>{{ totalRowCount }} Result</span>
                                <a href="" class="n-icon n-icon:arrow_swap n-deco n-deco-gap:1">
                                    정렬
                                </a>
                            </div>
                        </header>

                        <!--프로그램 카드 목록 (작성 중) (작성 완료) (모집 중)-->
                        <ul class="n-card-container bg-color:base-1 padding:7">
                            <li v-for="p in programs" :key="p.id" class="n-card bg-color:base-1 padding:6">
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
                                        <a @click.prevent="openMore()" href=""
                                            class="n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9">더보기</a>
                                    </div>
                                </div>

                                <div class="card-main">
                                    <div class="img-wrapper">
                                        <img src="/public/image/program_01.png" alt="대표사진" />
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
                                                    <span class="category" v-for="(c, index) in p.categoryNames"
                                                        :key="index">
                                                        {{ c
                                                        }}<span v-if="index < p.categoryNames.length - 1">·</span>
                                                    </span>
                                                </div>
                                            </div>

                                            <div class="card-footer-responsive">
                                                <a v-if="p.status === 'In Progress'" href=""
                                                    class="n-btn create">작성하기</a>
                                                <a v-else-if="p.status === 'Published'" href="" class="n-btn manage">예약
                                                    관리</a>
                                                <a v-else-if="p.status === 'Unpublished'" href=""
                                                    class="n-btn open">개설하기</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="card-footer">
                                    <a href="aa" class="n-btn create">작성하기</a>
                                </div>
                            </li>
                        </ul>

                        <!-- 페이지네이션 버튼 -->
                        <div class="pagination">
                            <button @click="goToPage(currentPage - 1)" :disabled="currentPage === 1">
                                〈
                            </button>

                            <button v-for="page in visiblePages" :key="page" @click="goToPage(page)"
                                :class="{ active: page === currentPage }">
                                {{ page }}
                            </button>

                            <button @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPageCount">
                                〉
                            </button>
                        </div>
                    </section>
                </section>

                <!-- 모달 창 -->
                <div v-if="moreIsOpen" class="more-overlay" @click="closeMore">
                    <div class="more">
                        <div class="more-close"><button @click="closeMore">Ⅹ</button></div>
                        <div class="more-content" @click.stop>
                            <a href="#" class="n-btn">수정하기</a>
                            <a href="#" class="n-btn">삭제하기</a>
                        </div>
                    </div>
                </div>

                <!--=== 필터 반응형 ==========================================-->
                <aside class="n-filter-aside">
                    <header class="n-title">
                        <h1 class="">Filter</h1>
                        <div>
                            <button class="n-icon n-icon:reset" style="--icon-color: var(--color-sub-1)"
                                @click="filterInit">
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
                                    <label><input class="categoryAll" type="checkbox"
                                            @change="selectCategoryAll" />All</label>
                                    <label v-for="c in categories" :key="c.id">
                                        <input class="categoryIds" type="checkbox" @change="selectCategory"
                                            :value="c.id" v-model="selectedCategories" />{{ c.name }}
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

                            <form action="" class="form">
                                <div class="modal-checkbox">
                                    <label><input class="programidAll" type="checkbox"
                                            @change="selectProgramAll" />All</label>
                                    <label v-for="p in programTitles" :key="p.id">
                                        <input class="programids" type="checkbox" @change="selectProgram" :value="p.id"
                                            v-model="selectedProgramIds" />{{ p.title }}
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
                                    <label><input class="statusCheckboxAll" type="checkbox"
                                            @change="selectStatusAll" />전체</label>
                                    <label><input class="statusCheckbox" type="checkbox" @change="selectStatus"
                                            :value="'In Progress'" v-model="selectedStatus" />작성중</label>
                                    <label><input class="statusCheckbox" type="checkbox" @change="selectStatus"
                                            :value="'Unpublished'" v-model="selectedStatus" />작성완료</label>
                                    <label><input class="statusCheckbox" type="checkbox" @change="selectStatus"
                                            :value="'Published'" v-model="selectedStatus" />모집중</label>
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
