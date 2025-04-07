import React from "react";
import { useNavigate } from "react-router-dom";
import "../../css/planes/styles.css";
import { Bell, User } from "lucide-react";

function PantallaPlanes() {
  const navigate = useNavigate();
  const [showNotifications, setShowNotifications] = React.useState(false);
  const [showUserOptions, setShowUserOptions] = React.useState(false);
  const [showLogoutModal, setShowLogoutModal] = React.useState(false); // Estado para la modal de logout
  const [isFreePlanActive, setIsFreePlanActive] = React.useState(true);
  const [isPremiumPlanActive, setIsPremiumPlanActive] = React.useState(false); 

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.clear();
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
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
        padding: "20px",
        overflowY: "auto",
      }}
    >
      <div className="content1">
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
              <button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
              </li>
            </ul>
          </div>
        )}
        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Planes y Precios</h2>
        <div className="pricing">
            <div className="plan">
            <h3>Plan Free</h3>
            <p>0€/mes</p>
            <ul>
                <li>Gestor del inventario</li>
                <li>Alertas personalizadas</li>
                <li>Estadísticas mínimas</li>
            </ul>
            {isFreePlanActive ? (
                <button className="menu-btn active" onClick={() => navigate("/plan-actual")}>Actual</button>
            ) : (
                <button className="menu-btn inactive" onClick={() => navigate("/plan-activar-free")}>Cambiar</button>
            )}
            </div>

            <div className="plan">
            <h3>Plan Premium</h3>
            <p>25€/mes</p>
            <ul>
                <li>Todas las funciones del plan Free</li>
                <li>IA personal para gestión optimizada</li>
                <li>Análisis detallado y predictivo</li>
                <li>Predicción de la oferta y la demanda</li>
                <li>Gestor de proveedores</li>
                <li>Gestor de restock y control de pérdidas</li>
                <li>Alertas personalizadas avanzadas</li>
                <li>Gestor del inventario automatizado</li>
            </ul>
            {isPremiumPlanActive ? (
                <button className="menu-btn active" onClick={() => navigate("/plan-mejorar")}>Actual</button>
            ) : (
                <button className="menu-btn inactive" onClick={() => navigate("/plan-activar-premium")}>Mejorar</button>
            )}
            </div>
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

export default PantallaPlanes;
