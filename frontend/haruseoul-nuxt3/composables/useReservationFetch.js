import useUserDetails from "~/composables/useUserDetails.js";

export function useReservationFetch(url, options = {}) {
    const { token } = useUserDetails();
    const config = useRuntimeConfig();

    // 옵션의 헤더에 토큰이 있는 경우 Authorization을 추가
    options.headers = {
        ...options.headers,
        ...(token.value && { Authorization: `Bearer ${token.value}` }),
    };

    // URL이 중복되지 않도록 apiBase와 함께 구성
    const apiUrl = url.startsWith("http") ? url : `${config.public.apiBase}${url}`;
    return $fetch(apiUrl, options);
}