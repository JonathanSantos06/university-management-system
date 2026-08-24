import React from "react";
import { Paper, Table, TableHead, TableRow, TableCell, TableBody, Chip, Box, Button } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import { AdminLayout } from "../../layouts/AdminLayout";
import { mockEnrollments } from "../../mock/data";

export const AdminEnrollments: React.FC = () => (
  <AdminLayout title="Inscripciones">
    <Box sx={{ display: "flex", justifyContent: "flex-end", mb: 2 }}>
      <Button variant="contained" startIcon={<AddIcon />}>Nueva inscripción</Button>
    </Box>
    <Paper sx={{ borderRadius: 3 }} variant="outlined">
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Alumno</TableCell>
            <TableCell>Periodo</TableCell>
            <TableCell align="center">Materias</TableCell>
            <TableCell align="center">Estatus</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {mockEnrollments.map((e) => (
            <TableRow key={e.id} hover>
              <TableCell>{e.student}</TableCell>
              <TableCell>{e.period}</TableCell>
              <TableCell align="center">{e.subjectsCount}</TableCell>
              <TableCell align="center"><Chip label={e.status} color="primary" size="small" /></TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Paper>
  </AdminLayout>
);
