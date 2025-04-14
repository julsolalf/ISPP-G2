import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const token = localStorage.getItem("token");
const idProveedor = localStorage.getItem("proveedorId");

const obtenerProveedor = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/proveedores/${idProveedor}`, {
      method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
    });
    if (!response.ok) {
      throw new Error("Error al obtener el proveedor");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener el proveedor:", error);
    return null;
  }
};

function VerProveedor() {
  const { id } = useParams(); 
  const navigate = useNavigate();
  const [proveedor, setProveedor] = useState(null);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); 
  const [showDeleteModal, setShowDeleteModal] = useState(false);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };


  useEffect(() => {
    const cargarProveedor = async () => {
      const data = await obtenerProveedor();
      setProveedor(data);
    };
    cargarProveedor();
  }, [id]);

  const eliminarProveedor = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/proveedores/${idProveedor}`, {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        method: "DELETE",
      });
      if (!response.ok) {
        throw new Error("Error al eliminar el proveedor");
      }
      navigate("/proveedores"); 
    } catch (error) {
      console.error("Error al eliminar el proveedor:", error);
    }
  };

  if (!proveedor) {
    return <h2>Proveedor no encontrado</h2>;
  }

  return (
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
              <li><button className="user-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate("/proveedores")} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>        
        <h1 className="title">GastroStock</h1>
        <h2>Proveedor</h2>
        <div className="empleado-card">
          <h1 className="proveedor-nombre">{proveedor.name}</h1>
          <p><strong>Email:</strong> {proveedor.email}</p>
          <p><strong>Teléfono:</strong> {proveedor.telefono}</p>
          <p><strong>Dirección:</strong> {proveedor.direccion}</p>
          
          <button style={{ background: "#157E03", color: "white" }} onClick={() => {
            localStorage.setItem("proveedorId", proveedor.id)
            navigate(`/editarProveedor/${proveedor.id}`)}}>Editar Proveedor</button>
          <button style={{ background: "#9A031E", color: "white" }} onClick={() => setShowDeleteModal(true)}>Eliminar Proveedor</button>
        </div>

        {showDeleteModal && (
          <div className="modal-overlay">
            <div className="modal">
              <h3>¿Está seguro que desea eliminar este proveedor?</h3>
              <div className="modal-buttons">
                <button className="confirm-btn" onClick={eliminarProveedor}>Sí</button>
                <button className="cancel-btn" onClick={() => setShowDeleteModal(false)}>No</button>
              </div>
            </div>
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
      </div>
  );
}

export default VerProveedor;
