import React from "react";
import { Box, Paper, Typography, Chip, Stack } from "@mui/material";
import { StudentLayout } from "../../layouts/StudentLayout";
import { mockCurriculum } from "../../mock/data";

const statusColor: Record<string, string> = {
  APROBADA: "#2E7D32",
  REPROBADA: "#C0392B",
  CURSANDO: "#0B3D91",
  PENDIENTE: "#B0B6C0",
};

export const StudentCurriculum: React.FC = () => {
  const semesters = Array.from(new Set(mockCurriculum.map((s) => s.semester))).sort();
  return (
    <StudentLayout title="Retícula de la carrera">
      <Typography variant="body2" color="text.secondary" mb={2}>
        Ingeniería en Sistemas Computacionales — vista por semestre
      </Typography>
      <Stack direction="row" spacing={2} sx={{ overflowX: "auto", pb: 2 }}>
        {semesters.map((sem) => (
          <Paper key={sem} variant="outlined" sx={{ p: 2, minWidth: 220, borderRadius: 3 }}>
            <Typography variant="subtitle2" fontWeight={700} mb={1.5}>Semestre {sem}</Typography>
            <Stack spacing={1}>
              {mockCurriculum.filter((s) => s.semester === sem).map((s) => (
                <Box
                  key={s.id}
                  sx={{
                    p: 1.2, borderRadius: 2, borderLeft: `4px solid ${statusColor[s.status]}`,
                    bgcolor: "#F7F9FC",
                  }}
                >
                  <Typography variant="body2" fontWeight={600}>{s.name}</Typography>
                  <Typography variant="caption" color="text.secondary">{s.code}</Typography>
                  <br />
                  <Chip label={s.status} size="small" sx={{ mt: 0.5, bgcolor: statusColor[s.status], color: "#fff" }} />
                </Box>
              ))}
            </Stack>
          </Paper>
        ))}
      </Stack>
    </StudentLayout>
  );
};
