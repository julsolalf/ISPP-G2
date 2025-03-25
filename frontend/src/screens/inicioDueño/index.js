import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/inicio/styles.css";

function HomeScreen() {
  const navigate = useNavigate();
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  return (
    <div
      className="home-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        height: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
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
              <li>
                <button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button>
              </li>
              <li>
                <button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button>
              </li>
              <li>
                <button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button>
              </li>
            </ul>
          </div>
        )}

        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Inicio</h2>

        <div className="button-container1">
            <button className="menu-btn" onClick={() => navigate("/empleados")}><span role="img" aria-label="empleados">👨‍💼</span> Empleados</button>
            <button className="menu-btn" onClick={() => navigate("/dashboard")}><span role="img" aria-label="dashboard">📊</span> Dashboard</button>
            <button className="menu-btn" onClick={() => navigate("/inventario")}><span role="img" aria-label="inventario">📦</span> Inventario</button>
            <button className="menu-btn" onClick={() => navigate("/ventas")}><span role="img" aria-label="ventas">💰</span> Ventas</button>
            <button className="menu-btn" onClick={() => navigate("/carta")}><span role="img" aria-label="carta">🍽️</span> Carta</button>
            <button className="menu-btn" onClick={() => navigate("/proveedores")}><span role="img" aria-label="proveedores">📋</span> Proveedores</button>
        </div>
      </div>
    </div>
  );
}

export default HomeScreen;
