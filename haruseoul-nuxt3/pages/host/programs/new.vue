<script setup>

import {onMounted, ref} from "vue";
import axios from "axios";

window.addEventListener('DOMContentLoaded', function () {
  if (!window.location.hash) {
    window.location.hash = '#intro';
  }
});


const categories = ref([]);


//================Fetch Function==========
const fetchCategories = async () => {
  const response = await axios.get("http://localhost:8080/api/v1/categories");

  categories.value = response.data;

  // console.log(categories.value);
}

//=======================LifeCycle Functions==========
onMounted(() => {
  fetchCategories();
})
</script>
<template>
  <Head>
    <Title> Host Program Create 페이지 </Title>
  </Head>
  <main>
    <nav class="n-bar-underline-create">
      <h1>목차</h1>
      <ul class="item-wrapper padding-x:6 justify-content:flex-start">
        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"><a href="#intro" class="">소개</a></li>
        <li class="n-btn:hover n-btn n-btn-border:none n-btn-radius:0"><a href="#detail" class="">세부사항</a></li>
        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"><a href="#course" class="">코스</a></li>
        <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"><a href="#inclusion" class="">포함사항</a></li>
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
              <img src="/image/pen.svg" alt="아이콘">
              <label>
                <input type="text" class="input-text" placeholder="제목">
              </label>
            </div>

            <div class="input-with-icon">
              <img src="/image/pen.svg" alt="아이콘">
              <label>
                <input type="text" class="input-text" placeholder="주소">
              </label>
            </div>

            <div class="input-with-icon">
              <label>
                <img src="/image/pin.svg" alt="아이콘">
                <input type="text" class="input-text" placeholder="장소 설명">
              </label>
            </div>


            <div class="course-select-time">
              <div class="time-label">
                <img src="/image/pin.svg" alt="아이콘">
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
                <img src="/image/pin.svg" alt="아이콘">
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
          <textarea id="included" name="included" rows="4" cols="50"
                    class="input-textarea"
                    placeholder="각 사항들은 enter로 구분해주세요"></textarea>

          <p class="text-align:end p-bottom:4">0 / 1000</p>

        </div>

        <div class="form-group">
          <div class="font-size:9 fw:bold p-bottom:4">미포함사항 <span class="font-size:6 fw:1">(선택사항)</span></div>
          <p class="fw:100 p-bottom:2">Name what customers need to pay extra for or what they may expect to see
            that isn't included in the
            price. This allows customers to appropriately set their expectations.</p>
          <label for="not-included" class="d:none">미포함 항목을 작성하세요</label>
          <textarea id="not-included" name="not-included" rows="4" cols="50"
                    class="input-textarea"
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
          <textarea id="preparation" name="preparation" rows="4" cols="50"
                    class="input-textarea"
                    placeholder="각 사항들은 enter로 구분해주세요"></textarea>
          <p class="text-align:end p-bottom:4">0 / 1000</p>
        </div>
        <div class="form-group">
          <div class="font-size:9 fw:bold p-bottom:4">주의사항 <span class="font-size:6 fw:1">(선택사항)</span></div>
          <p class="fw:100 p-bottom:2">Add any remaining information that customers must know before they book.
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
                  <div><img src="/image/profile_cat.png" alt="이미지"></div>
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
                  <div><img src="/image/home-guest.png" alt="이미지"></div>
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
                  <div><img src="/image/home-host.png" alt="이미지"></div>
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
@import url("/css/host/program/create.css");
</style>