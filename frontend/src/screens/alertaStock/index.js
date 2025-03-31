import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/listados/styles.css";

function AlertaStock() {
  const navigate = useNavigate();
  const [productos, setProductos] = useState([
    {
      nombre: "Lata de Refresco",
      categoria: "Bebidas",
      cantidad: 100,
      cantidadAlerta: 70,
      caducidades: [
        { unidades: 70, fecha: "2026-09-13" },
        { unidades: 30, fecha: "2026-09-17" }
      ]
    },
    {
      nombre: "Botella de Agua",
      categoria: "Bebidas",
      cantidad: 50,
      cantidadAlerta: 20,
      caducidades: [
        { unidades: 25, fecha: "2025-06-30" },
        { unidades: 25, fecha: "2025-07-10" }
      ]
    },
    {
      nombre: "Paquete de Galletas",
      categoria: "Snacks",
      cantidad: 80,
      cantidadAlerta: 40,
      caducidades: [
        { unidades: 50, fecha: "2024-12-20" },
        { unidades: 30, fecha: "2025-01-05" }
      ]
    },
    {
      nombre: "Caja de Cereal",
      categoria: "Desayuno",
      cantidad: 150,
      cantidadAlerta: 100,
      caducidades: [
        { unidades: 80, fecha: "2025-03-01" },
        { unidades: 70, fecha: "2025-03-15" }
      ]
    },
    {
      nombre: "Paquete de Harina",
      categoria: "Ingredientes",
      cantidad: 120,
      cantidadAlerta: 50,
      caducidades: [
        { unidades: 60, fecha: "2025-06-15" },
        { unidades: 60, fecha: "2025-06-20" }
      ]
    },
    {
      nombre: "Salsa de Tomate",
      categoria: "Salsas",
      cantidad: 200,
      cantidadAlerta: 100,
      caducidades: [
        { unidades: 120, fecha: "2024-11-01" },
        { unidades: 80, fecha: "2024-11-15" }
      ]
    },
    {
      nombre: "Leche UHT",
      categoria: "Lácteos",
      cantidad: 70,
      cantidadAlerta: 30,
      caducidades: [
        { unidades: 40, fecha: "2025-04-15" },
        { unidades: 30, fecha: "2025-04-25" }
      ]
    },
    {
      nombre: "Aceite de Oliva",
      categoria: "Aceites y Vinagres",
      cantidad: 60,
      cantidadAlerta: 20,
      caducidades: [
        { unidades: 30, fecha: "2025-09-01" },
        { unidades: 30, fecha: "2025-09-10" }
      ]
    },
    {
      nombre: "Arroz",
      categoria: "Granos",
      cantidad: 200,
      cantidadAlerta: 100,
      caducidades: [
        { unidades: 100, fecha: "2025-11-01" },
        { unidades: 100, fecha: "2025-11-15" }
      ]
    },
    {
      nombre: "Manzanas",
      categoria: "Frutas",
      cantidad: 120,
      cantidadAlerta: 60,
      caducidades: [
        { unidades: 60, fecha: "2025-03-01" },
        { unidades: 60, fecha: "2025-03-10" }
      ]
    },
    {
      nombre: "Pechuga de Pollo",
      categoria: "Carnes",
      cantidad: 50,
      cantidadAlerta: 20,
      caducidades: [
        { unidades: 30, fecha: "2025-04-20" },
        { unidades: 20, fecha: "2025-04-30" }
      ]
    },
    {
      nombre: "Papas Fritas",
      categoria: "Snacks",
      cantidad: 100,
      cantidadAlerta: 40,
      caducidades: [
        { unidades: 60, fecha: "2025-01-20" },
        { unidades: 40, fecha: "2025-02-01" }
      ]
    }
  ]);
  
  
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout
const [searchTerm, setSearchTerm] = useState("");
  

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  return (
    <div
      className="home-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        height: "100vh",
        overflowY: "auto",
      }}
    >
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
              <button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Alerta Stock</h2>

        <div className="button-container3">
          <input 
            type="text" 
            className="search-input" 
            placeholder="🔍 Buscar" 
            onChange={(e) => setSearchTerm(e.target.value)} 
          />
          <button className="button">🔍 Filtrar</button>
          <button className="ver-btn">➕ Anadir todo al 🛒 </button>
        </div>
        
        <div className="empleados-grid1">
          {productos.map((producto, index) => (
            <div key={index} className="empleado-card">
              <h3>{producto.nombre}</h3>
              <p>Cantidad: {producto.cantidad}</p>
              <p>Cantidad Alerta: {producto.cantidadAlerta}</p>
              <button className="ver-btn" >Anadir al 🛒</button>
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

export default AlertaStock;
