import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import axios from "axios";
import "../../css/listados/styles.css";

function Proveedores() {
  const navigate = useNavigate();
  const [proveedores, setProveedores] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

  useEffect(() => {
    const fetchProveedores = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/proveedores");
        setProveedores(response.data);
      } catch (error) {
        console.error("Error al obtener los proveedores:", error);
      }
    };
    fetchProveedores();
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
              <button className="close-btn" onClick={toggleNotifications}>
                X
              </button>
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
              <button className="close-btn" onClick={toggleUserOptions}>
                X
              </button>
            </div>
            <ul>
              <li>
                <button className="user-btn" onClick={() => navigate("/perfil")}>
                  Ver Perfil
                </button>
              </li>
              <li>
                <button className="user-btn" onClick={() => navigate("/planes")}>
                  Ver planes
                </button>
              </li>
              <li>
                <button className="user-btn" onClick={() => navigate("/logout")}>
                  Cerrar Sesión
                </button>
              </li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">
          ⬅ Volver
        </button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Proveedores</h2>

        <div className="button-container3">
          <button className="button" onClick={() => navigate("/añadirProveedor")}>
            ➕ Añadir
          </button>
          <button className="button">📥 Exportar</button>
          <button className="button">🔍 Filtrar</button>
        </div>

        <div className="empleados-grid">
          {proveedores.map((proveedor) => (
            <div key={proveedor.id} className="empleado-card">
              <h3>{proveedor.name}</h3>
              <p>{proveedor.direccion}</p>
              <p>{proveedor.telefono}</p>
              <button className="ver-btn" onClick={() => navigate("/verProveedor")}>
                Ver
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Proveedores;
