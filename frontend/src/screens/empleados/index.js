import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import axios from "axios"; // Importa axios
import "../../css/listados/styles.css";

function Empleados() {
  const navigate = useNavigate();
  const [empleados, setEmpleados] = useState([]);  // Cambia el estado para manejar empleados vacíos
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

  const loadEmpleados = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/empleados/negocio/1");  // Arrglar para que coja el negocio del usuario
      setEmpleados(response.data); 
    } catch (error) {
      console.error("Error al cargar los empleados:", error);
    }
  };

  useEffect(() => {
    loadEmpleados();  
  }, []);  

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

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

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Empleados</h2>

        <div className="button-container3">
          <button className="button" onClick={() => navigate("/añadirEmpleado")}>➕ Añadir</button>
          <button className="button">📥 Exportar</button>
          <button className="button">🔍 Filtrar</button>
        </div>

        <div className="empleados-grid">
          {empleados.length > 0 ? (
            empleados.map((empleado, index) => (
              <div key={index} className="empleado-card">
                <h3>{empleado.firstName}</h3>
                <p>{empleado.user.authority.authority}</p>
                <p>{empleado.numTelefono}</p>
                <button className="ver-btn" onClick={() => navigate("/verEmpleado")}>Ver</button>
              </div>
            ))
          ) : (
            <p>No hay empleados disponibles</p> 
          )}
        </div>
      </div>
    </div>
  );
}

export default Empleados;
