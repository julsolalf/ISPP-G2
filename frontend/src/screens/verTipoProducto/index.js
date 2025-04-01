import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const obtenerProductosPorCategoria = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosInventario/categoria/${localStorage.getItem("categoriaNombre")}`);
    if (!response.ok) {
      throw new Error("Error al obtener los productos de la categoría");
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error al obtener los productos:", error);
    return [];
  }
};

function VerTipoProducto() {
  const { categoriaId } = useParams();
  const navigate = useNavigate();
  const [categoria, setCategoria] = useState(null);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout


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
  const [productos, setProductos] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

  useEffect(() => {
    const cargarProductos = async () => {
      const productosCategoria = await obtenerProductosPorCategoria();
      setProductos(productosCategoria);
    };
    cargarProductos();
  }, [categoriaId]);

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
              <button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
              </li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <h1>Productos</h1>
        <div className="button-container3">
          <button className="button" onClick={() => {
            localStorage.setItem("categoriaNombre", localStorage.getItem("categoriaNombre"));
            navigate("/anadirProductoInventario")}}>➕ Añadir</button>
          <button className="button">📥 Exportar</button>
          <button className="button">🔍 Filtrar</button>
        </div>

        {productos.length === 0 ? (
          <h3>No hay productos en esta categoría</h3>
        ) : (
        <div className="empleados-grid">
          {productos.map((producto) => (
            <div key={producto.id} className="empleado-card" 
            onClick={() => {
              localStorage.setItem("productoId", producto.id);
              navigate(`/categoria/${localStorage.getItem("categoriaNombre")}/producto/${producto.id}`)}}
             style={{ cursor: "pointer" }}>
              <h3>{producto.name}</h3>
              <p>Cantidad: {producto.cantidadDeseada}</p>
              {producto.cantidadDeseada <= producto.cantidadAviso && (
                <p style={{ color: "red" }}>⚠ Stock bajo</p>
              )}
            </div>
          ))}
          </div>
        )}
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

export default VerTipoProducto;
