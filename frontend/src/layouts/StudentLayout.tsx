import React from "react";
import DashboardIcon from "@mui/icons-material/Dashboard";
import PersonIcon from "@mui/icons-material/Person";
import MenuBookIcon from "@mui/icons-material/MenuBook";
import AccountTreeIcon from "@mui/icons-material/AccountTree";
import DescriptionIcon from "@mui/icons-material/Description";
import FolderIcon from "@mui/icons-material/Folder";
import { AppLayout, NavItem } from "./AppLayout";

const items: NavItem[] = [
  { label: "Dashboard", path: "/student/dashboard", icon: <DashboardIcon /> },
  { label: "Mi perfil", path: "/student/profile", icon: <PersonIcon /> },
  { label: "Materias inscritas", path: "/student/subjects", icon: <MenuBookIcon /> },
  { label: "Retícula", path: "/student/curriculum", icon: <AccountTreeIcon /> },
  { label: "Kardex", path: "/student/kardex", icon: <DescriptionIcon /> },
  { label: "Documentos", path: "/student/documents", icon: <FolderIcon /> },
];

export const StudentLayout: React.FC<{ title: string; children: React.ReactNode }> = ({ title, children }) => (
  <AppLayout items={items} title={title}>{children}</AppLayout>
);
