import {apiRequest, getRequest} from "../../utils/api.js";

const fetchMockData = async () => {
  try {
    // Mock API 호출
    return await getRequest('/posts');
  } catch (error) {
    console.error("Mock API 호출 실패:", error);
    return [];
  }
};

const renderMockData = async () => {
  const mockData = await fetchMockData();
  const container = document.getElementById("mock-data-container");

  // 데이터를 화면에 표시
  container.innerHTML = mockData
    .map((item) => `<div class="card"><h3>${item.title}</h3><p>${item.body}</p></div>`)
    .join("");
};

// 페이지 로드 시 실행
document.addEventListener("DOMContentLoaded", renderMockData);
