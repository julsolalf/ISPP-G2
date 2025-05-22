import React, { useEffect, useState } from "react";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable"; // IMPORTACIÓN DEL PLUGIN

import { useNavigate, Link } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/listados/styles.css";
import Notificaciones from "../../components/Notifications";

function PantallaPerdidas() {
  const [productosCaducados, setProductosCaducados] = useState([]);
  const [filtro, setFiltro] = useState("");
  const [ordenAscendente, setOrdenAscendente] = useState(true);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const navigate = useNavigate();  
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);


  useEffect(() => {
    const fetchCaducados = async () => {
      try {
        const negocioId = localStorage.getItem("negocioId");
        const fechaActual = new Date(); // Obtener la fecha actual, si quieres ver lotes en esta pantalla pon una fecha posterior a 10-10-2025
        const formattedDate = fechaActual.toISOString().split('T')[0];
        const token = localStorage.getItem("token");
  
        const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/lotes/negocio/${negocioId}/fecha/${formattedDate}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
  
        if (!response.ok) {
          throw new Error("Error al obtener los lotes caducados");
        }
  
        const data = await response.json();
        setProductosCaducados(data);
      } catch (error) {
        console.error("Error al obtener productos caducados:", error);
      }
    };
  
    fetchCaducados();
  }, []);
  

  const productosFiltrados = productosCaducados
  .filter((lote) =>
    lote.producto.name.toLowerCase().includes(filtro.toLowerCase())
  )
  .sort((a, b) => {
    const nombreA = a.producto.name.toLowerCase();
    const nombreB = b.producto.name.toLowerCase();
    return ordenAscendente ? nombreA.localeCompare(nombreB) : nombreB.localeCompare(nombreA);
  });


    const exportarPDF = () => {
      const doc = new jsPDF();
      doc.text("Productos Caducados", 14, 10);
    
      const tabla = productosFiltrados.map((producto) => [
        producto.nombre,
        producto.cantidadTotal,
        producto.fechasCaducidad.map((fecha) => new Date(fecha).toLocaleDateString()).join(", "),
      ]);
    
      autoTable(doc, {
        head: [["Nombre", "Cantidad", "Fechas de caducidad"]],
        body: tabla,
      });
    
      doc.save("productos_caducados.pdf");
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

        <button onClick={() => navigate("/inicioDueno")} className="back-button">
          ⬅ Volver
        </button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>Pérdidas</h2>

        <div className="button-container3">
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
        {productosFiltrados.length > 0 ? (
  productosFiltrados.map((lote, index) => (
    <div key={index} className="empleado-card">
      <h3>{lote.producto.name}</h3>
      <p>Cantidad: {lote.cantidad}</p>
      <p>Fecha de caducidad: {new Date(lote.fechaCaducidad).toLocaleDateString()}</p>
    </div>
  ))
) : (
  <p>No se encontraron productos caducados</p>
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

export default PantallaPerdidas;
