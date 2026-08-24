import React from "react";
import { Grid, Paper, Typography, List, ListItem, ListItemText, Chip } from "@mui/material";
import { AdminLayout } from "../../layouts/AdminLayout";
import { mockKpis, mockPendingDocuments } from "../../mock/data";

const StatCard: React.FC<{ label: string; value: string | number }> = ({ label, value }) => (
  <Paper sx={{ p: 3, borderRadius: 3 }} variant="outlined">
    <Typography variant="body2" color="text.secondary">{label}</Typography>
    <Typography variant="h4" fontWeight={700}>{value}</Typography>
  </Paper>
);

export const AdminDashboard: React.FC = () => (
  <AdminLayout title="Dashboard administrativo">
    <Grid container spacing={2} mb={3}>
      <Grid item xs={12} sm={6} md={3}><StatCard label="Alumnos totales" value={mockKpis.totalStudents} /></Grid>
      <Grid item xs={12} sm={6} md={3}><StatCard label="Carreras activas" value={mockKpis.activeCareers} /></Grid>
      <Grid item xs={12} sm={6} md={3}><StatCard label="Docs. pendientes" value={mockKpis.pendingDocuments} /></Grid>
      <Grid item xs={12} sm={6} md={3}><StatCard label="Inscripciones abiertas" value={mockKpis.openEnrollments} /></Grid>
    </Grid>

    <Paper sx={{ p: 3, borderRadius: 3 }} variant="outlined">
      <Typography variant="subtitle1" fontWeight={600} mb={1}>Documentos pendientes de validación</Typography>
      <List>
        {mockPendingDocuments.map((d) => (
          <ListItem key={d.id} divider secondaryAction={<Chip label="Pendiente" color="warning" size="small" />}>
            <ListItemText primary={`${d.student} — ${d.type}`} secondary={`Cargado: ${d.uploadedAt}`} />
          </ListItem>
        ))}
      </List>
    </Paper>
  </AdminLayout>
);
