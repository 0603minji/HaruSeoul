<script setup>
import {useRoute} from 'vue-router';

const emit = defineEmits(['pageChange']);
const props = defineProps({
  href: {
    type: String,
    default: "./"
  },
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

console.log('Pager2, route.query: ', route.query)
</script>

<template>
  <div class="mt:4 text-align:center">
    <ul class="n-bar">
      <li>
        <NuxtLink @click="emit('pageChange', startNum - 1)" class="n-btn"
                  :to="{ path: 'reservations', query: { ...route.query, p: (startNum - 1 < 1) ? 1 : startNum - 1} }">
          이전
        </NuxtLink>
      </li>
      <li v-for="p in pageNumbers" :key="p">
        <NuxtLink @click="emit('pageChange', p)" class="n-btn"
                  :class="{ active: p === (route.query.p || 1) }"
                  :to="{ path: 'reservations', query: { ...route.query, p: p } }">
          {{ p }}
        </NuxtLink>
      </li>
      <li>
        <NuxtLink @click="emit('pageChange', startNum + 5)" class="n-btn"
                  :to="{ path: 'reservations', query: { ...route.query, p: (startNum + 5 > totalPages) ? totalPages : startNum + 5} }">
          다음
        </NuxtLink>
      </li>
    </ul>
  </div>
</template>