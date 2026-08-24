import React from "react";
import {
  Paper, Typography, Table, TableHead, TableRow, TableCell, TableBody, Button, Box, Chip,
} from "@mui/material";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";
import { StudentLayout } from "../../layouts/StudentLayout";
import { mockCurriculum, mockStudentProfile } from "../../mock/data";

export const StudentKardex: React.FC = () => {
  const approvedCredits = mockCurriculum
    .filter((s) => s.status === "APROBADA")
    .reduce((acc, s) => acc + s.credits, 0);
  const totalCredits = mockCurriculum.reduce((acc, s) => acc + s.credits, 0);

  return (
    <StudentLayout title="Kardex académico">
      <Paper sx={{ p: 3, borderRadius: 3 }} variant="outlined">
        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 2 }}>
          <Box>
            <Typography variant="h6">{mockStudentProfile.fullName}</Typography>
            <Typography variant="body2" color="text.secondary">
              {mockStudentProfile.studentCode} · {mockStudentProfile.career}
            </Typography>
          </Box>
          <Button
            variant="contained" startIcon={<PictureAsPdfIcon />}
            onClick={() => alert("Demo: en el sistema real esto descarga el Kardex en PDF (document-service).")}
          >
            Descargar PDF
          </Button>
        </Box>

        <Typography variant="body2" color="text.secondary" mb={2}>
          Créditos acumulados: {approvedCredits} / {totalCredits}
        </Typography>

        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Semestre</TableCell>
              <TableCell>Código</TableCell>
              <TableCell>Materia</TableCell>
              <TableCell align="center">Créditos</TableCell>
              <TableCell align="center">Calificación</TableCell>
              <TableCell align="center">Estatus</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {mockCurriculum.map((s) => (
              <TableRow key={s.id}>
                <TableCell>{s.semester}</TableCell>
                <TableCell>{s.code}</TableCell>
                <TableCell>{s.name}</TableCell>
                <TableCell align="center">{s.credits}</TableCell>
                <TableCell align="center">{s.grade ?? "—"}</TableCell>
                <TableCell align="center">
                  <Chip
                    size="small"
                    label={s.status}
                    color={s.status === "APROBADA" ? "success" : s.status === "REPROBADA" ? "error" : "default"}
                  />
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Paper>
    </StudentLayout>
  );
};
