import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/listados/styles.css";  // Asegúrate de que tu archivo de estilos tenga las clases necesarias
import { Bell, User } from "lucide-react";

// Función para obtener los pedidos desde la API
const obtenerPedidos = async () => {
  try {
    const response = await fetch("https://ispp-2425-g2.ew.r.appspot.com/api/pedidos");  // URL de la API de pedidos
    if (!response.ok) {
      throw new Error("Error al obtener los pedidos");
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error al obtener los pedidos:", error);
    return [];
  }
};

function VerPedidos() {
  const navigate = useNavigate();
  const [pedidos, setPedidos] = useState([]);
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
    const cargarDatos = async () => {
      const datosPedidos = await obtenerPedidos();
      setPedidos(datosPedidos);
    };

    cargarDatos();
  }, []);

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
        <h1>📜 Historial de Pedidos</h1>
        <div className="empleados-grid">
          {pedidos.map((pedido) => (
            <div
              key={pedido.id}
              className="empleado-card"
              onClick={() => navigate(`/ventas/${pedido.id}`)}
              style={{ cursor: "pointer" }}
            >
              <h3>Pedido #{pedido.id}</h3>
              <p>Fecha: {new Date(pedido.fecha).toLocaleString()}</p>
              <p>Total: ${pedido.precioTotal.toFixed(2)}</p>
              <p>Mesa: {pedido.mesa.name}</p>
              <p>Empleado: {pedido.empleado.firstName} {pedido.empleado.lastName}</p>
              <p>Negocio: {pedido.mesa.negocio.name}</p>
            </div>
          ))}
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

export default VerPedidos;
