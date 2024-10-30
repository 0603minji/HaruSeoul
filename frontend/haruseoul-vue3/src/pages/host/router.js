
import Host from "../../layouts/Host.vue";
import programRouter from './programs/router.js';
import reservationRouter from './reservations/router.js';
import Index from './Index.vue'
export default {
    path: "/host", component: Host,
    children: [
        {path: "index", component: Index},
        programRouter,
        reservationRouter
    ]
}