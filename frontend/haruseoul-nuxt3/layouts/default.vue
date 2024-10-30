<script setup>
import Header from "~/components/Header.vue";
import Footer from "~/components/Footer.vue";
</script>
<template>
  <div>
    <!-- default header -->
    <Header />
    <slot />
    <!-- default footer -->
    <Footer />
  </div>
</template>
