import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";


function Inventario() {
  const navigate = useNavigate();
  const [categorias, setCategorias] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  
  const negocioId = "1"; 
  const [showLogoutModal, setShowLogoutModal] = useState(false); 
  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  useEffect(() => {
    fetch("http://localhost:8080/api/categorias/negocio/" + negocioId) 
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error al obtener las categorías");
        }
        return response.json();
      })
      .then((data) => {
        const categoriasInventario = data
          .filter((categoria) => categoria.pertenece === "INVENTARIO")
          .map((categoria) => ({
            id: categoria.id,
            nombre: categoria.name,
            pertenece: categoria.pertenece,
            emoticono: obtenerEmoticono(categoria.name),
          }));

        setCategorias(categoriasInventario);
      })
      .catch((error) => console.error("Error:", error));
  }, [negocioId]);

  const obtenerEmoticono = (nombre) => {
    switch (nombre) {
      case "COMIDA":
        return "🍽️";
      case "BEBIDAS":
        return "🥤";
      case "CARNES":
        return "🥩";
      case "FRUTAS":
        return "🍎";
      case "VERDURAS":
        return "🥦";
      case "PANADERIA":
        return "🍞";
      case "DULCES":
        return "🍬";
      default:
        return "🍴";
    }
  };

  const handleCategoriaClick = (categoriaId, nombreCategoria) => {
    navigate(`/verTipoProducto/${categoriaId}`, {
      state: { tipo: "INVENTARIO", nombreCategoria }
    });
  };
  
  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

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
        <h2>Inventario</h2>
        <div className="button-container3">
          <button className="button">📥 Exportar</button>
          <button className="button">🔍 Filtrar</button>
        </div>

        <div className="empleados-grid1">
          {categorias.map((categoria, index) => (
            <div key={index} className="empleado-card">
              <h4>{categoria.emoticono}</h4>
              <h3>{categoria.nombre}</h3>
              <button 
                className="ver-btn" 
                onClick={() => handleCategoriaClick(categoria.id, categoria.nombre)}  
              >
                👁️ Ver
              </button>
            </div>
          ))}
        </div>

        <div className="button-container1">
          <button className="button" onClick={() => navigate("/alertaStock")}>⚠️ Alerta Stock</button>
          <button className="button" onClick={() => navigate("/perdidas")}>📉 Pérdidas</button>
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

export default Inventario;