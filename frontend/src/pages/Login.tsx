import React, { useState } from "react";

import {
  Box,
  Paper,
  TextField,
  Button,
  Typography,
  Alert,
  Stack,
  Avatar,
} from "@mui/material";

import SchoolIcon from "@mui/icons-material/School";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export const Login: React.FC = () => {
  const [username, setUsername] = useState("jperez");
  const [password, setPassword] = useState("Password123!");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    setError("");
    setLoading(true);

    try {
      const ok = await login(username, password);

      if (!ok) {
        setError("Usuario o contraseña incorrectos.");
        return;
      }

      const storedUser = localStorage.getItem("user");

      if (!storedUser) {
        setError("No se pudo obtener la información del usuario.");
        return;
      }

      const user = JSON.parse(storedUser);

      if (user.roles.includes("ALUMNO")) {
        navigate("/student/dashboard");
      } else if (user.roles.includes("ADMIN")) {
        navigate("/admin/dashboard");
      } else if (user.roles.includes("DOCENTE")) {
        navigate("/teacher/dashboard");
      } else {
        navigate("/");
      }
    } catch (error) {
      console.error(error);
      setError("No fue posible conectar con el servidor.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        background:
          "linear-gradient(135deg, #0B3D91 0%, #123B7A 60%, #0A2A63 100%)",
      }}
    >
      <Paper
        elevation={6}
        sx={{
          p: 5,
          width: 420,
          borderRadius: 3,
        }}
      >
        <Stack alignItems="center" spacing={1} mb={3}>
          <Avatar
            sx={{
              bgcolor: "#0B3D91",
              width: 56,
              height: 56,
            }}
          >
            <SchoolIcon fontSize="large" />
          </Avatar>

          <Typography variant="h5" fontWeight={700}>
            SGU
          </Typography>

          <Typography variant="body2" color="text.secondary">
            Sistema de Gestión Universitaria
          </Typography>
        </Stack>

        <form onSubmit={handleSubmit}>
          <Stack spacing={2}>

            {error && (
              <Alert severity="error">
                {error}
              </Alert>
            )}

            <TextField
              label="Usuario"
              type="text"
              //value={username}
              onChange={(e) => setUsername(e.target.value)}
              fullWidth
              required
            />

            <TextField
              label="Contraseña"
              type="password"
              //value={password}
              onChange={(e) => setPassword(e.target.value)}
              fullWidth
              required
            />

            <Button
              type="submit"
              variant="contained"
              size="large"
              fullWidth
              sx={{ py: 1.3 }}
              disabled={loading}
            >
              {loading ? "Iniciando sesión..." : "Iniciar sesión"}
            </Button>

          </Stack>
        </form>
      </Paper>
    </Box>
  );
};
