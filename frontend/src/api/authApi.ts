import { LoginRequest, LoginResponse } from "../types/types";
import api from "./axios";

export const loginApi = async (
  credentials: LoginRequest
): Promise<LoginResponse> => {
  const response = await api.post<LoginResponse>(
    "/auth/login",
    credentials
  );

  return response.data;
};

export const logoutApi = async (
  refreshToken: string
): Promise<void> => {
  await api.post("/auth/logout", {
    refreshToken,
  });
};