import {
  createMemoryHistory,
  createRouter,
  createWebHistory,
} from "vue-router";
import { createApp } from "vue";
import App from "./App.vue";
import Guest from "./layouts/Guest.vue";
import GuestReservationList from "./pages/guest/reservation/List.vue";
import GuestReservationDetail from "./pages/guest/reservation/Detail.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    // { path: "/", component: HomeView },
    {
      path: "/guest",
      component: Guest,
      children: [
        { path: "reservation/list", component: GuestReservationList },
        { path: "reservation/detail", component: GuestReservationDetail },
      ],
    },
  ],
});

createApp(App).use(router).mount("#app");
