document.addEventListener("DOMContentLoaded", () => {
    const carrierButton = document.getElementById("carrierButton");
    const modal = document.getElementById("carrierModal");
    const modalContent = modal.querySelector(".modal-content");
    const carrierItems = document.querySelectorAll(".carrier-item");

    // 통신사 선택 버튼 클릭 시 모달 표시
    carrierButton.addEventListener("click", () => {
        modal.style.display = "flex"; // 모달 보이기
        setTimeout(() => {
            modal.classList.add("show"); // 애니메이션 시작
        }, 10);
    });

    // 통신사 리스트 클릭 시 값 설정 및 모달 닫기
    carrierItems.forEach(item => {
        item.addEventListener("click", (event) => {
            carrierButton.textContent = event.target.textContent; // 선택된 통신사 버튼에 표시
            closeModal();
        });
    });

    // 모달 외부 클릭 시 닫기
    modal.addEventListener("click", (event) => {
        if (event.target === modal) {
            closeModal();
        }
    });

    // 모달 닫기 함수
    function closeModal() {
        modal.classList.remove("show"); // 애니메이션 종료
        setTimeout(() => {
            modal.style.display = "none"; // 모달 숨기기
        }, 300); // 애니메이션 지속 시간
    }
});
