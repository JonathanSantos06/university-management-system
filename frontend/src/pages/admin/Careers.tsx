import React from "react";
import { Grid, Paper, Typography, Box, Chip, Button } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import { AdminLayout } from "../../layouts/AdminLayout";
import { mockCareers } from "../../mock/data";

export const AdminCareers: React.FC = () => (
  <AdminLayout title="Gestión de carreras">
    <Box sx={{ display: "flex", justifyContent: "flex-end", mb: 2 }}>
      <Button variant="contained" startIcon={<AddIcon />}>Nueva carrera</Button>
    </Box>
    <Grid container spacing={2}>
      {mockCareers.map((c) => (
        <Grid item xs={12} md={6} key={c.id}>
          <Paper sx={{ p: 3, borderRadius: 3 }} variant="outlined">
            <Box sx={{ display: "flex", justifyContent: "space-between" }}>
              <Typography variant="h6">{c.name}</Typography>
              <Chip label={c.code} color="primary" size="small" />
            </Box>
            <Typography variant="body2" color="text.secondary" mt={1}>
              {c.totalSemesters} semestres · {c.activeStudents} alumnos activos
            </Typography>
          </Paper>
        </Grid>
      ))}
    </Grid>
  </AdminLayout>
);
