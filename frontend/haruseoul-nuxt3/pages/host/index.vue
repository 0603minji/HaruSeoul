<template>
  <main>

    <section class="layout-body"> <!-- main 내 모든 -->
      <!--=== heading ==========================================-->
      <!--    예약 관리           +일정추가-->
      <header class="n-title">
        <h1 class="">예약관리</h1>
        <div>
          <a href="" class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:plus n-deco">일정 추가</a>
        </div>
      </header>

      <!--=== host/reservation/list 예약관리 상단바==========================================-->
      <nav class="n-bar-underline">
        <h1 class="d:none">네비탭</h1>
        <ul class="item-wrapper">
          <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"><a href="" class="">예정된 일정</a>
          </li>
          <li class="active n-btn:hover n-btn n-btn-border:none n-btn-radius:0"><a href="" class="">지난
              일정</a>
          </li>
          <li class="n-btn n-btn:hover n-btn-border:none n-btn-radius:0"><a href="" class="">취소된 일정</a>
          </li>
        </ul>
      </nav>

      <div class="layout-main">
        <div class="layout-main-list">
          <!--=== 필터 .n-filter ==========================================-->
          <!--모집 중, 예약 확정, 폐지 임박, 종료, 폐지, 필터-->
          <section class="n-filter md:d:none bg-color:base-1">
            <h1 class="d:none">필터</h1>

            <div class="overflow-x:auto">
              <ul class="item-wrapper">
                <li><a href=""
                    class="active n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:calendar n-icon-size:1 n-deco n-deco-gap:1">기간</a>
                </li>
                <li><a href=""
                    class="active active:border n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:pending n-icon-size:1 n-deco n-deco-gap:1">프로그램
                    상태</a></li>
                <li><a href=""
                    class="n-btn n-btn-pg-filter n-btn:hover n-icon n-icon:search n-icon-size:1 n-deco n-deco-gap:1">프로그램</a>
                </li>
              </ul>
            </div>

            <div class="reset-box">
              <div class="gradation"></div>
              <a href="" class="icon-box n-deco1 n-icon n-icon:reset">
                초기화
              </a>
            </div>
          </section>

          <!--=== 목록 ==========================================-->
          <section class="rv-list">
            <!--=== 정렬 .list-header ==========================================-->
            <header class="list-header bg-color:base-1">
              <h1 class="d:none">예약카드 목록</h1>
              <div>
                <span class="list-item-count">23 Result</span>
                <!--            <button type="button" class="n-icon n-icon:arrow_swap n-deco n-deco-gap:1">-->
                <!--                정렬-->
                <!--            </button>-->
                <a href="?view=calendar"
                  class="option list-view-toggle n-icon n-icon:calendar_month n-icon-color:base-6 n-deco n-deco-gap:1 margin-left:auto">
                  <span class="md:d:inline">캘린더로 보기</span>
                </a>

                <span class="separator"></span>

                <a href="" class="option sort n-icon n-icon:arrow_swap n-icon-color:base-6 n-deco n-deco-gap:1">
                  정렬
                </a>
              </div>
            </header>

            <!--예약 카드 목록. (모집 중) (예약 확정) (폐지 임박)-->
            <ul class="n-card-container bg-color:base-1">
              <!-- =================================== 모집 중 예약 카드 1개 =================================== -->
              <li class="n-card n-card:hover padding:6">
                <a class="n-link-box" href="detail?id=1"></a>
                <h2 class="d:none">예약 카드</h2>

                <div class="card-header">
                  <div class="left">
                    <span class="n-panel-tag n-panel-tag:blue">모집 중</span>
                    <!--                            <span class="n-panel-tag n-panel-tag:red">폐지 임박</span>-->
                    <!--                            <span class="n-panel-tag n-panel-tag:green">예약 확정</span>-->
                    <!--                            <span class="n-panel-tag">취소</span>-->
                    <!--                            <span class="n-panel-tag n-panel-tag:gray">종료 </span>-->
                  </div>
                  <div class="right">
                    <a href="" class="n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9">더보기</a>
                  </div>
                </div>

                <div class="card-main">
                  <div class="img-wrapper">
                    <img src="/public/image/program_01.png" alt="대표사진">
                  </div>

                  <div class="card-info-wrapper">
                    <p class="title">Gyeong-bok-gung Palace Tour With Wearing Han-bok</p>
                    <div class="card-info">
                      <span class="n-icon n-icon:calendar n-deco">24.09.26 Thu</span>
                      <span class="n-panel-tag">D-4</span>
                    </div>
                  </div>

                  <!-- md:footer: card-footer영역에 존재하다가 992px이상에서 card-main의 우측으로 이동 -->
                  <div class="applicant-status lg:show">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">3 / 5</span>
                  </div>
                </div>

                <div class="card-footer">
                  <div class="applicant-status margin-left:auto">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">3 / 5</span>
                  </div>
                </div>
              </li>

              <!-- =================================== 폐지임박 예약 카드 1개 =================================== -->
              <li class="n-card n-card:hover padding:6">
                <a class="n-link-box" href="detail?id=1"></a>
                <h2 class="d:none">예약 카드</h2>

                <div class="card-header">
                  <div class="left">
                    <!--                            <span class="n-panel-tag n-panel-tag:blue">모집 중</span>-->
                    <span class="n-panel-tag n-panel-tag:red">폐지 임박</span>
                    <!--                            <span class="n-panel-tag n-panel-tag:green">예약 확정</span>-->
                    <!--                            <span class="n-panel-tag">취소</span>-->
                    <!--                            <span class="n-panel-tag n-panel-tag:gray">종료 </span>-->
                  </div>
                  <div class="right">
                    <a href="" class="n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9">더보기</a>
                  </div>
                </div>

                <div class="card-main">
                  <a class="n-link-box" href="detail?id=1"></a>
                  <div class="img-wrapper">
                    <img src="/public/image/program_01.png" alt="대표사진">
                  </div>

                  <div class="card-info-wrapper">
                    <p class="title">Gyeong-bok-gung Palace Tour With Wearing Han-bok</p>
                    <div class="card-info">
                      <span class="n-icon n-icon:calendar n-deco">24.09.26 Thu</span>
                      <span class="n-panel-tag">D-4</span>
                    </div>
                  </div>

                  <!-- md:footer: card-footer영역에 존재하다가 992px이상에서 card-main의 우측으로 이동 -->
                  <div class="applicant-status lg:show">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">3 / 5</span>
                  </div>
                </div>

                <div class="card-footer">
                  <div class="applicant-status margin-left:auto">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">3 / 5</span>
                  </div>
                </div>
              </li>

              <!-- =================================== 예약 확정 예약 카드 1개 =================================== -->
              <li class="n-card n-card:hover padding:6">
                <a class="n-link-box" href="detail?id=1"></a>
                <h2 class="d:none">예약 카드</h2>

                <div class="card-header">
                  <div class="left">
                    <!--                            <span class="n-panel-tag n-panel-tag:blue">모집 중</span>-->
                    <!--                            <span class="n-panel-tag n-panel-tag:red">폐지 임박</span>-->
                    <span class="n-panel-tag n-panel-tag:green">예약 확정</span>
                    <!--                            <span class="n-panel-tag">취소</span>-->
                    <!--                            <span class="n-panel-tag n-panel-tag:gray">종료 </span>-->
                  </div>
                  <div class="right">
                    <a href="" class="n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9">더보기</a>
                  </div>
                </div>

                <div class="card-main">
                  <a class="n-link-box" href="detail?id=1"></a>
                  <div class="img-wrapper">
                    <img src="/public/image/program_01.png" alt="대표사진">
                  </div>

                  <div class="card-info-wrapper">
                    <p class="title">Gyeong-bok-gung Palace Tour With Wearing Han-bok</p>
                    <div class="card-info">
                      <span class="n-icon n-icon:calendar n-deco">24.09.26 Thu</span>
                      <span class="n-panel-tag">D-4</span>
                    </div>
                  </div>

                  <!-- md:footer: card-footer영역에 존재하다가 992px이상에서 card-main의 우측으로 이동 -->
                  <div class="applicant-status lg:show">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">3 / 5</span>
                  </div>
                </div>

                <div class="card-footer">
                  <div class="applicant-status margin-left:auto">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">3 / 5</span>
                  </div>
                </div>
              </li>

              <!-- =================================== 취소 예약 카드 1개 =================================== -->
              <li class="n-card n-card:hover padding:6">
                <a class="n-link-box" href="detail?id=1"></a>
                <h2 class="d:none">예약 카드</h2>

                <div class="card-header">
                  <div class="left">
                    <!--                            <span class="n-panel-tag n-panel-tag:blue">모집 중</span>-->
                    <!--                            <span class="n-panel-tag n-panel-tag:red">폐지 임박</span>-->
                    <!--                            <span class="n-panel-tag n-panel-tag:green">예약 확정</span>-->
                    <span class="n-panel-tag">취소</span>
                    <!--                            <span class="n-panel-tag n-panel-tag:gray">종료 </span>-->
                  </div>
                  <div class="right">
                    <a href="" class="n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9">더보기</a>
                  </div>
                </div>

                <div class="card-main">
                  <a class="n-link-box" href="detail?id=1"></a>
                  <div class="img-wrapper">
                    <img src="/public/image/program_01.png" alt="대표사진">
                  </div>

                  <div class="card-info-wrapper">
                    <p class="title">Gyeong-bok-gung Palace Tour With Wearing Han-bok</p>
                    <div class="card-info">
                      <span class="n-icon n-icon:calendar n-deco">24.09.26 Thu</span>
                      <!--                                <span class="n-panel-tag">D-4</span>-->
                    </div>
                  </div>

                  <!-- md:footer: card-footer영역에 존재하다가 992px이상에서 card-main의 우측으로 이동 -->
                  <div class="applicant-status lg:show">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">3 / 5</span>
                  </div>
                </div>

                <div class="card-footer">
                  <div class="applicant-status margin-left:auto">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">3 / 5</span>
                  </div>
                </div>
              </li>

              <!-- =================================== 종료 예약 카드 1개 =================================== -->
              <li class="n-card n-card:hover padding:6">
                <a class="n-link-box" href="detail?id=1"></a>
                <h2 class="d:none">예약 카드</h2>

                <div class="card-header">
                  <div class="left">
                    <!--                            <span class="n-panel-tag n-panel-tag:blue">모집 중</span>-->
                    <!--                            <span class="n-panel-tag n-panel-tag:red">폐지 임박</span>-->
                    <!--                            <span class="n-panel-tag n-panel-tag:green">예약 확정</span>-->
                    <!--                            <span class="n-panel-tag">취소</span>-->
                    <span class="n-panel-tag n-panel-tag:gray">종료 </span>
                  </div>
                  <div class="right">
                    <a href="" class="n-icon n-icon:more_vertical n-icon-size:4 n-icon-color:base-9">더보기</a>
                  </div>
                </div>

                <div class="card-main">
                  <a class="n-link-box" href="detail?id=1"></a>
                  <div class="img-wrapper">
                    <img src="/public/image/program_01.png" alt="대표사진">
                  </div>

                  <div class="card-info-wrapper">
                    <p class="title">Gyeong-bok-gung Palace Tour With Wearing Han-bok</p>
                    <div class="card-info">
                      <span class="n-icon n-icon:calendar n-deco">24.09.26 Thu</span>
                      <!--                                <span class="n-panel-tag">D-4</span>-->
                    </div>
                  </div>

                  <!-- md:footer: card-footer영역에 존재하다가 992px이상에서 card-main의 우측으로 이동 -->
                  <div class="applicant-status lg:show">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">3 / 5</span>
                  </div>
                </div>

                <div class="card-footer">
                  <div class="applicant-status margin-left:auto">
                    <div class="guest-profile-container">
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_01.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/profile.png" alt="게스트 프로필">
                      </div>
                      <div class="guest-profile-wrapper">
                        <img src="/public/image/program_02.png" alt="게스트 프로필">
                      </div>
                    </div>
                    <span class="n-icon n-icon:group n-icon-size:2 n-icon-color:main-3 n-deco n-deco-gap:1">3 / 5</span>
                  </div>
                </div>
              </li>
            </ul>

          </section>
        </div>

        <aside class="layout-main-aside">

        </aside>
      </div>
      <!--=== aside ==========================================-->
      <!--        <aside>-->
      <!--            <header>-->
      <!--                <h1>필터</h1>-->
      <!--                <button onclick="closeModal()"><img src="/public/image/host/close.svg" alt="닫기"></button>-->
      <!--            </header>-->

      <!--            <form>-->
      <!--                <section>-->
      <!--                    <div>-->
      <!--                        <h1>프로그램</h1>-->
      <!--                        <div><img src="/public/image/init.svg" alt="초기화">초기화</div>-->
      <!--                    </div>-->

      <!--                    <div>-->
      <!--                        <img style="margin-right: 4px" src="/public/image/search.svg" alt="search">-->
      <!--                        프로그램 선택-->
      <!--                        <img style="margin-left: auto" src="/public/image/dropdown.svg" alt="dropdown">-->
      <!--                    </div>-->

      <!--                    <ul>-->
      <!--                        <li><label><input type="checkbox" name="pg-title"> 청계천과 광장시장 돌아보고 힙지로에서 저녁먹기</label>-->
      <!--                        </li>-->
      <!--                        <li><label><input type="checkbox" name="pg-title"> 연희동에서 브런치먹고 도자기공방 원데이클래스 함께-->
      <!--                            하기</label></li>-->
      <!--                        <li><label><input type="checkbox" name="pg-title"> 남산타워 방문 후 명동 쇼핑 투어</label></li>-->
      <!--                        <li><label><input type="checkbox" name="pg-title"> 홍대에서 인디 밴드 공연 관람 및 야시장 탐방</label>-->
      <!--                        </li>-->
      <!--                        <li><label><input type="checkbox" name="pg-title"> 강남에서 럭셔리 디너와 클럽 나이트 즐기기</label></li>-->
      <!--                        <li><label><input type="checkbox" name="pg-title"> 한강에서 자전거 타기 및 피크닉</label></li>-->
      <!--                        <li><label><input type="checkbox" name="pg-title"> 서울 숲에서 자연과 함께하는 힐링 워크</label></li>-->
      <!--                        <li><label><input type="checkbox" name="pg-title"> 이태원에서 다문화 음식을 즐기며 문화 체험</label></li>-->
      <!--                    </ul>-->
      <!--                </section>-->

      <!--                <section>-->
      <!--                    <div>-->
      <!--                        <h1>기간</h1>-->
      <!--                        <div style="font-size: 14px; color:#24A0ED"><img src="/public/image/init.svg" alt="초기화">초기화-->
      <!--                        </div>-->
      <!--                    </div>-->


      <!--                    <div>-->
      <!--                        <p>시작일</p>-->
      <!--                        <p>-</p>-->
      <!--                        <p>종료일</p>-->
      <!--                    </div>-->

      <!--                    <div>-->
      <!--                        <div>-->
      <!--                            <button>&lt;</button>-->
      <!--                            <div>-->
      <!--                                <span>2024</span>-->
      <!--                                <span>9</span>-->
      <!--                            </div>-->
      <!--                            <button>&gt;</button>-->
      <!--                        </div>-->
      <!--                        <div>-->
      <!--                            &lt;!&ndash; 요일 헤더 &ndash;&gt;-->
      <!--                            <div>Mo</div>-->
      <!--                            <div>Tu</div>-->
      <!--                            <div>We</div>-->
      <!--                            <div>Th</div>-->
      <!--                            <div>Fr</div>-->
      <!--                            <div>Sa</div>-->
      <!--                            <div>Su</div>-->

      <!--                            &lt;!&ndash; 날짜 셀들 &ndash;&gt;-->
      <!--                            &lt;!&ndash; 첫째 주 &ndash;&gt;-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-29">29</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-30">30</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-31">31</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-1">1</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-2">2</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-3">3</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-4">4</label></div>-->

      <!--                            &lt;!&ndash; 둘째 주 &ndash;&gt;-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-5">5</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-6">6</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-7">7</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-8">8</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-9">9</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-10">10</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-11">11</label></div>-->

      <!--                            &lt;!&ndash; 셋째 주부터 활성화 &ndash;&gt;-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-12">12</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-13">13</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-14">14</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-16">16</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-17">17</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-18">18</label></div>-->

      <!--                            <div><input name="day" type="checkbox"><label for="day-19">19</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-20">20</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-21">21</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-22">22</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-23">23</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-24">24</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-25">25</label></div>-->

      <!--                            <div><input name="day" type="checkbox"><label for="day-26">26</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-27">27</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-28">28</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-29-alt">29</label></div>-->
      <!--                            <div><input name="day" type="checkbox"><label for="day-30-alt">30</label></div>-->
      <!--                            <div><input name="day" type="checkbox" disabled><label for="day-31-alt">31</label>-->
      <!--                            </div>-->
      <!--                        </div>-->
      <!--                    </div>-->
      <!--                </section>-->
      <!--                <button type="submit">확인</button>-->
      <!--            </form>-->
      <!--        </aside>-->
      <!--        <aside>-->
      <!--            <header>-->
      <!--                <h1>예약자</h1>-->
      <!--                <button onclick="closeModal()"><img src="/public/image/close.svg" alt="닫기"></button>-->
      <!--            </header>-->
      <!--            <div>-->
      <!--                <span>게스트 정보</span>-->
      <!--                <span>1:1 Chat</span>-->
      <!--                <span>추방</span>-->
      <!--            </div>-->

      <!--            <ul id="reserved-guest">-->
      <!--                <li>-->
      <!--                    <div>-->
      <!--                        <div>-->
      <!--                            <img src="image/host/guest-profileimg.svg" alt="guest profile image">-->
      <!--                        </div>-->
      <!--                        <div>-->
      <!--                            <span style="font-size: 16px">예약자 1 닉네임</span><br>-->
      <!--                            <span style="font-size: 14px">jasmin01@gmail.com</span>-->
      <!--                        </div>-->
      <!--                    </div>-->

      <!--                    <div>-->
      <!--                        <a href=""><img src="/public/image/chat.svg" alt="chat"></a>-->
      <!--                    </div>-->
      <!--                    <div>-->
      <!--                        <a href=""><img src="/public/image/reject.svg" alt="reject reserved guest"></a>-->
      <!--                    </div>-->
      <!--                </li>-->

      <!--                <li>-->
      <!--                    <div>-->
      <!--                        <div>-->
      <!--                            <img src="/public/image/guest-profileimg.svg" alt="guest profile image">-->
      <!--                        </div>-->
      <!--                        <div>-->
      <!--                            <span style="font-size: 16px">예약자 1 닉네임</span><br>-->
      <!--                            <span style="font-size: 14px">jasmin01@gmail.com</span>-->
      <!--                        </div>-->
      <!--                    </div>-->

      <!--                    <div>-->
      <!--                        <a href=""><img src="/public/image/chat.svg" alt="chat"></a>-->
      <!--                    </div>-->
      <!--                    <div>-->
      <!--                        <a href=""><img src="/public/image/reject.svg" alt="reject reserved guest"></a>-->
      <!--                    </div>-->
      <!--                </li>-->
      <!--            </ul>-->

      <!--            <div>-->
      <!--                <a><img src="/public/image/chat-all.svg" alt="chat-all">전체 Chat</a>-->
      <!--            </div>-->
      <!--        </aside>-->
      <!--        <aside>-->
      <!--            <header>-->
      <!--                <h1>일정추가</h1>-->
      <!--                <button onclick="closeModal()">-->
      <!--                    <img src="/public/image/close.svg" alt="닫기">-->
      <!--                </button>-->
      <!--            </header>-->

      <!--            <form method="post" action="/submit-dates">-->
      <!--                <section>-->
      <!--                    <h1>프로그램</h1>-->
      <!--                    <label>-->
      <!--                        프로그램을 선택하세요-->
      <!--                        <select name="program-title">-->
      <!--                            <option>한복입고 북촌 인기 카페...</option>-->
      <!--                            <option>청계천 광장시장과 힙지로에서 저녁...</option>-->
      <!--                            <option>연희동 도자기공방과 인기 카페...</option>-->
      <!--                        </select>-->
      <!--                    </label>-->
      <!--                </section>-->

      <!--                <section>-->
      <!--                    <h1>진행일 선택</h1>-->

      <!--                    <div>-->
      <!--                        <button type="button" id="prev-month">&lt;</button>-->
      <!--                        <div>-->
      <!--                            <span id="year">2024</span>-->
      <!--                            <span id="month">9</span>-->
      <!--                        </div>-->
      <!--                        <button type="button" id="next-month">&gt;</button>-->
      <!--                    </div>-->

      <!--                    <section>-->
      <!--                        <h1>요일</h1>-->

      <!--                        <div>Mo</div>-->
      <!--                        <div>Tu</div>-->
      <!--                        <div>We</div>-->
      <!--                        <div>Th</div>-->
      <!--                        <div>Fr</div>-->
      <!--                        <div>Sa</div>-->
      <!--                        <div>Su</div>-->

      <!--                        &lt;!&ndash; 첫째 주 &ndash;&gt;-->
      <!--                        <div><input type="checkbox" id="day-29" disabled><label for="day-29">29</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-30" disabled><label for="day-29">29</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-31" disabled><label for="day-29">29</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-1" disabled><label for="day-29">29</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-2" disabled><label for="day-29">29</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-3" disabled><label for="day-29">29</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-4" disabled><label for="day-29">29</label>-->
      <!--                        </div>-->

      <!--                        &lt;!&ndash; 둘째 주 &ndash;&gt;-->
      <!--                        <div><input type="checkbox" id="day-5" disabled><label for="day-5">5</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-6" disabled><label for="day-6">6</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-7" disabled><label for="day-7">7</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-8" disabled><label for="day-8">8</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-9" disabled><label for="day-9">9</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-10" disabled><label for="day-10">10</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-11" disabled><label for="day-11">11</label>-->
      <!--                        </div>-->

      <!--                        &lt;!&ndash; 셋째 주부터 활성화 &ndash;&gt;-->
      <!--                        <div><input type="checkbox" id="day-12"><label for="day-12">12</label></div>-->
      <!--                        <div><input type="checkbox" id="day-13"><label for="day-13">13</label></div>-->
      <!--                        <div><input type="checkbox" id="day-14"><label for="day-14">14</label></div>-->
      <!--                        <div><input type="checkbox" id="day-16"><label for="day-16">16</label></div>-->
      <!--                        <div><input type="checkbox" id="day-17"><label for="day-17">17</label></div>-->
      <!--                        <div><input type="checkbox" id="day-18"><label for="day-18">18</label></div>-->

      <!--                        <div><input type="checkbox" id="day-19"><label for="day-19">19</label></div>-->
      <!--                        <div><input type="checkbox" id="day-20"><label for="day-20">20</label></div>-->
      <!--                        <div><input type="checkbox" id="day-21"><label for="day-21">21</label></div>-->
      <!--                        <div><input type="checkbox" id="day-22"><label for="day-22">22</label></div>-->
      <!--                        <div><input type="checkbox" id="day-23"><label for="day-23">23</label></div>-->
      <!--                        <div><input type="checkbox" id="day-24"><label for="day-24">24</label></div>-->
      <!--                        <div><input type="checkbox" id="day-25"><label for="day-25">25</label></div>-->

      <!--                        <div><input type="checkbox" id="day-26"><label for="day-26">26</label></div>-->
      <!--                        <div><input type="checkbox" id="day-27" disabled><label for="day-27">27</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-28"><label for="day-28">28</label></div>-->
      <!--                        <div><input type="checkbox" id="day-29-alt"><label for="day-29-alt">29</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-30-alt"><label for="day-30-alt">30</label>-->
      <!--                        </div>-->
      <!--                        <div><input type="checkbox" id="day-31-alt" disabled><label-->
      <!--                                for="day-31-alt">31</label>-->
      <!--                        </div>-->
      <!--                    </section>-->
      <!--                </section>-->
      <!--                <button type="submit">확인</button>-->
      <!--            </form>-->
      <!--        </aside>-->
    </section>

  </main>
</template>

<style scoped>
.layout-body {
  .n-bar-underline {
    .item-wrapper {
      justify-content: space-between;
    }
  }

  .layout-main {
    display: flex;

    .layout-main-list {
      flex-grow: 1;
      width: 100%;

      .n-filter {
        .n-btn {
          --btn-font-size: 12px;
        }
      }

      /*===============================================================================================*/

      .list-header {
        padding: 8px 20px;

        div {
          .list-view-toggle {
            >span {
              display: none;
            }
          }
        }
      }

      /*===============================================================================================*/

      .n-card-container {
        padding: 8px 16px;
      }

      /*===============================================================================================*/

      .separator {
        display: flex;
        width: 1px;
        height: 16px;
        background-color: var(--color-base-6);
        margin-left: 4px;
        margin-right: 4px;
      }

      /*===============================================================================================*/

      .n-card {
        .card-header {
          .right>a {
            position: relative;
            /* a태그는 기본 position이 static. static이면 z-index가 안 먹힘*/
            z-index: 2;
          }
        }

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
      }

      /*===============================================================================================*/
    }

    .layout-main-aside {
      display: none;
      flex-shrink: 0;
      width: 250px;
      margin: 0 16px;
      background-color: var(--color-base-3);
    }
  }
}

@media (min-width: 400px) {
  .layout-body {
    .n-bar-underline {
      .item-wrapper {
        --bar-gap: 24px;
        justify-content: flex-start;
      }
    }
  }
}

@media (min-width: 768px) {
  .layout-body {
    margin-left: auto;
    margin-right: auto;
    /*min-width: 992px;*/
    width: 100%;
    max-width: 1092px;

    .n-title {
      >div {
        margin-right: calc(16px + 250px + 16px - 20px);
      }
    }

    .layout-main {
      .layout-main-list {}

      .layout-main-aside {
        display: flex;
      }
    }
  }

  .md\:d\:inline {
    display: inline;
  }

  .md\:d\:none {
    display: none;
  }
}
</style>