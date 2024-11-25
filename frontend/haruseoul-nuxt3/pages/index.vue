<template>
  <main>
    <div class="container">
      <section class="index">
        <h1 class="d:none">메인</h1>
        <div>
          <div class="aaa">
            <p class="a-1">뻔한 서울 투어 Nope!</p>
            <p class="a-2">리얼 서울 라이프를 경험해보세요</p>
          </div>
          <img class="main-img" src="/assets/image/main1.jpg" alt="">
          <div>
            <NuxtLink class="n-btn n-btn-background-color:main" href="/programs">1day 서울러 되러가기</NuxtLink>
          </div>
        </div>
        <div>
          <div class="aaa">
            <p class="a-1">당신의 평범한 일상 게스트에겐 특별한 경험</p>
            <p class="a-2">당신도 호스트가 되어보세요</p>
          </div>
          <img class="main-img" src="/assets/image/main2.jpg" alt="">
          <div>
            <NuxtLink class="n-btn n-btn-background-color:main" href="/host/programs">5분만에 호스트 되러가기</NuxtLink>
          </div>
        </div>
      </section>
      <article class="main-content">
        <h1 class="d:none">서비스 설명</h1>
        <p class="article-title">Haru Seoul은 외국인들에게 서울에서 현지인의 삶을 직접 체험할 수 있는 하루를 제공합니다.</p>
        <p class="article-content">단순한 관광이 아니라 서울 시민처럼 대중교통을 이용하고, 길거리 음식을 맛보고, 전통시장이나 동네 카페를 방문하거나,
          서울의 숨은 명소들을 체험하는 프로그램을 제공합니다.</p>
      </article>
    </div>
  </main>
</template>
<style scoped>
.container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
}

.index {
  > div {
    position: relative;
    width: 100%;
    //max-width: 400px; /* 필요에 따라 너비 조절 */
    overflow: hidden;
  }
}

.main-img {
  position: relative;
  height: 100%;
  width: 100%;
  display: block;
}

.aaa {
  position: absolute;
  //top: 20px;
  //left: 20px;
  width: 100%;
  color: white;
  z-index: 1;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0)); /* 위쪽은 진한 음영, 아래쪽으로 갈수록 투명 */
}

.a-1 {
  font-size: 14px;
  //font-weight: bold;
  padding-top: 30px;
  padding-left: 30px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.6);
}

.a-2 {
  font-size: 18px;
  font-weight: bold;
  padding-top: 10px;
  padding-left: 30px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.6);
}

.main-content {
  padding: 10px 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .article-title {

    font-size: 20px;
    font-weight: bold;
    color: var(--color-main-1);
    padding: 10px 0;
  }

  .article-content {
    color: var(--color-base-9);
    padding: 10px 0;
  }
}

.n-btn {
  position: absolute;
  bottom: 20px;
  right: 20px;

}

@media (min-width: 768px) {
  .container {
    max-width: 1096px;
    margin: 0 auto; /* 가운데 정렬 */
  }
  .index{
    display: flex;
    flex-direction: row;
  }
}


</style>
<script setup lang="ts">
</script>