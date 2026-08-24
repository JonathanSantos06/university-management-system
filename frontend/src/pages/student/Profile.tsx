import React from "react";
import { Paper, Typography, Grid, Divider, Avatar, Box, Chip } from "@mui/material";
import { StudentLayout } from "../../layouts/StudentLayout";
import { mockStudentProfile } from "../../mock/data";

const Field: React.FC<{ label: string; value: string }> = ({ label, value }) => (
  <Grid item xs={12} sm={6}>
    <Typography variant="caption" color="text.secondary">{label}</Typography>
    <Typography variant="body1" fontWeight={500}>{value}</Typography>
  </Grid>
);

export const StudentProfilePage: React.FC = () => {
  const p = mockStudentProfile;
  return (
    <StudentLayout title="Mi perfil">
      <Paper sx={{ p: 4, borderRadius: 3 }} variant="outlined">
        <Box sx={{ display: "flex", alignItems: "center", gap: 2, mb: 3 }}>
          <Avatar sx={{ width: 72, height: 72, bgcolor: "#0B3D91", fontSize: 28 }}>
            {p.fullName.charAt(0)}
          </Avatar>
          <Box>
            <Typography variant="h6">{p.fullName}</Typography>
            <Typography variant="body2" color="text.secondary">{p.studentCode}</Typography>
            <Chip label={p.status} color="success" size="small" sx={{ mt: 0.5 }} />
          </Box>
        </Box>
        <Divider sx={{ mb: 3 }} />
        <Typography variant="subtitle1" fontWeight={600} mb={2}>Datos académicos</Typography>
        <Grid container spacing={2} mb={3}>
          <Field label="Carrera" value={p.career} />
          <Field label="Semestre actual" value={String(p.currentSemester)} />
          <Field label="Periodo de ingreso" value={p.admissionPeriod} />
          <Field label="Estatus" value={p.status} />
        </Grid>
        <Divider sx={{ mb: 3 }} />
        <Typography variant="subtitle1" fontWeight={600} mb={2}>Datos personales</Typography>
        <Grid container spacing={2}>
          <Field label="CURP" value={p.curp} />
          <Field label="Correo personal" value={p.email} />
          <Field label="Teléfono" value={p.phone} />
          <Field label="Domicilio" value={p.address} />
        </Grid>
      </Paper>
    </StudentLayout>
  );
};
