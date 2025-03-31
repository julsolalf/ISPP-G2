import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const obtenerCategoriasCarta = async () => {
  try {
    const response = await fetch("http://localhost:8080//api/categorias/negocio/1");
    if (!response.ok) {
      throw new Error("Error al obtener las categorías de la carta");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener las categorías de la carta:", error);
    return [];
  }
};

function Carta() {
  const navigate = useNavigate();
  const [categorias, setCategorias] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  /* useEffect(() => {
    const usuario = JSON.parse(localStorage.getItem("user")); // Obtener usuario del localStorage
    
    // Verificar si el usuario tiene el rol de "empleado"
    const esEmpleado = usuario?.authorities?.some(auth => auth.authority === "ROLE_EMPLEADO");

    if (!esEmpleado) {
      navigate("/"); // Redirige si no es un empleado
      return;
    }

    const cargarCategorias = async () => {
      const datosCategorias = await obtenerCategoriasCarta();
      setCategorias(datosCategorias);
    };

    cargarCategorias();
  }, [navigate]); */

  useEffect(() => {
    const cargarCategorias = async () => {
      const datosCategorias = await obtenerCategoriasCarta();
      setCategorias(datosCategorias);
    };
    cargarCategorias();
  }, []);

  return (
    <div className="home-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        height: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
      }}>
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

        {showUserOptions && (
          <div className="notification-bubble user-options">
            <div className="notification-header">
              <strong>Usuario</strong>
              <button className="close-btn" onClick={() => setShowUserOptions(false)}>X</button>
            </div>
            <ul>
              <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
              <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
              <li><button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Carta</h2>
        <div className="button-container3">
          <button className="button" onClick={() => navigate("/anadirCategoria")}>➕ Anadir</button>
          <button className="button">📥 Exportar</button>
          <button className="button">🔍 Filtrar</button>
        </div>

        <div className="empleados-grid1">
          {categorias.map((categoria) => (
            <div key={categoria.id} className="empleado-card">
              <h3>{categoria.name}</h3>
              <button className="ver-btn" onClick={() => navigate(`/carta/${categoria.id}`)}>👁️ Ver</button>
            </div>
          ))}
        </div>
        
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

export default Carta;
