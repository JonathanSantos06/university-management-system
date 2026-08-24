import React from "react";
import {
  Paper, Typography, Table, TableHead, TableRow, TableCell, TableBody, Chip,
} from "@mui/material";
import { StudentLayout } from "../../layouts/StudentLayout";
import { mockCurriculum } from "../../mock/data";

const statusColor: Record<string, "success" | "error" | "primary" | "default"> = {
  APROBADA: "success",
  REPROBADA: "error",
  CURSANDO: "primary",
  PENDIENTE: "default",
};

export const StudentSubjects: React.FC = () => (
  <StudentLayout title="Materias inscritas y calificaciones">
    <Paper sx={{ borderRadius: 3 }} variant="outlined">
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Código</TableCell>
            <TableCell>Materia</TableCell>
            <TableCell align="center">Semestre</TableCell>
            <TableCell align="center">Créditos</TableCell>
            <TableCell align="center">Calificación</TableCell>
            <TableCell align="center">Estatus</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {mockCurriculum.map((s) => (
            <TableRow key={s.id} hover>
              <TableCell>{s.code}</TableCell>
              <TableCell>{s.name}</TableCell>
              <TableCell align="center">{s.semester}</TableCell>
              <TableCell align="center">{s.credits}</TableCell>
              <TableCell align="center">{s.grade ?? "—"}</TableCell>
              <TableCell align="center">
                <Chip label={s.status} color={statusColor[s.status]} size="small" />
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  </StudentLayout>
);
