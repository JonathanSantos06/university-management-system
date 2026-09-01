import React from "react";
import {
  AppBar, Toolbar, Typography, Drawer, List, ListItemButton, ListItemIcon,
  ListItemText, Box, Avatar, IconButton, Chip, Divider,
} from "@mui/material";
import SchoolIcon from "@mui/icons-material/School";
import LogoutIcon from "@mui/icons-material/Logout";
import { useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const drawerWidth = 250;

export interface NavItem {
  label: string;
  path: string;
  icon: React.ReactNode;
}

export const AppLayout: React.FC<{ items: NavItem[]; title: string; children: React.ReactNode }> = ({
  items, title, children,
}) => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout } = useAuth();

  return (
    <Box sx={{ display: "flex", minHeight: "100vh" }}>
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: "border-box", bgcolor: "#0B3D91", color: "#fff" },
        }}
      >
        <Toolbar sx={{ gap: 1 }}>
          <SchoolIcon />
          <Typography variant="h6" noWrap fontWeight={700}>SGU</Typography>
        </Toolbar>
        <Divider sx={{ borderColor: "rgba(255,255,255,0.15)" }} />
        <List sx={{ mt: 1 }}>
          {items.map((item) => {
            const active = location.pathname === item.path;
            return (
              <ListItemButton
                key={item.path}
                selected={active}
                onClick={() => navigate(item.path)}
                sx={{
                  mx: 1, my: 0.3, borderRadius: 2,
                  "&.Mui-selected": { bgcolor: "rgba(255,255,255,0.15)" },
                  "&:hover": { bgcolor: "rgba(255,255,255,0.10)" },
                }}
              >
                <ListItemIcon sx={{ color: "#fff", minWidth: 38 }}>{item.icon}</ListItemIcon>
                <ListItemText primary={item.label} />
              </ListItemButton>
            );
          })}
        </List>
      </Drawer>

      <Box sx={{ flexGrow: 1, display: "flex", flexDirection: "column" }}>
        <AppBar position="static" color="inherit" sx={{ bgcolor: "#fff", borderBottom: "1px solid #E3E7EE" }}>
          <Toolbar sx={{ justifyContent: "space-between" }}>
            <Typography variant="h6" color="text.primary">{title}</Typography>
            <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
              <Chip label={user?.roles ?? ""} size="small" color="secondary" />
              <Typography variant="body2" color="text.secondary">{user?.username}</Typography>
              <Avatar sx={{ width: 32, height: 32, bgcolor: "#0B3D91" }}>
                {user?.username?.charAt(0) ?? "U"}
              </Avatar>
              <IconButton onClick={() => { logout(); navigate("/login"); }} title="Cerrar sesión">
                <LogoutIcon />
              </IconButton>
            </Box>
          </Toolbar>
        </AppBar>
        <Box sx={{ p: 3, flexGrow: 1, bgcolor: "background.default" }}>{children}</Box>
      </Box>
    </Box>
  );
};
