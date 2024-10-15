import {createMemoryHistory, createRouter, createWebHistory,} from "vue-router";
import {createApp} from "vue";
import App from "./App.vue";
import Guest from "./layouts/Guest.vue";
import GuestReservationList from "./pages/guest/reservation/List.vue";
import GuestReservationDetail from "./pages/guest/reservation/Detail.vue";
import HostProgramCreate from "./pages/host/program/Create.vue";
import Host from "./layouts/Host.vue";
import HostProgramList from "./pages/host/program/List.vue";
import HostReservationList from "./pages/host/reservation/List.vue";
import Default from "./layouts/Default.vue";
import ProgramList from "./pages/program//List.vue";
import ProgramDetail from "./pages/program//Detail.vue";

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
        {
            path: "/host", component: Host,
            children: [
                {path: "program/create", component: HostProgramCreate},
                {path: "program/list", component: HostProgramList},
                {path: "reservation/list", component: HostReservationList}
            ]
        },
        {
            path: "/program", component: Default,
            children:
                [
                    {path: "list", component: ProgramList},
                    {path: "detail", component: ProgramDetail}
                ]
        }
    ]
});

createApp(App).use(router).mount("#app");
