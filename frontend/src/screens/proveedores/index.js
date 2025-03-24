import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/listados/styles.css";

function Proveedores() {
  const navigate = useNavigate();
  const [proveedores, setProveedores] = useState([
    { nombre: "Distribuidora A", telefono: "123-456-7890", descripcion: "Proveedor de bebidas y refrescos" },
    { nombre: "Proveedor B", telefono: "987-654-3210", descripcion: "Especialista en carnes y embutidos" },
    { nombre: "Comercializadora C", telefono: "456-789-0123", descripcion: "Lácteos y productos derivados" },
  ]);

  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

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
        <h2>Proveedores</h2>

        <div className="button-container3">
          <button className="button" onClick={() => navigate("/añadirProveedor")}>➕ Añadir</button>
          <button className="button">📥 Exportar</button>
          <button className="button">🔍 Filtrar</button>
        </div>


        <div className="empleados-grid">
          {proveedores.map((proveedor, index) => (
            <div key={index} className="empleado-card">
              <h3>{proveedor.nombre}</h3>
              <p>{proveedor.descripcion}</p>
              <p>{proveedor.telefono}</p>
              <button className="ver-btn" onClick={() => navigate("/verProveedor")}>Ver</button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Proveedores;
