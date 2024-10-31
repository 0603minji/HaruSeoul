<template>
    <main>

        <section class="layout-body">
            <h1 class="d:none">프로그램 목록</h1>

            <div class="layout-main">
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
                            <a href="" class="icon-box n-deco1 n-icon n-icon:reset">
                                초기화
                            </a>
                        </div>
                    </section>

                    <!--=== 프로그램 목록==========================================-->
                    <section class="pg-list">
                        <!--=== 정렬 .list-header ==========================================-->
                        <header class="list-header bg-color:base-1">
                            <h1 class="d:none">프로그램 목록</h1>
                            <div>
                                <span class="list-item-count">60 Result</span>
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
                        <ul class="n-card-container-column bg-color:base-1">
                            <!-- =================================== 예약 카드 1개 =================================== -->
                            <li class="n-card n-card-column bg-color:base-1" th:each="i : ${#numbers.sequence(1, 10)}">
                                <a class="n-link-box" href="detail?id=1"></a>
                                <h2 class="d:none">프로그램 카드</h2>

                                <div class="card-main">
                                    <div class="img-wrapper">
                                        <img src="/public/image/program_01.png" alt="대표사진">
                                    </div>

                                    <div class="card-info-wrapper">
                                        <div class="card-info gap:1 color:base-7 font-size:5">
                                            <span>Culture</span>
                                            <span>·</span>
                                            <span>Activity</span>
                                        </div>
                                        <p class="title">Gyeong-bok-gung Palace Tour With Wearing Han-bok</p>

                                        <div class="card-info">
                                            <span class="n-icon n-icon:star n-deco"
                                                style="--icon-color: var(--color-yellow-1)"></span>
                                            <span>4.9 (27)</span>
                                        </div>
                                        <div class="card-info">
                                            <span class="n-icon n-icon:clock_sand n-deco"></span>
                                            <span>4 hours</span>
                                        </div>
                                    </div>
                                </div>

                                <div class="card-footer justify-content:flex-end">
                                    <span
                                        class="n-icon n-icon:price n-icon-size:2 n-deco n-deco-gap:1 font-size:8 margin-right:3">27,900</span>
                                </div>
                            </li>
                        </ul>
                    </section>
                </div>

                <!--=== 필터 @media (min-width:768px)==========================================-->
                <aside class="layout-main-aside">
                    <header class="n-title">
                        <h1 class="">Filter</h1>
                        <div>
                            <button class="n-icon n-icon:reset" style="--icon-color: var(--color-sub-1)">초기화</button>
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
                                        <input type="date" placeholder="Start Date">
                                    </label>
                                    <span>~</span>
                                    <label>
                                        <input type="date" placeholder="End Date">
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
                                    <label><input type="checkbox">All</label>
                                    <label><input type="checkbox">Activity</label>
                                    <label><input type="checkbox">Culture</label>
                                    <label><input type="checkbox">Shopping</label>
                                    <label><input type="checkbox">Food</label>
                                    <label><input type="checkbox">Nature</label>
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
                                    <div class="range-slider">
                                        <input type="range" min="0" max="300000" value="0" class="slider">
                                        <input type="range" min="0" max="300000" value="300000" class="slider">
                                    </div>
                                    <div class="price-inputs">
                                        <input type="text" placeholder="₩0" value="₩0" readonly>
                                        <span class="tilde">~</span>
                                        <input type="text" placeholder="₩300000" value="₩300000" readonly>

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
                                    <div><label><input type="radio" class="radio">All</label></div>
                                    <div><label><input type="radio" class="radio">English</label></div>
                                    <div><label><input type="radio" class="radio">Japanese</label></div>
                                    <div><label><input type="radio" class="radio">Chinese</label></div>
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
                                            <input type="number" min="0" max="8" value="0">
                                            <button class="n-btn">＋</button>
                                        </div>
                                    </div>

                                    <span class="tilde">~</span>

                                    <div class="max-section">
                                        <span>Max</span>
                                        <div class="input-group">
                                            <button class="n-btn">-</button>
                                            <input type="number" min="0" max="8" value="8">
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
                                    <div><label><input type="radio" class="radio">All</label></div>
                                    <div><label><input type="radio" class="radio">Under 2 hours</label></div>
                                    <div><label><input type="radio" class="radio">2 ~ 4 hours</label></div>
                                    <div><label><input type="radio" class="radio">4 ~ 6 hours</label></div>
                                    <div><label><input type="radio" class="radio">6 ~ 8 hours</label></div>
                                    <div><label><input type="radio" class="radio">Over 8 hours</label></div>
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
                                    <div><label><input type="radio" class="radio">All</label></div>
                                    <div><label><input type="radio" class="radio">Before 12 PM</label></div>
                                    <div><label><input type="radio" class="radio">12 PM to 6 PM</label></div>
                                    <div><label><input type="radio" class="radio">After 6 PM</label></div>
                                </div>

                            </form>
                        </details>
                    </div>
                </aside>
            </div>
        </section>

    </main>
</template>
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

            .n-card-container-column {}

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

@media (min-width: 768px) {
    .layout-body {
        margin-left: auto;
        margin-right: auto;
        /*min-width: 992px;*/
        width: 100%;
        max-width: 1092px;

        .layout-main {
            .layout-main-list {}

            .layout-main-aside {
                display: flex;
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
                position: absolute;
                pointer-events: none;
            }

            .slider:first-child {
                z-index: 1;
                /* 첫 번째 슬라이더가 뒷부분에 위치하게 함 */
            }

            .slider:last-child {
                z-index: 2;
                /* 두 번째 슬라이더가 앞부분에 위치하게 하여 겹칠 때도 정상적으로 보이도록 설정 */
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
                z-index: 5;
                pointer-events: auto;
                /* 슬라이더 핸들에서 이벤트 발생 허용 */
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