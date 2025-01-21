const BASE_URL = "http://localhost:8080";

// Axios 인스턴스 생성
const axiosInstance = axios.create({
  //baseURL: "https://jsonplaceholder.typicode.com", // Mock API URL
    baseURL: BASE_URL,
  timeout: 10000, // 요청 타임아웃 (10초)
  headers: {
    "Content-Type": "application/json",
    Accept: "application/json",
  },
});

// 요청 인터셉터
axiosInstance.interceptors.request.use(
    async (config) => {
        // Access Token 검증
        try {
            const verifyResponse = await axiosInstance.get("/api/auth/protected-resource", {
                withCredentials: true, // 쿠키 전송 활성화
            });

            console.log("토큰 검증 성공:", verifyResponse.data);
        } catch (error) {
            if (error.response.status === 401) {
                console.warn("토큰 검증 실패:", error.response.data.message);
                handleSessionExpired();
            }
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// 세션 만료 처리
const handleSessionExpired = () => {
    alert("세션이 만료되었습니다. 다시 로그인해주세요.");
    Cookies.remove("accessToken");
    Cookies.remove("refreshToken");
    window.location.href = "/email-login";
};

// 응답 인터셉터
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
      console.error("API 호출 중 오류 발생:", error);

      const originalRequest = error.config;

      // Access Token 만료(401) 처리
      if (error.response.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;

          try {
              // Back-end로 Refresh Token 검증 요청 (withCredentials로 HTTP-only 쿠키 포함)
              const refreshResponse = await axiosInstance.post("/api/auth/refresh-token", {}, {withCredentials: true});

              if (refreshResponse.data && refreshResponse.data.accessToken) {
                  // 새로운 Access Token이 반환되었으면 요청 헤더 갱신
                  originalRequest.headers.Authorization = `Bearer ${refreshResponse.data.accessToken}`;

                  // 원래 요청 재시도
                  return axiosInstance(originalRequest);
              }
          } catch (refreshError) {
              console.error("세션 만료. 다시 로그인 필요:", refreshError);
              handleSessionExpired(); // 세션 만료 처리
          }
      }

      return Promise.reject(error);
  }
);

export default axiosInstance;