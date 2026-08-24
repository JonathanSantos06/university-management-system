import React from "react";
import { Paper, Typography, Box, Stack, Chip } from "@mui/material";
import { AdminLayout } from "../../layouts/AdminLayout";
import { mockCurriculum, mockCareers } from "../../mock/data";

export const AdminCurriculum: React.FC = () => {
  const semesters = Array.from(new Set(mockCurriculum.map((s) => s.semester))).sort();
  return (
    <AdminLayout title="Gestión de retículas">
      <Typography variant="body2" color="text.secondary" mb={2}>
        Editando retícula: {mockCareers[0].name} ({mockCareers[0].code})
      </Typography>
      <Stack direction="row" spacing={2} sx={{ overflowX: "auto", pb: 2 }}>
        {semesters.map((sem) => (
          <Paper key={sem} variant="outlined" sx={{ p: 2, minWidth: 220, borderRadius: 3 }}>
            <Typography variant="subtitle2" fontWeight={700} mb={1.5}>Semestre {sem}</Typography>
            <Stack spacing={1}>
              {mockCurriculum.filter((s) => s.semester === sem).map((s) => (
                <Box key={s.id} sx={{ p: 1, borderRadius: 2, bgcolor: "#F7F9FC" }}>
                  <Typography variant="body2" fontWeight={600}>{s.name}</Typography>
                  <Chip label={`${s.credits} créditos`} size="small" sx={{ mt: 0.5 }} />
                </Box>
              ))}
            </Stack>
          </Paper>
        ))}
      </Stack>
    </AdminLayout>
  );
};
