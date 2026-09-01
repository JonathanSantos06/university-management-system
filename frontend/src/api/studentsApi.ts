import { Student } from "../types/types";
import api from "./axiosStudents";

export const getStudentsApi = async (): Promise<Student[]> => {
  const response = await api.get<Student[]>("/students");
  return response.data;
};

export const getStudentApi = async (id: string): Promise<Student> => {
  const response = await api.get<Student>(`/students/${id}`);
  return response.data;
};

export const getStudentsExpApi = async (): Promise<Student> => {
  const response = await api.get<Student>("/students/me");
  return response.data;
};