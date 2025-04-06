import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const obtenerProducto = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosVenta/${localStorage.getItem("productoId")}`);
    if (!response.ok) {
      throw new Error("Error al obtener el producto");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener el producto:", error);
    return null;
  }
};

function VerProductoCartaDueno() {
  const navigate = useNavigate();
  const [producto, setProducto] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
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

  useEffect(() => {
    const cargarProducto = async () => {
      const data = await obtenerProducto();
      setProducto(data);
    };
    cargarProducto();
  }, []);

  const eliminarProducto = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/productosVenta/${producto.id}`, {
        method: "DELETE",
      });
      if (!response.ok) {
        throw new Error("Error al eliminar el producto");
      }
      navigate(`/verTipoProductoCartaDueno/${localStorage.getItem("categoriaNombre")}`);
    } catch (error) {
      console.error("Error al eliminar el producto:", error);
    }
  };

  if (!producto) {
    return <h2>Producto no encontrado</h2>;
  }

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
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>        
        <h1 className="title">GastroStock</h1>
        <h1>Producto</h1>
        <div className="empleado-card">
          <h1 className="producto-nombre">{producto.name}</h1>
          <p><strong>Categoría:</strong> {producto.categoria?.name || "Sin categoría"}</p>
          {/* <p><strong>Ingredientes:</strong> {producto.name}</p> */}
          <p><strong>Ingredientes:</strong> {producto.ingredientes?.length > 0
  ? producto.ingredientes.map(ing => `${ing.productoInventario.name} (${ing.cantidad})`).join(", ")
  : "No tiene ingredientes"}
</p>

          <p><strong>Precio:</strong> {producto.precioVenta} €</p>
          <button style={{background: "#157E03", color: "white"}} onClick={() => navigate(`/editarProductoCartaDueno/${producto.id}`)}>Editar Producto</button>
          <button style={{background: "#9A031E", color: "white"}} onClick={() => setShowDeleteModal(true)}>Eliminar Producto</button>
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

{showDeleteModal && (
          <div className="modal-overlay">
            <div className="modal">
              <h3>¿Está seguro que desea eliminar este producto?</h3>
              <div className="modal-buttons">
                <button className="confirm-btn" onClick={eliminarProducto}>Sí</button>
                <button className="cancel-btn" onClick={() => setShowDeleteModal(false)}>No</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default VerProductoCartaDueno;