import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/listados/styles.css";
import "../../css/paginasBase/styles.css";

function Empleados() {
  const navigate = useNavigate();
  const [negocios, setNegocios] = useState([]);  
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout

  const loadNegocios = async () => {
    const token = localStorage.getItem("token");
    const duenoId = localStorage.getItem("duenoId");
    try {
      if (duenoId) {
        const response = await fetch(`http://localhost:8080/api/negocios`, {
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
    localStorage.setItem("negocioId", negocioId);
    navigate("/inicioDueno");
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  return (
    <div className="page-container">
      <div className="fondo">
      <div>
        <button onClick={() => navigate("/")} className="back-button" style={{marginLeft:"15%", fontSize:"28px"}}>⬅ Volver</button>
        <Bell size={40} style={{marginLeft:"80%",marginTop:"1%"}}className="icon" onClick={toggleNotifications} />
        <User size={40} style={{marginLeft:"3%"}} className="icon" onClick={toggleUserOptions} />
      </div>
      {showNotifications && (
      <div className="notification-bubble" style={{marginRight:"17.5%",marginTop:"5%"}}>
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
        <div className="notification-bubble user-options" style={{marginRight:"13.5%",marginTop:"5%"}}>
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
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <div className="clearfix"></div>
        <h1 className="title" style={{marginTop:"10%"}}>GastroStock</h1>
        <div className="clearfix"></div>

        <h2>Inicia en tu negocio</h2>
        <div className="empleados-grid">
        {negocios.length > 0 ? (
          negocios.map((negocio, index) => (
            <div key={index} className="empleado-card">
              <h3>{negocio.name}</h3>
              <button className="ver-btn" onClick={() => handleVerNegocio(negocio.id)}>Ver</button>
            </div>
          ))
        ) : (
        <>
        </>
        )}
        </div>
        <button className="login-btn" onClick={() => navigate("/registroNegocio")} style={{fontSize:"26px"}}>Registrar negocio</button>

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
