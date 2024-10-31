<template>
    sigh up
</template>
<style scoped>
.container {
    width: 100%;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    text-align: center;

    .form-group {
        display: flex;
        flex-direction: column;
        margin-bottom: 15px;
        text-align: left;

        input[type="text"],
        input[type="password"],
        input[type="email"],
        input[type="tel"],
        input[type="date"] {
            width: 100%;
            padding: 10px;
            font-size: 1em;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
        }

        .button-container {
            gap: 1rem;
            display: flex;
        }

    }

}


.title {
    margin-bottom: 20px;
    font-weight: bold;
    font-size: 1.5em;
}


label {
    display: block;
    margin-bottom: 5px;
    font-weight: bold;
}

.error-message {
    padding: 5px;
    font-size: 0.85em;
}

.error-message.error {
    color: red;
    /* 빨간색 */
}

.error-message.success {
    color: #2ecc71;
    /* 초록색 */
}

.n-btn {
    border: none;
}

.n-btn-background-color\:main {
    margin: 20px 0;
    box-sizing: border-box;
    width: 100%;
}

@media (min-width: 768px) {
    .container {
        max-width: 1096px;
        margin: 0 auto;
        /* 가운데 정렬 */
    }
}
</style>