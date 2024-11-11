import useUserDetails from "~/composables/useUserDetails";

export default defineNuxtRouteMiddleware(async (to) => {
    const userDetails = useUserDetails();
    //
    // // LocalStorage에서 사용자 정보를 불러옴
    // await userDetails.loadUserFromStorage();

    // console.log("유저Id:", userDetails.userId.value);
    // console.log("토큰:", userDetails.token.value);
    // console.log("익명 여부:", userDetails.isAnonymous());

    // 로그인이 필요한 경로에서 인증 여부 확인
    if (((to.path.startsWith("/host")
        || to.path.startsWith("/guest"))
        || to.path.startsWith("/mypage")) 
        && userDetails.isAnonymous()) {
        console.log("익명 사용자입니다. 로그인 페이지로 리다이렉트합니다.");
        return  navigateTo(`/signin?returnURL=${to.fullPath}`);
    }
});
