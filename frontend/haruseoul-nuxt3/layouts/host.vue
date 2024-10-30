<script setup>
import Header from "~/components/host/Header.vue";
import Footer from "~/components/Footer.vue";
</script>
<template>
  <div>
    <Header />
    <slot />
    <Footer />
  </div>
</template>
