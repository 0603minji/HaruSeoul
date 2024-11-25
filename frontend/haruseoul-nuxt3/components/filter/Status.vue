<script setup>
import {onMounted, ref, defineEmits} from "vue";
import axios from "axios";
import {useReservationFetch} from "~/composables/useReservationFetch.js";

//=======================변수영역==========
const statuses = ref([]);
const selectedStatus = ref(0);
const selectedReservations = ref([]);  // 선택된 예약 목록
const emit = defineEmits(['selectStatusIds', 'selectedReservations']);
const token = localStorage.getItem("token");
const userDetail = useUserDetails();

//================Fetch Function==========
// 상태를 가져오는 비동기 함수
const fetchStatuses = async () => {
  try {
    const response = await axios.get("http://localhost:8083/api/v1/statuses", {
      headers: {
        Authorization: `Bearer ${token}` // JWT 토큰 추가
      }
    });
    statuses.value = response.data.filter(status => ![1, 2, 5].includes(status.id));
    console.log("Fetched statuses:", response.data);  // 데이터 확인 로그
    // 불러오고 필터링하면 css gap 이 적용되므로, 결제완료 상태를 필터링하여 가져와야 한다.
  } catch (error) {
    console.error("Error fetching statuses:", error);
  }
};

//=======================LifeCycle Functions==========
// 컴포넌트가 마운트될 때 상태를 가져오는 함수 호출
onMounted(() => {
  fetchStatuses();
});

const createQuery = (statusIds) => {
  const isDeleted = statusIds.includes(4);
  const params = {
    mIds: userDetail.id.value,
    isDeleted: isDeleted
  }
  if (isDeleted) {
    // isDeleted 가 true 일 경우 결제완료 상태 (id: 1, 2, 5)을 제외한 나머지 상태만 추가
    params.sIds = statusIds.filter(id => ![1, 2, 5].includes(id));  // 결제완료 상태를 제외
  } else {
    // isDeleted 가 false 일 경우 모든 상태를 그대로 추가
    params.sIds = statusIds;
  }

  return params;
}

// 예약 목록을 가져오는 비동기 함수
const fetchReservations = async (statusIds) => {
  try {
    const params = createQuery(statusIds); // 쿼리 생성 함수 호출
    console.log("파람값:",params)
    const response = await useReservationFetch(
        `http://localhost:8083/api/v1/guest/reservations`,
        {query: params}
    );

    console.log("Fetched reservations:", response);  // 데이터 확인 로그
    selectedReservations.value = response; // 예약 목록 저장
    emit('selectedReservations', selectedReservations.value);
  } catch (error) {
    console.error("Error fetching reservations:", error);
  }
};

//  전체 필터링 누르면 selectedStatus 값이 null 이 되면서 모든 예약 출력 ----------------------
const selectStatusAll = async () => {
  selectedReservations.value = [];
  selectedStatus.value = null; // status 필터링이 안된 상태
  try {
    await fetchReservations([1, 2, 3, 4, 5, 6]);
    emit('selectStatusIds', [1, 2, 3, 4, 5, 6]);
  } catch (error) {
    console.error("Error fetching all reservations:", error);
  }
};

//  예약 상태 선택시 서버에서 목록을 업데이트 ------------------------------------------------
const selectReservations = async (statusIds) => {
  // 만약 isDeleted가 존재한다면 '취소됨'으로 처리해놓고 아래 진행식이 진행되어야 한다.
  const params = createQuery(statusIds);
  if (params.isDeleted === true) {
    statusIds = [4];
  }

  if (statusIds.length > 0) {
    try {
      await fetchReservations(statusIds);  // ID를 통해 예약 목록을 가져옵니다.
      emit('selectStatusIds', statusIds);
    } catch (error) {
      console.error("Error fetching selected status:", error);
    }
  } else {
    console.log("No status selected.");
  }
};

// // Function to get the display value based on s.id
const getDisplayValue = (statusId) => {
  // 상태 ID에 따른 반환값
  switch (statusId) {
    case 4:
      return "취소됨";
    case 1:
    case 2:
    case 5:
      return "결제완료";
    case 6:
      return "예약확정";
    case 3:
      return "이용완료";
    default:
      return ""; // 매핑되지 않은 상태의 기본값
  }
};

</script>

<template>
  <section class="n-filter n-filter-rv bg-color:base-1 padding-x:6">
    <h1 class="d:none">필터</h1>
    <div class="overflow-x:auto">
      <ul class="item-wrapper padding-y:6">
        <li>
          <NuxtLink href="" class="n-btn n-btn-rv-filter n-btn:hover"
                    :class="{ active: selectedStatus === null || selectedStatus === 0 }"
                    @click.prevent="(e) => { e.preventDefault(); selectStatusAll(); }"
          >전체
          </NuxtLink>
        </li>

        <!-- 결제완료 버튼을 1개로 고정 -->
        <li>
          <NuxtLink @click.prevent="(e) => { e.preventDefault(); selectReservations([1, 2, 5]); }" href=""
                    class="n-btn n-btn n-btn-rv-filter n-btn:hover"
                    :class="{ active: selectedStatus === '결제완료' }">결제완료
          </NuxtLink>
        </li>

        <!--         나머지 상태 버튼들 -->
        <li v-for="s in statuses.sort((a, b) => b.id - a.id)" :key="s.id">
          <NuxtLink @click.prevent="(e) => { e.preventDefault(); selectReservations([s.id])}" href=""
                    class="n-btn n-btn n-btn-rv-filter n-btn:hover"
                    :class="{ active: selectedStatus === s.id }">{{ getDisplayValue(s.id) }}
          </NuxtLink>
        </li>
      </ul>
    </div>
  </section>
</template>
