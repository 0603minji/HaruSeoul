import useUserDetails from "~/composables/useUserDetails.js";

export function useAuthFetch(url, options = {}) {
    const {token} = useUserDetails();

    options.headers = {
        ...options.headers,
    ...(token.value && {Authorization: `Bearer ${token.value}`})
        ,
}
    const config = useRuntimeConfig();
    return useFetch(`${config.public.apiBase}${url}`,options);
}