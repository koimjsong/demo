document.addEventListener("DOMContentLoaded", () => {
    const phoneNumberInput = document.getElementById("phoneNumber");
    const nextButton = document.getElementById("nextButton");

    phoneNumberInput.addEventListener("input", (event) => {

        // 숫자만 입력되도록 필터링
        const rawValue = event.target.value.replace(/[^0-9]/g, "");

        // 형식 적용
        let formattedNumber = rawValue;
        if (rawValue.length > 3 && rawValue.length <= 7) {
            formattedNumber = `${rawValue.slice(0, 3)} ${rawValue.slice(3)}`;
        } else if (rawValue.length > 7) {
            formattedNumber = `${rawValue.slice(0, 3)} ${rawValue.slice(3, 7)} ${rawValue.slice(7)}`;
        }

        phoneNumberInput.value = formattedNumber;

        if (phoneNumberInput.value.length == 13) {
            nextButton.disabled = false;
            nextButton.classList.add("active");
        } else {
            nextButton.disabled = true;
            nextButton.classList.remove("active");
        }
    });
});
