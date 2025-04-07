import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import axios from "axios"; // Importa axios
import "../../css/listados/styles.css";

function Empleados() {
  const navigate = useNavigate();
  const [negocios, setNegocios] = useState([]);  
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout

  const loadNegocios = async () => {
    try {
      const response = await axios.get("http://localhost:8080/api/negocios/dueno/1");  // Arrglar para que coja el negocio del usuario
      setNegocios(response.data); 
    } catch (error) {
      console.error("Error al cargar los negocios:", error);
    }
  };

  useEffect(() => {
    loadNegocios();  
  }, []);  

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
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
              <button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
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
                <button className="ver-btn" onClick={() => navigate("/inicioDueno")}>Ver</button>
              </div>
            ))
          ) : (
            <p>No hay negocios disponibles</p> 
          )}
        </div>

        {/* Modal de Confirmación para Logout */}
        {showLogoutModal && (
          <div className="modal-overlay">
            <div className="modal">
              <h3>¿Está seguro que desea abandonar la sesión?</h3>
              <div className="modal-buttons">
                <button className="confirm-btn" onClick={handleLogout}>Sí</button>
                <button className="cancel-btn" onClick={() => setShowLogoutModal(false)}>No</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Empleados;
