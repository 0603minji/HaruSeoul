<script setup>
import Header from "~/components/host/DefaultHeader.vue";
import Footer from "~/components/DefaultFooter.vue";
</script>
<template>
  <div class="layout-root">
    <Header />
    <slot />
    <Footer />
  </div>
</template>
