import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";
import Notificaciones from "../../components/Notifications";

const VerEntregasProveedor = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const proveedorId = localStorage.getItem("proveedorId");
  const [carritos, setCarritos] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  useEffect(() => {
    const obtenerCarritos = async () => {
      try {
        const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/carritos/proveedor/${proveedorId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error("Error al obtener los carritos del proveedor");
        }

        const data = await response.json();
        setCarritos(data);
      } catch (error) {
        console.error("Error al obtener los carritos:", error);
        setCarritos([]);
      }
    };

    obtenerCarritos();
  }, [proveedorId]);

  const handleClick = (carritoId) => {
    navigate(`/confirmarPendiente/${carritoId}`);
  };

  return (
    <div
      className="home-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        minHeight: "100vh",
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
              <button className="close-btn" onClick={toggleUserOptions}>X</button>
            </div>
            <ul>
              <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
              <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
              <button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
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

        <button onClick={() => navigate(`/verProveedor/${proveedorId}`)} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>Entregas del Proveedor</h2>

        <div className="productos-container">
          {carritos.length === 0 ? (
            <h3>No hay entregas registradas para este proveedor</h3>
          ) : (
            <div className="productos-grid">
              {carritos.map((carrito) => (
                <div
                  key={carrito.id}
                  className="producto-card clickable"
                  onClick={() => handleClick(carrito.id)}
                  style={{ cursor: "pointer" }}
                >
                  <h3>Carrito #{carrito.id}</h3>
                  <p>Fecha de entrega: {carrito.diaEntrega}</p>
                  <p>Precio total: {carrito.precioTotal.toFixed(2)} €</p>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default VerEntregasProveedor;
