import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/paginasBase/styles.css";
import { Bell, User } from "lucide-react"; 
import Notificaciones from "../../components/Notifications";

function AnadirProveedor() {
  const [name, setNombre] = useState("");
  const [email, setEmail] = useState("");
  const [telefono, setTelefono] = useState("");
  const [direccion, setDireccion] = useState(""); 
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); 
  const token = localStorage.getItem("token");
  const negocio_id = localStorage.getItem("negocioId"); 
  

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  const navigate = useNavigate();

  const handleRegister = async () => {
    try {
      if (!negocio_id) {
        alert("No se ha seleccionado un negocio.");
        return;
      }
  
      const proveedorData = {
        name,
        email,
        telefono,
        direccion,
        negocioId: negocio_id
      };
  
      const response = await fetch("http://localhost:8080/api/proveedores", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(proveedorData), // Convertimos los datos a JSON
      });
  
      if (response.status === 201) {
        alert("Proveedor añadido con éxito");
        navigate("/proveedores"); // Redirige a la página de proveedores si la creación es exitosa
      } else {
        console.error("Error al añadir el proveedor:", response.statusText);
      }
    } catch (error) {
      console.error("Error al añadir el proveedor:", error.message);
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
        padding: "20px",
        overflowY: "auto"
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
              <li><button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button className="back-button" onClick={() => navigate("/proveedores")}>
          ← Volver
        </button>

        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>          
        <h1 className="title">GastroStock</h1>
        <h2>Anadir Proveedor</h2>

        <input
          type="text"
          placeholder="Nombre"
          value={name}
          onChange={(e) => setNombre(e.target.value)}
        />
        <input
          type="email"
          placeholder="Correo Electrónico"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="tel"
          placeholder="Teléfono"
          value={telefono}
          onChange={(e) => setTelefono(e.target.value)}
        />
        <input
          type="text"
          placeholder="Dirección"
          value={direccion}
          onChange={(e) => setDireccion(e.target.value)}
        />

        <button onClick={handleRegister} className="login-btn">Anadir Proveedor</button>
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

export default AnadirProveedor;
