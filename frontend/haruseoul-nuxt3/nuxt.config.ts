// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2024-04-03',
  devtools: { enabled: true },
  css: ['@/assets/bundle.css'],
  runtimeConfig: {
    public: {
      apiBase: 'http://localhost:8083/api/v1/'
    }
  },
  plugins: [
    { src: '~/plugins/kakao.js', mode: 'client' }  // 카카오 SDK 플러그인 추가 (클라이언트에서만 로드)
  ],
  nitro: {
    devServer: {
      port: 3003, // 원하는 포트 번호로 변경
    },
  }
});