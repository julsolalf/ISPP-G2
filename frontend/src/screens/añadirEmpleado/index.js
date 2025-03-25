import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/paginasBase/styles.css";
import { Bell, User } from "lucide-react"; 

function AñadirEmpleado() {
  const [nombre, setNombre] = useState("");
  const [apellidos, setApellidos] = useState("");
  const [genero, setGenero] = useState("");
  const [correo, setCorreo] = useState("");
  const [telefono, setTelefono] = useState("");
  const [codigoEmpleado, setCodigoEmpleado] = useState("");
  const [rol, setRol] = useState("");
  const [turno, setTurno] = useState("");
  const [posicion, setPosicion] = useState("");
const [showNotifications, setShowNotifications] = useState(false);
const [showUserOptions, setShowUserOptions] = useState(false);

const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };
  
  const navigate = useNavigate();

  const handleRegister = () => {
    console.log("Añadiendo empleado con:", {
      nombre,
      apellidos,
      genero,
      correo,
      telefono,
      codigoEmpleado,
      rol,
      turno,
      posicion
    });

    navigate("/empleados");
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
                    <button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button>
                </li>
                </ul>
            </div>
            )}

        <button className="back-button" onClick={() => navigate(-1)}>
          ← Volver
        </button>

        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Añadir Empleado</h2>

        <input
          type="text"
          placeholder="Nombre"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
        />
        <input
          type="text"
          placeholder="Apellidos"
          value={apellidos}
          onChange={(e) => setApellidos(e.target.value)}
        />
        <input
          type="text"
          placeholder="Género"
          value={genero}
          onChange={(e) => setGenero(e.target.value)}
        />
        <input
          type="email"
          placeholder="Correo Electrónico"
          value={correo}
          onChange={(e) => setCorreo(e.target.value)}
        />
        <input
          type="tel"
          placeholder="Teléfono"
          value={telefono}
          onChange={(e) => setTelefono(e.target.value)}
        />
        <input
          type="text"
          placeholder="Código Empleado"
          value={codigoEmpleado}
          onChange={(e) => setCodigoEmpleado(e.target.value)}
        />
        <input
          type="text"
          placeholder="Rol"
          value={rol}
          onChange={(e) => setRol(e.target.value)}
        />
        <input
          type="text"
          placeholder="Turno"
          value={turno}
          onChange={(e) => setTurno(e.target.value)}
        />
        <input
          type="text"
          placeholder="Posición"
          value={posicion}
          onChange={(e) => setPosicion(e.target.value)}
        />

        <button onClick={handleRegister} className="login-btn">Añadir Empleado</button>
      </div>
    </div>
  );
}

export default AñadirEmpleado;
