<template>
  <section>

    <div v-if="props.expanded" class="divider"></div>
    <div v-if="props.expanded" class="course-moving-wrapper">
      <div class="d:flex al-items:center">
        <p class="p:1">이동수단</p>
        <label>
          <select class="input-select" v-model="route.transportationId">
            <option :value="null">선택 안 함</option>
            <option v-for="t in transportationIds" :key="t.id" :value="t.id">
              {{ t.name }}
            </option>
          </select>
        </label>
      </div>
      <div class="d:flex al-items:center">
        <p class="p:1">이동시간</p>
        <label>
          <select class="input-select" v-model="route.transportationDuration">
            <option :value="null">선택 안 함</option>
            <option value="5">5분</option>
            <option value="10">10분</option>
            <option value="15">15분</option>
            <option value="20">20분</option>
            <option value="25">25분</option>
            <option value="30">30분</option>
          </select>
        </label>
      </div>
    </div>
    <div v-if="props.expanded" class="divider"></div>

    <div class="course-card">
      <div class="card-header">
        <div class="title">{{ props.order > 1 ? `${props.order}` : '출발지' }}</div>
        <div v-if="props.order > 1" @click.prevent="removeRoute"
              class="n-icon n-icon:exit">
        </div>
      </div>

      <div class="course-input-area">

        <div class="input-with-icon">
          <img src="/public/image/pen.svg" alt="아이콘">
          <label>
            <input type="text" class="input-text" placeholder="제목" v-model="route.title">
          </label>
          <p v-show="titleValidation()" class="warning">** 3자 이상 60자 이하이어야 합니다 **</p>
        </div>

        <div class="input-with-icon">
          <img src="/public/image/pin.svg" alt="아이콘">
          <label>
            <input type="text" class="input-text" placeholder="주소" v-model="route.address">
          </label>
          <p v-show="addressValidation()" class="warning">** 3자 이상 60자 이하이어야 합니다 **</p>
        </div>

        <div class="input-with-icon">
          <label>
            <img src="/public/image/pen.svg" alt="아이콘">
            <input type="text" class="input-text" placeholder="장소 설명" v-model="route.description">
          </label>
          <p v-show="descriptionValidation()" class="warning">** 3자 이상 60자 이하이어야 합니다 **</p>
        </div>

        <div class="course-select-time">
          <div class="time-label">
            <img src="/public/image/pin.svg" alt="아이콘">
            <span>시작시각</span>
          </div>
          <div v-if="props.order === 1" class="d:flex">
            <!-- 출발지: 부모에서 전달된 값 사용 -->
            <div class="time-select-wrapper">
              <input type="text" class="input-select readonly" :value="startTimeHour" readonly/>
              <p>시</p>
            </div>
            <div class="time-select-wrapper">
              <input type="text" class="input-select readonly" :value="startTimeMinute" readonly/>
              <p>분</p>
            </div>
          </div>
          <div v-else class="d:flex min-width:5">
            <!-- 경유지: 선택 가능 -->
            <div class="time-select-wrapper">
              <label>
                <select id="start-hour" class="input-select" name="start-hour" v-model="route.startTimeHour">
                  <option value="00">00</option>
                  <option value="01">01</option>
                  <option value="02">02</option>
                  <option value="03">03</option>
                  <option value="04">04</option>
                  <option value="05">05</option>
                  <option value="06">06</option>
                  <option value="07">07</option>
                  <option value="08">08</option>
                  <option value="09">09</option>
                  <option value="10">10</option>
                  <option value="11">11</option>
                  <option value="12">12</option>
                  <option value="13">13</option>
                  <option value="14">14</option>
                  <option value="15">15</option>
                  <option value="16">16</option>
                  <option value="17">17</option>
                  <option value="18">18</option>
                  <option value="19">19</option>
                  <option value="20">20</option>
                  <option value="21">21</option>
                  <option value="22">22</option>
                  <option value="23">23</option>
                </select>
              </label>
              <p>시</p>
            </div>
            <div class="time-select-wrapper">
              <label>
                <select id="start-minute" class="input-select" name="start-minute" v-model="route.startTimeMinute">
                  <option value="00">00</option>
                  <option value="10">10</option>
                  <option value="20">20</option>
                  <option value="30">30</option>
                  <option value="40">40</option>
                  <option value="50">50</option>
                </select>
              </label>
              <p>분</p>
            </div>
          </div>
        </div>

        <div class="duration-wrapper">
          <div class="time-label">
            <img src="/public/image/pin.svg" alt="아이콘">
            <span>소요시간</span>
          </div>
          <div class="duration-pm-wrapper">
            <button class="n-btn btn-minus" @click.prevent="minusDuration">-</button>
            <input type="number" id="duration" class="duration-input no-spinner" name="duration" min="0"
                   v-model="route.duration">
            <button class="n-btn btn-plus" @click.prevent="plusDuration">+</button>
          </div>
          <span>분</span>
        </div>

      </div>
    </div>

  </section>
</template>

<script setup>

import {onMounted, defineProps, defineEmits, reactive, watch} from "vue";
import axios from "axios";

const isExpanded = ref(false); // 표시 상태
const toggleExpanded = () => {
  isExpanded.value = !isExpanded.value;
};


onMounted(() => {
  fetchTransportationIds();
});

const transportationIds = ref([]);

//  부모가 자식한테 전달할 때
//  defineProps : 부모 컴포넌트로부터 전달된 props를 사용하기 위한 함수
//  부모 컴포넌트가 order라는 데이터 전달 (oreder : 몇번째 route컴포넌트인지)
const props = defineProps(['order', 'startTimeHour', 'startTimeMinute', 'isStartPoint', 'expanded']);

//  자식이 부모한테 전달할 때
//  defineEmits : 자식 컴포넌트가 부모 컴포넌트로 이벤트를 전달하는 함수
//  emit('updateRoute', data) 형태로 이벤트 발생
//  첫 번째 인자 : updateRoute라는 이벤트
//  두 번째 인자 : 부모에게 전달할 데이터
const emit = defineEmits(['updateRoute', 'validationPassed', 'removeRoute']);


//  reactive : 반응형 객체로 만드는 함수
//             객체 값이 변화하면 관련된 UI를 업데이트.
//  route라는 반응형 객체 안에 여러 데이터를 저장
const route = reactive({
  title: '',
  order: props.order, // 현재 길이를 기준으로 순서를 설정
  address: '',
  description: '',
  duration: 0,
  transportationId: null,
  transportationDuration: null
});



const removeRoute = () => {
  emit("removeRoute", props.order - 1); // 부모에게 index 전달
};

//===================== Fetch Functions ====================
const fetchTransportationIds = async () => {
  const response = await axios.get("http://localhost:8083/api/v1/transportation");
  transportationIds.value = response.data;
}

//  부모 컴포넌트로 데이터를 보내는 함수
//  emit으로 updateRoute 이벤트를 발생시킴
//  그 안에 현재 route 객체를 전달
//  { ...route } : route 객체의 복사본을 만들어 데이터를 전달하는 역할
const updateParent = () => {
  if (route.title || route.address || route.description || route.duration > 0 || route.transportationId || route.transportationDuration > 0) {
    emit('updateRoute', route.order, {...route});
  }
};


//  watch : 특정 데이터를 감시하는 함수
//  route 객체가 변경될 때마다 자동으로 updateParent 함수를 호출
//  { deep: true } 옵션 : route 객체의 하위 속성(내부 속성들)까지 감시
watch(route, () => {
  updateParent();
  checkValidation();
}, {deep: true});

const minusDuration = () => {
  if (route.duration == 0) {
    return;
  }
  route.duration -= 10
}

const plusDuration = () => {
  if (route.duration == 600) {
    return;
  }
  route.duration += 10
}

//====== Validation Checking Functions =========================

const titleValidation = () => {
  return ((route.title.length > 60) || (route.title.length > 0 && route.title.length < 3));
}

const addressValidation = () => {
  return ((route.address.length > 60) || (route.address.length > 0 && route.address.length < 3));
}

const descriptionValidation = () => {
  return ((route.description.length > 60) || (route.description.length > 0 && route.description.length < 3));
}

// 유효성 검사 통과 확인 후 부모에 이벤트 발생
// 유효성 검사 통과 확인 후 부모에 이벤트 발생
const checkValidation = () => {
  if (!titleValidation() && !addressValidation() && !descriptionValidation()) {
    emit('validationPassed', props.order - 1, true); // props.order - 1로 index 값 전달
  } else {
    emit('validationPassed', props.order - 1, false); // 유효성 검사 실패 시 false 전달
  }
};


</script>

<style scoped>
.intro,
.detail,
.course,
.inclusion,
.caution,
.images-upload-container {
  display: none;
  scroll-margin-top: 39px;
}

/*현재 타겟 섹션만 보이게 설정*/
.intro:target,
.detail:target,
.course:target,
.inclusion:target,
.caution:target,
.images-upload-container:target {
  display: block;
}

/* 경고 문구 스타일 */

.warning {
  color: red;
  font-size: 0.9rem;
}

.n-bar-underline-create {
  display: flex;
  flex-direction: column;
  overflow-x: auto;
  position: sticky;
  top: 0;
  /* 상단에서 고정되는 위치를 지정 */
  z-index: 30;
  /* 다른 요소 위에 나타나도록 설정 */
  background-color: white;
  /* 스크롤할 때 배경 유지 */

  h1 {
    display: none;
  }

  .item-wrapper {
    --bar-gap: 16px;
    align-items: center;
    justify-content: space-between;
    display: inline-flex;
    gap: var(--bar-gap);

    > * {
      position: relative;
    }

    /*=============== hover, active상태 ================================*/

    > *::after {
      content: "";
      width: 100%;
      height: 3px;
      background-color: var(--color-main-4);
      position: absolute;
      left: 0;
      bottom: 0;
      z-index: 1;

      visibility: hidden;
    }

    > *:hover::after {
      background-color: var(--color-main-4);
      visibility: visible;
    }

    > .active::after {
      background-color: var(--color-main-1);
      visibility: visible;
    }

    > .active:hover::after {
      background-color: var(--color-main-1);
      visibility: visible;
    }
  }
}

.n-bar-underline-create::after {
  content: "";
  width: 100%;
  height: 1px;
  background-color: var(--color-base-5);
  /*position: absolute;*/
  /*left: 0;*/
  /*bottom: 0;*/
  /*z-index: 1;*/
}


/* 섹션 기본 스타일 */
.intro {
  padding: 16px;
  /*background-color: #f9f9f9;*/
  border-radius: 10px;
  /*max-width: 600px;*/
  margin: 0 auto;

  /* 폼 그룹 스타일 */

  .form-group {
    margin-bottom: 20px;
  }

  .label-wrapper {
    margin-bottom: 5px;
  }

  .form-label {
    font-size: 1rem;
    font-weight: bold;
    display: block;
    margin-bottom: 5px;
  }

  /* 인풋 필드 스타일 */

  .input-text,
  .input-textarea {
    width: 100%;
    padding: 10px;
    font-size: 1rem;
    border: 1px solid #ccc;
    border-radius: var(--border-radius-2);
    margin-top: 5px;
    box-sizing: border-box;
  }

  .input-text:focus,
  .input-textarea:focus {
    border-color: #0056b3;
    outline: none;
  }

  /* 텍스트 길이 제한 표시 스타일 */

  .char-counter {
    font-size: 0.9rem;
    color: #666;
    text-align: right;
    margin-top: 5px;
  }


  .category-note {
    color: red;
    font-size: 0.9rem;
  }

}

.detail {
  padding: 16px;
  /*background-color: #f9f9f9;*/
  border-radius: 10px;
  /*max-width: 600px;*/
  margin: 0 auto;

  .section-title {
    font-size: 1.5rem;
    font-weight: bold;
    margin-bottom: 20px;
    color: #333;
  }

  /* 폼 그룹 스타일 */

  .form-group {
    margin-bottom: 20px;
  }

  .form-label {
    font-size: 1rem;
    font-weight: bold;
    margin-bottom: 5px;
    align-content: center;
    align-items: center;
    padding: 0 10px 0 0;
  }

  /* 선택 박스 스타일 */

  .input-select {
    width: 26%;
    padding: 10px;
    font-size: 1rem;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-sizing: border-box;
    margin-top: 5px;
  }

  .input-text {
    /*flex: 1; !* 입력 필드가 남은 공간을 차지하게 설정 *!*/
    width: 30%;
    padding: 0 5px;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-sizing: border-box;
  }

  /* 시간 선택 박스 스타일 */


  .time-unit {
    margin: 0 10px;
    font-size: 1rem;
    color: #333;
  }

  /* 인원 수 카운터 스타일 */

  .counter-wrapper {
    display: flex;
    align-items: center;
  }

  .counter-btn {
    background-color: #001f3f;
    color: white;
    border: none;
    padding: 10px;
    cursor: pointer;
    border-radius: 5px;
  }

  .counter-input {
    width: 50px;
    text-align: center;
    font-size: 1rem;
    margin: 0 10px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }

  .people-unit {
    font-size: 1rem;
    color: #333;
    padding: 0 10px;
  }

  /* 비용 입력 스타일 */

  .currency-unit {
    margin-left: 10px;
    font-size: 1rem;
    align-items: center;
    align-content: center;
  }
}

.course {
  padding: 16px;
  /*background-color: #f9f9f9;*/
  margin: 0 auto;

  h1 {
    font-size: 16px;
    font-weight: bold;
    padding-bottom: 10px;
  }
}

.course-card {
  display: flex;
  flex-direction: column;
  border-radius: 11px;
  border: 1px solid var(--color-base-7);
  background-color: var(--color-base-4);
  /*padding: 16px; !* 카드 내부 여백 추가 *!*/
}

.course-input-area {
  padding: 12px;
}

.card-header {
  background-color: var(--color-main-1);
  color: white;
  text-align: center;
  -webkit-border-top-left-radius: 10px;
  -webkit-border-top-right-radius: 10px;
  padding: 8px 0;
  //font-size: 20px;
  font-weight: bold;
  display: flex; /* flexbox 활성화 */
  align-items: center; /* 세로 가운데 정렬 */
  justify-content: space-between;
  .title{
    margin: 0 auto;
  }
  .n-icon\:exit {
    background-color: var(--color-main-3);
    transform: translateX(-10px); /* 왼쪽으로 10px 이동 */
    cursor: pointer;
  }
}


.input-with-icon {
  position: relative;
  display: inline-block;
  width: 100%;
  margin-top: 10px;
}

.input-with-icon img {
  position: absolute;
  left: 10px;
  top: 50%;
  transform: translateY(-50%);
  width: 16px;
  /* 아이콘 크기 */
  height: 16px;
  pointer-events: none;
  /* 클릭 시 이미지가 아닌 입력 필드에 포커스 */
}

.input-with-icon input {
  padding-left: 40px;
  /* 아이콘에 맞게 padding 추가 */
  height: 40px;
  border: 1px solid #ccc;
  border-radius: 10px;
  font-size: 1rem;
}

.input-text {
  width: 100%;
  /*padding: 10px;*/
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: var(--border-radius-2);
  /*margin-top: 5px;*/
  box-sizing: border-box;
}

.input-text:focus {
  border-color: #0056b3;
  outline: none;
}

.course-select-time {
  display: flex;
  align-items: center;
  gap: 10px;
  /* 아이콘과 텍스트 사이 간격 */
  margin-top: 10px;
  /* 아래 간격 추가 */
  justify-content: space-between;
  padding: 0 10px;
  width: 100%;
  font-size: 1rem;
}

.time-label {
  display: flex;
  /*flex-grow: 1;*/
  gap: 10px;
  width: 70%
}

.time-select-wrapper {
  display: flex;
  flex-grow: 1;
  width: 100%;
  gap: 10px;
  align-items: center;

  label {
    flex-grow: 1;
  }
}

.duration-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
  /* 아이콘과 텍스트 사이 간격 */
  /*margin-bottom: 20px; !* 아래 간격 추가 *!*/
  justify-content: space-between;
  padding: 0 10px;
  width: 100%;
  font-size: 1rem;
}

.duration-pm-wrapper {
  display: flex;
  align-items: center;
  position: relative;
  width: 100%;
  margin-top: 10px;
}

.duration-input {
  width: 100%;
  padding: 10px 40px;
  /* 왼쪽, 오른쪽에 버튼을 배치하기 위한 패딩 */
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: var(--border-radius-2);
  box-sizing: border-box;
  text-align: center;
  /* 텍스트 중앙 정렬 */
}

.btn-minus {
  position: absolute;
  left: 10px;
  /* 왼쪽에 배치 */
  background-color: var(--color-main-2);
  border: none;
  padding: 8px 12px;
  color: white;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
}

.btn-plus {
  position: absolute;
  right: 10px;
  /* 오른쪽에 배치 */
  background-color: var(--color-main-2);
  border: none;
  padding: 8px 12px;
  color: white;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
}

.btn-minus:focus,
.btn-plus:focus {
  outline: none;
}

.course-moving-wrapper {
  display: flex;
  border: 1px solid var(--color-base-7);
  background-color: #E4F7FF;
  border-radius: 10px;
  align-items: center;
  justify-content: center;
  /* 좌우로 균등하게 배치 */
  gap: 10px;
  padding: 10px 20px;
  margin-bottom: 10px;
}


.inclusion {
  padding: 16px;
  /*background-color: #f9f9f9;*/
  border-radius: 10px;
  /*max-width: 600px;*/
  margin: 0 auto;

  .input-text,
  .input-textarea {
    width: 100%;
    padding: 10px;
    font-size: 1rem;
    border: 1px solid #ccc;
    border-radius: var(--border-radius-2);
    margin-top: 5px;
    box-sizing: border-box;
    background-color: #f9f9f9;
  }
}

.caution {
  padding: 16px;
  /*background-color: #f9f9f9;*/
  border-radius: 10px;
  /*max-width: 600px;*/
  margin: 0 auto;

  .input-text,
  .input-textarea {
    width: 100%;
    padding: 10px;
    font-size: 1rem;
    border: 1px solid #ccc;
    border-radius: var(--border-radius-2);
    margin-top: 5px;
    box-sizing: border-box;
    background-color: #f9f9f9;
  }
}

.images-upload-container {
  padding: 16px;
  /*background-color: #f9f9f9;*/
  border-radius: 10px;
  margin: 0 auto;


  label input[type="file"] {
    display: none;
  }

  h1 {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
  }

  header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 5px 0;

    label {
      border: none;
      background-color: var(--color-sub-2);
    }
  }
}

.upload-images {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  margin-top: 30px;
}

.image-container {
  display: flex;
  flex-direction: column;
  padding: 10px;
  position: relative;
  width: 100%;
  height: 100%;
}


.image {
  border: 1px solid;
  /*background-color: #d3d3d3;*/
  display: flex;
  box-sizing: border-box;
  width: 100%;
  aspect-ratio: 1 / 1;
  position: relative;
  justify-content: center;
  align-items: center;

  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
    display: block;
  }

  button {
    position: absolute;
    background-color: #001F3F;
    top: 10px;
    right: 10px;
    color: white;
    border: none;
    border-radius: 50%;
    width: 20px;
    height: 20px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.image-title-main,
.image-title {
  /*position: relative;*/
  display: flex;
  white-space: nowrap;
  justify-content: flex-start;
  border: 1px solid;
  border-radius: 15px;
  margin-bottom: 10px;
  margin-top: 10px;
  width: 25%;

  > span {
    display: block;
    width: 100%;
    text-align: center;
    padding: 3px 5px;
  }

}

.image-title-main {
  background-color: #001F3F;
  color: #fff;
}

.image {
  background-color: var(--color-base-3);
}

.n-btn-color\:sub-1 {
  background-color: var(--color-sub-1);
  /*border-color: var(--color-sub-1);*/
  color: var(--color-base-1)
}

.n-btn-color\:main-2 {
  background-color: var(--color-main-2);
  /*border-color: var(--color-sub-1);*/
  color: var(--color-base-1)
}

/* 버튼 스타일 */
.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

textarea {
  resize: vertical;
  /* 세로 방향만 크기 조절 가능 */
}

.input-select {
  width: 100%;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  /*box-sizing: border-box;*/
}

.divider {
  width: 1px;
  height: 24px;
  /* 선의 높이 */
  background-color: transparent;
  /* 배경색을 투명으로 */
  border-left: 3px dotted var(--color-base-9);
  /* 점선 설정 */
  margin: 0 auto;
}


@media (min-width: 768px) {

  .intro,
  .detail,
  .course,
  .inclusion,
  .caution,
  .images-upload-container {
    scroll-margin-top: 100px;
  }


  main {
    display: flex;
    align-items: flex-start;
    /* 목차와 폼이 위쪽에 정렬되도록 설정 */
    padding: 0 16px;
    /* 좌우 패딩 추가 */
    margin: 0 auto;
    /* 가운데 정렬 */
    max-width: 1096px;
    /* 최대 너비를 1096px로 고정 */
    width: 100%;
    /* 화면에 맞춰 너비 설정 */
  }

  .n-bar-underline-create {
    display: flex;
    flex-direction: column;
    /* 목차를 세로로 배치 */
    align-items: flex-start;
    /* 왼쪽 정렬 */
    width: 250px;
    /* 목차 너비 고정 */
    position: relative;
    height: auto;
    margin-left: 20px;
    margin-top: 20px;

    h1 {
      display: flex;
      font-weight: bold;
      padding: 10px 0;
    }
  }

  .n-bar-underline-create::after {
    display: none;
    /*position: absolute;*/
    /*left: 0;*/
    /*bottom: 0;*/
    /*z-index: 1;*/
  }


  .n-bar-underline-create .item-wrapper {
    display: flex;
    flex-direction: column;
    /* 항목들을 세로로 배치 */
    align-items: flex-start;
    /* 항목들도 왼쪽 정렬 */
    width: 100%;
  }

  .item-wrapper {
    position: relative;
    /* 자식 요소의 위치를 상대적으로 설정 */
    padding: 16px;
    border: 1px solid #ccc;
    border-radius: 10px;
  }

  .item-wrapper > * {
    position: relative;
    align-items: center;
    display: flex;
  }

  .item-wrapper > *::before {
    content: "";
    width: 5px;
    /* 줄의 너비를 3px로 설정 */
    height: 86%;
    /* 줄의 높이를 항목 전체로 설정 */
    border-radius: 10px;
    background-color: var(--color-main-4);
    /* 줄 색상 (검정색이나 다크 색상) */
    position: absolute;
    left: 0;
    /* 줄을 항목의 왼쪽에 배치 */
    visibility: hidden;
  }

  .item-wrapper > *:hover::before {
    /*display: none;*/
    visibility: visible;
  }

  /* 활성화된 상태에서 줄 색상 변경 */
  .item-wrapper > .active::before {
    background-color: var(--color-main-1);
    /* 활성화된 항목의 줄 색상 (원하는 색상으로 변경 가능) */
    visibility: visible;
  }


  .item-wrapper > *::after {
    display: none;
    /*content: "";*/
    /*width: 3px; !* 줄의 너비를 3px로 설정 *!*/
    /*height: 100%; !* 줄의 높이를 항목 전체로 설정 *!*/
    /*background-color: var(--color-main-4); !* 줄 색상 *!*/
    /*position: absolute;*/
    /*left: -10px; !* 줄을 항목의 왼쪽에 배치 *!*/
    /*top: 0; !* 항목의 상단에서 줄이 시작되도록 설정 *!*/
    /*visibility: hidden; !* 기본적으로 줄을 숨김 *!*/
  }

  /* hover 시 왼쪽에 줄이 나타나도록 설정 */
  .item-wrapper > *:hover::after {
    display: none;
    /*visibility: visible;*/
  }

  /* active 상태에서 줄 색상을 변경 */
  .item-wrapper > .active::after {
    display: none;
    /*background-color: var(--color-main-1); !* 활성화된 항목의 줄 색상 *!*/
    /*visibility: visible;*/
  }


  form {
    flex-grow: 1;
    /* 나머지 공간을 폼이 차지하도록 설정 */
    width: 100%;
    /* 화면에 맞춰 폼의 너비 설정 */
    padding: 16px;
    /* 폼에 적당한 패딩 추가 */
  }

  .upload-images {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    margin-top: 30px;

  }
}

@media (max-width: 768px) {
  .item-wrapper {
    display: flex;
    justify-content: center;
    gap: 20px;
    /* 원하는 gap 크기 설정 (간격 크기) */
    padding: 0;
    /* ul 기본 패딩 제거 */
    margin: 0;
    /* ul 기본 마진 제거 */

    li {
      flex: 1;
      text-align: center;
      padding: 10px;
    }
  }
}

/*========== input 태그의 증감버튼 없애기 ==========*/
/* Chrome, Safari, Edge, Opera */
input[type="number"].no-spinner::-webkit-outer-spin-button,
input[type="number"].no-spinner::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* Firefox */
input[type="number"].no-spinner {
  -moz-appearance: textfield;
}
</style>