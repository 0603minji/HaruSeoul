<!--1. 프로그램 필터에 목록을 db에서 불러오기-->
<!--2. 프로그램으로 필터링 (체크박스 선택)-->
<!--3. 카테고리 필터링-->
<!--4. 상태 필터링-->
<!--5. 페이지 네이션 구현-->
<!--6. programs/new에서 값 입력받으면 새로운 프로그램 조회 가능 구현-->
<!--7. status를 db에서는 integer로 저장해두고 프론트에서 if status==1 이면 "작성중"으로 구현-->
<!--8. route 테이블 생성-->

<script setup>
import { onMounted, ref } from "vue";
import axios from "axios";
import Category from "~/components/filter/Category.vue";

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
        "http://localhost:8080/api/v1/host/programs", {
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
    programTitles.value = response.data.map(p => ({ id: p.id, title: p.title }));
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
                    <a href="" class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:plus n-deco">프로그램 등록</a>
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
                            <li class="n-card bg-color:base-1 padding:6" v-for="p in programs">
                                <h2 class="d:none">프로그램 카드</h2>

                                <div class="card-header">
                                    <div class="left">
                                        <span class="n-panel-tag not-submitted">{{
                                            p.status
                                            }}</span>
                                    </div>
                                    <div class="right">
                                        <a href=""
                                            class="n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9">더보기</a>
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
                                                    <span class="category" v-for="(c, index) in p.categoryNames"
                                                        :key="index">
                                                        {{ c }}<span v-if="index < p.categoryNames.length - 1"> ·</span>
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
                            <button class="n-icon n-icon:reset" style="--icon-color: var(--color-sub-1)">
                                초기화
                            </button>
                        </div>
                    </header>
                    <div class="filters">
                        <!-- 카테고리 필터 -->
                        <Category />
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
                                            v-model="selectedPrograms">{{ p.title }}
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
.n-card {
    /* 해당영역 클릭 시 링크로 이동 */
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

    /* card header 태그 */

    .card-header {
        .n-panel-tag {
            --tag-border-radius: 14px;
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
            transition: border-color .3s, background-color .3s;
            user-select: none;
            white-space: nowrap;
            width: auto
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


/*===================== 반응형 구현 ====================*/


/*필터 어사이드*/
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

.filter[open] {
    .collapse {
        --collapse-padding: 24px 0 12px 0;

        .n-icon\:arrow_up:before {
            --icon: url(/css/image/icon/arrow_down.svg);
        }
    }
}

/*컨텐츠 + 필터어사이드 묶은 카테고리*/
.n-layout-mj {
    display: flex;
    flex-direction: column;
    min-width: 300px;
    max-width: 1092px;

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
            --title-padding: 10px 4px;

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
        flex-direction: row;
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
            transition: border-color .3s, background-color .3s;
            user-select: none;
            white-space: nowrap;
            width: auto
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
        flex-direction: row;
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
</style>
