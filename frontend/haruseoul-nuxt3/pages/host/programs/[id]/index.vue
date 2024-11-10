<script setup>
import { useRoute } from 'vue-router'
import axios from "axios";
import { reactive } from 'vue';

// import { useFetch } from '@nuxtjs/composition-api'

const route = useRoute()
const id = route.params.id
const oneProgram = reactive({});
const durationHour = ref(0);
const durationMinute = ref(0);
// const { data: programData } = await useFetch(() => `/api/programs/${id}`)

onMounted(() => {
    console.log(id);
    fetchOneProgram();
})

const fetchOneProgram = async () => {
    const response = await axios.get(
        "http://localhost:8080/api/v1/host/programs/" + id
    );
    oneProgram.value = response.data;
    console.log("data :", oneProgram.value);
    console.log("data :", oneProgram.value.categoryNames);
    
    durationHour.value = parseInt(oneProgram.value.endTimeHour) - parseInt(oneProgram.value.startTimeHour);
    durationMinute.value = parseInt(oneProgram.value.endTimeMinute) - parseInt(oneProgram.value.startTimeMinute);
    if(durationMinute.value<0) {
        durationMinute.value = durationMinute.value + 60;
        durationHour.value = durationHour.value - 1;
    }
    if(durationMinute.value===30) {
        durationMinute.value=5;
    }
    console.log("durationHour.value : ", durationHour.value);
    console.log("duration minute : ", durationMinute.value);
};

</script>

<template>
    <main>
        <section class="main-wrapper">
            <section class="program-detail">
                <h1 class="d:none">program-detail 페이지</h1>

                <!--========================= 네비게이션 탭 =================================== -->
                <nav class="n-bar-underline padding-y:5">
                    <h1 class="d:none">네비탭</h1>
                    <ul class="item-wrapper padding-x:1">
                        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                            <a href="#intro">Summary</a>
                        </li>
                        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                            <a href="#program-overview">Intro</a>
                        </li>
                        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                            <a href="#course-information">Course</a>
                        </li>
                        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                            <a href="#meeting-location">Meeting Point</a>
                        </li>
                        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                            <a href="#inclusions">Inclusions</a>
                        </li>
                        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                            <a href="#things-to-know">Notice</a>
                        </li>
                        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0">
                            <a href="#review">Reviews</a>
                        </li>
                    </ul>
                </nav>


                <!-- =========================== 이미지 + 카테고리 + 프로그램 제목 + 평점 리뷰 + 가격 ========================== -->
                <section class="bg-color:base-1 width:10p">
                    <h1 class="d:none">대표 이미지 + 카테고리 + 프로그램 제목 + 평점리뷰 + 가격</h1>
                    <!--=====  대표 이미지 =====-->
                    <div class="thumbnail-wrapper">
                        <button class="n-btn thumbnail-btn thumbnail-btn:left"><span
                                class="n-icon n-icon:arrow_left"></span>
                        </button>
                        <img src="/public/image/thumbnail.png" alt="대표사진" class="thumbnail-img">
                        <button class="n-btn thumbnail-btn thumbnail-btn:right"><span
                                class="n-icon n-icon:arrow_right"></span>
                        </button>
                        <div class="thumbnail-btn thumbnail-btn:num n-deco">1/5</div>
                    </div>
                    <!--==== 카테고리 ====-->
                    <div class="categories">
                        <div v-if="oneProgram.value && oneProgram.value.categoryNames" class="n-panel-tag category"
                            v-for="c in oneProgram.value.categoryNames">
                            {{ c }}
                        </div>
                        <div class="edit-btn">
                            <button class="n-icon n-icon:edit"></button>
                        </div>
                    </div>
                    <!--========= 프로그램 제목 + 평점리뷰 + 가격 =========-->
                    <div class="info">
                        <div style="display: flex; align-items: center;">
                            <p class="title" v-if="oneProgram.value && oneProgram.value.title">
                                {{ oneProgram.value.title }}
                            </p>
                            <div class="edit-btn">
                                <button class="n-icon n-icon:edit"></button>
                            </div>
                        </div>
                        <div class="rating-review">
                            <span class="n-icon n-icon:star n-deco" v-if="oneProgram.value">
                                {{ oneProgram.value.rating === null ? '0.0' : oneProgram.value.rating }}
                            </span>
                            <span>/</span>
                            <span>5.0</span>
                            <span>(Reviews)</span>

                            <div class="price">
                                <div class="n-icon n-icon:price n-deco"
                                    style="display: flex; justify-content: center; align-items: center; height: inherit; font-size: var(--font-size-10); gap: 0;"
                                    v-if="oneProgram.value && oneProgram.value.price">
                                    {{ oneProgram.value.price }}
                                </div>
                                <div
                                    style="display: flex; justify-content: center; align-items: center; height: inherit; margin-left: var(--gap-1); font-size: var(--font-size-6);">
                                    / Per person
                                </div>
                            </div>
                            <div class="edit-btn">
                                <button class="n-icon n-icon:edit"></button>
                            </div>
                        </div>
                    </div>
                    <div class="cross-line"></div>
                </section>


                <section class="content">
                    <!--========== 개요 ==========-->
                    <section id="intro" class="intro" style="margin-bottom: 20px;">
                        <div class="background-color:base-1" style="padding: 0 var(--gap-6); border-bottom:0;">
                            <div class="id-container" style="margin-top: 0;">
                                <div class="details">
                                    <section>
                                        <h1>개요</h1>
                                        <div class="list-container">
                                            <ul>
                                                <li class="list-content">
                                                    <span class="n-icon n-icon:globe n-deco"
                                                    v-if="oneProgram.value && oneProgram.value.language">
                                                        {{ oneProgram.value.language }}
                                                    </span>
                                                    <div class="edit-btn m-left:3">
                                                        <button class="n-icon n-icon:edit"></button>
                                                    </div>
                                                </li>
                                                <li class="list-content">
                                                    <span class="n-icon n-icon:people n-deco" v-if="oneProgram.value">
                                                        {{ oneProgram.value.groupSizeMin }}/{{ oneProgram.value.groupSizeMax }}
                                                    </span>
                                                    <span>(min/max)</span>
                                                    <div class="edit-btn m-left:3">
                                                        <button class="n-icon n-icon:edit"></button>
                                                    </div>
                                                </li>
                                                <li class="list-content">
                                                    <span class="n-icon n-icon:clock n-deco">
                                                        {{ durationHour }}.{{ durationMinute }}
                                                    </span>
                                                    <span>hours</span>
                                                    <div class="edit-btn m-left:3">
                                                        <button class="n-icon n-icon:edit"></button>
                                                    </div>
                                                </li>
                                                <li class="list-content">
                                                    <span class="n-icon n-icon:placeholder n-deco" v-if="oneProgram.value && oneProgram.value.route">
                                                       {{ oneProgram.value.route[0].address }}
                                                    </span>
                                                    <div class="edit-btn m-left:3">
                                                        <button class="n-icon n-icon:edit"></button>
                                                    </div>
                                                </li>
                                            </ul>
                                        </div>
                                    </section>
                                </div>
                            </div>
                        </div>
                    </section>


                    <!-- ================== 호스트 프로필 카드 ================== -->
                    <section style="margin-bottom: 20px;">
                        <h1>호스트정보</h1>
                        <div class="background-color:base-1" style="padding: 0 var(--gap-6);">
                            <div class="id-container">
                                <!-- 프로필 카드 -->
                                <section class="profile-card">
                                    <h1>프로필 카드</h1>
                                    <div class="overview">
                                        <div class="img-wrapper">
                                            <img src="/public/image/profile.png" alt="호스트프사">
                                        </div>
                                        <div>
                                            <div>Jasmin</div>
                                            <div class="n-icon n-icon:star n-deco">
                                                <span>4.5 / 5.0</span>
                                                <span>(999)</span>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="" class="n-btn n-btn:hover">See Profile</a>
                                </section>
                            </div>
                        </div>
                    </section>


                    <!--  프로그램 소개  -->
                    <section id="program-overview" style="margin-bottom: 20px;">
                        <h1>Program Introduction</h1>
                        <div class="background-color:base-1" style="padding: 0 var(--gap-6);">
                            <div class="program-container">
                                <div class="content-header">
                                    <span class="title">Program Introduction</span>
                                    <div class="edit-btn m-left:3">
                                        <button class="n-icon n-icon:edit"></button>
                                    </div>
                                </div>
                                <div class="text">
                                    <p class="p-summary" v-if="oneProgram.value && oneProgram.value.detail">
                                        {{  oneProgram.value.detail }}
                                    </p>
                                    <button class="n-icon n-icon:arrow_down n-deco-pos:right n-deco">see more</button>
                                </div>
                            </div>
                        </div>
                    </section>


                    <!--===============  코스 =================-->
                    <section id="course-information" class="program">
                        <h1>Course</h1>
                        <div class="background-color:base-1" style="padding: 0 var(--gap-6);">
                            <div class="id-container">
                                <div class="content-header">
                                    <span class="title">Course</span>
                                    <div class="edit-btn m-left:3">
                                        <button class="n-icon n-icon:edit"></button>
                                    </div>
                                </div>
                                <div class="details">
                                    <div class="map-img-wrapper">
                                        <img class="map-img" src="/public/image/map.png" alt="코스지도">
                                    </div>

                                    <section class="n-course-flow">
                                        <!--     출발지    -->
                                        <div class="point">
                                            <div class="icon-wrapper">
                                                <span class="n-icon n-icon:rectangle">막대기</span>
                                                <span class="n-icon n-icon:placeholder">위치아이콘</span>
                                            </div>
                                            <div class="point-detail">
                                                <div class="n-panel-tag n-panel-tag:time">
                                                    <span class="n-icon n-icon:clock n-deco">
                                                        16:00
                                                    </span>
                                                </div>
                                                <div>
                                                    <span class="point-name">Title</span>
                                                    <span>(duration)</span>
                                                    <div class="point-info">Info</div>
                                                </div>
                                            </div>
                                        </div>

                                        <!--   이동 수단   -->
                                        <div class="transport">
                                            <div class="icon-wrapper">
                                                <span class="n-icon n-icon:rectangle">막대기</span>
                                                <div class="img-wrapper">
                                                    <img src="/assets/image/icon/bus.png" alt="이동수단">
                                                </div>
                                            </div>
                                            <div>

                                                <div class="transport-detail">
                                                    <p>Bus</p>
                                                    <p>(transportation duration)</p>
                                                </div>
                                            </div>
                                        </div>


                                        <!--     경유지    -->
                                        <div class="point drop-by">
                                            <div class="icon-wrapper">
                                                <span class="n-icon n-icon:rectangle">막대기</span>
                                                <span class="n-icon n-icon:number1">위치아이콘</span>
                                            </div>
                                            <div class="point-detail">
                                                <div class="n-panel-tag n-panel-tag:time">
                                                    <span class="n-icon n-icon:clock n-deco">
                                                        17:00
                                                    </span>
                                                </div>
                                                <div>
                                                    <span class="point-name">Title</span>
                                                    <span>(duration)</span>
                                                    <div class="point-info">Info</div>
                                                </div>
                                            </div>
                                        </div>

                                        <!--   이동 수단   -->
                                        <div class="transport">
                                            <div class="icon-wrapper">
                                                <span class="n-icon n-icon:rectangle">막대기</span>
                                                <div class="img-wrapper">
                                                    <img src="/assets/image/icon/subway.png" alt="이동수단">
                                                </div>
                                            </div>
                                            <div>

                                                <div class="transport-detail">
                                                    <p>Subway</p>
                                                    <p>(transportation duration)</p>
                                                </div>
                                            </div>
                                        </div>


                                        <!--     경유지    -->
                                        <div class="point drop-by">
                                            <div class="icon-wrapper">
                                                <span class="n-icon n-icon:rectangle">막대기</span>
                                                <span class="n-icon n-icon:number1">위치아이콘</span>
                                            </div>
                                            <div class="point-detail">
                                                <div class="n-panel-tag n-panel-tag:time">
                                                    <span class="n-icon n-icon:clock n-deco">
                                                        19:00
                                                    </span>
                                                </div>
                                                <div>
                                                    <span class="point-name">Title</span>
                                                    <span>(duration)</span>
                                                    <div class="point-info">Info</div>
                                                </div>
                                            </div>
                                        </div>

                                        <!--   이동 수단   -->
                                        <div class="transport">
                                            <div class="icon-wrapper">
                                                <span class="n-icon n-icon:rectangle">막대기</span>
                                                <div class="img-wrapper">
                                                    <img src="/assets/image/icon/walk.png" alt="이동수단">
                                                </div>
                                            </div>
                                            <div>

                                                <div class="transport-detail">
                                                    <p>Walk</p>
                                                    <p>(transportation duration)</p>
                                                </div>
                                            </div>
                                        </div>


                                        <!--     도착지    -->
                                        <div class="point">
                                            <div class="icon-wrapper">
                                                <span class="n-icon n-icon:rectangle">막대기</span>
                                                <span class="n-icon n-icon:placeholder">위치아이콘</span>
                                            </div>
                                            <div class="point-detail">
                                                <div class="n-panel-tag n-panel-tag:time">
                                                    <span class="n-icon n-icon:clock n-deco">
                                                        23:00
                                                    </span>
                                                </div>
                                                <div>
                                                    <span class="point-name">Title</span>
                                                    <span>(duration)</span>
                                                    <div class="point-info">Info</div>
                                                </div>
                                            </div>
                                        </div>


                                    </section>
                                </div>
                            </div>
                        </div>
                    </section>

                    <!--==============  만나는 장소  + 포함사항 + 꼭알아두세요 =================-->
                    <section id="meeting-location" class="program">
                        <h1>만나는 장소 + 포함사항 + 꼭알아두세요</h1>

                        <!-- 만나는 장소 -->
                        <div class="background-color:base-1" style="padding: 0 var(--gap-6);">
                            <div class="id-container" style="border-bottom:0;">
                                <div class="content-header">
                                    <span class="title">Meeting Point</span>
                                    <div class="edit-btn m-left:3">
                                        <button class="n-icon n-icon:edit"></button>
                                    </div>
                                </div>
                                <div class="details">
                                    <section style="padding-top: 0;">
                                        <h1>만나는장소</h1>
                                        <div class="info-container">
                                            <p>Gang-nam Station 11th Exit</p>
                                            <div
                                                style="display:flex; align-items: center; padding: var(--gap-3) 0; color: var(--color-base-7);">
                                                <span class="n-icon n-icon:placeholder"
                                                    style="margin-right: var(--gap-1);">위치아이콘</span>
                                                <span style="margin-right: var(--gap-1);">Yeoksam-dong, Gangnam-gu,
                                                    Seoul</span>
                                            </div>
                                        </div>
                                        <div class="map-img-wrapper">
                                            <img class="map-img" src="/public/image/map.png" alt="map">
                                        </div>
                                    </section>
                                </div>
                            </div>

                            <!-- 포함사항 -->
                            <div id="inclusions" class="id-container" style="border-bottom:0;">
                                <div class="content-header">
                                    <span class="title">Inclusion</span>
                                    <div class="edit-btn m-left:3">
                                        <button class="n-icon n-icon:edit"></button>
                                    </div>
                                </div>
                                <div class="details">
                                    <section>
                                        <h1>포함사항</h1>
                                        <div class="list-container">
                                            <ul style="padding-left: 0;">
                                                <li class="info-input n-icon n-icon:success-circle-green">Entrance Fee
                                                </li>
                                                <li class="info-input n-icon n-icon:success-circle-green">Lunch Fee</li>
                                            </ul>
                                        </div>
                                        <div class="list-container">
                                            <ul style="padding-left: 0;">
                                                <li class="info-input n-icon n-icon:error">Personal expenses</li>
                                            </ul>
                                        </div>
                                    </section>
                                </div>
                            </div>

                            <!-- 꼭알아두세요 -->
                            <div id="things-to-know" class="id-container" style="margin-top: 0; border-bottom:0;">
                                <div class="content-header">
                                    <span class="title">Notice</span>
                                    <div class="edit-btn m-left:3">
                                        <button class="n-icon n-icon:edit"></button>
                                    </div>
                                </div>
                                <div class="details">

                                    <section>
                                        <h1>꼭 알아두세요!</h1>

                                        <!-- 준비물 -->
                                        <div style="padding: 0 var(--gap-6);">
                                            <h2 class="info-form n-icon n-icon:success-decagon">Essentials</h2>
                                            <ul>
                                                <li class="list-content">Comfortable shoes</li>
                                                <li class="list-content">Personal expenses</li>
                                                <li class="list-content">Passport</li>
                                                <li class="list-content">Identification card</li>
                                            </ul>
                                        </div>

                                        <!-- 주의사항 -->
                                        <div style="padding: var(--gap-6); padding-bottom: 0;">
                                            <h2 class="info-form n-icon n-icon:caution">Caution</h2>
                                            <ul>
                                                <li class="list-content">Extensive walking</li>
                                                <li class="list-content">A lot of outdoor activities</li>
                                                <li class="list-content">This program allows changes in travel dates and
                                                    transfers to other individuals</li>
                                                <li class="list-content">In case of cancellation due to severe weather,
                                                    we will inform you in advance.</li>
                                                <li class="list-content">Cancellations due to severe weather will apply
                                                    if there is a forecast of 10mm
                                                    or more of rainfall per hour or temperatures below -5°C, according
                                                    to the Korea Meteorological Administration's forecast
                                                    for the scheduled travel day.</li>
                                            </ul>
                                        </div>

                                        <div style="padding: var(--gap-6)">
                                            <h2 class="info-form n-icon n-icon:reset">Cancellation and Refund Policy
                                            </h2>
                                            <ul>
                                                <li class="list-content">Notification made at least 24 hours prior to
                                                    the travel date : 100% full refund of the travel fee.</li>
                                                <li class="list-content">Notification made within 24 hours of the travel
                                                    date : Cancellation/refund is not available.</li>
                                                <li class="list-content">The date and time of cancellation requests are
                                                    based on the local time where the travel package takes place.</li>
                                            </ul>
                                        </div>
                                    </section>
                                </div>
                            </div>


                        </div>
                    </section>


                    <!--  리뷰  -->

                    <div id="review" class="id-container" style="border-bottom:0;">
                        <div class="background-color:base-1" style="padding: 0 var(--gap-6);">
                            <div class="content-header">
                                <span class="title" style="padding: 0 var(--gap-6);">Reviews</span>
                            </div>
                            <div class="details">
                                <section>
                                    <h1>리뷰 60</h1>

                                    <div
                                        style="display:flex; justify-content: space-between; align-items: center; padding: 0 var(--gap-6);">
                                        <span style="display:flex; align-items: center; width: auto; height: 30px;">60
                                            Results</span>
                                        <div style="display: flex;">
                                            <a href="" class="n-icon n-icon:arrow_swap">정렬 아이콘</a>
                                            <span>Latest</span>
                                        </div>
                                    </div>

                                    <!--   리뷰 게스트 댓글   -->
                                    <section class="review-container">
                                        <h1>리뷰 댓글과 답글</h1>

                                        <div class="review">

                                            <div class="star-wrapper">
                                                <span class="n-icon n-icon:star">별</span>
                                                <span class="n-icon n-icon:star">별</span>
                                                <span class="n-icon n-icon:star">별</span>
                                                <span class="n-icon n-icon:star">별</span>
                                                <span class="n-icon n-icon:star">별</span>
                                            </div>


                                            <div class="guest-info">
                                                <img class="profile-img" src="/public/image/face.jpg" alt="프로필이미지">
                                                <div style="flex-grow: 1;">
                                                    <div class="meatball-icon">
                                                        <span class="name">김준성</span>
                                                        <label class="n-icon n-icon:meatball"><span>똥똥똥</span><input
                                                                type="checkbox"></label>
                                                    </div>
                                                    <span class="date">2024-09-29</span>
                                                </div>
                                            </div>


                                            <div class="text">
                                                <p class="p-summary">If you wanna take a break from Varanasi you find a
                                                    great
                                                    location and hospitality in
                                                    this rooftop in the heart of the town. Food is really tasty and
                                                    staff
                                                    really
                                                    great!
                                                    If you wanna take a break from Varanasi you find a great location
                                                    and
                                                    hospitality in
                                                    this rooftop in the heart of the town. Food is really tasty and
                                                    staff
                                                    really
                                                    great!</p>
                                                <button>더보기</button>
                                            </div>


                                            <ul>
                                                <li><img src="/public/image/face.jpg" alt="reviewPhoto"></li>
                                                <li><img src="/public/image/face.jpg" alt="reviewPhoto"></li>
                                                <li><img src="/public/image/face.jpg" alt="reviewPhoto"></li>
                                                <li><img src="/public/image/face.jpg" alt="reviewPhoto"></li>
                                                <li><img src="/public/image/face.jpg" alt="reviewPhoto"></li>
                                                <li><img src="/public/image/face.jpg" alt="reviewPhoto"></li>
                                            </ul>


                                        </div>

                                        <!--  리뷰 호스트 답글   -->
                                        <div style="display: flex; justify-content: center;">
                                            <div class="reply">
                                                <div class="host-info">
                                                    <img class="profile-img" src="/public/image/face.jpg" alt="프로필이미지">
                                                    <div style="flex-grow: 1;">
                                                        <div class="meatball-icon">
                                                            <span class="name">김민지</span>
                                                            <label class="n-icon n-icon:meatball"><span>똥똥똥</span><input
                                                                    type="checkbox"></label>
                                                        </div>
                                                        <span class="date">2024-09-30</span>
                                                    </div>
                                                </div>
                                                <div class="text">
                                                    <p class="p-summary">If you wanna take a break from Varanasi you
                                                        find a
                                                        great
                                                        location and hospitality
                                                        in this rooftop in the heart of the town. Food is really tasty
                                                        and
                                                        staff
                                                        really
                                                        great! If you wanna take a break from Varanasi you find a great
                                                        location
                                                        and
                                                        hospitality in this rooftop in the heart of the town. Food is
                                                        really
                                                        tasty
                                                        and
                                                        staff really great!</p>
                                                    <button>더보기</button>
                                                </div>
                                            </div>
                                        </div>
                                    </section>
                                </section>
                                <div
                                    style="display: flex; justify-content: center; align-items: center; margin-bottom: var(--gap-6);">
                                    <a href="#" class="n-btn n-btn:hover"
                                        style="padding: var(--gap-3) var(--height-4p); border: 1px solid var(--color-base-8);">리뷰
                                        더
                                        보기</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- 찜 -->
                <div class="floating-bar">
                    <button class="n-btn picked"><span class="n-icon n-icon:bookmark_simple">찜</span></button>
                    <label class="n-btn book"><span>예약하기</span><input type="checkbox" id="bookCheckbox" /></label>
                </div>
            </section>
        </section>
    </main>
</template>

<style scoped>
.program-detail {
    font-size: var(--font-size-7);
    max-width: 700px;

    .thumbnail-wrapper,
    .map-img-wrapper {
        background-color: var(--color-base-3);

        .thumbnail-img {
            max-width: 550px;
        }
    }

}

/* n-icon */

.n-icon {
    --icon-size: var(--icon-size-4);
    margin-right: 3px;
}

.n-icon:before {
    width: var(--icon-size);
    background-color: var(--icon-color);
    margin-right: var(--gap-1);
}

.n-icon\:edit:before {
    --icon: url(/assets/image/icon/edit.svg);
}

.n-icon\:star:before {
    background-color: var(--color-yellow-1);
}

.n-icon\:credit_card:before {
    width: var(--icon-size);
}

.n-icon\:instagram:before {
    width: var(--icon-size);
}

.n-icon\:success-circle-green:before {
    background-color: var(--color-green-1);
}

.n-icon\:error:before {
    background-color: var(--color-red-1);
}

.n-icon\:placeholder:before {
    background-size: var(--icon-size);
}

.n-icon\:rectangle:after,
.n-icon\:rectangle:before {
    background-color: var(--color-base-5);
}

.n-icon\:clock:after,
.n-icon\:clock:before {
    background-color: var(--color-base-7);
}

/* n-deco */
.n-deco {
    --deco-gap: var(--gap-2);
}

/* btn */

.edit-btn {
    background-color: var(--color-sub-2);
    display: flex;
    align-items: center;
    padding: 2px;
    border-radius: 6px;

    button {
        cursor: pointer;
    }
}

.copy-btn {
    color: var(--color-sub-1);
    /* 글씨 색을 파란색으로 설정 */
    background-color: transparent;
    /* 배경색 투명 */
    cursor: pointer;
    /* 커서가 손 모양으로 변경 */
    font-size: var(--font-size-1) px;
    /* 글자 크기 설정 */
}

.copy-btn:hover {
    font-weight: bolder;
    /*color: var(--color-main-1); !* 마우스 오버 시 배경색 변경 *!*/
}

/* */

.cross-line {
    background-color: var(--color-base-2);
    height: 20px;
}

#intro,
#program-overview,
#course-information,
#meeting-location,
#inclusions,
#things-to-know,
#review {
    scroll-margin-top: 39px;
}

.item-wrapper {
    overflow: hidden;
    --bar-gap: 10px;
}

/* thumbnail */
.thumbnail-wrapper {
    display: flex;
    position: relative;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    gap: 16px;
    flex-shrink: 0;
    overflow: hidden;
    min-width: 85px;

    .thumbnail-img {
        max-width: 700px;
        width: 100%;
        height: auto;
    }

    .thumbnail-btn {
        cursor: pointer;
        --btn-bg-hover: var(--color-base-2);
        --btn-padding: 2px;
        align-items: center;
        border-radius: var(--border-radius-4, 8px);
        display: inline-flex;
        padding: var(--btn-padding);
        transition: border-color .3s, background-color .3s;
        user-select: none;
        white-space: nowrap;

        background-color: rgba(0, 0, 0, 0.3);
        position: absolute;
        top: 50%;
        margin: 0;
        gap: 0;
        width: var(--icon-size);

        .n-icon {
            --icon-size: var(--icon-size-5);
            margin: 0;
        }

        .n-icon:before {
            background-color: var(--color-base-1);
        }
    }

    .thumbnail-btn\:left {
        left: 3%;
    }

    .thumbnail-btn\:right {
        right: 3%;
    }

    .thumbnail-btn\:num {
        color: var(--color-base-1);
        cursor: none;
        top: auto;
        bottom: 10px;
        right: 10px;
    }
}

/* categories */
.categories {
    display: flex;
    padding: var(--gap-7) var(--gap-5);
    padding-bottom: 0;
}

.category {
    margin-right: var(--gap-3);
    border-radius: var(--border-radius-3);
    --btn-border-color: var(--color-base-9);
    --btn-padding: 5px 12px;
}



/* title + reviews + price */
.info {
    .title {
        color: var(--color-base-9);
        font-size: var(--font-size-11);
        font-weight: bold;
        padding: var(--gap-3) var(--gap-5);
    }

    .rating-review {
        display: flex;
        flex-grow: 1;
        padding: var(--gap-3) var(--gap-7) var(--gap-8) var(--gap-7);
        gap: var(--gap-2);
        align-items: center;
    }

    .price {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 25px;
        margin-left: auto;
    }
}


/* intro */
.intro {

    .title {
        color: var(--color-base-9);
    }

    .list-container {
        display: block;
        color: var(--color-base-9);
        padding: var(--gap-4) 0;

        li {
            margin-bottom: var(--gap-1);
        }
    }

    .list-content {
        display: flex;
        font-size: var(--font-size-6);
        align-items: center;
        width: auto;
        height: 43px;
        justify-content: left;
        list-style: none;
    }

    li {
        color: var(--color-base-9);
        padding: var(--gap-4) 0;
    }

    .n-icon {
        color: var(--color-base-9);
    }

    .n-icon:before {
        width: var(--icon-size);
        background-color: var(--icon-color);
    }
}


.content {
    background-color: var(--color-base-2);
    font-size: var(--font-size-6);

    .content-header {
        padding: var(--gap-6);
        display: flex;


        .title {
            align-items: center;
            font-size: var(--font-size-8);
            font-weight: bold;
        }
    }

    body {
        color: var(--color-base-9);
        display: block;
        align-items: center;
        align-content: center;

        padding: var(--gap-6);
    }

    h1 {
        display: none;
    }

    .id-container {
        margin-top: var(--gap-6);
    }

    .program-container {

        /* 글 내용 */

        .text {
            padding: 0 var(--gap-6);

            .p-summary {
                display: -webkit-box;
                padding: 0 var(--gap-1);
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 3;
                overflow: hidden;
                text-overflow: ellipsis;
                line-height: 1.5;
            }

            .p-full {
                display: block;
            }


            /* 더보기 버튼 */

            button {
                width: var(--width-10p);
                background-color: var(--color-base-1);
                justify-content: center;
                padding: 25px 0;
                cursor: pointer;
                text-decoration: underline;
                text-decoration-color: var(--color-base-9);
                /* 밑줄 색상 */
                border-radius: 5px;
            }

            button:hover {
                font-weight: bold;
            }

        }
    }

    .map-img-wrapper {
        display: flex;
        position: relative;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        gap: 16px;
        flex-shrink: 0;
        overflow: hidden;
        min-width: 85px;
        aspect-ratio: 2 / 1;

        .map-img {
            width: 100%;
        }
    }
}


.program {
    .title {
        color: var(--color-base-9);
    }

    section {
        .title {
            color: var(--color-base-9);
            font-size: var(--font-size-8);
            font-weight: bold;
            padding: var(--gap-2);
        }

        .info-container {
            display: block;
            color: var(--color-base-9);
            padding: 0;
        }

        .list-container {
            display: block;
            color: var(--color-base-9);
            padding-bottom: var(--gap-5);

            li {
                margin-bottom: var(--gap-3);
            }
        }

        ul {
            list-style: none;
            /* 기본 점을 없앰 */
            padding-left: var(--gap-3);
        }

        .list-content {
            font-size: var(--font-size-6);
            align-items: center;
            width: auto;
        }

        .list-content::before {
            content: "•";
            /* 기본 점 없애고 커스텀 점 */
            display: inline-block;
            width: 1em;
            /* 점과 텍스트 사이의 간격 */
            padding-left: var(--gap-3);
            margin-left: -1.5em;
            /* 점을 왼쪽으로 이동 */
        }
    }


    h2 {
        font-size: var(--font-size-6);
        font-weight: bold;
        width: auto;
        height: 30px;
    }

    li {
        color: var(--color-main-2);
        padding: var(--gap-3);
    }
}


.info-container {
    display: flex;
    margin-bottom: var(--gap-6);
    justify-content: space-between;
}

.info-input {
    font-size: var(--font-size-6);
    display: flex;
    align-items: center;
    width: auto;
    justify-content: left;
}

/* folding */

.details {
    margin: 0;
    padding: var(--gap-4);

    .map-img {
        max-width: 700px;
        width: 100%;
        height: auto;
    }
}

/* 하단 플로팅 바 */
.floating-bar {
    display: flex;
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 60px;
    justify-content: center;
    align-items: center;
    gap: var(--gap-3);
    padding: var(--gap-6) var(--gap-3);
    background-color: var(--color-base-1);
    z-index: var(--modal-z-index);

    .picked {
        background-color: var(--color-base-2);
        height: 65%;

        .n-icon {
            margin-right: 0;
        }

        .n-icon\:bookmark_simple:before {
            --icon-size: var(--icon-size-4);
        }
    }


    .book {
        margin-left: var(--gap-2);
        background-color: var(--color-sub-1);
        color: var(--color-base-1);
        width: var(--width-9p);
        height: 65%;

        span {
            z-index: 1;
        }
    }

    input[type="checkbox"] {
        opacity: 0;
        position: absolute;
    }
}

/* 반응형 */

@media (min-width: 768px) {
    main {
        display: flex;
        flex-direction: column;
        align-items: center;

        .main-wrapper {
            display: flex;
            max-width: 1060px;
            position: relative;
            height: 100%
        }

        .program-detail {
            max-width: 700px;
            width: 100%;
            padding: 0 16px;
        }


        .thumbnail-img {
            background-size: contain;
        }

        .floating-bar {
            display: none;
        }


    }
}
</style>