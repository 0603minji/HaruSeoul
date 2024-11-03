<script setpup>

</script>
<template>
  <aside class="popup">
    <header class="popup-header">
      <h1 class="font-size:9">개설하기</h1>
      <button class="n-btn n-btn:hover n-btn-border:transparent n-icon n-icon:exit">닫기</button>
    </header>

    <form class="popup-body" action="">
      <div class="program-selector">
        <div class="title">
          <p class="font-size:8">프로그램 선택</p>
        </div>



      </div>

      <div class="date-picker">
        <div class="title">
          <p class="font-size:8">진행일 선택</p>
          <button
              class="margin-left:auto n-btn n-btn:hover n-btn-bd:none n-icon n-icon-size:2 n-icon:reset n-icon-color:sub-1 n-deco color:sub-1">
            초기화
          </button>
        </div>
        <FilterCalendarV2/>
      </div>


      <div class="submit">
        <button class="n-btn n-btn:hover n-btn-bg-color:sub n-btn-size:1">확인</button>
      </div>
    </form>
  </aside>
</template>
<style scoped>
.popup {
  border: 1px solid black;
  border-radius: 16px 16px 0 0;
  background-color: var(--color-base-1);
  //padding: 16px 24px;
  display: flex;
  flex-direction: column;

  .popup-header {
    height: 64px;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;

    button {
      position: absolute;
      right: 14px;
    }
  }

  .popup-body {
    display: flex;
    flex-direction: column;
    padding: 0 10px;

    .title {
      display: flex;
      padding: 0 0 0 10px;
      align-items: center;
      margin-bottom: 8px;

      button {
        --btn-padding: 8px 10px;
      }
    }

    .program-selector {

    }
    .date-picker {
      display: flex;
      flex-direction: column;
    }

    .submit {
      margin-left: auto;
      padding: 20px 0;
    }
  }

}
</style>