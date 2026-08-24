import { createTheme } from "@mui/material/styles";

export const theme = createTheme({
  palette: {
    primary: { main: "#0B3D91" },
    secondary: { main: "#C9A227" },
    background: { default: "#F4F6F9" },
  },
  shape: { borderRadius: 10 },
  typography: {
    fontFamily: ['"Inter"', '"Roboto"', "sans-serif"].join(","),
    h4: { fontWeight: 700 },
    h5: { fontWeight: 700 },
    h6: { fontWeight: 600 },
  },
  components: {
    MuiPaper: { styleOverrides: { root: { backgroundImage: "none" } } },
    MuiAppBar: { styleOverrides: { root: { boxShadow: "none" } } },
  },
});
