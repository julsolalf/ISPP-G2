import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const TPV = () => {
  const [mesas, setMesas] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const navigate = useNavigate();
  
  useEffect(() => {
    const token = localStorage.getItem("token");  // Obtener el token del localStorage

    const fetchMesas = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/mesas/negocio/1`, {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,  // Incluir el token en los encabezados
          },
        });

        if (!res.ok) throw new Error("Error al obtener mesas");
        const data = await res.json();
        setMesas(data);  // Guardar las mesas en el estado
      } catch (error) {
        console.error("Error cargando mesas:", error);
      }
    };

    fetchMesas();  // Llamar a la función de obtener mesas
  }, []);

  const handleMesaClick = (mesaId) => {
    localStorage.setItem("mesaId", mesaId);  // Guardar el ID de la mesa en el localStorage
    navigate(`/productos/${mesaId}`);
  };

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
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
      }}
    >
    <div className="content">

    <div className="icon-container-right">
          <Bell size={30} className="icon" onClick={() => setShowNotifications(!showNotifications)} />
          <User size={30} className="icon" onClick={() => setShowUserOptions(!showUserOptions)} />
        </div>

        {showNotifications && (
          <div className="notification-bubble">
            <div className="notification-header">
              <strong>Notificaciones</strong>
              <button className="close-btn" onClick={() => setShowNotifications(false)}>X</button>
            </div>
            <ul>
              <li>Notificación 1</li>
              <li>Notificación 2</li>
              <li>Notificación 3</li>
            </ul>
          </div>
        )}

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

        {showUserOptions && (
          <div className="notification-bubble user-options">
            <div className="notification-header">
              <strong>Usuario</strong>
              <button className="close-btn" onClick={() => setShowUserOptions(false)}>X</button>
            </div>
            <ul>
              <li>
                <button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button>
              </li>
              <li>
                <button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button>
              </li>
              <li>
                <button className="user-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
              </li>
            </ul>
          </div>
        )}
      <button onClick={() => navigate("/inicioEmpleado")} className="back-button">⬅ Volver</button>
      <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
      <h1 className="title">GastroStock</h1>
      <h2>🪑 Selecciona una Mesa</h2>
      <div className="empleados-grid">
        {mesas.map((mesa) => (
          <div key={mesa.id} className="empleado-card">
            <h3>{mesa.name || `Mesa ${mesa.id}`}</h3>
            <button className="ver-btn" onClick={() => handleMesaClick(mesa.id)}>
              Seleccionar
            </button>
          </div>
        ))}
      </div>
    </div>
    </div>
  );
};

export default TPV;
