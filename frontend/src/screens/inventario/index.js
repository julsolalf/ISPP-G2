import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react"; 

const categoriasIniciales = [
  {
    id: 1,
    nombre: "Bebidas",
    emoticono: "🥤",
    productos: [
      { nombre: "Coca-Cola", cantidad: 20, alertaStock: 5 },
      { nombre: "Agua", cantidad: 50, alertaStock: 10 },
      { nombre: "Cerveza", cantidad: 15, alertaStock: 3 },
      { nombre: "Jugo de Naranja", cantidad: 30, alertaStock: 8 },
      { nombre: "Sprite", cantidad: 25, alertaStock: 6 },
    ],
  },
  {
    id: 2,
    nombre: "Carnes",
    emoticono: "🥩",
    productos: [
      { nombre: "Pollo", cantidad: 10, alertaStock: 2 },
      { nombre: "Res", cantidad: 8, alertaStock: 3 },
      { nombre: "Cerdo", cantidad: 12, alertaStock: 4 },
      { nombre: "Pavo", cantidad: 5, alertaStock: 1 },
      { nombre: "Cordero", cantidad: 7, alertaStock: 2 },
    ],
  },
  {
    id: 3,
    nombre: "Lácteos",
    emoticono: "🥛",
    productos: [
      { nombre: "Leche", cantidad: 30, alertaStock: 7 },
      { nombre: "Queso", cantidad: 25, alertaStock: 5 },
      { nombre: "Yogur", cantidad: 18, alertaStock: 4 },
      { nombre: "Mantequilla", cantidad: 12, alertaStock: 3 },
      { nombre: "Crema", cantidad: 20, alertaStock: 6 },
    ],
  },
  {
    id: 4,
    nombre: "Frutas",
    emoticono: "🍎",
    productos: [
      { nombre: "Manzanas", cantidad: 40, alertaStock: 8 },
      { nombre: "Bananas", cantidad: 35, alertaStock: 7 },
      { nombre: "Naranjas", cantidad: 50, alertaStock: 10 },
      { nombre: "Fresas", cantidad: 25, alertaStock: 5 },
      { nombre: "Uvas", cantidad: 30, alertaStock: 6 },
    ],
  },
  {
    id: 5,
    nombre: "Verduras",
    emoticono: "🥦",
    productos: [
      { nombre: "Lechuga", cantidad: 20, alertaStock: 4 },
      { nombre: "Tomates", cantidad: 15, alertaStock: 3 },
      { nombre: "Zanahorias", cantidad: 25, alertaStock: 6 },
      { nombre: "Espinacas", cantidad: 12, alertaStock: 3 },
      { nombre: "Pepinos", cantidad: 30, alertaStock: 7 },
    ],
  },
  {
    id: 6,
    nombre: "Panadería",
    emoticono: "🥖",
    productos: [
      { nombre: "Pan", cantidad: 50, alertaStock: 10 },
      { nombre: "Baguette", cantidad: 30, alertaStock: 6 },
      { nombre: "Croissants", cantidad: 20, alertaStock: 5 },
      { nombre: "Pan de Molde", cantidad: 40, alertaStock: 8 },
      { nombre: "Panecillos", cantidad: 15, alertaStock: 3 },
    ],
  },
  {
    id: 7,
    nombre: "Dulces",
    emoticono: "🍬",
    productos: [
      { nombre: "Chocolates", cantidad: 40, alertaStock: 8 },
      { nombre: "Galletas", cantidad: 30, alertaStock: 6 },
      { nombre: "Caramelos", cantidad: 50, alertaStock: 12 },
      { nombre: "Chicles", cantidad: 25, alertaStock: 5 },
      { nombre: "Confites", cantidad: 20, alertaStock: 4 },
    ],
  },
];

function Inventario() {
  const navigate = useNavigate();
  const [categorias, setCategorias] = useState(categoriasIniciales);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

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
                <button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button>
              </li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Inventario</h2>
        <div className="button-container3">
          <button className="button" onClick={() => navigate("/añadirCategoria")}>➕ Añadir</button>
          <button className="button">📥 Exportar</button>
          <button className="button">🔍 Filtrar</button>
        </div>

        <div className="empleados-grid1">
          {categorias.map((categoria, index) => (
            <div key={index} className="empleado-card">
              <h4>{categoria.emoticono}</h4>
              <h3>{categoria.nombre}</h3>
              <button className="ver-btn" onClick={() => navigate(`/verTipoProducto/${categoria.id}`)}>👁️ Ver</button>
            </div>
          ))}
        </div>
        <div className="button-container1">
          <button className="button" onClick={() => navigate("/alertaStock")}>⚠️ Alerta Stock</button>
          <button className="button" onClick={() => navigate("/perdidas")}>📉 Pérdidas</button>
        </div>
      </div>
    </div>
  );
}

export default Inventario;
