import Index from "@/pages/guest/reservations/Index.vue";

export default {
    path: "reservations",
    children: [
        {path: "", component: Index}
    ]
}