import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/listados/styles.css";

function Empleados() {
  const navigate = useNavigate();
  const [negocios, setNegocios] = useState([]);  
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const token = localStorage.getItem("token");

  const loadNegocios = async () => {
    try {
      const user = JSON.parse(localStorage.getItem("user")); // Recuperamos y parseamos el objeto del localStorage
      const userId = user?.id; // Accedemos al id del usuario
  
      if (userId) {
        const response = await fetch(`http://localhost:8080/api/negocios/dueno/${userId}`, {
          method: "GET", 
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
  
        if (response.ok) {
          const data = await response.json(); // Parseamos la respuesta JSON
          setNegocios(data); // Establecemos los negocios en el estado
        } else {
          console.error("Error al cargar los negocios:", response.statusText);
        }
      } else {
        console.log("No se pudo obtener el negocioId.");
      }
    } catch (error) {
      console.error("Error al cargar los negocios:", error);
    }
  };
  

  useEffect(() => {
    loadNegocios();  
  }, []);  

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleVerNegocio = (negocioId) => {
    // Guardamos el negocioId en localStorage
    localStorage.setItem("negocioId", negocioId);
    // Navegamos a la pantalla de inicio del dueño
    navigate("/inicioDueno");
  };

  return (
    <div
      className="home-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        height: "100vh",
        overflowY: "auto",
      }}
    >
      <div className="content">
        <div className="icon-container-right">
          <Bell size={30} className="icon" onClick={toggleNotifications} />
          <User size={30} className="icon" onClick={toggleUserOptions} />
        </div>

        {showNotifications && (
          <div className="notification-bubble">
            <div className="notification-header">
              <strong>Notificaciones</strong>
              <button className="close-btn" onClick={toggleNotifications}>X</button>
            </div>
            <ul>
              <li>Notificación 1</li>
              <li>Notificación 2</li>
              <li>Notificación 3</li>
            </ul>
          </div>
        )}

        {showUserOptions && (
          <div className="notification-bubble user-options">
            <div className="notification-header">
              <strong>Usuario</strong>
              <button className="close-btn" onClick={toggleUserOptions}>X</button>
            </div>
            <ul>
              <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
              <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
              <li><button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Inicia en tu negocio</h2>
        <button className="login-btn" onClick={() => navigate("/registroNegocio")}>Registrar negocio</button>

        <div className="empleados-grid">
          {negocios.length > 0 ? (
            negocios.map((negocio, index) => (
              <div key={index} className="empleado-card">
                <h3>{negocio.name}</h3>
                <button className="ver-btn" onClick={() => handleVerNegocio(negocio.id)}>Ver</button>
              </div>
            ))
          ) : (
            <p>No hay negocios disponibles</p> 
          )}
        </div>
      </div>
    </div>
  );
}

export default Empleados;
