import Index from "@/pages/host/reservations/Index.vue";

export default {
    path: "reservations",
    children: [
        {path: "", component: Index}
    ]
}