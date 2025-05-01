import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/listados/styles.css";  
import { Bell, User } from "lucide-react";

const token = localStorage.getItem("token");
const negocioId = 1//localStorage.getItem("negocioId");

// Función para obtener los pedidos desde la API
const obtenerPedidos = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/pedidos/dto/venta/${negocioId}`,{
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }); 
    if (!response.ok) {
      throw new Error("Error al obtener los pedidos");
    }
    const data = await response.json();
    console.log(data); // Verifica los datos que estás recibiendo
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
  const [searchTerm, setSearchTerm] = useState("");
  const [filter, setFilter] = useState("");  
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  
  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  useEffect(() => {
    const cargarDatos = async () => {
      const datosPedidos = await obtenerPedidos();
      const pedidosFiltrados = datosPedidos.filter(pedido => pedido.precioTotal > 0);
      setPedidos(pedidosFiltrados);
    };
  
    cargarDatos();
  }, []);
  

  const handleFilter = () => {
    let filteredPedidos = [...pedidos];
    if (searchTerm) {
      filteredPedidos = filteredPedidos.filter((pedido) =>
        `${pedido.empleado.firstName} ${pedido.empleado.lastName}`.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    

    if (filter === "fecha-ascendente") {
      filteredPedidos.sort((a, b) => new Date(a.fecha) - new Date(b.fecha));
    } else if (filter === "fecha-descendente") {
      filteredPedidos.sort((a, b) => new Date(b.fecha) - new Date(a.fecha));
    } else if (filter === "precio-ascendente") {
      filteredPedidos.sort((a, b) => a.precioTotal - b.precioTotal);
    } else if (filter === "precio-descendente") {
      filteredPedidos.sort((a, b) => b.precioTotal - a.precioTotal);
    }

    return filteredPedidos;
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
                <button className="user-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
              </li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate("/inicioEmpleado")} className="back-button">⬅ Volver</button>
        <Link to="/inicioEmpleado">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>          
        <h1 className="title">GastroStock</h1>
        <h2>📜 Historial de Pedidos</h2>
    
        <div className="button-container">
          <input
            type="text"
            className="search-input"
            placeholder="Buscar por empleado"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />

          <select
            className="filter-select"
            value={filter}
            onChange={(e) => setFilter(e.target.value)}
          >
            <option value="">Ordenar por</option>
            <option value="fecha-ascendente">Fecha Ascendente</option>
            <option value="fecha-descendente">Fecha Descendente</option>
            <option value="precio-ascendente">Precio Ascendente</option>
            <option value="precio-descendente">Precio Descendente</option>
          </select>
        </div>

        <div className="empleados-grid">
  {handleFilter().map((pedido) => (
    <div key={pedido.id} className="empleado-card">
      <h3>Pedido #{pedido.id}</h3>
      <p>Fecha: {new Date(pedido.fecha).toLocaleString()}</p>
      <p>Total: ${pedido.precioTotal.toFixed(2)}</p>
      <button className="ver-btn" onClick={() => navigate(`/ventasEmpleado/${pedido.id}`)}>
        Ver
      </button>
    </div>
  ))}
</div>

      </div>
    </div>
  );
}

export default VerPedidos;
