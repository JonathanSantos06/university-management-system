import React from "react";
import {
  Paper, Table, TableHead, TableRow, TableCell, TableBody, TextField, Chip,
} from "@mui/material";
import { AdminLayout } from "../../layouts/AdminLayout";
import { mockCurriculum } from "../../mock/data";

export const AdminGrades: React.FC = () => (
  <AdminLayout title="Captura de calificaciones">
    <Paper sx={{ borderRadius: 3 }} variant="outlined">
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Código</TableCell>
            <TableCell>Materia</TableCell>
            <TableCell align="center">Calificación</TableCell>
            <TableCell align="center">Estatus</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {mockCurriculum.map((s) => (
            <TableRow key={s.id} hover>
              <TableCell>{s.code}</TableCell>
              <TableCell>{s.name}</TableCell>
              <TableCell align="center">
                <TextField size="small" defaultValue={s.grade ?? ""} sx={{ width: 90 }} />
              </TableCell>
              <TableCell align="center">
                <Chip
                  size="small" label={s.status}
                  color={s.status === "APROBADA" ? "success" : s.status === "REPROBADA" ? "error" : "default"}
                />
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  </AdminLayout>
);
