import React from "react";
import {
  Paper, List, ListItem, ListItemText, Chip, Button, Stack,
} from "@mui/material";
import CheckIcon from "@mui/icons-material/Check";
import CloseIcon from "@mui/icons-material/Close";
import { AdminLayout } from "../../layouts/AdminLayout";
import { mockPendingDocuments } from "../../mock/data";

export const AdminDocuments: React.FC = () => (
  <AdminLayout title="Validación de documentos">
    <Paper sx={{ p: 2, borderRadius: 3 }} variant="outlined">
      <List>
        {mockPendingDocuments.map((d) => (
          <ListItem
            key={d.id}
            divider
            secondaryAction={
              <Stack direction="row" spacing={1}>
                <Button size="small" color="success" variant="contained" startIcon={<CheckIcon />}>Validar</Button>
                <Button size="small" color="error" variant="outlined" startIcon={<CloseIcon />}>Rechazar</Button>
              </Stack>
            }
          >
            <ListItemText
              primary={`${d.student} — ${d.type}`}
              secondary={`Cargado: ${d.uploadedAt}`}
            />
            <Chip label="Pendiente" color="warning" size="small" sx={{ mr: 2 }} />
          </ListItem>
        ))}
      </List>
    </Paper>
  </AdminLayout>
);
