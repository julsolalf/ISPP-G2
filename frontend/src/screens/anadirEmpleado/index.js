import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/paginasBase/styles.css";
import { Bell, User } from "lucide-react"; 

function AnadirEmpleado() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [numTelefono, setNumTelefono] = useState("");
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout
  const [descripcion, setDescripcion] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [tokenEmpleado, setTokenEmpleado] = useState("");
  const navigate = useNavigate();

  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const token = localStorage.getItem("token");

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  const negocioId = localStorage.getItem("negocioId");

  const handleRegister = async () => {
    const empleadoData = {
      username,
      password,
      firstName,
      lastName,
      email,
      numTelefono,
      tokenEmpleado,
      descripcion,
      negocio: negocioId
    };
  
    try {
      const response = await fetch("http://localhost:8080/api/empleados", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(empleadoData),
      });
      if (response.status === 201) {
        alert("Empleado añadido con éxito");
        navigate("/empleados");
      }
    } catch (error) {
      console.error("Error al añadir empleado:", error);
      alert("Hubo un problema al añadir el empleado");
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
                <button className="close-btn" onClick={toggleUserOptions}>X</button>
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

        <button className="back-button" onClick={() => navigate("/empleados")}>
          ← Volver
        </button>

        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>        
        <h1 className="title">GastroStock</h1>
        <h2>Anadir Empleado</h2>

        <input
          type="text"
          placeholder="Nombre"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <input
          type="text"
          placeholder="Apellidos"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
        <input
          type="email"
          placeholder="Correo Electrónico"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="tel"
          placeholder="Número de Teléfono"
          value={numTelefono}
          onChange={(e) => setNumTelefono(e.target.value)}
        />
        <input
          type="text"
          placeholder="Nombre de Usuario"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="Contrasena"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="text"
          placeholder="Token Empleado"
          value={tokenEmpleado}
          onChange={(e) => setTokenEmpleado(e.target.value)}
        />
        <input
          type="text"
          placeholder="Descripción del puesto"
          value={descripcion}
          onChange={(e) => setDescripcion(e.target.value)}
        />

        <button onClick={handleRegister} className="login-btn">Anadir Empleado</button>
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

export default AnadirEmpleado;
