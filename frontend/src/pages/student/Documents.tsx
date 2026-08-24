import React from "react";
import {
  Paper, Typography, List, ListItem, ListItemText, Chip, Button, Box, Alert,
} from "@mui/material";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import { StudentLayout } from "../../layouts/StudentLayout";
import { mockDocuments } from "../../mock/data";

const statusColor: Record<string, "success" | "error" | "warning"> = {
  VALIDADO: "success",
  RECHAZADO: "error",
  PENDIENTE: "warning",
};

export const StudentDocuments: React.FC = () => (
  <StudentLayout title="Expediente digital / Documentos">
    <Paper sx={{ p: 3, borderRadius: 3 }} variant="outlined">
      <Typography variant="subtitle1" fontWeight={600} mb={2}>Documentos requeridos</Typography>
      <List>
        {mockDocuments.map((d) => (
          <ListItem
            key={d.id}
            divider
            secondaryAction={
              d.fileName ? (
                <Chip label={d.status} color={statusColor[d.status]} size="small" />
              ) : (
                <Button size="small" variant="outlined" startIcon={<UploadFileIcon />}
                  onClick={() => alert("Demo: subiría el archivo al document-service.")}>
                  Subir
                </Button>
              )
            }
          >
            <ListItemText
              primary={d.type}
              secondary={
                d.fileName
                  ? `${d.fileName} · ${d.uploadedAt}${d.rejectionReason ? " · Motivo: " + d.rejectionReason : ""}`
                  : "No cargado"
              }
            />
          </ListItem>
        ))}
      </List>
      <Box mt={2}>
        <Alert severity="info">
          Los documentos rechazados deben volver a cargarse. El personal administrativo revisa y valida cada documento.
        </Alert>
      </Box>
    </Paper>
  </StudentLayout>
);
