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
  const [loading, setLoading] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const navigate = useNavigate();

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleRegister = async () => {
    if (password !== confirmPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }
  
    const data = {
      username: usuario,
      password: password,
      firstName: ownerFirstName,
      lastName: ownerLastName,
      email: email,
      numTelefono: phone,
    };
  
    try {
      setLoading(true);
      
      const registerResponse = await axios.post("http://localhost:8080/api/auth/register", data);
      console.log("Registro exitoso:", registerResponse.data);
  
      const loginResponse = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: usuario,
          password: password,
        }),
      });
  
      const loginData = await loginResponse.json();
      const token = loginData.token;
      localStorage.setItem("token", token);
  
      const userResponse = await fetch("http://localhost:8080/api/users/me", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const user = await userResponse.json();
      localStorage.setItem("user", JSON.stringify(user));
  
      const duenoResponse = await fetch(`http://localhost:8080/api/duenos/user/${user.id}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const dueno = await duenoResponse.json();
      localStorage.setItem("duenoId", dueno.id);
      navigate("/elegirNegocio");
    } catch (error) {
      console.error("Error en el registro o login:", error.response?.data || error.message);
      alert(error.response?.data?.message || "Error al registrar/iniciar sesión.");
    } finally {
      setLoading(false);
    }
  };
  
  
  return (
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
              <button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button>
            </li>
          </ul>
        </div>
      )}

      <button className="back-button" onClick={() => navigate("/")}>
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
      
      <button onClick={handleRegister} className="login-btn" disabled={loading}>
        {loading ? "Registrando..." : "Registrarse"}
      </button>
    </div>
  );
}

export default PantallaRegistroDueno;
