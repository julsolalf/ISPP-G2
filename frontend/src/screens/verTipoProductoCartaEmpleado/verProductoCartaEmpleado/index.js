import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

// Obtener detalles del producto
const obtenerProducto = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosVenta/${localStorage.getItem("productoId")}`);
    if (!response.ok) throw new Error("Error al obtener el producto");
    return await response.json();
  } catch (error) {
    console.error("Error al obtener el producto:", error);
    return null;
  }
};

// Obtener ingredientes del producto
const obtenerIngredientes = async (productoId) => {
  try {
    const res = await fetch(`http://localhost:8080/api/ingredientes/productoVenta/${productoId}`);
    if (!res.ok) throw new Error("Error al obtener ingredientes");
    return await res.json();
  } catch (err) {
    console.error("Error cargando ingredientes:", err);
    return [];
  }
};

function VerProductoCartaEmpleado() {
  const navigate = useNavigate();
  const [producto, setProducto] = useState(null);
  const [ingredientes, setIngredientes] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.removeItem("userToken");
    navigate("/");
  };

  useEffect(() => {
    const cargarDatos = async () => {
      const prod = await obtenerProducto();
      if (prod) {
        setProducto(prod);
        const ingr = await obtenerIngredientes(prod.id);
        setIngredientes(ingr);
      }
    };
    cargarDatos();
  }, []);

  if (!producto) return <h2>Producto no encontrado</h2>;

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
              <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
              <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
              <li><button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <h1>Producto</h1>
        <div className="empleado-card">
          <h1 className="producto-nombre">{producto.name}</h1>
          <p><strong>Categoría:</strong> {producto.categoria?.name || "Sin categoría"}</p>

          <p><strong>Ingredientes:</strong></p>
          {ingredientes.length > 0 ? (
            <ul style={{ listStyle: "none", padding: 0 }}>
              {ingredientes.map((ing) => (
                <li key={ing.id}>🧂 {ing.productoInventario.name} - {ing.cantidad}</li>
              ))}
            </ul>
          ) : (
            <p>No tiene ingredientes</p>
          )}

          <p><strong>Precio:</strong> {producto.precioVenta} €</p>
        </div>

        {/* Modal de Logout */}
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

export default VerProductoCartaEmpleado;
