import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";
import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";
import Notificaciones from "../../components/Notifications";

function Inventario() {
  const navigate = useNavigate();
  const [categorias, setCategorias] = useState([]);
  const [filtro, setFiltro] = useState(""); // Estado para el filtro
  const [ordenAscendente, setOrdenAscendente] = useState(true); // Estado para la ordenación
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout
  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const token = localStorage.getItem("token");
  const negocioId = localStorage.getItem("negocioId");

  const obtenerCategorias = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/categorias/negocio/${negocioId}/inventario`, {
        method: "GET",
        headers: { 
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Error al obtener las categorías");
      }

      return await response.json();
    } catch (error) {
      console.error("Error al obtener las categorías:", error);
      return [];
    }
  };

  useEffect(() => {
    
    const cargarDatos = async () => {
      const datosCategorias = await obtenerCategorias();
      setCategorias(datosCategorias);
    };
    cargarDatos();
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  // Filtrar categorías según el filtro de texto
  const categoriasFiltradas = categorias.filter((categoria) =>
    categoria.name.toLowerCase().includes(filtro.toLowerCase())
  );

  // Ordenar categorías
  const categoriasOrdenadas = categoriasFiltradas.sort((a, b) => {
    if (ordenAscendente) {
      return a.name.localeCompare(b.name);
    } else {
      return b.name.localeCompare(a.name);
    }
  });

  const exportarPDF = () => {
    const doc = new jsPDF();
    doc.text("Lista de Categorías de Inventario", 14, 10);
    
    autoTable(doc, {
      startY: 20,
      head: [["Nombre de Categoría", "ID de Categoría"]],
      body: categorias.map((categoria) => [
        categoria.name,
        categoria.id,
      ]),
    });
    
    doc.save("categorias_inventario.pdf");
  };
  
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
          <Bell size={30} className="icon" onClick={toggleNotifications} />
          <User size={30} className="icon" onClick={toggleUserOptions} />
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
              <li>
                <button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button>
              </li>
              <li>
                <button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button>
              </li>
              <li>
                <button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
              </li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate("/inicioDueno")} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>        
        <h1 className="title">GastroStock</h1>
        <h2>Inventario</h2>
        <div className="button-container3">
          <button className="button" onClick={() => navigate("/anadirCategoria")}>➕ Añadir</button>
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

        <div className="empleados-grid1">
          {categoriasOrdenadas.map((categoria) => (
            <div key={categoria.id} className="empleado-card">
              <h3>{categoria.name}</h3>
              <button 
                className="ver-btn" 
                onClick={() => {
                  localStorage.setItem("categoriaNombre", categoria.name); // Guardar en localStorage
                  navigate(`/verTipoProducto/${categoria.name}`); // Redirigir a la pantalla
                }}>
                👁️ Ver
              </button>
            </div>
          ))}
        </div>

        <div className="button-container1">
          <button className="button" onClick={() => navigate("/alertaStock")}>⚠️ Alerta Stock</button>
          <button className="button" onClick={() => navigate("/perdidas")}>📉 Pérdidas</button>
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

export default Inventario;
