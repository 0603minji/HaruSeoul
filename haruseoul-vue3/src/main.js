import {createRouter, createWebHistory,} from "vue-router";
import {createApp} from "vue";

import guestRouter from './pages/guest/router.js';
import hostRouter from './pages/host/router.js';

import App from "./App.vue";

import Anonymous from "./layouts/Anonymous.vue";
import ProgramList from "@/pages/anonymous//List.vue";
import ProgramDetail from "@/pages/anonymous//Detail.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        guestRouter,
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
