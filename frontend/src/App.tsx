import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { Login } from "./pages/Login";
import { StudentDashboard } from "./pages/student/Dashboard";
import { StudentProfilePage } from "./pages/student/Profile";
import { StudentSubjects } from "./pages/student/Subjects";
import { StudentCurriculum } from "./pages/student/Curriculum";
import { StudentKardex } from "./pages/student/Kardex";
import { StudentDocuments } from "./pages/student/Documents";
import { AdminDashboard } from "./pages/admin/Dashboard";
import { AdminStudents } from "./pages/admin/Students";
import { AdminCareers } from "./pages/admin/Careers";
import { AdminSubjects } from "./pages/admin/Subjects";
import { AdminCurriculum } from "./pages/admin/Curriculum";
import { AdminEnrollments } from "./pages/admin/Enrollments";
import { AdminGrades } from "./pages/admin/Grades";
import { AdminDocuments } from "./pages/admin/Documents";
import { ProtectedRoute } from "./router/ProtectedRoute";

const App: React.FC = () => (
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/login" element={<Login />} />

      <Route path="/student/dashboard" element={<ProtectedRoute allow={["ALUMNO"]}><StudentDashboard /></ProtectedRoute>} />
      <Route path="/student/profile" element={<ProtectedRoute allow={["ALUMNO"]}><StudentProfilePage /></ProtectedRoute>} />
      <Route path="/student/subjects" element={<ProtectedRoute allow={["ALUMNO"]}><StudentSubjects /></ProtectedRoute>} />
      <Route path="/student/curriculum" element={<ProtectedRoute allow={["ALUMNO"]}><StudentCurriculum /></ProtectedRoute>} />
      <Route path="/student/kardex" element={<ProtectedRoute allow={["ALUMNO"]}><StudentKardex /></ProtectedRoute>} />
      <Route path="/student/documents" element={<ProtectedRoute allow={["ALUMNO"]}><StudentDocuments /></ProtectedRoute>} />

      <Route path="/admin/dashboard" element={<ProtectedRoute allow={["ADMIN", "PERSONAL_ADMINISTRATIVO"]}><AdminDashboard /></ProtectedRoute>} />
      <Route path="/admin/students" element={<ProtectedRoute allow={["ADMIN", "PERSONAL_ADMINISTRATIVO"]}><AdminStudents /></ProtectedRoute>} />
      <Route path="/admin/careers" element={<ProtectedRoute allow={["ADMIN"]}><AdminCareers /></ProtectedRoute>} />
      <Route path="/admin/subjects" element={<ProtectedRoute allow={["ADMIN"]}><AdminSubjects /></ProtectedRoute>} />
      <Route path="/admin/curriculum" element={<ProtectedRoute allow={["ADMIN"]}><AdminCurriculum /></ProtectedRoute>} />
      <Route path="/admin/enrollments" element={<ProtectedRoute allow={["ADMIN", "PERSONAL_ADMINISTRATIVO"]}><AdminEnrollments /></ProtectedRoute>} />
      <Route path="/admin/grades" element={<ProtectedRoute allow={["ADMIN", "PERSONAL_ADMINISTRATIVO"]}><AdminGrades /></ProtectedRoute>} />
      <Route path="/admin/documents" element={<ProtectedRoute allow={["ADMIN", "PERSONAL_ADMINISTRATIVO"]}><AdminDocuments /></ProtectedRoute>} />

      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  </BrowserRouter>
);

export default App;
