document.addEventListener("DOMContentLoaded", () => {

    const responseOutput = document.getElementById("response-output");
    const userId = document.getElementById("user-id");
    const title = document.getElementById("title-content");
    const content = document.getElementById("update-content");

    getButtonEvent(responseOutput);
    deleteButtonEvent(responseOutput);
    postButtonEvent(userId, title, content);
    updateButtonEvent(userId, title, content);
});

function getButtonEvent(responseOutput) {
    document.getElementById("button1").addEventListener("click", async () => {
        try {
            const response = await getRequest("api/home/posts");
            responseOutput.textContent = JSON.stringify(response, null, 2);
        } catch (error) {
            responseOutput.textContent = `Error: ${error.message}`;
        }
    });
}

function deleteButtonEvent(responseOutput) {
    document.getElementById("button2").addEventListener("click", async () => {
        try {
            const postId = prompt("삭제할 게시물 ID를 입력하세요:");

            const response = await deleteRequest(`api/home/posts/${postId}`);
            responseOutput.textContent = JSON.stringify(response, null, 2);
        } catch (error) {
            responseOutput.textContent = `Error: ${error.message}`;
        }
    });
}

function postButtonEvent(userId, title, content) {
    document.getElementById("post-button").addEventListener("click", async () => {

        try {

            const response = await postRequest("/api/home/posts", {
                userId: parseInt(userId.value, 10),
                title: title.value,
                body: content.value,
            });

            document.getElementById("response-output").textContent = JSON.stringify(response, null, 2);
        } catch (error) {
            console.error("Error during post request:", error);
            document.getElementById("response-output").textContent = `Error: ${error.response?.message || error.message}`;
        }
    });
}

function updateButtonEvent(userId, title, content) {
    document.getElementById("update-button").addEventListener("click", async () => {

        const postId = prompt("수정할 게시물 ID를 입력하세요:");

        try {
            const payload = {
                userId: parseInt(userId.value, 10),
                title: title.value,
                body: content.value,
            };
            const response = await putRequest(`/api/home/posts/${postId}`, payload);
            document.getElementById("response-output").textContent = JSON.stringify(response, null, 2);
        } catch (error) {
            console.error("PUT 요청 실패:", error);
            document.getElementById("response-output").textContent = `Error: ${error.response?.data?.message || error.message}`;
        }
    });
}




