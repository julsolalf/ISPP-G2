import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

function PantallaAñadirProductoCarta() {
  const navigate = useNavigate();
  const [nombre, setNombre] = useState("");
  const [precioVenta, setPrecioVenta] = useState("");
  const [categoriaNombre, setCategoriaNombre] = useState("");
  const [categoriaId, setCategoriaId] = useState("");
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout
  const negocioId = 1; // Debería ser dinámico según el usuario logueado

  useEffect(() => {
    const storedCategoriaNombre = localStorage.getItem("categoriaNombre");
    setCategoriaNombre(storedCategoriaNombre);

    if (storedCategoriaNombre) {
      fetch(`http://localhost:8080/api/categorias/nombre/${localStorage.getItem("categoriaNombre")}`)
        .then(response => response.json())
        .then(data => {
          if (Array.isArray(data)) {
            const categoriaDelNegocio = data.find(categoria => categoria.negocio.id === negocioId);
            if (categoriaDelNegocio) {
              setCategoriaId(categoriaDelNegocio.id);
            } else {
              alert("No se encontró la categoría para este negocio");
            }
          } else {
            alert("La respuesta de la API no es una lista de categorías");
          }
        })
        .catch((error) => {
          console.error("Error obteniendo el ID de la categoría", error);
          alert("Hubo un problema al obtener el ID de la categoría");
        });
    }
  }, []);

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!nombre.trim() || !precioVenta ) {
      alert("Todos los campos son obligatorios");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/productosVenta", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          name: nombre,
          precioVenta: parseFloat(precioVenta),
          categoria: { id: categoriaId }
        }),
      });
      if (!response.ok) {
        throw new Error("Error al añadir el producto");
      }
      alert("Producto añadido con éxito");
      navigate(-1);
    } catch (error) {
      console.error("Error en la solicitud:", error);
      alert("Hubo un problema al añadir el producto");
    }
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
              <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
              <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
              <li><button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Añadir Producto</h2>

        <form onSubmit={handleSubmit} className="form-container">
          <>Introduce los datos del producto:</>
          <input type="text" placeholder="Nombre del producto" value={nombre} onChange={(e) => setNombre(e.target.value)} required />
          <input type="number" placeholder="Precio de venta" value={precioVenta} onChange={(e) => setPrecioVenta(e.target.value)} required />
          {categoriaNombre && (
            <div>
              <strong>Categoría: </strong>
              <span>{categoriaNombre}</span>
            </div>
          )}
          <input type="submit" value="Añadir" className="button" />
        </form>
      </div>
    </div>
  );
}

export default PantallaAñadirProductoCarta;
