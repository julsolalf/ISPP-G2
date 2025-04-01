import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Bell, User } from "lucide-react";
import "../../css/paginasBase/styles.css";

function PantallaRegistroDueno() {
  const [ownerFirstName, setOwnerFirstName] = useState("");
  const [ownerLastName, setOwnerLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [token, setToken] = useState("");
  const [loading, setLoading] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const navigate = useNavigate();
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  const handleRegister = async () => {
    if (password !== confirmPassword) {
      alert("Las contrasenas no coinciden");
      return;
    }

    const data = {
      firstName: ownerFirstName,
      lastName: ownerLastName,
      email,
      numTelefono: phone,
      tokenDueno: token,
      user: {
        username: usuario,
        password: password,
        authority: {
          id: 1,
          authority: "dueno",
        },
      },
    };

    try {
      setLoading(true);
      const response = await axios.post("http://localhost:8080/api/duenos", data);
      console.log("Registro exitoso:", response.data);
      alert("Registro del dueño exitoso. Registre ahora el negocio.");
      navigate("/registroNegocio");
    } catch (error) {
      console.error("Error en el registro:", error.response?.data || error.message);
      alert(error.response?.data?.message || "Error al registrar. Verifica los datos.");
    } finally {
      setLoading(false);
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
              <button className="close-btn" onClick={toggleNotifications}>
                X
              </button>
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
              <button className="close-btn" onClick={toggleUserOptions}>
                X
              </button>
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

        <button className="back-button" onClick={() => navigate(-1)}>
          ← Volver
        </button>

        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Registrarse</h2>

        <input type="text" placeholder="Nombre del dueno" value={ownerFirstName} onChange={(e) => setOwnerFirstName(e.target.value)} />
        <input type="text" placeholder="Apellidos del dueno" value={ownerLastName} onChange={(e) => setOwnerLastName(e.target.value)} />
        <input type="email" placeholder="Correo Electrónico" value={email} onChange={(e) => setEmail(e.target.value)} />
        <input type="tel" placeholder="Teléfono" value={phone} onChange={(e) => setPhone(e.target.value)} />
        <input type="text" placeholder="Usuario" value={usuario} onChange={(e) => setUsuario(e.target.value)} />
        <input type="password" placeholder="Contrasena" value={password} onChange={(e) => setPassword(e.target.value)} />
        <input type="password" placeholder="Confirmar Contrasena" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
        <input type="text" placeholder="Token de dueno" value={token} onChange={(e) => setToken(e.target.value)} />
        
        <button onClick={handleRegister} className="login-btn" disabled={loading}>
          {loading ? "Registrando..." : "Registrarse"}
        </button>

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

export default PantallaRegistroDueno;
