<script setup>

import { onMounted, ref } from "vue";
import axios from "axios";
//=======================변수영역==========
const categories = ref([]);


//================Fetch Function==========
const fetchCategories = async () => {
  const response = await axios.get("http://localhost:8083/api/v1/categories");

  categories.value = response.data;

  // console.log(categories.value);
}

//=======================LifeCycle Functions==========
onMounted(() => {
  fetchCategories();
})

</script>
<template>
  <details open class="filter">
    <summary class="collapse">
      <span class="title">카테고리</span>
      <span class="n-icon n-icon:arrow_up">펼치기 버튼</span>
    </summary>

    <form action="" class="form">
      <div class="modal-checkbox">
        <label v-for="c in categories" :key="c.id">
          <input type="checkbox" />{{ c.name }}
        </label>
      </div>
    </form>
  </details>

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
      --icon: url(/assets/image/icon/arrow_down.svg);
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