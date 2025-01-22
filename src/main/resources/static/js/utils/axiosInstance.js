const BASE_URL = "http://localhost:8080";

// Axios 인스턴스 생성
const axiosInstance = axios.create({
    baseURL: BASE_URL,
  timeout: 10000, // 요청 타임아웃 (10초)
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


// 세션 만료 처리
const handleSessionExpired = () => {
    alert("세션이 만료되었습니다. 다시 로그인해주세요.");
    Cookies.remove("accessToken");
    Cookies.remove("refreshToken");
    window.location.href = "/email-login";
};

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

        if (refreshResponse.data && refreshResponse.data.accessToken) {
          // 새로운 Access Token으로 요청 헤더 갱신
          console.log("새로운 Access Token으로 요청 헤더 갱신합니다.");
          Cookies.set("accessToken", refreshResponse.data.accessToken);
          originalRequest.headers.Authorization = `Bearer ${refreshResponse.data.accessToken}`;

          // 원래 요청 재시도
          console.log("원래 요청 재시도합니다");
          return axiosInstance(originalRequest);
        }
      } catch (refreshError) {
        console.error("리프레시 토큰 만료:", refreshError);
        handleSessionExpired();
      }
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;




/*
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

      const accessToken = Cookies.get("accessToken");
      const refreshToken = Cookies.get("refreshToken");

      if (accessToken) {
        config.headers.Authorization = `Bearer ${accessToken}`;
      }

      // Access Token 검증
      if (accessToken && isTokenExpired(accessToken)) {
          console.log("accessToken 만료 되었습니다. refreshToken을 이용하여 accessToken을 새로 발급합니다.");
          try {
              const newAccessToken = await refreshAccessToken(refreshToken);
              if (newAccessToken) {
                  console.log("쿠키에 새로운 Access Token이 저장되었습니다.");
                  Cookies.set("accessToken", newAccessToken, { path: "/", httpOnly: false });
                  config.headers.Authorization = `Bearer ${newAccessToken}`;
              } else {
                  handleSessionExpired();
              }
          } catch (error) {
              handleSessionExpired();
          }
      }
      return config;
  },
  (error) => Promise.reject(error)
);

// 토큰 만료 여부 검사
const isTokenExpired = (token) => {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.exp * 1000 < Date.now();
};

// Refresh Token을 사용하여 Access Token 갱신
const refreshAccessToken = async (refreshToken) => {
    try {
        const response = await axios.post(BASE_URL + "/api/auth/refresh", {
            refreshToken,
        });
        return response.data.accessToken;
    } catch (error) {
        console.error("토큰 갱신 실패:", error);
        return null;
    }
};

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
  (error) => {
    console.error("API 호출 중 오류 발생:", error);
    return Promise.reject(error);
  }
);

export default axiosInstance;*/
