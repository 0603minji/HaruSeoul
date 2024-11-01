<script setup>

import {onMounted, ref} from "vue";
import axios from "axios";


const categories = ref([]);


//================Fetch Function==========
const fetchCategories = async () => {
  const response = await axios.get("http://localhost:8080/api/v1/categories");

  categories.value = response.data;

  // console.log(categories.value);
}

//=======================LifeCycle Functions==========
onMounted(() => {

  if (!window.location.hash) {
    window.location.hash = '#intro';
  }

  fetchCategories();
})
</script>
<template>
  <main>
    <nav class="n-bar-underline-create">
      <h1>목차</h1>
      <ul class="item-wrapper padding-x:6 justify-content:flex-start">
        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"><a href="#intro" class="">소개</a></li>
        <li class="n-btn:hover n-btn n-btn-border:none n-btn-radius:0"><a href="#detail" class="">세부사항</a></li>
        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"><a href="#course" class="">코스</a></li>
        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"><a href="#inclusion" class="">포함사항</a>
        </li>
        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"><a href="#caution" class="">추가정보</a></li>
        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"><a href="#image" class="">사진</a></li>
      </ul>
    </nav>

    <form method="post" action="" enctype="multipart/form-data">
      <section id="intro" class="intro">
        <h1 class="d:none">소개</h1>

        <div class="form-group">
          <div class="label-wrapper">
            <label for="title" class="form-label">제목</label>
          </div>
          <input class="input-text" id="title" type="text" name="title" placeholder="Enter title">
          <div class="char-counter">
            <span>0/60</span>
          </div>
        </div>

        <div class="form-group">
          <div class="label-wrapper">
            <label for="description" class="form-label">상세 설명</label>
          </div>
          <textarea class="input-textarea" id="description" name="description" rows="8" cols="50"
                    placeholder=""></textarea>
          <p class="char-counter">0/1000</p>
        </div>
        <div class="form-group">
          <div class="form-label">카테고리</div>
          <div class="d:flex flex-wrap:wrap gap:2 bg-color:base-2 bd-radius:4 p:2 jc:space-between">
            <label v-for="c in categories" :key="c.id" class="d:flex al-items:center w:1/3">
              <input type="checkbox" :value="c.id" class="mr:2">{{ c.name }}
            </label>
          </div>
        </div>
        <p class="category-note">**최대 2개까지 선택 가능합니다.</p>

        <div class="button-group">
          <button type="submit" class="n-btn n-btn-bg-color:main">임시저장 후 나가기</button>
          <a class="n-btn n-btn-bg-color:main" href="#detail">다음</a>
        </div>
      </section>


      <section id="detail" class="detail">
        <h1 class="d:none">개요</h1>

        <div class="form-group">
          <div class="form-label">진행언어</div>

          <select name="language" class="input-select">
            <option>English</option>
            <option>Japanese</option>
            <option>Chinese</option>
          </select>

        </div>
        <div class="form-group">
          <div class="form-label">시작 시각</div>
          <div class="time-select-wrapper">
            <select id="" name="" class="input-select">
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
              <option value="16">17</option>
              <option value="18">18</option>
              <option value="19">19</option>
              <option value="20">20</option>
              <option value="21">21</option>
              <option value="22">22</option>
              <option value="23">23</option>
              <option value="24">24</option>
            </select>
            <p class="time-unit">시</p>
            <select id="" name="" class="input-select">
              <option value="00">00</option>
              <option value="10">10</option>
              <option value="20">20</option>
              <option value="30">30</option>
              <option value="40">40</option>
              <option value="50">50</option>
            </select>
            <p class="time-unit">분</p>
          </div>
        </div>
        <div class="form-group">
          <div class="form-label">종료 시각</div>
          <div class="time-select-wrapper">
            <select id="" name="" class="input-select">
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
              <option value="16">17</option>
              <option value="18">18</option>
              <option value="19">19</option>
              <option value="20">20</option>
              <option value="21">21</option>
              <option value="22">22</option>
              <option value="23">23</option>
              <option value="24">24</option>
            </select>
            <p class="time-unit">시</p>
            <select id="" name="" class="input-select">
              <option value="00">00</option>
              <option value="10">10</option>
              <option value="20">20</option>
              <option value="30">30</option>
              <option value="40">40</option>
              <option value="50">50</option>
            </select>
            <p class="time-unit">분</p>
          </div>
        </div>
        <div class="">
          <div class="d:flex form-group gap:6">
            <div class="form-label">최대 인원</div>
            <div class="counter-wrapper">
              <button class="n-btn n-btn-color:main-2">-</button>
              <input type="number" name="max-count" class="counter-input" min="1" max="5" value="0">
              <button class="n-btn n-btn-color:main-2">+</button>
              <p class="people-unit">명</p>
            </div>
          </div>

          <div class="d:flex form-group gap:6">
            <div class="form-label">최소 인원</div>
            <div class="counter-wrapper">
              <button class="n-btn n-btn-color:main-2">-</button>
              <input type="number" name="min-count" class="counter-input" min="1" max="5" value="0">
              <button class="n-btn n-btn-color:main-2">+</button>
              <p class="people-unit">명</p>
            </div>
          </div>

          <p class="warning p-bottom:4">* 호스트 본인을 제외한 인원 수를 입력하세요.</p>

        </div>

        <div class="form-group d:flex">
          <div class="form-label">총 비용</div>
          <input type="number" name="cost" class="input-text" min="0" max="1000000">
          <p class="currency-unit">원</p>
        </div>

        <div class="button-group">
          <div><a class="n-btn n-btn-bg-color:main" href="#intro">이전</a></div>
          <button type="submit" class="n-btn n-btn-bg-color:main">임시저장 후 나가기</button>
          <div><a class="n-btn n-btn-bg-color:main" href="#course">다음</a></div>
        </div>

      </section>
      <section id="course" class="course">
        <h1>코스</h1>

        <div class="course-card">
          <div class="card-header">출발지</div>

          <div class="course-input-area">

            <div class="input-with-icon">
              <img src="/public/image/pen.svg" alt="아이콘">
              <label>
                <input type="text" class="input-text" placeholder="제목">
              </label>
            </div>

            <div class="input-with-icon">
              <img src="/public/image/pen.svg" alt="아이콘">
              <label>
                <input type="text" class="input-text" placeholder="주소">
              </label>
            </div>

            <div class="input-with-icon">
              <label>
                <img src="/public/image/pin.svg" alt="아이콘">
                <input type="text" class="input-text" placeholder="장소 설명">
              </label>
            </div>


            <div class="course-select-time">
              <div class="time-label">
                <img src="/public/image/pin.svg" alt="아이콘">
                <span>시작시각</span>
              </div>
              <div class="time-select-wrapper">
                <label><select id="start-hour" class="input-select" name="start-hour">
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
                  <select id="start-minute" class="input-select" name="start-minute">
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


            <div class="duration-wrapper">
              <div class="time-label">
                <img src="/public/image/pin.svg" alt="아이콘">
                <span>소요시간</span>
              </div>
              <div class="duration-pm-wrapper">
                <button class="n-btn btn-minus">-</button>
                <input type="number" id="duration" class="duration-input" name="duration" value="0.0">
                <button class="n-btn btn-plus">+</button>
              </div>
            </div>

          </div>
        </div>
        <div class="divider"></div>

        <div class="course-moving-wrapper">
          <div class="d:flex al-items:center">
            <p class="p:1">이동수단</p>
            <label>
              <select class="input-select">
                <option value=""></option>
              </select>
            </label>
          </div>
          <div class="d:flex al-items:center">
            <p class="p:1">이동시간</p>
            <label>
              <select class="input-select">
                <option value=""></option>
              </select>
            </label>
          </div>
        </div>


        <div class="d:flex jc:end m-top:5">
          <button class="n-btn n-btn-color:sub-1 n-btn-size:3 al-items:center">+ 경유지</button>
        </div>

        <div class="map">지도</div>

        <div class="button-group">
          <div><a class="n-btn n-btn-bg-color:main" href="#detail">이전</a></div>
          <button type="submit" class="n-btn n-btn-bg-color:main">임시저장 후 나가기</button>
          <div><a class="n-btn n-btn-bg-color:main" href="#inclusion">다음</a></div>
        </div>

      </section>


      <section id="inclusion" class="inclusion">
        <h1 class="d:none">포함 사항</h1>
        <div class="form-group">
          <div class="font-size:9 fw:bold p-bottom:4">포함 사항</div>
          <p class="fw:100 p-bottom:2">List all the features that are included in the price so customers
            understand the value for money of
            your activity. Start a new line for each one.</p>
          <label for="included" class="d:none">포함 항목을 작성하세요</label>
          <textarea id="included" name="included" rows="4" cols="50" class="input-textarea"
                    placeholder="각 사항들은 enter로 구분해주세요"></textarea>

          <p class="text-align:end p-bottom:4">0 / 1000</p>

        </div>

        <div class="form-group">
          <div class="font-size:9 fw:bold p-bottom:4">미포함사항 <span class="font-size:6 fw:1">(선택사항)</span></div>
          <p class="fw:100 p-bottom:2">Name what customers need to pay extra for or what they may expect to
            see
            that isn't included in the
            price. This allows customers to appropriately set their expectations.</p>
          <label for="not-included" class="d:none">미포함 항목을 작성하세요</label>
          <textarea id="not-included" name="not-included" rows="4" cols="50" class="input-textarea"
                    placeholder="각 사항들은 enter로 구분해주세요"></textarea>
          <p class="text-align:end p-bottom:4">0 / 1000</p>
        </div>

        <div class="button-group">
          <div><a class="n-btn n-btn-bg-color:main" href="#course">이전</a></div>
          <button type="submit" class="n-btn n-btn-bg-color:main">임시저장 후 나가기</button>
          <div><a class="n-btn n-btn-bg-color:main" href="#caution">다음</a></div>
        </div>
      </section>


      <section id="caution" class="caution">
        <h1 class="d:none">추가정보</h1>
        <div class="form-group">
          <div class="font-size:9 fw:bold p-bottom:4">준비물 <span class="font-size:6 fw:1">(선택사항)</span></div>
          <p class="fw:100 p-bottom:2">Such as a towel for a boat tour, or comfortable shoes for a hike.
            This information appears on the activity details page & voucher.</p>
          <label for="preparation" class="d:none">준비 항목을 작성하세요</label>
          <textarea id="preparation" name="preparation" rows="4" cols="50" class="input-textarea"
                    placeholder="각 사항들은 enter로 구분해주세요"></textarea>
          <p class="text-align:end p-bottom:4">0 / 1000</p>
        </div>
        <div class="form-group">
          <div class="font-size:9 fw:bold p-bottom:4">주의사항 <span class="font-size:6 fw:1">(선택사항)</span></div>
          <p class="fw:100 p-bottom:2">Add any remaining information that customers must know before they
            book.
            This information appears on the activity details page.</p>
          <label for="notice" class="d:none">주의사항을 작성하세요</label>
          <textarea id="notice" name="notice" rows="4" class="input-textarea" cols="50"
                    placeholder="각 사항들은 enter로 구분해주세요"></textarea>
          <p class="text-align:end p-bottom:4">0 / 1000</p>
        </div>
        <div class="form-group">
          <div class="font-size:9 fw:bold p-bottom:4">요청사항 <span class="font-size:6 fw:1">(선택사항)</span></div>
          <p class="fw:100 p-bottom:2">항구 규정에 따라 그룹에 포함된 모든 참가자의 이름, 성별, 생년월일, 국적, 연락/신분 정보를 제공해야 합니다.
            이 정보는 다른 사람과 공유되지 않으며 활동 종료 후 삭제됩니다. 식단 제한 관련 정보 또한 입력해주세요.</p>
          <label for="request" class="d:none">요청 사항을 작성하세요</label>
          <textarea id="request" name="request" class="input-textarea" rows="4" cols="50"
                    placeholder="각 사항들은 enter로 구분해주세요"></textarea>
          <p class="text-align:end p-bottom:4">0 / 1000</p>
        </div>
        <div class="button-group">
          <div><a class="n-btn n-btn-bg-color:main" href="#inclusion">이전</a></div>
          <button type="submit" class="n-btn n-btn-bg-color:main">임시저장 후 나가기</button>
          <div><a class="n-btn n-btn-bg-color:main" href="#image">다음</a></div>
        </div>
      </section>

      <section id="image" class="images-upload-container">
        <header>
          <h1>사진 업로드</h1>
          <label class="n-btn n-btn-border:sub n-btn-size:1"><input type="file" id="fileInput">+ 파일 추가</label>
        </header>
        <div style="border: 1px solid; border-radius: 10px; padding: 0 20px 20px 20px">
          <div class="upload-images">

            <div class="image-container">

              <div class="image-title-main">
                <span>대표</span>
              </div>
              <div class="image">
                <div>
                  <div><img src="/public/image/profile_cat.png" alt="이미지"></div>
                  <div>
                    <button>X</button>
                  </div>
                </div>
              </div>

            </div>

            <div class="image-container">

              <div class="image-title">
                <span>1</span>
              </div>
              <div class="image">
                <div>
                  <div><img src="/public/image/home-guest.png" alt="이미지"></div>
                  <div>
                    <button>X</button>
                  </div>
                </div>
              </div>
            </div>

            <div class="image-container">
              <div class="image-title">
                <span>2</span>
              </div>
              <div class="image">
                <div>
                  <div><img src="/public/image/home-host.png" alt="이미지"></div>
                  <div>
                    <button>X</button>
                  </div>
                </div>
              </div>
            </div>

            <div class="image-container">
              <div class="image-title">
                <span>3</span>
              </div>
              <div class="image">
                <div>
                  <div><img src="" alt="이미지"></div>
                  <div>
                    <button>X</button>
                  </div>
                </div>
              </div>
            </div>

            <div class="image-container">
              <div class="image-title">
                <span>4</span>
              </div>
              <div class="image">
                <div>
                  <div><img src="" alt="이미지"></div>
                  <div>
                    <button>X</button>
                  </div>
                </div>
              </div>
            </div>

            <div class="image-container">
              <div class="image-title">
                <span>5</span>
              </div>
              <div class="image">
                <div>
                  <div><img src="" alt="이미지"></div>
                  <div>
                    <button>X</button>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
        <div class="button-group">
          <div><a class="n-btn n-btn-bg-color:main" href="#caution">이전</a></div>
          <button type="submit" class="n-btn n-btn-bg-color:main">임시저장 후 나가기</button>
          <div>
            <button type="submit" class="n-btn n-btn-bg-color:main">작성완료</button>
          </div>
        </div>
      </section>
    </form>
  </main>
</template>
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

  /* 경고 문구 스타일 */

  .warning {
    color: red;
    font-size: 0.9rem;
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
  font-weight: bold;
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
</style>