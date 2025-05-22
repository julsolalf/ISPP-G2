import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";
import Notificaciones from "../../../components/Notifications";

function VerProducto() {
  const navigate = useNavigate();
  const [producto, setProducto] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const [lotes, setLotes] = useState([]);
  const token = localStorage.getItem("token");

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  const obtenerLotes = async () => {
    try {
      const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/lotes/producto/${localStorage.getItem("productoId")}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) {
        throw new Error("Error al obtener el lote");
      }
      return await response.json();
    } catch (error) {
      console.error("Error al obtener el lote:", error);
      return null;
    }
  }

  const obtenerProducto = async () => {
    try {
      const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/productosInventario/${localStorage.getItem("productoId")}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) {
        throw new Error("Error al obtener el producto");
      }
      return await response.json();
    } catch (error) {
      console.error("Error al obtener el producto:", error);
      return null;
    }
  };

  useEffect(() => {
    const cargarProducto = async () => {
      const data = await obtenerProducto();
      if (data) {
        setProducto(data);
        const lote = await obtenerLotes(data.id);
        setLotes(lote);
      }
    };
    cargarProducto();
  }, []);

  const eliminarProducto = async () => {
    try {
      const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/productosInventario/${producto.id}`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
           },
      });
      if (!response.ok) {
        throw new Error("Error al eliminar el producto");
      }
      navigate(`/verTipoProducto/${localStorage.getItem("categoriaNombre")}`);
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
          <Bell size={30} className="icon" onClick={() => setShowNotifications(!showNotifications)} />
          <User size={30} className="icon" onClick={() => setShowUserOptions(!showUserOptions)} />
        </div>

        {showNotifications && (
          <div className="icon-container-right">
          <Notificaciones />
          <User size={30} className="icon" onClick={toggleUserOptions} />
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
              <li><button className="user-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
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

        <button onClick={() => navigate(`/verTipoProducto/${localStorage.getItem("categoriaNombre")}`)} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>        
        <h1 className="title">GastroStock</h1>
        <h2>Producto</h2>
        <div className="empleado-card">
          <h1 className="producto-nombre">{producto.name}</h1>
          <p><strong>Precio Compra:</strong> {producto.precioCompra}</p>
          <p><strong>Cantidad Deseada:</strong> {producto.cantidadDeseada}</p>
          <p><strong>Cantidad Aviso:</strong> {producto.cantidadAviso}</p>
          <p><strong>Proveedor:</strong> {producto.proveedor.name}</p>
          {lotes && lotes.length > 0 ? (
  <div>
    <p className="lotes-titulo">Lotes:</p>
    <ul>
      {lotes.map((lote, index) => (
        <li key={index}>
          <p><strong>Cantidad:</strong> {lote.cantidad}</p>
          <p>
  <strong>Fecha de caducidad:</strong>{" "}
  {new Date(lote.fechaCaducidad + "T00:00:00").toLocaleDateString("es-ES", {
    year: "numeric",
    month: "long",
    day: "numeric",
  })}
</p>
        </li>
      ))}
    </ul>
  </div>
) : (
  <p>No hay lotes disponibles para este producto.</p>
)}

          {producto.cantidadDeseada <= producto.cantidadAviso && (
            <p className="producto-alerta">⚠ Stock bajo</p>
          )}
          <button style={{background: "#157E03", color: "white"}} onClick={() => navigate(`/editarProductoInventario/${producto.id}`)}>Editar Producto</button>
          <button style={{background: "#9A031E", color: "white"}} onClick={() => setShowDeleteModal(true)}>Eliminar Producto</button>
        </div>

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

export default VerProducto;