<script setup>
import Header from "~/components/DefaultHeader.vue";
import Footer from "~/components/DefaultFooter.vue";
</script>
<template>
  <div class="layout-root">
    <!-- default header -->
    <Header />
    <slot />
    <!-- default footer -->
    <Footer />
  </div>
</template>
