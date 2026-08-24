import React from "react";
import {
  Paper, Table, TableHead, TableRow, TableCell, TableBody, Box, Button,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import { AdminLayout } from "../../layouts/AdminLayout";
import { mockCurriculum } from "../../mock/data";

const uniqueSubjects = Array.from(new Map(mockCurriculum.map((s) => [s.code, s])).values());

export const AdminSubjects: React.FC = () => (
  <AdminLayout title="Gestión de materias">
    <Box sx={{ display: "flex", justifyContent: "flex-end", mb: 2 }}>
      <Button variant="contained" startIcon={<AddIcon />}>Nueva materia</Button>
    </Box>
    <Paper sx={{ borderRadius: 3 }} variant="outlined">
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Código</TableCell>
            <TableCell>Nombre</TableCell>
            <TableCell align="center">Créditos</TableCell>
            <TableCell align="center">Semestre sugerido</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {uniqueSubjects.map((s) => (
            <TableRow key={s.code} hover>
              <TableCell>{s.code}</TableCell>
              <TableCell>{s.name}</TableCell>
              <TableCell align="center">{s.credits}</TableCell>
              <TableCell align="center">{s.semester}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  </AdminLayout>
);
