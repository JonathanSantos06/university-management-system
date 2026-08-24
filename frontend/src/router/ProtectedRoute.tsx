import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../mock/authContext";
import { Role } from "../mock/data";

export const ProtectedRoute: React.FC<{ allow: Role[]; children: React.ReactElement }> = ({ allow, children }) => {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  if (!allow.includes(user.role)) return <Navigate to="/login" replace />;
  return children;
};
