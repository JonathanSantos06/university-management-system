import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";


interface ProtectedRouteProps {
  allow: string[];
  children: React.ReactElement;
}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  allow,
  children,
}) => {
  const { user, isAuthenticated } = useAuth();
  const location = useLocation();

  if (!isAuthenticated || !user) {
    return (
      <Navigate
        to="/login"
        replace
        state={{ from: location }}
      />
    );
  }

  const hasPermission = user.roles.some((role) =>
    allow.includes(role)
  );

  if (!hasPermission) {
    return <Navigate to="/login" replace />;
  }

  return children;
};