import React, { useState } from "react";
import {
  Box, Paper, TextField, Button, Typography, Alert, MenuItem, Stack, Avatar,
} from "@mui/material";
import SchoolIcon from "@mui/icons-material/School";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../mock/authContext";
import { mockUsers } from "../mock/data";

export const Login: React.FC = () => {
  const [username, setUsername] = useState("jperez");
  const [password, setPassword] = useState("Password123!");
  const [error, setError] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const ok = login(username);
    if (!ok) {
      setError("Usuario no encontrado en el demo.");
      return;
    }
    const found = mockUsers.find((u) => u.username === username)!;
    navigate(found.role === "ALUMNO" ? "/student/dashboard" : "/admin/dashboard");
  };

  return (
    <Box
      sx={{
        minHeight: "100vh", display: "flex", alignItems: "center", justifyContent: "center",
        background: "linear-gradient(135deg, #0B3D91 0%, #123B7A 60%, #0A2A63 100%)",
      }}
    >
      <Paper elevation={6} sx={{ p: 5, width: 420, borderRadius: 3 }}>
        <Stack alignItems="center" spacing={1} mb={3}>
          <Avatar sx={{ bgcolor: "#0B3D91", width: 56, height: 56 }}>
            <SchoolIcon fontSize="large" />
          </Avatar>
          <Typography variant="h5" fontWeight={700}>SGU</Typography>
          <Typography variant="body2" color="text.secondary">
            Sistema de Gestión Universitaria
          </Typography>
        </Stack>

        <form onSubmit={handleSubmit}>
          <Stack spacing={2}>
            {error && <Alert severity="error">{error}</Alert>}
            <TextField
              select label="Usuario demo" value={username}
              onChange={(e) => setUsername(e.target.value)} fullWidth
            >
              <MenuItem value="jperez">jperez (ALUMNO)</MenuItem>
              <MenuItem value="admin">admin (ADMIN)</MenuItem>
              <MenuItem value="control1">control1 (PERSONAL_ADMINISTRATIVO)</MenuItem>
            </TextField>
            <TextField
              label="Contraseña" type="password" value={password}
              onChange={(e) => setPassword(e.target.value)} fullWidth
            />
            <Button type="submit" variant="contained" size="large" fullWidth sx={{ py: 1.3 }}>
              Iniciar sesión
            </Button>
            <Typography variant="caption" color="text.secondary" textAlign="center">
              Demo con datos simulados — sin backend conectado.
            </Typography>
          </Stack>
        </form>
      </Paper>
    </Box>
  );
};
