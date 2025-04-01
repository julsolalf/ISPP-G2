import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import axios from "axios";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import "../../css/listados/styles.css";

function Proveedores() {
  const navigate = useNavigate();
  //  const { negocioId } = useAuth(); 
  //TODO: Cambiar por el negocioId del usuario logueado
  const negocioId = 1; // Simulación de negocio ID
  const [proveedores, setProveedores] = useState([]);
  const [filtro, setFiltro] = useState(""); 
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const [ordenAscendente, setOrdenAscendente] = useState(true); 

  useEffect(() => {
    if (!negocioId) {
      console.error("No se ha seleccionado un negocio.");
      return;
    }

    const fetchProveedores = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/proveedores/negocio/${negocioId}`);
        setProveedores(response.data);
      } catch (error) {
        console.error("Error al obtener los proveedores:", error);
      }
    };

    fetchProveedores();
  }, [negocioId]);

  const proveedoresFiltrados = proveedores.filter((prov) =>
    prov.name.toLowerCase().includes(filtro.toLowerCase())
  );

  const proveedoresOrdenados = [...proveedoresFiltrados].sort((a, b) => {
    const nombreA = a.name.toLowerCase();
    const nombreB = b.name.toLowerCase();

    if (nombreA < nombreB) {
      return ordenAscendente ? -1 : 1;
    }
    if (nombreA > nombreB) {
      return ordenAscendente ? 1 : -1;
    }
    return 0;
  });

  const exportarPDF = () => {
    const doc = new jsPDF();
    
    doc.text("Lista de Proveedores", 14, 10);
    
    autoTable(doc, {
      startY: 20,
      head: [["Nombre", "Dirección", "Teléfono", "Email"]],
      body: proveedores.map((prov) => [prov.name, prov.direccion, prov.telefono, prov.email]),
    });

    doc.save("proveedores.pdf");
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
              <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
              <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
              <li><button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">
          ⬅ Volver
        </button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Proveedores</h2>

        <div className="button-container3">
          <button className="button" onClick={() => navigate("/anadirProveedor")}>➕ Añadir</button>
          <button className="button" onClick={exportarPDF}>📥 Exportar</button>
          
          <div className="filter-container">
            <button className="filter-btn">🔍 Filtrar</button>
            <div className="filter-options">
              <input
                type="text"
                placeholder="Filtrar por nombre"
                value={filtro}
                onChange={(e) => setFiltro(e.target.value)}
                className="filter-input"
              />
              <div className="sort-options">
                <button onClick={() => setOrdenAscendente(true)} className="sort-btn">🔼 Ascendente</button>
                <button onClick={() => setOrdenAscendente(false)} className="sort-btn">🔽 Descendente</button>
              </div>
            </div>
          </div>
        </div>

        <div className="empleados-grid">
          {proveedoresOrdenados.length > 0 ? (
            proveedoresOrdenados.map((proveedor) => (
              <div key={proveedor.id} className="empleado-card">
                <h3>{proveedor.name}</h3>
                <p>{proveedor.direccion}</p>
                <p>{proveedor.telefono}</p>
                <button className="ver-btn" onClick={() => navigate(`/verProveedor/${proveedor.id}`)}>Ver</button>
              </div>
            ))
          ) : (
            <p>No se encontraron proveedores</p>
          )}
        </div>

        {showLogoutModal && (
          <div className="modal-overlay">
            <div className="modal">
              <h3>¿Está seguro que desea abandonar la sesión?</h3>
              <div className="modal-buttons">
                <button className="confirm-btn" onClick={() => navigate("/")}>Sí</button>
                <button className="cancel-btn" onClick={() => setShowLogoutModal(false)}>No</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Proveedores;
