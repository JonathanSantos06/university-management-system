import React, { useEffect, useState } from "react";
import {
  Paper,
  Typography,
  Grid,
  Divider,
  Avatar,
  Box,
  Chip,
  CircularProgress,
  Alert,
} from "@mui/material";

import { StudentLayout } from "../../layouts/StudentLayout";
import { getStudentsExpApi } from "../../api/studentsApi";
import { Student } from "../../types/types";

const Field: React.FC<{ label: string; value: string }> = ({
  label,
  value,
}) => (
  <Grid item xs={12} sm={6}>
    <Typography variant="caption" color="text.secondary">
      {label}
    </Typography>

    <Typography variant="body1" fontWeight={500}>
      {value || "No disponible"}
    </Typography>
  </Grid>
);

export const StudentProfilePage: React.FC = () => {
  const [student, setStudent] = useState<Student | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadStudent = async () => {
      try {
        setLoading(true);

        const data = await getStudentsExpApi();

        setStudent(data);
      } catch (error) {
        console.error("Error al obtener el perfil:", error);

        setError(
          "No se pudo cargar la información del estudiante."
        );
      } finally {
        setLoading(false);
      }
    };

    loadStudent();
  }, []);

  if (loading) {
    return (
      <StudentLayout title="Mi perfil">
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            minHeight: 300,
          }}
        >
          <CircularProgress />
        </Box>
      </StudentLayout>
    );
  }

  if (error) {
    return (
      <StudentLayout title="Mi perfil">
        <Alert severity="error">
          {error}
        </Alert>
      </StudentLayout>
    );
  }

  if (!student) {
    return (
      <StudentLayout title="Mi perfil">
        <Alert severity="warning">
          No se encontró información del estudiante.
        </Alert>
      </StudentLayout>
    );
  }

  const personalData = student.personalData;

  const address = student.addresses?.find(
    (address) => address.addressType === "ACTUAL"
  );

  const fullName = [
    personalData.firstName,
    personalData.lastNamePaternal,
    personalData.lastNameMaternal,
  ]
    .filter(Boolean)
    .join(" ");

  const fullAddress = address
    ? [
        address.street,
        address.extNumber
          ? `No. ${address.extNumber}`
          : "",
        address.intNumber
          ? `Int. ${address.intNumber}`
          : "",
        address.neighborhood,
        address.city,
        address.state,
        address.postalCode,
      ]
        .filter(Boolean)
        .join(", ")
    : "No disponible";

  return (
    <StudentLayout title="Mi perfil">

      <Paper
        sx={{
          p: 4,
          borderRadius: 3,
        }}
        variant="outlined"
      >

        {/* ENCABEZADO */}

        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            gap: 2,
            mb: 3,
          }}
        >

          <Avatar
            sx={{
              width: 72,
              height: 72,
              bgcolor: "#0B3D91",
              fontSize: 28,
            }}
          >
            {personalData.firstName?.charAt(0)}
          </Avatar>

          <Box>

            <Typography variant="h6">
              {fullName}
            </Typography>

            <Typography
              variant="body2"
              color="text.secondary"
            >
              {student.studentCode}
            </Typography>

            <Chip
              label={student.status}
              color="success"
              size="small"
              sx={{
                mt: 0.5,
              }}
            />

          </Box>

        </Box>

        <Divider sx={{ mb: 3 }} />

        {/* DATOS ACADÉMICOS */}

        <Typography
          variant="subtitle1"
          fontWeight={600}
          mb={2}
        >
          Datos académicos
        </Typography>

        <Grid
          container
          spacing={2}
          mb={3}
        >

          <Field
            label="Carrera"
            value={student.careerId}
          />

          <Field
            label="Semestre actual"
            value={String(student.currentSemester)}
          />

          <Field
            label="Periodo de ingreso"
            value={student.admissionPeriodId}
          />

          <Field
            label="Estatus"
            value={student.status}
          />

        </Grid>

        <Divider sx={{ mb: 3 }} />

        {/* DATOS PERSONALES */}

        <Typography
          variant="subtitle1"
          fontWeight={600}
          mb={2}
        >
          Datos personales
        </Typography>

        <Grid
          container
          spacing={2}
        >

          <Field
            label="CURP"
            value={personalData.curp}
          />

          <Field
            label="Correo personal"
            value={personalData.personalEmail}
          />

          <Field
            label="Teléfono"
            value={personalData.phone}
          />

          <Field
            label="Domicilio"
            value={fullAddress}
          />

        </Grid>

      </Paper>

    </StudentLayout>
  );
};