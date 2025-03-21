import { createContext, useState, ReactNode } from "react";
import Cookies from "js-cookie";

type AuthContextType = {
  token: string | null;
  setToken: (token: string) => void;
};

const AuthContext = createContext<AuthContextType>({
  token: null,
  setToken: () => {},
});

const AuthProvider = ({ children }: { children: ReactNode }) => {
  const safeToken = Cookies.get("token") ?? null;
  const [token, setTokenState] = useState<string | null>(safeToken);

  const setToken = (token: string) => {
    setTokenState(token);
    Cookies.set("token", token);
  };

  return <AuthContext.Provider value={{ token, setToken }}>{children}</AuthContext.Provider>;
};

export { AuthContext, AuthProvider };
