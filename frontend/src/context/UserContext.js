import { createContext, useContext, useState, useEffect } from "react";

const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    // Simulamos la carga del usuario autenticado (puedes cambiar esto)
    const fetchUser = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/users/1");
        const data = await response.json();
        setUser(data);
      } catch (error) {
        console.error("Error al obtener usuario:", error);
      }
    };

    fetchUser();
  }, []);

  return (
    <UserContext.Provider value={{ user, setUser }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => {
  return useContext(UserContext);
};
