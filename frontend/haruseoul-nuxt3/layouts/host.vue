<script setup>
import Header from "~/components/host/Header.vue";
import Footer from "~/components/Footer.vue";
</script>
<template>
  <div class="layout-root">
    <Header />
    <slot />
    <Footer />
  </div>
</template>
