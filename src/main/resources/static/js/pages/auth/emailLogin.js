import {apiRequest, getRequest, postRequest} from "../../utils/api.js";

document.addEventListener("DOMContentLoaded", () => {
    const sendEmailButton = document.getElementById("send-email");
    const verifyForm = document.getElementById("verify-form");

    // 이메일로 인증코드 전송
    sendEmailButton.addEventListener("click", async (event) => {
        event.preventDefault(); // 기본 동작 방지
        verifyForm.style.display = "block";
        handleSendCode();
    });

    // 이메일 인증
    document.getElementById("verify").addEventListener("click", (event) => {
        event.preventDefault();
        handleEmailVerification();
    });

});

async function handleSendCode() {
    const emailInput = document.getElementById("email");
    const email = emailInput.value;

    if (!email || !validateEmail(email)) {
        alert("올바른 이메일 주소를 입력해주세요.");
        return;
    }

    try {
        // API 호출
        const response = await postRequest(
            "/api/email/send-code",
            { email: email }
        );

        localStorage.setItem("email", email);

        /*            if (response.status === 200 && response.data.success) {
                        alert("인증번호가 이메일로 전송되었습니다.");
                    } else {
                        alert(response.data.message || "인증번호 전송에 실패했습니다.");
                    }*/
    } catch (error) {
        console.error("API 호출 오류:", error);
        alert("서버와 통신 중 오류가 발생했습니다. 다시 시도해주세요.");
    }
}

async function handleEmailVerification() {
    const email = localStorage.getItem("email");
    const verificationCode = document.getElementById("verification-code").value;

    if (!email) {
        alert("이메일이 없습니다. 인증번호를 요청하세요.");
        return;
    }

    if (!verificationCode) {
        alert("인증번호를 입력하세요.");
        return;
    }

    try {
        const response = await getRequest(
            "/api/email/verify",
            {email: decodeURIComponent(email), code: decodeURIComponent(verificationCode)}
            );

            console.log(response);

        if (response.success) {
            alert("이메일 인증에 성공했습니다.");
            document.cookie = `email=${encodeURIComponent(email)}; path=/; secure; HttpOnly;`;
            window.location.href = response.redirectUrl;
        } else {
            alert(response.message || "인증 실패");
        }
    } catch (error) {
        console.error("인증 요청 중 오류 발생:", error);
        alert("인증 요청 중 오류가 발생했습니다.");
    } finally {
        //localStorage.removeItem("email");
    }
}

/**
 * 이메일 유효성 검사 함수
 * @param {string} email - 입력된 이메일
 * @returns {boolean} 유효 여부
 */
function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}