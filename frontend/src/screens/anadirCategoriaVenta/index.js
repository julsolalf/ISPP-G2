import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";
import { MenuIconos } from "../../components/MenuIconos";

function PantallaAnadirCategoriaVenta() {
  const navigate = useNavigate();
  const [nombre, setNombre] = useState("");
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const token = localStorage.getItem("token");
  const negocioId = localStorage.getItem("negocioId");
  
  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!nombre.trim()) {
      alert("El nombre de la categoría no puede estar vacío");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/categorias", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          name: nombre,
          negocio: { id: negocioId },
          pertenece: "VENTA",
        }),
      });

      if (!response.ok) {
        throw new Error("Error al añadir la categoría");
      }
      alert("Categoría añadida con éxito");
      navigate(-1);
    } catch (error) {
      console.error("Error en la solicitud:", error);
      alert("Hubo un problema al añadir la categoría");
    }
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
                <button className="user-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
              </li>
            </ul>
          </div>
        )}

                <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
                <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
                <h1 className="title">GastroStock</h1>
                <MenuIconos/>
                <h1>Anadir categoría</h1>

        <form onSubmit={handleSubmit} className="form-container">
          <>Introduce el nombre de la categoría:</>
          <input
            type="text"
            placeholder="Nombre de la categoría"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            required
          />
          <input type="submit" value="Añadir" className="button" />
        </form>
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

export default PantallaAnadirCategoriaVenta