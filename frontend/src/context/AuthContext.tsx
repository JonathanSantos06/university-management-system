import React, {
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";
import { User } from "../types/types";
import { loginApi, logoutApi } from "../api/authApi";



interface AuthContextValue {
  user: User | null;
  accessToken: string | null;
  isAuthenticated: boolean;
  login: (
    username: string,
    password: string
  ) => Promise<User | null>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextValue | undefined>(
  undefined
);

export const AuthProvider: React.FC<{
  children: React.ReactNode;
}> = ({ children }) => {

  const [user, setUser] = useState<User | null>(null);

  const [accessToken, setAccessToken] =
    useState<string | null>(null);

  useEffect(() => {
    const storedToken =
      localStorage.getItem("accessToken");

    const storedUser =
      localStorage.getItem("user");

    if (storedToken && storedUser) {
      setAccessToken(storedToken);
      setUser(JSON.parse(storedUser));
    }
  }, []);

  const login = async (
    username: string,
    password: string
  ): Promise<User | null> => {

    try {

      const response = await loginApi({
        username,
        password,
      });

      localStorage.setItem(
        "accessToken",
        response.accessToken
      );

      localStorage.setItem(
        "refreshToken",
        response.refreshToken
      );

      localStorage.setItem(
        "user",
        JSON.stringify(response.user)
      );

      setAccessToken(response.accessToken);
      setUser(response.user);

      return response.user;

    } catch (error) {

      console.error(
        "Error al iniciar sesión:",
        error
      );

      return null;
    }
  };

  const logout = async () => {

    const refreshToken =
      localStorage.getItem("refreshToken");

    try {

      if (refreshToken) {
        await logoutApi(refreshToken);
      }

    } catch (error) {

      console.error(
        "Error al cerrar sesión:",
        error
      );

    } finally {

      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      localStorage.removeItem("user");

      setAccessToken(null);
      setUser(null);
    }
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        accessToken,
        isAuthenticated: !!accessToken,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {

  const context = useContext(AuthContext);

  if (!context) {
    throw new Error(
      "useAuth debe utilizarse dentro de AuthProvider"
    );
  }

  return context;
};

