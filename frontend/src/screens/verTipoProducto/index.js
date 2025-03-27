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
              <li><button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <h1>Productos</h1>

        {productos.length === 0 ? (
          <h3>No hay productos en esta categoría</h3>
        ) : (
          <div className="empleados-grid">
            {productos.map((producto) => (
              <div key={producto.id} className="empleado-card" 
                  
                   onClick={() => {
                    localStorage.setItem("productoId", producto.id);
                    navigate(`/categoria/${categoriaId}/producto/${producto.id}`)}}
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
      </div>
    </div>
  );
}

export default VerTipoProducto;