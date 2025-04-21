import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/listados/styles.css";

function AlertaStock() {
  const navigate = useNavigate();
  const [productos, setProductos] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const [searchTerm, setSearchTerm] = useState("");
  const token = localStorage.getItem("token");
  const storedNegocioId = localStorage.getItem("negocioId");

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  const cargarDatos = async () => {
    try {
      const [productosRes, lotesRes] = await Promise.all([
        fetch("http://localhost:8080/api/productosInventario", {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }),
        fetch("http://localhost:8080/api/lotes", {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }),
      ]);

      if (!productosRes.ok || !lotesRes.ok) {
        throw new Error("Error al cargar datos");
      }

      const productosData = await productosRes.json();
      const lotesData = await lotesRes.json();

      // Calcular cantidad por producto
      const cantidadPorProducto = {};
      lotesData.forEach((lote) => {
        const productoId = lote.producto?.id;
        if (productoId != null) {
          cantidadPorProducto[productoId] = (cantidadPorProducto[productoId] || 0) + lote.cantidad;
        }
      });

      // Filtrar productos del negocio actual con cantidades por debajo de los umbrales
      const productosFiltrados = productosData
        .filter(p => p.categoria?.negocio?.id == storedNegocioId) // Filtrar por negocio
        .map((producto) => {
          const cantidadTotal = cantidadPorProducto[producto.id] || 0;
          return {
            ...producto,
            cantidad: cantidadTotal,
          };
        })
        .filter((producto) =>
          producto.cantidad < producto.cantidadAviso ||
          producto.cantidad < producto.cantidadDeseada ||
          producto.cantidad < producto.cantidadReserva
        );

      setProductos(productosFiltrados);
    } catch (error) {
      console.error("Error al cargar datos:", error);
    }
  };

  useEffect(() => {
    cargarDatos(); // Cargar al iniciar

    const intervalo = setInterval(() => {
      cargarDatos(); // Refrescar cada 15 minutos
    }, 15 * 60 * 1000); // 15 minutos

    return () => clearInterval(intervalo); // Limpiar intervalo al desmontar componente
  }, []);

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
              <li>Stock bajo detectado</li>
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
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>        
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
          <button className="ver-btn">➕ Añadir todo al 🛒</button>
        </div>
        
        <div className="empleados-grid1">
          {productos
            .filter((producto) =>
              producto.name.toLowerCase().includes(searchTerm.toLowerCase())
            )
            .map((producto, index) => (
              <div key={index} className="empleado-card">
                <h3>{producto.name}</h3>
                <p>Cantidad: {producto.cantidad}</p>
                <p>Cantidad Alerta: {producto.cantidadAviso}</p>
                <button className="ver-btn">Añadir al 🛒</button>
              </div>
          ))}
        </div>

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
