<script setup>

</script>
<template>

  <section class="calendar-container">
    <header class="calendar-header">
      <h1 class="d:none">May 2024</h1>
      <button class="to-prev-month n-btn border-radius:full n-icon n-icon:arrow_left n-icon-size:3" type="button">이전</button>
      <div class="year-month-wrapper font-size:9 font-weight:bold">
        <select class="year">
          <option value="2024">2024</option>
          <option value="2025">2025</option>
          <option value="2026">2026</option>
        </select>

        <select class="month">
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
      <button class="to-next-month n-btn border-radius:full n-icon n-icon:arrow_right n-icon-size:3" type="button">다음</button>
    </header>

    <ul class="weekdays">
      <li>
        <abbr title="Sunday">S</abbr>
      </li>
      <li>
        <abbr title="Monday">M</abbr>
      </li>
      <li>
        <abbr title="Tuesday">T</abbr>
      </li>
      <li>
        <abbr title="Wednesday">W</abbr>
      </li>
      <li>
        <abbr title="Thursday">T</abbr>
      </li>
      <li>
        <abbr title="Friday">F</abbr>
      </li>
      <li>
        <abbr title="Saturday">S</abbr>
      </li>
    </ul>

    <ol class="day-grid">
      <li class="month-prev"><label><input type="checkbox"><span>29</span></label></li>
      <li class="month-prev"><label><input type="checkbox"><span>30</span></label></li>
      <li class="month-prev"><label><input type="checkbox"><span>31</span></label></li>

      <li><label><input type="checkbox"><span>1</span></label></li>
      <li><label><input type="checkbox"><span>2</span></label></li>
      <li><label><input type="checkbox"><span>3</span></label></li>
      <li><label><input type="checkbox"><span>4</span></label></li>
      <li><label><input type="checkbox"><span>5</span></label></li>
      <li><label><input type="checkbox"><span>6</span></label></li>
      <li><label><input type="checkbox"><span>7</span></label></li>
      <li><label><input type="checkbox"><span>8</span></label></li>
      <li><label><input type="checkbox"><span>9</span></label></li>
      <li class="today"><label><input type="checkbox"><span>10</span></label></li>
      <li><label><input type="checkbox"><span>11</span></label></li>
      <li><label><input type="checkbox"><span>12</span></label></li>
      <li class="available selected"><label><input type="checkbox"><span>13</span></label></li>
      <li class="available"><label><input type="checkbox"><span>14</span></label></li>
      <li class="available"><label><input type="checkbox"><span>15</span></label></li>
      <li class="available selected"><label><input type="checkbox"><span>16</span></label></li>
      <li class="available"><label><input type="checkbox"><span>17</span></label></li>
      <li class="available"><label><input type="checkbox"><span>18</span></label></li>
      <li class="available"><label><input type="checkbox"><span>19</span></label></li>
      <li class="available selected"><label><input type="checkbox"><span>20</span></label></li>
      <li class="available"><label><input type="checkbox"><span>21</span></label></li>
      <li class="available"><label><input type="checkbox"><span>22</span></label></li>
      <li class="available selected"><label><input type="checkbox"><span>23</span></label></li>
      <li class="available"><label><input type="checkbox"><span>24</span></label></li>
      <li class="available"><label><input type="checkbox"><span>25</span></label></li>
      <li class="available"><label><input type="checkbox"><span>26</span></label></li>
      <li class="available occupied"><label><input type="checkbox"><span>27</span></label></li>
      <li class="available"><label><input type="checkbox"><span>28</span></label></li>
      <li class="available"><label><input type="checkbox"><span>29</span></label></li>
      <li class="available"><label><input type="checkbox"><span>30</span></label></li>

      <li class="month-next"><label><input type="checkbox"><span>1</span></label></li>
      <li class="month-next"><label><input type="checkbox"><span>2</span></label></li>
    </ol>
  </section>

</template>

<style scoped>
.calendar-container {
  //max-width: 768px;
  //min-width: 250px;
  width: 373px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 14px;
  box-shadow: 0 1.88px 2px 0 rgba(0, 14, 51, 0.05);


  .calendar-header {
    display: flex;
    justify-content: center;
    align-items: center;
    padding-bottom: 8px;

    .year-month-wrapper {
      display: flex;
      //box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);

      .year, .month {
        font-size: 1rem;
      }
    }

    button {
      background-color: var(--color-base-1);
      --btn-border-color: transparent;
      box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);
    }

    .to-prev-month {
      margin-left: auto;
      margin-right: 16px;
    }

    .to-next-month {
      margin-right: auto;
      margin-left: 16px;
    }
  }

  .weekdays {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    font-weight: 500;
    font-size: 0.75rem;
    justify-items: center;

    li {
      abbr {
        text-decoration: none;
      }
    }
  }

  .day-grid {
    width: 100%;
    display: grid;
    gap: 8px;
    grid-template-columns: repeat(7, 1fr);


    li {
      min-width: 0;
      overflow: hidden;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;

      height: 6vh;
      /* background-color: var(--color-base-1); */
      border-radius: 4px;
      transition: background-color 0.3s ease, border-color 0.3s ease, color 0.3s ease;

      > label {
        /* 체크박스 숨김 */

        input[type="checkbox"] {
          display: none;
        }


        span {
          color: rgba(0, 23, 84, 0.15);
          font-size: 0.75rem;
          font-weight: 500;
        }
      }
    }

    .today {
      border: 2px solid var(--color-sub-1);

       label span {
          color: var(--color-base-9);
      }
    }

    :is(.month-prev, .month-next) {
      visibility: hidden;
      background-color: transparent;
    }

    /* 개설가능한 날짜 */

    .available {
      background-color: var(--color-base-1);
      box-shadow: 0 1.88px 0.88px 0 rgba(0, 14, 51, 0.05);
      /* position x, y, blur, spread, color */

      > label span {
        color: var(--color-base-9);
      }
    }

    .available:not(.selected):hover {
      background-color: var(--color-base-4);
    }

    /* 선택된 개설가능한 날짜 */

    .selected {
      background-color: var(--color-sub-1);

      > label span {
        color: var(--color-base-1);
      }
    }

    /* 이미 개설된 프로그램 존재하는 날짜 */

    .occupied {
      background-color: var(--color-base-3);
      box-shadow: none;

      > label span {
        color: rgba(0, 23, 84, 0.15);
      }
    }
  }
}

@media (min-width: 768px) {
  .calendar-container {

  }
}
</style>