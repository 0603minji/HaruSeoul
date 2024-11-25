import {defineNuxtPlugin} from "#app";


export default defineNuxtPlugin((nuxtApp) => {
    const userDetails = useUserDetails();

    if(import.meta.client) {
        userDetails.loadUserFromStorage();
    }
});