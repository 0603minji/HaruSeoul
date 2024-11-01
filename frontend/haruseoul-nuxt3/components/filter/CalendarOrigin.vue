<script setup>

</script>
<template>

    <!-- <aside class="n-modal">
        <header class="modal-header">
            <h1 class="margin:auto font-weight:bold font-size:8">필터</h1>
            <button class="border:none n-btn n-btn-size:1">X</button>
        </header> -->

    <!--   기간    -->
    <section>
        <h1 class="d:none">기간 필터</h1>
        <form action="">
            <!-- <div>
                    <p>기간</p>
                    <div class="modal-duration">
                        <label><input type="text"></label>
                        <span>~</span>
                        <label><input type="text"></label>
                    </div>
                </div>

                <div class="modal-reset">
                    <p>날짜</p>
                    <button
                        class="border:none n-icon n-icon-size:2 n-icon:reset n-icon-color:sub-1 n-btn n-deco color:sub-1">
                        초기화
                    </button>
                </div> -->

            <div class="calendar-container">
                <div class="calendar-top">
                    <button class="n-btn border-radius:full" type="button" id="prev-month">&lt;</button>
                    <div class="font-size:9 font-weight:bold">
                        <select id="year">
                            <option value="2024">2024</option>
                            <option value="2025">2025</option>
                            <option value="2026">2026</option>
                        </select>

                        <select id="month">
                            <option value="1">1월</option>
                            <option value="2">2월</option>
                            <option value="3">3월</option>
                            <option value="4">4월</option>
                            <option value="5">5월</option>
                            <option value="6">6월</option>
                            <option value="7">7월</option>
                            <option value="8">8월</option>
                            <option value="9">9월</option>
                            <option value="10">10월</option>
                            <option value="11">11월</option>
                            <option value="12">12월</option>
                        </select>
                    </div>
                    <button class="n-btn border-radius:full" type="button" id="next-month">&gt;</button>
                </div>

                <section class="calendar">
                    <h1 class="d:none">달력</h1>
                    <div class="d:flex jc:space-around">
                        <div>Mo</div>
                        <div>Tu</div>
                        <div>We</div>
                        <div>Th</div>
                        <div>Fr</div>
                        <div>Sa</div>
                        <div>Su</div>
                    </div>
                    <!-- 첫째 주 -->
                    <div class="calendar-row">
                        <div><label><input type="checkbox">29</label></div>
                        <div><label><input type="checkbox">30</label></div>
                        <div><label><input type="checkbox">31</label></div>
                        <div><label><input type="checkbox">1</label></div>
                        <div><label><input type="checkbox">2</label></div>
                        <div><label><input type="checkbox">3</label></div>
                        <div><label><input type="checkbox">4</label></div>
                    </div>
                    <!-- 둘째 주 -->
                    <div class="calendar-row">
                        <div><label><input type="checkbox">5</label></div>
                        <div><label><input type="checkbox">6</label></div>
                        <div><label><input type="checkbox">7</label></div>
                        <div><label><input type="checkbox">8</label></div>
                        <div><label><input type="checkbox">9</label></div>
                        <div><label><input type="checkbox">10</label></div>
                        <div><label><input type="checkbox">11</label></div>
                    </div>
                    <!-- 셋째 주부터 활성화 -->
                    <div class="calendar-row">
                        <div><label><input type="checkbox">12</label></div>
                        <div><label><input type="checkbox">13</label></div>
                        <div><label><input type="checkbox">14</label></div>
                        <div><label><input type="checkbox">16</label></div>
                        <div><label><input type="checkbox">17</label></div>
                        <div><label><input type="checkbox">18</label></div>
                        <div><label><input type="checkbox">19</label></div>
                    </div>

                    <div class="calendar-row">
                        <div><label><input type="checkbox">20</label></div>
                        <div><label><input type="checkbox">21</label></div>
                        <div><label><input type="checkbox">22</label></div>
                        <div><label><input type="checkbox">23</label></div>
                        <div><label><input type="checkbox">24</label></div>
                        <div><label><input type="checkbox">25</label></div>
                        <div><label><input type="checkbox">26</label></div>
                    </div>

                    <div class="calendar-row">
                        <div><label><input type="checkbox">27</label></div>
                        <div><label><input type="checkbox">28</label></div>
                        <div><label><input type="checkbox">29</label></div>
                        <div><label><input type="checkbox">30</label></div>
                        <div><label><input type="checkbox">31</label></div>
                        <div><label><input type="checkbox">1</label></div>
                        <div><label><input type="checkbox">2</label></div>
                    </div>

                </section>
            </div>

            <!-- <div class="modal-footer">
                    <button class="n-btn n-btn-color:sub-1 n-btn-size:1">확인</button>
                </div> -->

        </form>
    </section>
    <!-- </aside> -->

</template>

<style scoped>
.n-modal {
    --modal-border-radius: 16px;
    --modal-border: none;
    --modal-box-shadow: rgba(0, 0, 0, .08) 0 16px 20px -4px, rgba(0, 0, 0, .03) 0 8px 8px -4px;
    --modal-z-index: 1200;
    background-color: var(--color-base-1);
    border: var(--modal-border);
    border-top-left-radius: var(--modal-border-radius);
    border-top-right-radius: var(--modal-border-radius);
    box-shadow: var(--modal-box-shadow);
    max-width: 90vw;
    padding: 16px 24px;

    /* 모달을 화면 아래에 고정 */
    /*position: fixed;*/
    left: 50%;
    top: auto;
    /* 상단 고정을 해제 */
    bottom: 0;
    /* 하단에 고정 */
    /*transform: translateX(-50%); !* 수평 가운데 정렬 *!*/

    /* 화면 크기 조정 */
    height: auto;
    /* 높이를 자동으로 설정 */
    z-index: var(--modal-z-index);

    .modal-header {
        display: flex;
        justify-content: space-between;
    }

    .modal-duration {
        display: flex;
        justify-content: center;
        align-items: center;
        border: 1px solid #ccc;
        padding: 5px;
        border-radius: 10px;
        width: 100%;
        /* 입력 필드가 적절한 크기로 보이게 설정 */

        >input {
            border: none;
            outline: none;
            text-align: center;
            font-size: 14px;
            width: 45%;
            /* 입력 필드 너비 조정 */
        }
    }

    .modal-reset {
        display: flex;
        justify-content: space-between;

        >p {
            display: flex;
            align-items: center;

            >button {
                border: none;
            }
        }
    }

    .modal-footer {
        display: flex;
        justify-content: end;
        margin-top: 10px;

        >button {
            border-radius: 9999px;
            background-color: var(--color-sub-1);
            border-color: var(--color-sub-1);
            color: var(--color-base-1)
        }
    }

    .min-max-container {
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 100%;
        /* 전체 너비를 조정하고 싶으면 값 변경 */

        >.min-section,
        .max-section {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding-top: 10px;
        }

        >.input-group {
            display: flex;
            align-items: center;
            gap: 5px;
            /* 버튼과 입력창 사이의 간격 */
        }


        input[type="number"] {
            width: 50px;
            height: 30px;
            text-align: center;
            border-radius: 4px;
            border: 1px solid #ccc;
            font-size: 16px;
        }

        >.tilde {
            font-size: 24px;
            padding: 0 10px;
        }
    }

}

.n-modal::before {
    content: '';
    display: block;
    position: absolute;
    left: 0;
    right: 0;
    bottom: 0;
    height: 16px;
    background-color: var(--color-base-1);
    border-bottom-left-radius: 0;
    /* 하단 좌우 radius 제거 */
    border-bottom-right-radius: 0;
}


aside.n-modal {
    width: 100%;
    max-width: none;
    /*height: auto;*/
    /*margin: 0 auto;*/

    /*background-color: white;*/
    /*border-radius: 10px;*/

    /*position: relative;*/
}

/* 모바일(768px 이하) 기준 모달 스타일 */
@media (max-width: 768px) {
    aside.n-modal {
        width: 100%;
        /* 모바일에서는 너비를 90%로 설정 */
        max-width: none;
        /* 최대 너비 제한 해제 */
    }
}

.calendar-container {
    background-color: var(--color-bg-base-1);
    padding-bottom: 16px;
    padding-top: 16px;

    .calendar-top {
        display: flex;
        justify-content: space-between;
        padding-bottom: 8px;
    }

    .calendar .calendar-row {
        display: flex;
        justify-content: space-around;
        font-weight: bold;
        padding-top: 6px;
    }

    .calendar input[type="checkbox"] {
        display: none;
        /* 체크박스 숨김 */
    }

    .calendar label {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 50px;
        height: 50px;
        /*border: 1px solid #ddd;*/
        box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
        border-radius: 6px;
        cursor: pointer;
        transition: background-color 0.3s ease, border-color 0.3s ease, color 0.3s ease;
        font-size: 18px;
        /*>input[type="checkbox"]:checked{*/
        /*    background-color: var(--color-sub-1);*/
        /*    color: white;*/
        /*    border-color: var(--color-sub-1);*/
        /*}*/
    }


    .calendar label:has(input:checked) {
        background-color: var(--color-sub-1);
        border-color: var(--color-sub-1);
        color: white;
    }

    /* 선택된 날짜 스타일 */
    /*.calendar input[type="checkbox"]:checked + label {*/
    /*    background-color: var(--color-sub-1);*/
    /*    color: white;*/
    /*    border-color: var(--color-sub-1);*/
    /*}*/

    /* 마우스 오버 시 스타일 */

    .calendar label:hover {
        background-color: #87ceeb;
        border-color: #87ceeb;
    }
}
</style>