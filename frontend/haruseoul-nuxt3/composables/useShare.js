// 1. nuxt.config.js에 클라이언트 사이드에서만 로드하도록하는 플러그인 추가
// 2. 카카오 SDK 를 로드하고 초기화하는 플러그인을 ~/plugins/kakao.js 파일에 작성
// 3. 아래의 코드 설정으로 카카오톡 공유하기 기능 사용 가능
// 해당 설정은 만들어둔 버튼을 바탕으로 기능을 구현하게 해주는 형식

export default function useShare() {
    // 카카오 SDK가 클라이언트에서만 로드되도록 확인
    if (process.client && !window.Kakao) {
        // 카카오 SDK 로드
        const script = document.createElement('script');
        script.src = 'https://developers.kakao.com/sdk/js/kakao.js';
        script.onload = () => {
            // SDK 로드 후 초기화
            Kakao.init('6c64b7e06b1c1dfdbd86e1f3a5021286'); // 본인의 카카오 자바스크립트 앱 키를 입력
        };
        document.head.appendChild(script);
    }

    const shareToKakao = (url) => {
        if (process.client && window.Kakao) {
            // 카카오 공유 API 호출
            Kakao.Share.sendDefault({
                objectType: "feed",
                content: {
                    title: "Haru Seoul!",
                    description: "로컬 서울 여행을 즐겨보세요!",
                    imageUrl: "https://ifh.cc/g/1zT2dF.jpg", // 이미지 URL 설정
                    link: {
                        mobileWebUrl: url,
                        webUrl: url,
                    },
                },
                buttons: [
                    {
                        title: "Haru Seoul!",
                        link: {
                            mobileWebUrl: "http://localhost:3003/",
                            webUrl: "http://localhost:3003/",
                        },
                    },
                ],
            });
        } else {
            console.error("카카오 SDK가 로드되지 않았습니다.");
        }
    };

    return {
        shareToKakao,
    };
}
