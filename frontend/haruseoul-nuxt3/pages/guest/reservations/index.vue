<script setup>
import { onMounted, ref } from "vue";
import axios from "axios";
import Status from "~/components/filter/Status.vue";

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
            <Status />
            <!--=== 예약 카드 목록 ===================================================-->
            <ul class="n-card-container bg-color:base-1">
                <!-- =================================== 카드 1개 =================================== -->
                <li v-for="r in reservations" class="n-card bg-color:base-1 padding:6">
                    <a class="n-link-box" href="detail?id=1"></a>
                    <h1 class="d:none">예약 카드</h1>

                    <div class="card-header">
                        <div class="left">
                            <span class="n-panel-tag completed">{{}}</span>
                        </div>
                    </div>

                    <div class="card-main">
                        <div class="img-wrapper">
                            <img src="/public/image/program_02.png" alt="대표사진" />
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
                                    <a href="aa" class="n-btn bg-color:main-1 color:base-1">Host 문의</a>
                                    <a href="aa" class="n-btn">리뷰 작성</a>
                                    <a href="aa"
                                        class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card-footer margin-top:2">
                        <a href="aa" class="n-btn bg-color:main-1 color:base-1">Host 문의</a>
                        <a href="aa" class="n-btn">리뷰 작성</a>
                        <a href="aa"
                            class="n-btn n-icon n-icon:share border-color:transparent flex-grow:0 padding-y:0">공유하기</a>
                    </div>
                </li>
            </ul>
        </section>
    </main>
</template>

<style scoped>
/* 예약 중, 이용완료, 취소 */
.n-panel-tag {
    --tag-border-radius: 14px;
    --tag-padding: 6px 8px;
    font-weight: 600;
    /* semi-bold */
}

.n-panel-tag.completed {
    --tag-border-color: var(--color-base-8);
    --tag-bg-color: var(--color-base-8);
    color: var(--color-base-1);
}

.n-panel-tag.canceled {
    --tag-border-color: var(--color-base-3);
    --tag-bg-color: var(--color-base-3);
    color: var(--color-base-8);
}

/*======================================================*/

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

    .card-footer>.n-btn {
        z-index: 2;
    }

    .card-main {
        .img-wrapper {
            width: 94px;
            min-width: 94px;
        }
    }
}

/*=========================================================*/
/*반응형 구현*/
.n-layout-mj {
    max-width: 1092px;
    margin: 0 auto;
}

@media (max-width: 768px) {
    .n-card-container {
        max-width: 100%;
        padding: 0 20px 16px 20px;

        .card-header-responsive {
            display: none;
        }

    }

    .card-footer-responsive {
        display: none;
    }
}


@media (min-width: 768px) and (max-width: 1092px) {
    .n-card-container {
        max-width: 100%;
        padding: 0 20px 16px 20px;

        .card-footer {
            display: none;
        }

        .card-header-responsive {
            display: block;

            .n-panel-tag {
                --tag-padding: 3px 6px;
            }
        }

        .card-header {
            display: none;
        }

        .card-main {
            .img-wrapper {
                width: 110px;
                min-width: 110px;
            }
        }
    }



    .card-footer-responsive {
        width: 400px;
        display: flex;
        align-items: center;
        justify-content: end;
        gap: 20px;

        .n-btn {
            flex-grow: 0;
            flex-shrink: 1;

            --btn-font-size: 14px;
            --btn-border-color: var(--color-main-1);
            --btn-border-radius: 12px;
            /* --btn-bg-hover: var(--color-base-2); */
            --btn-bg-color: transparent;
            --btn-padding: 12px 16px;
            align-items: center;
            background-color: var(--btn-bg-color);
            border: 1px solid var(--btn-border-color, var(--color-base-3));
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
            width: auto;

            z-index: 2;
        }
    }

    .card-info-responsive {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
}


@media (min-width: 1092px) {
    .n-card-container {
        max-width: 1092px;
        padding: 0 20px 16px 20px;

        .card-footer {
            display: none;
        }

        .card-header-responsive {
            display: block;
        }

        .card-header {
            display: none;
        }

        .card-main {
            .img-wrapper {
                width: 110px;
                min-width: 110px;
            }
        }
    }



    .card-footer-responsive {
        width: 400px;
        display: flex;
        align-items: center;
        justify-content: end;
        gap: 20px;

        .n-btn {
            flex-grow: 0;
            flex-shrink: 1;

            --btn-font-size: 14px;
            --btn-border-color: var(--color-main-1);
            --btn-border-radius: 12px;
            /* --btn-bg-hover: var(--color-base-2); */
            --btn-bg-color: transparent;
            --btn-padding: 12px 16px;
            align-items: center;
            background-color: var(--btn-bg-color);
            border: 1px solid var(--btn-border-color, var(--color-base-3));
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
            width: auto;

            z-index: 2;
        }
    }

    .card-info-responsive {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
}
</style>