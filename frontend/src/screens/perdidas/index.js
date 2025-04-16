import React, { useEffect, useState } from "react";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable"; // IMPORTACIÓN DEL PLUGIN

import { useNavigate, Link } from "react-router-dom";
import { Bell, User } from "lucide-react";

function PantallaPerdidas() {
  const [productosCaducados, setProductosCaducados] = useState([]);
  const [filtro, setFiltro] = useState("");
  const [ordenAscendente, setOrdenAscendente] = useState(true);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const navigate = useNavigate();

  const today = new Date("08-02-2025");  // Fecha actual
  const formattedDate = today.toISOString().split('T')[0]; // Formato YYYY-MM-DD
  const token = localStorage.getItem("token");

  
  

  useEffect(() => {
    const fetchCaducados = async () => {
      try {
        const negocioId = localStorage.getItem("negocioId");
        console.log("Negocio ID:", negocioId); // Verifica que negocioId esté correcto
        const response = await fetch(`http://localhost:8080/api/lotes/fechaCaducidad/${formattedDate}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });
  
        console.log("Response:", response);
        if (!response.ok) {
          throw new Error("Error al obtener los lotes caducados");
        }
  
        const data = await response.json();
        console.log("Datos recibidos desde el backend:", data); // Verifica qué datos devuelve la API

        const formatearFecha = (fecha) => {
          const d = new Date(fecha);
          const year = d.getFullYear();
          const month = String(d.getMonth() + 1).padStart(2, "0");
          const day = String(d.getDate()).padStart(2, "0");
          return `${year}-${month}-${day}`;
        };
        
  
        // Filtrar productos por negocioId y fechaCaducidad
        const productosFiltrados = data.filter((lote) => {
          const nombre = lote.producto.name || "Desconocido";
          const cantidad = lote.cantidad || 0;
          const fechaCaducidad = formatearFecha(lote.fechaCaducidad); // <-- convierte la fecha del lote
          const restaurante = lote.reabastecimiento?.negocio?.id;
        
          const fechaActual = formatearFecha(new Date("09-02-2025")); // <-- convierte la fecha actual
        
          const fechaValida = fechaCaducidad < fechaActual; // <-- ahora es una comparación entre strings
          const negocioValido = restaurante === parseInt(negocioId);
        
          console.log("Fecha caducidad:", fechaCaducidad);
          console.log("Fecha actual:", fechaActual);
          console.log("Fecha válida:", fechaValida);
          console.log("Negocio válido:", negocioValido);
        
          return negocioValido && fechaValida;
        });
  
        console.log("Productos filtrados:", productosFiltrados); // Verifica si hay productos filtrados
  
        if (productosFiltrados.length === 0) {
          console.log("No se encontraron productos para esta fecha.");
          return;
        }
  
        // Agrupar los productos
        const agrupados = {};
        productosFiltrados.forEach((lote) => {
          const nombre = lote.producto.name || "Desconocido";
          const fechaCaducidad = lote.fechaCaducidad;
          const cantidad = lote.cantidad || 0;
  
          if (!agrupados[nombre]) {
            agrupados[nombre] = {
              nombre,
              cantidadTotal: 0,
              fechasCaducidad: [],
            };
          }
          agrupados[nombre].cantidadTotal += cantidad;
          agrupados[nombre].fechasCaducidad.push(fechaCaducidad);
        });
  
        console.log("Productos agrupados:", Object.values(agrupados)); // Verifica los productos agrupados
  
        setProductosCaducados(Object.values(agrupados));
      } catch (error) {
        console.error("Error al obtener productos caducados:", error);
      }
    };
    fetchCaducados();
  }, []);
  
  

  const productosFiltrados = productosCaducados
    .filter((producto) =>
      producto.nombre.toLowerCase().includes(filtro.toLowerCase())
    )
    .sort((a, b) => {
      const nombreA = a.nombre.toLowerCase();
      const nombreB = b.nombre.toLowerCase();
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
            productosFiltrados.map((producto,index) => (
              <div key={index} className="empleado-card">
                <h3>{producto.nombre}</h3>
                <p>Cantidad total: {producto.cantidadTotal}</p>
                <p>Fechas de caducidad:</p>
                <ul>
                  {producto.fechasCaducidad.map((fecha, i) => (
                    <li key={i}>{new Date(fecha).toLocaleDateString()}</li>
                  ))}
                </ul>
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
