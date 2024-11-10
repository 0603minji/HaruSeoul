export default () => {
    const id = useState('id', () => null);
    const username = useState('username', () => null);
    const email = useState('email', () => null);
    const birth = useState('birth', () => null);
    // const roles = useState('roles', () => []);
    const token = useState('token', () => null);

    const isAnonymous = () => {
        return !username.value || !token.value;
    }

    const login = (loginInfo) => {
        id.value = loginInfo.id;
        username.value = loginInfo.username;
        email.value = loginInfo.email;
        birth.value = loginInfo.birth;
        // roles.value = loginInfo.roles;
        token.value = loginInfo.token;

        if (process.client) {
            localStorage.setItem('id', JSON.stringify(loginInfo.id));
            localStorage.setItem('username', loginInfo.username);
            localStorage.setItem('email', loginInfo.email);
            localStorage.setItem('birth', loginInfo.birth);
            // localStorage.setItem('roles', JSON.stringify(loginInfo.roles)); //[] -> "[]"
            localStorage.setItem('token', loginInfo.token);
        }
        console.log(username.value);
    }

    const loadUserFromStorage = async () => {
        if (process.client) {
            const storedUsername = localStorage.getItem('username');
            const storedToken = localStorage.getItem('token');

            if (storedUsername && storedToken) {
                id.value = JSON.parse(localStorage.getItem('id'));
                token.value = storedToken;
                birth.value = localStorage.getItem('birth');
                username.value = storedUsername;
                email.value = localStorage.getItem('email');
            }
        }
    }

    const logout = () => {
        id.value = null;
        username.value = null;
        email.value = null;
        birth.value = null;
        // roles.value = [];
        token.value = null;
    }

    // const hasRole = role => roles.value.includes(role);

    return {
        id,
        username,
        email,
        birth,
        // roles,
        token,
        isAnonymous,
        loadUserFromStorage,
        login,
        // hasRole,
        logout
    }
}