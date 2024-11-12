if (process.client && !window.Kakao) {
    // 카카오 SDK 로드
    const script = document.createElement('script');
    script.src = 'https://developers.kakao.com/sdk/js/kakao.js';

    script.onload = () => {
        // 카카오 SDK가 로드되면 초기화
        if (window.Kakao) {
            // 여기에 본인의 카카오 앱 키를 입력하세요.
            Kakao.init('6c64b7e06b1c1dfdbd86e1f3a5021286');
            console.log('Kakao SDK 로드 완료');
        }
    };

    script.onerror = () => {
        console.error('카카오 SDK 로드 실패');
    };

    // 문서의 head에 스크립트 태그 추가
    document.head.appendChild(script);
}
