import React, { useState } from "react";
import {
  Paper, Typography, Table, TableHead, TableRow, TableCell, TableBody, Chip,
  TextField, Box, Button,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import { AdminLayout } from "../../layouts/AdminLayout";
import { mockAdminStudents } from "../../mock/data";

const statusColor: Record<string, "success" | "warning" | "default"> = {
  ACTIVO: "success",
  BAJA_TEMPORAL: "warning",
  BAJA_DEFINITIVA: "default",
};

export const AdminStudents: React.FC = () => {
  const [query, setQuery] = useState("");
  const filtered = mockAdminStudents.filter((s) =>
    (s.fullName + s.studentCode).toLowerCase().includes(query.toLowerCase())
  );

  return (
    <AdminLayout title="Gestión de alumnos">
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 2 }}>
        <TextField
          size="small" placeholder="Buscar por nombre o matrícula"
          value={query} onChange={(e) => setQuery(e.target.value)} sx={{ width: 320 }}
        />
        <Button variant="contained" startIcon={<AddIcon />}>Nuevo alumno</Button>
      </Box>
      <Paper sx={{ borderRadius: 3 }} variant="outlined">
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Matrícula</TableCell>
              <TableCell>Nombre</TableCell>
              <TableCell>Carrera</TableCell>
              <TableCell align="center">Semestre</TableCell>
              <TableCell align="center">Estatus</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {filtered.map((s) => (
              <TableRow key={s.id} hover sx={{ cursor: "pointer" }}>
                <TableCell>{s.studentCode}</TableCell>
                <TableCell>{s.fullName}</TableCell>
                <TableCell>{s.career}</TableCell>
                <TableCell align="center">{s.semester}</TableCell>
                <TableCell align="center">
                  <Chip label={s.status} color={statusColor[s.status] ?? "default"} size="small" />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
    </AdminLayout>
  );
};
