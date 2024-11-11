import useUserDetails from "~/composables/useUserDetails.js";


export function useDataFetch(url, options={}){

    const {token} = useUserDetails();

    options.headers = {
        ...options.headers,
        ...(token.value && {Authorization: `Bearer ${token.value}`})
        ,
    }

    const config = useRuntimeConfig();
    return $fetch(`${config.public.apiBase}${url}`,options);
}