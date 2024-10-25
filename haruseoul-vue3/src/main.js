import {createMemoryHistory, createRouter, createWebHistory,} from "vue-router";
import {createApp} from "vue";

import hostRouter from './pages/host/router.js';

import App from "./App.vue";
import Guest from "./layouts/Guest.vue";
import GuestReservationList from "./pages/guest/reservation/List.vue";
import GuestReservationDetail from "./pages/guest/reservation/Detail.vue";
import Anonymous from "./layouts/Anonymous.vue";
import ProgramList from "@/pages/anonymous//List.vue";
import ProgramDetail from "@/pages/anonymous//Detail.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/guest", component: Guest,
            children: [
                {path: "reservation/list", component: GuestReservationList},
                {path: "reservation/detail", component: GuestReservationDetail},
            ],
        },
        hostRouter,
        {
            path: "/", component: Anonymous,
            children:
                [
                    {path: "list", component: ProgramList},
                    {path: "detail", component: ProgramDetail}
                ]
        }
    ]
});

createApp(App).use(router).mount("#app");
