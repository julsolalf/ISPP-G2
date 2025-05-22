import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/listados/styles.css";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";
import Notificaciones from "../../components/Notifications";

function Empleados() {
  const navigate = useNavigate();
  const [empleados, setEmpleados] = useState([]);
  const [filtro, setFiltro] = useState("");
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const [ordenAscendente, setOrdenAscendente] = useState(true);
  const [showFilterOptions, setShowFilterOptions] = useState(false); // Estado para desplegar filtro
  const token = localStorage.getItem("token");
  const negocioId = localStorage.getItem("negocioId");

  const loadEmpleados = async () => {
    try {
      const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/empleados/negocio/${negocioId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Error al cargar los empleados");
      }

      const data = await response.json();
      setEmpleados(data); // Asignar empleados al estado
    } catch (error) {
      console.error("Error al cargar los empleados:", error);
    }
  };

  useEffect(() => {
    loadEmpleados();
  }, []);

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/inicioSesion");
  };

  const exportarPDF = () => {
    const doc = new jsPDF();
    doc.text("Lista de Empleados", 14, 10);
    autoTable(doc, {
      startY: 20,
      head: [["Nombre", "Apellidos", "Rol", "Descripción", "Teléfono", "Email", "Usuario"]],
      body: empleadosOrdenados.map((emp) => [
        emp.firstName,
        emp.lastName,
        emp.user?.authority?.authority || "",
        emp.descripcion,
        emp.numTelefono,
        emp.email,
        emp.user?.username,
      ]),
    });
    doc.save("empleados.pdf");
  };


  const empleadosFiltrados = empleados.filter((empleado) =>
    empleado.firstName.toLowerCase().includes(filtro.toLowerCase())
  );

  const empleadosOrdenados = [...empleadosFiltrados].sort((a, b) => {
    const nombreA = a.firstName.toLowerCase();
    const nombreB = b.firstName.toLowerCase();

    if (nombreA < nombreB) {
      return ordenAscendente ? -1 : 1;
    }
    if (nombreA > nombreB) {
      return ordenAscendente ? 1 : -1;
    }
    return 0;
  });

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
          <div className="icon-container-right">
          <Notificaciones />
          <User size={30} className="icon" onClick={toggleUserOptions} />
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

        <button onClick={() => navigate("/inicioDueno")} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>        
        <h1 className="title">GastroStock</h1>
        <h2>Empleados</h2>

        <div className="button-container3">
          <button className="button" onClick={() => navigate("/anadirEmpleado")}>➕ Añadir</button>
          <button className="button" onClick={exportarPDF}>📥 Exportar</button>

          <div className="filter-container">
            <button 
              className="filter-btn" 
              onClick={() => setShowFilterOptions(!showFilterOptions)} // Alternar visibilidad de opciones
            >
              🔍 Filtrar
            </button>

            {showFilterOptions && (
              <div className="filter-options">
                <input
                  type="text"
                  placeholder="Filtrar por nombre"
                  value={filtro}
                  onChange={(e) => setFiltro(e.target.value)} // Cambiar filtro
                  className="filter-input"
                />
                <div className="sort-options">
                  <button onClick={() => setOrdenAscendente(true)} className="sort-btn">🔼 Ascendente</button>
                  <button onClick={() => setOrdenAscendente(false)} className="sort-btn">🔽 Descendente</button>
                </div>
              </div>
            )}
          </div>
        </div>

        <div className="empleados-grid">
          {empleadosOrdenados.length > 0 ? (
            empleadosOrdenados.map((empleado) => (
              <div key={empleado.id} className="empleado-card">
                <h3>{empleado.firstName}</h3>
                <p>{empleado.user.authority.authority}</p>
                <p>{empleado.numTelefono}</p>
                <button className="ver-btn" onClick={() => {
                  localStorage.setItem("empleadoId", empleado.id);
                  navigate(`/verEmpleado/${empleado.id}`);
                }}>Ver</button>
              </div>
            ))
          ) : (
            <p>No hay empleados disponibles</p>
          )}
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

export default Empleados;
