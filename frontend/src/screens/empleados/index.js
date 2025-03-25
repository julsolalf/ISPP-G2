import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/listados/styles.css";

function Empleados() {
  const navigate = useNavigate();
  const [empleados, setEmpleados] = useState([
    { nombre: "Juan Pérez", telefono: "123-456-7890", rol: "Cajero", codigo: "EMP001", turno: "Mañana", posicion: "Caja 1", fechaCreacion: "2023-06-15" },
    { nombre: "María Gómez", telefono: "987-654-3210", rol: "Supervisor", codigo: "EMP002", turno: "Tarde", posicion: "Supervisión", fechaCreacion: "2022-11-22" },
    { nombre: "Carlos Ramírez", telefono: "456-789-0123", rol: "Repartidor", codigo: "EMP003", turno: "Noche", posicion: "Reparto", fechaCreacion: "2024-01-10" },
  ]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout

  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
    navigate("/inicioSesion"); // Redirigir a la pantalla de inicio de sesión
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
          {empleados.map((empleado, index) => (
            <div key={index} className="empleado-card">
              <h3>{empleado.nombre}</h3>
              <p>{empleado.rol}</p>
              <p>{empleado.telefono}</p>
              <p>{empleado.posicion}</p>
              <button className="ver-btn" onClick={() => navigate("/verEmpleado")}>Ver</button>
            </div>
          ))}
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
