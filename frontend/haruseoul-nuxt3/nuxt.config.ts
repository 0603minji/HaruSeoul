// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  devtools: { enabled: true },
  css: ['@/assets/bundle.css'],
  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:8080/api/v1/'
    }
  }
});