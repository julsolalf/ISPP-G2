import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";
import Notificaciones from "../../../components/Notifications";

// Función para obtener la venta desde el backend
const obtenerVenta = async (ventaId) => {
  try {
    const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/pedidos/${ventaId}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      }
    );
    if (!response.ok) {
      throw new Error("Error al obtener la venta");
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error al obtener la venta:", error);
    return null;
  }
};

function VerVentaEspecifica() {
  const { ventaId } = useParams();
  const navigate = useNavigate();
  const [venta, setVenta] = useState(null);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  
  const eliminarVenta = async () => {
    try {
      const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/pedidos/${ventaId}`, {
        method: "DELETE",
      });
      if (!response.ok) {
        throw new Error("Error al eliminar la venta");
      }
      navigate("/ventas"); 
    } catch (error) {
      console.error("Error al eliminar la venta:", error);
    }
  };

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  useEffect(() => {
    const cargarDatos = async () => {
      const ventaData = await obtenerVenta(ventaId);
      setVenta(ventaData);
    };
    cargarDatos();
  }, [ventaId]);

  if (!venta) {
    return <h2>Venta no encontrada</h2>;
  }

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

        <button onClick={() => navigate("/ventas")} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>          <h1 className="title">GastroStock</h1>
        <h2>Ver Pedido</h2>
        

        <div className="producto-card">
          <h1 className="producto-nombre">Pedido #{venta.id}</h1>
          <p className="producto-atributo"><strong>Fecha:</strong> {new Date(venta.fecha).toLocaleString()}</p>
          <p className="producto-atributo"><strong>Total:</strong> ${venta.precioTotal.toFixed(2)}</p>
          <p className="producto-atributo"><strong>Mesa:</strong> {venta.mesa.name}</p>
          <p className="producto-atributo"><strong>Empleado:</strong> {venta.empleado.firstName} {venta.empleado.lastName}</p>
          <p className="producto-atributo"><strong>Negocio:</strong> {venta.mesa.negocio.name}</p>
          <button style={{ background: "#157E03", color: "white" }} onClick={() => {
            localStorage.setItem("ventaId", venta.id)
            navigate(`/editarVenta/${venta.id}`)}}>Editar Pedido</button>
          <button style={{ background: "#9A031E", color: "white" }} onClick={() => setShowDeleteModal(true)}>Eliminar Pedido</button>
        </div>
        {showDeleteModal && (
          <div className="modal-overlay">
            <div className="modal">
              <h3>¿Está seguro que desea eliminar este pedido?</h3>
              <div className="modal-buttons">
                <button className="confirm-btn" onClick={eliminarVenta}>Sí</button>
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
    </div>
  );
}

export default VerVentaEspecifica;
