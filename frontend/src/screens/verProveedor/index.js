import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";
import Notificaciones from "../../components/Notifications";

const token = localStorage.getItem("token");

const obtenerProveedor = async (idProveedor) => {
  try {
    const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/proveedores/${idProveedor}`, {
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

const obtenerDiasRepartoProveedor = async (idProveedor) => {
  try {
    const response = await fetch(`http://localhost:8080/api/diasReparto/proveedor/${idProveedor}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      throw new Error("Error al obtener los días de reparto del proveedor");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener los días de reparto:", error);
    return [];
  }
};

function VerProveedor() {
  const { id } = useParams(); 
  const navigate = useNavigate();
  const [proveedor, setProveedor] = useState(null);
  const [diasReparto, setDiasReparto] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); 
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [cargando, setCargando] = useState(true);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };


  useEffect(() => {
    const cargarDatos = async () => {
      setCargando(true); // empezamos cargando
      const dataProveedor = await obtenerProveedor(id);
      setProveedor(dataProveedor);
  
      const dias = await obtenerDiasRepartoProveedor(id);
      const diasFormateados = dias.map(d => d.diaSemana);
      setDiasReparto(diasFormateados);
      setCargando(false); // terminamos de cargar
    };
    cargarDatos();
  }, [id]);
  

  const eliminarProveedor = async () => {
    try {
      const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/proveedores/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
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

  if (cargando) {
    return <h2>Cargando proveedor...</h2>;
  }
  
  if (!proveedor) {
    return <h2>Proveedor no encontrado</h2>;
  }
  
  return (
    <div className="home-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        height: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
      }}>
      <div className="content">
        <div className="icon-container-right">
          <Bell size={30} className="icon" onClick={() => setShowNotifications(!showNotifications)} />
          <User size={30} className="icon" onClick={() => setShowUserOptions(!showUserOptions)} />
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
          <p><strong>Días de Reparto:</strong> {diasReparto.length > 0 ? diasReparto.join(", ") : "No especificados"}</p>
          
          <button style={{ background: "#157E03", color: "white" }} onClick={() => navigate(`/editarProveedor/${proveedor.id}`)}>Editar Proveedor</button>
          <button style={{ background: "#9A031E", color: "white" }} onClick={() => setShowDeleteModal(true)}>Eliminar Proveedor</button>
          <button style={{ background: "#F57C20", color: "white" }} onClick={() => navigate(`/verCarritoProveedor/${proveedor.id}`)}>Ver Carrito</button>
          <button style={{ background: "#F57C20", color: "white" }} onClick={() => navigate(`/verCarritosPendientes/${proveedor.id}`)}>Ver Pendientes</button>
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
    </div>
  );
}

export default VerProveedor;
