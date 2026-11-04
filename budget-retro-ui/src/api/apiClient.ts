import axios from 'axios';

const API_BASE_URL = "http://localhost:8080";

const apiClient = axios.create({
    baseURL: API_BASE_URL
});

apiClient.interceptors.request.use(
    (config) => {
        // could log here?
        return config;
    },
    (error) => {
        // could log an error here
        return Promise.reject(error);
    },
);

export const handleApiError = (error) => {
    const status = error?.response?.status;
    console.log(status);

    return Promise.resolve();
}

export const handleResponseError = async (error) => {
    const originalRequest = error.config;

    console.log(originalRequest);


    handleApiError(error);

    return Promise.reject(error);
}

apiClient.interceptors.response.use((response) => {
    return response;
}, handleResponseError);

export default apiClient;

export const createApiClient = (basePath) => {
    return {
        get: (path = '', config = {},) => apiClient.get(`${basePath}${path}`, config),
        post: (path = '', data = {}, config = {},) => apiClient.post(`${basePath}${path}`, data, config),
        put: (path = '', data = {}, config = {},) => apiClient.put(`${basePath}${path}`, data, config),
        patch: (path = '', data = {}, config = {},) => apiClient.patch(`${basePath}${path}`, data, config),
        delete: (path = '', config = {},) => apiClient.delete(`${basePath}${path}`, config),
    };
};