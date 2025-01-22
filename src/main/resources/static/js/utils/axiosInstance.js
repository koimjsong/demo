const BASE_URL = "http://localhost:8080";

// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: BASE_URL,
  timeout: 100000, // 요청 타임아웃 (100초)
  headers: {
    "Content-Type": "application/json",
    Accept: "application/json",
  },
  withCredentials: true, // 쿠키 포함 설정
});


// 요청 인터셉터
axiosInstance.interceptors.request.use(
  (config) => {
    // 요청에 쿠키 포함
    config.withCredentials = true;

    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터
axiosInstance.interceptors.response.use(
  (response) => response, // 성공 응답 처리
  async (error) => {
    const originalRequest = error.config;

    // Access Token 만료 시 처리
    if (error.response && (error.response.status === 403 || error.response.status === 401) && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        // Refresh Token을 사용해 Access Token 갱신
        const refreshResponse = await axios.post(
          `${BASE_URL}/api/auth/refresh-token`,
          {},
          { withCredentials: true }
        );
        console.log("refresh Token을 사용해 Access Token 갱신합니다.");

        if (refreshResponse.data && refreshResponse.data.success) {

          // 원래 요청 재시도
          console.log("원래 요청 재시도합니다");
          return axiosInstance(originalRequest);
        } else {
            console.error("Access Token 갱신 실패: ", refreshResponse.data.message);
            // 세션 만료 처리
            handleSessionExpired();
        }
      } catch (refreshError) {
        console.error("리프레시 토큰 만료:", refreshError);
        handleSessionExpired();
      }
    }

    return Promise.reject(error);
  }
);


// 세션 만료 처리
const handleSessionExpired = () => {
    alert("세션이 만료되었습니다. 다시 로그인해주세요.");
    Cookies.remove("accessToken");
    Cookies.remove("refreshToken");
    window.location.href = "/email-login";
};

export default axiosInstance;
