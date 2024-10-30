
import Guest from "../../layouts/Guest.vue";
import reservationRouter from './reservations/router.js';
import Index from './Index.vue'
export default {
    path: "/guest", component: Guest,
    children: [
        {path: "index", component: Index},
        reservationRouter
    ]
}