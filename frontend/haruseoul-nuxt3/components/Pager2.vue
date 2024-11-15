<script setup>
import {useRoute} from 'vue-router';

const emit = defineEmits(['pageChange']);
const props = defineProps({
  pageNumbers: {
    type: Array,
    default: []
  },
  startNum: {
    type: Number,
    default: 1
  },
  totalPages: {
    type: Number,
    default: 0
  }
});

const route = useRoute();

</script>

<template>
  <ul class="pagination">
    <li>
      <NuxtLink @click="emit('pageChange', (props.startNum - 1 < 1) ? 1 : props.startNum - 1)" class="nuxt-link n-btn"
                :to="{ path: 'reservations', query: { ...route.query, p: (props.startNum - 1 < 1) ? 1 : props.startNum - 1} }">
        〈〈
      </NuxtLink>
    </li>
    <li v-for="p in props.pageNumbers" :key="p">
      <NuxtLink @click="emit('pageChange', p)" class="nuxt-link n-btn"
                :class="{'inactive': p > props.totalPages, 'active' : p === (Number(route.query.p) || 1)}"
                :to="{ path: 'reservations', query: { ...route.query, p: p } }">
        {{ p }}
      </NuxtLink>
    </li>
    <li>
      <NuxtLink @click="emit('pageChange', (props.startNum + 5 > props.totalPages) ? props.totalPages : props.startNum + 5)" class="nuxt-link n-btn"
                :to="{ path: 'reservations', query: { ...route.query, p: (props.startNum + 5 > props.totalPages) ? props.totalPages : props.startNum + 5} }">
        〉〉
      </NuxtLink>
    </li>
  </ul>
</template>

<style scoped>


.pagination {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin: 20px 0;

  li {
    .nuxt-link {
      --btn-padding: 0;
      --btn-bg-hover: var(--color-base-4);
      transition: 0.1s;

      padding: 4px 10px;
      border: none;
      border-radius: 4px;
      background-color: var(--color-base-3);
      cursor: pointer;
    }

    .nuxt-link:hover {
      background-color: var(--btn-bg-hover);
    }

    .nuxt-link.active {
      color: var(--color-base-1);
      background-color: var(--color-main-2);
    }
    .nuxt-link.active:hover {
      background-color: var(--color-main-1);
    }

    .nuxt-link.inactive {
      pointer-events: none; /* 클릭을 비활성화 */
      opacity: 0.5; /* 비활성화된 링크 시각적으로 흐리게 */
    }
  }
}
</style>