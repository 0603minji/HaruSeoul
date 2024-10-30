import New from "@/pages/host/programs/New.vue";
import Index from "@/pages/host/programs/Index.vue";

export default {
    path: "programs",
    children: [
        {path: "", component: Index},
        {path: "new", component: New},

    ]
}