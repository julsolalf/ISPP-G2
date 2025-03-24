import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const obtenerCategorias = async () => {
  return [
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
  ];
};

function VerTipoProducto() {
  const { categoriaId } = useParams();
  const navigate = useNavigate();
  const [categoria, setCategoria] = useState(null);
  const [showNotifications, setShowNotifications] = useState(false);
    const [showUserOptions, setShowUserOptions] = useState(false);
  
    const toggleNotifications = () => {
      setShowNotifications(!showNotifications);
    };
  
    const toggleUserOptions = () => {
      setShowUserOptions(!showUserOptions);
    };

  useEffect(() => {
    const cargarDatos = async () => {
      const categorias = await obtenerCategorias();
      const categoriaEncontrada = categorias.find((cat) => cat.id === parseInt(categoriaId));
      setCategoria(categoriaEncontrada);
    };

    cargarDatos();
  }, [categoriaId]);

  if (!categoria) {
    return <h2>Categoría no encontrada</h2>;
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
        <h1>{categoria.emoticono} {categoria.nombre}</h1>
        <div className="empleados-grid">
          {categoria.productos.map((producto, index) => (
            <div key={index} className="empleado-card">
              <h3>{producto.nombre}</h3>
              <p>Cantidad: {producto.cantidad}</p>
              {producto.cantidad <= producto.alertaStock && (
                <p style={{ color: "red" }}>⚠ Stock bajo</p>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default VerTipoProducto;
