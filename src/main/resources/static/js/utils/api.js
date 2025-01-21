import axiosInstance from "./axiosInstance.js";

/**
 * 공통 API 요청 핸들러 - 사용 안 함 or 수정해야 함
 * @param {string} endpoint - API endpoint
 * @param {string} method - HTTP method (e.g., GET, POST, PUT, DELETE)
 * @param {object} [data] - 요청 페이로드 (POST/PUT 요청)
 * @returns {Promise<object>} - 응답 데이터
 */
export const apiRequest = async (endpoint, method = "GET", data = null) => {
    try {
        const options = {
            url: endpoint,
            method,
            headers: {
                "Content-Type": "application/json",
/*                ...headers, // 추가 헤더 병합*/
            },
        };

        if (data) {
            options.data = data;
        }

        const response = await axiosInstance(options);
        return response.data;
    } catch (error) {
        console.error(`API 요청 실패: ${method} ${endpoint}`, error);
        throw error;
    }
};

/**
 * GET 요청 (쿼리 파라미터 포함)
 * @param {string} endpoint - API endpoint
 * @param {object} params - 요청에 필요한 쿼리 파라미터
 * @returns {Promise<object>} - 응답 데이터
 */
export const getRequest = async (endpoint, params = {}) => {
    try {
        const response = await axiosInstance.get(endpoint, { params });
        return response.data;
    } catch (error) {
        console.error(`GET 요청 실패: ${endpoint}`, error);
        throw error;
    }
};

/**
 * POST 요청 (바디에 데이터 포함)
 * @param {string} endpoint - API endpoint
 * @param {object} data - 요청 바디에 포함될 데이터
 * @returns {Promise<object>} - 응답 데이터
 */
export const postRequest = async (endpoint, data = {}) => {
    try {
        const response = await axiosInstance.post(endpoint, data);
        return response.data;
    } catch (error) {
        console.error(`POST 요청 실패: ${endpoint}`, error);
        throw error;
    }
};

/**
 * PUT 요청 (바디에 데이터 포함)
 * @param {string} endpoint - API endpoint
 * @param {object} data - 요청 바디에 포함될 데이터
 * @returns {Promise<object>} - 응답 데이터
 */
export const putRequest = async (endpoint, data = {}) => {
    try {
        const response = await axiosInstance.put(endpoint, data);
        return response.data;
    } catch (error) {
        console.error(`PUT 요청 실패: ${endpoint}`, error);
        throw error;
    }
};

/**
 * DELETE 요청 (쿼리 파라미터 포함)
 * @param {string} endpoint - API endpoint
 * @param {object} params - 요청에 필요한 쿼리 파라미터
 * @returns {Promise<object>} - 응답 데이터
 */
export const deleteRequest = async (endpoint, params = {}) => {
    try {
        const response = await axiosInstance.delete(endpoint, { params });
        return response.data;
    } catch (error) {
        console.error(`DELETE 요청 실패: ${endpoint}`, error);
        throw error;
    }
};

