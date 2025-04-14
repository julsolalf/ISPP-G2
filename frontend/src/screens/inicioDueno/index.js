import { useState } from "react";
import { useNavigate, Link  } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/inicio/styles.css";

function HomeScreen() {
  const navigate = useNavigate();
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  return (
    <div className="inicio-container">
      <div className="fondo">
        <div>
          <button onClick={() => navigate("/")} className="back-button" style={{marginLeft:"3%", fontSize:"28px"}}>⬅ Volver</button>
          <Bell size={40} style={{marginLeft:"70%",marginTop:"1%"}}className="icon" onClick={toggleNotifications} />
          <User size={40} style={{marginLeft:"3%"}} className="icon" onClick={toggleUserOptions} />
        </div>
        {showNotifications && (
        <div className="notification-bubble" style={{marginRight:"72%",marginTop:"5%"}}>
          <div className="notification-header">
            <strong>Notificaciones</strong>
            <button className="close-btn" onClick={toggleNotifications}>X</button>
          </div>
          <ul>
            <li>Notificación 1</li>
            <li>Notificación 2</li>
            <li>Notificación 3</li>
          </ul>
        </div>)}
        {showUserOptions && (
        <div className="notification-bubble user-options" style={{marginRight:"72%",marginTop:"5%"}}>
          <div className="notification-header">
            <strong>Usuario</strong>
            <button className="close-btn" onClick={toggleUserOptions}>X</button>
          </div>
          <ul>
            <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
            <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
            <button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
          </ul>
        </div>)}
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo-incio"/>
        </Link>  
        <div className="clearfix"></div>
        <h1 className="title" style={{marginTop:"5%"}}>GastroStock</h1>
        <div className="clearfix"></div>
        <div className="button-container1">
          <div className="clearfix"></div>
          <button className="menu-btn" style={{fontSize:"32px"}} onClick={() => navigate("/empleados")}><span role="img" aria-label="empleados">👨‍💼</span> Empleados</button>
          <div className="clearfix" style={{marginTop:"2%"}}></div>
          <button className="menu-btn" style={{fontSize:"32px"}} onClick={() => navigate("/dashboard")}><span role="img" aria-label="dashboard">📊</span> Dashboard</button>
          <div className="clearfix" style={{marginTop:"2%"}}></div>
          <button className="menu-btn" style={{fontSize:"32px"}} onClick={() => navigate("/inventario")}><span role="img" aria-label="inventario">📦</span> Inventario</button>
          <div className="clearfix" style={{marginTop:"2%"}}></div>
          <button className="menu-btn" style={{fontSize:"32px"}} onClick={() => navigate("/ventas")}><span role="img" aria-label="ventas">💰</span> Ventas</button>
          <div className="clearfix" style={{marginTop:"2%"}}></div>
          <button className="menu-btn" style={{fontSize:"32px"}} onClick={() => navigate("/carta")}><span role="img" aria-label="carta">🍽️</span> Carta</button>
          <div className="clearfix" style={{marginTop:"2%"}}></div>
          <button className="menu-btn" style={{fontSize:"32px"}} onClick={() => navigate("/proveedores")}><span role="img" aria-label="proveedores">📋</span> Proveedores</button>
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

export default HomeScreen;
