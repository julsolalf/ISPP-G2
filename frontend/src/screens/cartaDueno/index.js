import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const obtenerCategorias = async () => {
    try {
      /*
      Falta una lógica de que en cada pantalla esté guardada la información del usuario logueado y por tanto el respectivo negocioId
      const negocioId = localStorage.getItem("negocioId"); // Obtiene el ID del negocio guardado
      if (!negocioId) {
        throw new Error("No se encontró el ID del negocio");
      }
      const response = await fetch(`http://localhost:8080/api/categorias/negocio/${negocioId}/inventario`);*/
  
      const response = await fetch("http://localhost:8080/api/categorias/negocio/1");
      
      if (!response.ok) {
        throw new Error("Error al obtener las categorías");
      }
      
      const categorias = await response.json();
  
      // Filtrar solo las categorías que son de venta
      const categoriasVenta = categorias.filter(cat => cat.pertenece === "VENTA");
  
      return categoriasVenta;
    } catch (error) {
      console.error(error);
      return [];
    }
  };

function CartaDueno() {
  const navigate = useNavigate();
  const [categorias, setCategorias] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);


  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  useEffect(() => {
    const cargarDatos = async () => {
      const datosCategorias = await obtenerCategorias();
      setCategorias(datosCategorias);
    };
    cargarDatos();
  }, []);


  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
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
        <h2>Carta</h2>
        <div className="button-container3">
          <button className="button" onClick={() => navigate("/añadirCategoria")}>➕ Añadir</button>
          <button className="button">📥 Exportar</button>
          <button className="button">🔍 Filtrar</button>
        </div>

        <div className="empleados-grid1">
          {categorias.map((categoria) => (
            <div key={categoria.id} className="empleado-card">
            <h3>{categoria.name}</h3>
            <button 
              className="ver-btn" 
              onClick={() => {
                localStorage.setItem("categoriaNombre", categoria.name); // Guardar en localStorage
                navigate(`/verTipoProductoCartaDueno/${categoria.name}`); // Redirigir a la pantalla
              }}>
              👁️ Ver
            </button>
          </div>
          ))}
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

export default CartaDueno;
