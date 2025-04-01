import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const obtenerProductosVentaPorCategoria = async (categoriaId) => {
  try {
    const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/productosVenta/categoria/${categoriaId}`);
    if (!response.ok) {
      throw new Error("Error al obtener los productos de la categoría");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener los productos:", error);
    return [];
  }
};

function VerProductosVenta() {
  const { categoriaId } = useParams();
  const navigate = useNavigate();
  const [productos, setProductos] = useState([]);
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

  useEffect(() => {
    const cargarProductos = async () => {
      const productosCategoria = await obtenerProductosVentaPorCategoria(categoriaId);
      setProductos(productosCategoria);
    };
    cargarProductos();
  }, [categoriaId]);

  return (
    <div className="home-container" style={{
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
              <li><button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <h1>Productos de la Categoría</h1>

        {productos.length === 0 ? (
          <h3>No hay productos en esta categoría</h3>
        ) : (
          <div className="empleados-grid">
            {productos.map((producto) => (
              <div key={producto.id} className="empleado-card" 
                   onClick={() => navigate(`/categoria/${categoriaId}/producto/${producto.id}`)}
                   style={{ cursor: "pointer" }}>
                <h3>{producto.name}</h3>
                <p>{producto.ingredientes.join(", ")}</p>
              </div>
            ))}
          </div>
        )}

        {showLogoutModal && (
          <div className="modal-overlay">
            <div className="modal">
              <h3>¿Está seguro que desea abandonar la sesión?</h3>
              <div className="modal-buttons">
                <button className="confirm-btn" onClick={() => {
                  localStorage.removeItem("userToken");
                  navigate("/");
                }}>Sí</button>
                <button className="cancel-btn" onClick={() => setShowLogoutModal(false)}>No</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default VerProductosVenta;
