export type Role = "ADMIN" | "ALUMNO" | "PERSONAL_ADMINISTRATIVO";

export interface MockUser {
  id: string;
  username: string;
  fullName: string;
  role: Role;
  email: string;
}

export const mockUsers: MockUser[] = [
  { id: "u1", username: "admin", fullName: "Ana Administradora", role: "ADMIN", email: "admin@sgu.edu.mx" },
  { id: "u2", username: "jperez", fullName: "Juan Pérez Gómez", role: "ALUMNO", email: "jperez@alumnos.sgu.edu.mx" },
  { id: "u3", username: "control1", fullName: "Laura Ramírez", role: "PERSONAL_ADMINISTRATIVO", email: "control1@sgu.edu.mx" },
];

export interface Subject {
  id: string;
  code: string;
  name: string;
  credits: number;
  semester: number;
  status: "APROBADA" | "REPROBADA" | "CURSANDO" | "PENDIENTE";
  grade?: number;
}

export const mockCurriculum: Subject[] = [
  { id: "s1", code: "ISC-101", name: "Programación I", credits: 8, semester: 1, status: "APROBADA", grade: 8.5 },
  { id: "s2", code: "ISC-102", name: "Matemáticas Discretas", credits: 7, semester: 1, status: "APROBADA", grade: 9.0 },
  { id: "s3", code: "ISC-103", name: "Fundamentos de Redes", credits: 6, semester: 1, status: "REPROBADA", grade: 5.0 },
  { id: "s4", code: "ISC-201", name: "Programación II", credits: 8, semester: 2, status: "CURSANDO" },
  { id: "s5", code: "ISC-202", name: "Estructura de Datos", credits: 8, semester: 2, status: "PENDIENTE" },
  { id: "s6", code: "ISC-203", name: "Bases de Datos I", credits: 8, semester: 2, status: "CURSANDO" },
  { id: "s7", code: "ISC-301", name: "Sistemas Operativos", credits: 8, semester: 3, status: "PENDIENTE" },
  { id: "s8", code: "ISC-302", name: "Bases de Datos II", credits: 8, semester: 3, status: "PENDIENTE" },
  { id: "s9", code: "ISC-303", name: "Ingeniería de Software I", credits: 7, semester: 3, status: "PENDIENTE" },
];

export interface StudentProfile {
  studentCode: string;
  fullName: string;
  career: string;
  currentSemester: number;
  status: string;
  curp: string;
  email: string;
  phone: string;
  address: string;
  admissionPeriod: string;
}

export const mockStudentProfile: StudentProfile = {
  studentCode: "2025ISC0001",
  fullName: "Juan Pérez Gómez",
  career: "Ingeniería en Sistemas Computacionales",
  currentSemester: 2,
  status: "ACTIVO",
  curp: "PEGJ060314HDFRZN08",
  email: "juan.perez@gmail.com",
  phone: "55 1234 5678",
  address: "Av. Reforma 123, Centro, CDMX",
  admissionPeriod: "2025-2",
};

export interface DocumentItem {
  id: string;
  type: string;
  fileName: string;
  status: "PENDIENTE" | "VALIDADO" | "RECHAZADO";
  uploadedAt: string;
  rejectionReason?: string;
}

export const mockDocuments: DocumentItem[] = [
  { id: "d1", type: "CURP", fileName: "curp_jperez.pdf", status: "VALIDADO", uploadedAt: "2025-07-01" },
  { id: "d2", type: "Acta de nacimiento", fileName: "acta_jperez.pdf", status: "PENDIENTE", uploadedAt: "2025-07-02" },
  {
    id: "d3",
    type: "Comprobante de domicilio",
    fileName: "domicilio_jperez.pdf",
    status: "RECHAZADO",
    uploadedAt: "2025-07-02",
    rejectionReason: "Comprobante con más de 3 meses de antigüedad",
  },
  { id: "d4", type: "Certificado de bachillerato", fileName: "", status: "PENDIENTE", uploadedAt: "-" },
  { id: "d5", type: "Identificación oficial", fileName: "", status: "PENDIENTE", uploadedAt: "-" },
];

export interface AdminStudent {
  id: string;
  studentCode: string;
  fullName: string;
  career: string;
  semester: number;
  status: string;
}

export const mockAdminStudents: AdminStudent[] = [
  { id: "b1", studentCode: "2025ISC0001", fullName: "Juan Pérez Gómez", career: "ISC", semester: 2, status: "ACTIVO" },
  { id: "b2", studentCode: "2025LAE0001", fullName: "María López Hernández", career: "LAE", semester: 2, status: "ACTIVO" },
  { id: "b3", studentCode: "2024ISC0087", fullName: "Diego Torres Ruiz", career: "ISC", semester: 5, status: "ACTIVO" },
  { id: "b4", studentCode: "2023ISC0033", fullName: "Sofía Castillo Vega", career: "ISC", semester: 8, status: "BAJA_TEMPORAL" },
];

export interface Career {
  id: string;
  code: string;
  name: string;
  totalSemesters: number;
  activeStudents: number;
}

export const mockCareers: Career[] = [
  { id: "c1", code: "ISC", name: "Ingeniería en Sistemas Computacionales", totalSemesters: 9, activeStudents: 210 },
  { id: "c2", code: "LAE", name: "Licenciatura en Administración", totalSemesters: 8, activeStudents: 150 },
];

export const mockPendingDocuments = [
  { id: "pd1", student: "Juan Pérez Gómez", type: "Acta de nacimiento", uploadedAt: "2025-07-02" },
  { id: "pd2", student: "María López Hernández", type: "CURP", uploadedAt: "2025-07-03" },
  { id: "pd3", student: "Diego Torres Ruiz", type: "Certificado de bachillerato", uploadedAt: "2025-07-04" },
];

export const mockEnrollments = [
  { id: "e1", student: "Juan Pérez Gómez", period: "2026-1", subjectsCount: 6, status: "ACTIVA" },
  { id: "e2", student: "María López Hernández", period: "2026-1", subjectsCount: 5, status: "ACTIVA" },
];

export const mockKpis = {
  totalStudents: 1240,
  activeCareers: 6,
  pendingDocuments: 37,
  openEnrollments: 812,
};
