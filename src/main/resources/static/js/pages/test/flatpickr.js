document.addEventListener("DOMContentLoaded", () => {
    const today = new Date();

    flatpickr("#start-date", {
        locale: "ko",
        dateFormat: "Y-m-d",
        defaultDate: today,
    });

    flatpickr("#end-date", {
        locale: "ko",
        dateFormat: "Y-m-d",
        defaultDate: today,
    });

    document.getElementById("submit-btn").addEventListener("click", () => {
        const startDate = document.getElementById("start-date").value;
        const endDate = document.getElementById("end-date").value;

        if (startDate && endDate) {
            alert(`${startDate} - ${endDate}`);
        } else {
            alert("날짜 선택 안됨.");
        }
    });
});
