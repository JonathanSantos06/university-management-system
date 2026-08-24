import React from "react";
import { Grid, Paper, Typography, Box, LinearProgress, Chip, Stack } from "@mui/material";
import { StudentLayout } from "../../layouts/StudentLayout";
import { mockCurriculum, mockStudentProfile } from "../../mock/data";

const StatCard: React.FC<{ label: string; value: string | number; color?: string }> = ({ label, value, color }) => (
  <Paper sx={{ p: 3, borderRadius: 3 }} elevation={0} variant="outlined">
    <Typography variant="body2" color="text.secondary">{label}</Typography>
    <Typography variant="h4" fontWeight={700} color={color ?? "text.primary"}>{value}</Typography>
  </Paper>
);

export const StudentDashboard: React.FC = () => {
  const approved = mockCurriculum.filter((s) => s.status === "APROBADA").length;
  const failed = mockCurriculum.filter((s) => s.status === "REPROBADA").length;
  const inProgress = mockCurriculum.filter((s) => s.status === "CURSANDO").length;
  const pending = mockCurriculum.filter((s) => s.status === "PENDIENTE").length;
  const progress = Math.round((approved / mockCurriculum.length) * 100);

  return (
    <StudentLayout title="Dashboard">
      <Typography variant="h5" mb={0.5}>Hola, {mockStudentProfile.fullName.split(" ")[0]} 👋</Typography>
      <Typography variant="body2" color="text.secondary" mb={3}>
        {mockStudentProfile.career} · Semestre {mockStudentProfile.currentSemester} · Matrícula {mockStudentProfile.studentCode}
      </Typography>

      <Grid container spacing={2} mb={3}>
        <Grid item xs={12} sm={6} md={3}><StatCard label="Aprobadas" value={approved} color="green" /></Grid>
        <Grid item xs={12} sm={6} md={3}><StatCard label="Reprobadas" value={failed} color="#C0392B" /></Grid>
        <Grid item xs={12} sm={6} md={3}><StatCard label="Cursando" value={inProgress} color="#0B3D91" /></Grid>
        <Grid item xs={12} sm={6} md={3}><StatCard label="Pendientes" value={pending} color="#8A8F99" /></Grid>
      </Grid>

      <Paper sx={{ p: 3, borderRadius: 3, mb: 3 }} variant="outlined">
        <Typography variant="subtitle1" fontWeight={600} mb={1}>Avance de la carrera</Typography>
        <LinearProgress variant="determinate" value={progress} sx={{ height: 10, borderRadius: 5, mb: 1 }} />
        <Typography variant="body2" color="text.secondary">{progress}% completado</Typography>
      </Paper>

      <Paper sx={{ p: 3, borderRadius: 3 }} variant="outlined">
        <Typography variant="subtitle1" fontWeight={600} mb={2}>Materias en curso</Typography>
        <Stack spacing={1.5}>
          {mockCurriculum.filter((s) => s.status === "CURSANDO").map((s) => (
            <Box key={s.id} sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <Box>
                <Typography variant="body1">{s.name}</Typography>
                <Typography variant="caption" color="text.secondary">{s.code} · {s.credits} créditos</Typography>
              </Box>
              <Chip label="Cursando" color="primary" size="small" variant="outlined" />
            </Box>
          ))}
        </Stack>
      </Paper>
    </StudentLayout>
  );
};
