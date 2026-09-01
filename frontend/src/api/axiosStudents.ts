import axios from "axios";

const studentsApi = axios.create({
  baseURL: import.meta.env.VITE_STUDENTS_API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

studentsApi.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export default studentsApi;