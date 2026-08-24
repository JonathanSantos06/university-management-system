import React from "react";
import DashboardIcon from "@mui/icons-material/Dashboard";
import GroupIcon from "@mui/icons-material/Group";
import SchoolIcon from "@mui/icons-material/School";
import MenuBookIcon from "@mui/icons-material/MenuBook";
import AccountTreeIcon from "@mui/icons-material/AccountTree";
import HowToRegIcon from "@mui/icons-material/HowToReg";
import GradeIcon from "@mui/icons-material/Grade";
import FolderIcon from "@mui/icons-material/Folder";
import { AppLayout, NavItem } from "./AppLayout";

const items: NavItem[] = [
  { label: "Dashboard", path: "/admin/dashboard", icon: <DashboardIcon /> },
  { label: "Alumnos", path: "/admin/students", icon: <GroupIcon /> },
  { label: "Carreras", path: "/admin/careers", icon: <SchoolIcon /> },
  { label: "Materias", path: "/admin/subjects", icon: <MenuBookIcon /> },
  { label: "Retículas", path: "/admin/curriculum", icon: <AccountTreeIcon /> },
  { label: "Inscripciones", path: "/admin/enrollments", icon: <HowToRegIcon /> },
  { label: "Calificaciones", path: "/admin/grades", icon: <GradeIcon /> },
  { label: "Documentos", path: "/admin/documents", icon: <FolderIcon /> },
];

export const AdminLayout: React.FC<{ title: string; children: React.ReactNode }> = ({ title, children }) => (
  <AppLayout items={items} title={title}>{children}</AppLayout>
);
