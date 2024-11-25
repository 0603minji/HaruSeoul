export default () => {
    const id = useState('id', () => null);
    const userId = useState('userId', () => null);
    const email = useState('email', () => null);
    const birth = useState('birth', () => null);
    const username = useState('username', () => null);
    // const roles = useState('roles', () => []);
    const profileImgSrc = useState('profileImgSrc', () => null);
    const token = useState('token', () => null);

    const isAnonymous = () => {
        return !userId.value || !token.value;
    }

    const login = (loginInfo) => {
        console.log("유저디테일에서 확인용:",loginInfo);
        id.value = loginInfo.id;
        userId.value = loginInfo.userId;
        username.value = loginInfo.username;
        email.value = loginInfo.email;
        birth.value = loginInfo.birth;
        profileImgSrc.value = loginInfo.profileImgSrc;
        // roles.value = loginInfo.roles;
        token.value = loginInfo.token;

        if (process.client) {
            localStorage.setItem('id', JSON.stringify(loginInfo.id));
            localStorage.setItem('userId', loginInfo.userId);
            localStorage.setItem('email', loginInfo.email);
            localStorage.setItem('birth', loginInfo.birth);
            localStorage.setItem('username', loginInfo.username);
            localStorage.setItem('profileImgSrc', loginInfo.profileImgSrc);
            localStorage.setItem('token', loginInfo.token);
            // localStorage.setItem('roles', JSON.stringify(loginInfo.roles)); //[] -> "[]"
        }

    }

    const loadUserFromStorage = async () => {
        if (process.client) {
            const storedUserId = localStorage.getItem('userId');
            const storedToken = localStorage.getItem('token');

            if (storedUserId && storedToken) {
                userId.value = storedUserId;
                token.value = storedToken;
                id.value = JSON.parse(localStorage.getItem('id'));
                username.value = localStorage.getItem('username');
                birth.value = localStorage.getItem('birth');
                email.value = localStorage.getItem('email');
                profileImgSrc.value = localStorage.getItem('profileImgSrc');
            }
        }
    }

    const logout = () => {
        id.value = null;
        userId.value = null;
        email.value = null;
        birth.value = null;
        username.value = null;
        // roles.value = [];
        token.value = null;
        if (process.client) {
            localStorage.removeItem('id');
            localStorage.removeItem('userId');
            localStorage.removeItem('email');
            localStorage.removeItem('birth');
            localStorage.removeItem('username');
            localStorage.removeItem('token');
            localStorage.removeItem('profileImgSrc');
        }

    }

    // const hasRole = role => roles.value.includes(role);

    return {
        id,
        userId,
        email,
        birth,
        username,
        token,
        profileImgSrc,
        // roles,
        isAnonymous,
        loadUserFromStorage,
        login,
        logout
        // hasRole,
    }
}