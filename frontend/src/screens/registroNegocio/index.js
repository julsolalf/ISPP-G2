import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/paginasBase/styles.css";

function PantallaRegistroNegocio() {
  const [name, setName] = useState("");
  const [tokenNegocio, setTokenNegocio] = useState("");
  const [direccion, setDireccion] = useState("");
  const [codigoPostal, setCodigoPostal] = useState("");
  const [ciudad, setCiudad] = useState("");
  const [pais, setPais] = useState("");
  const token = localStorage.getItem("token");
  const duenoId = localStorage.getItem("duenoId");
  const [loading, setLoading] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout
  const navigate = useNavigate();

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  const handleRegister = async () => {

    const data = {
      name,
      tokenNegocio,
      direccion,
      codigoPostal,
      ciudad,
      pais,
      idDueno: duenoId
    };

    try {
      setLoading(true);
      const response = await fetch("https://ispp-2425-g2.ew.r.appspot.com/api/negocios", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data)
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Error al registrar. Verifica los datos.");
      }
  
      const result = await response.json();
      console.log("Registro exitoso:", result);
      alert("Registro del negocio exitoso.");
      navigate("/elegirNegocio");
    } catch (error) {
      console.error("Error en el registro:", error.message);
      alert(error.message);
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

        <button className="back-button" onClick={() => navigate(-1)}>← Volver</button>

        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Registrar negocio</h2>

        <input type="text" placeholder="Nombre del negocio" value={name} onChange={(e) => setName(e.target.value)} />
        <input type="text" placeholder="Token de negocio" value={tokenNegocio} onChange={(e) => setTokenNegocio(e.target.value)} />
        <input type="text" placeholder="Dirección" value={direccion} onChange={(e) => setDireccion(e.target.value)} />
        <input type="text" placeholder="Código Postal" value={codigoPostal} onChange={(e) => setCodigoPostal(e.target.value)} />
        <input type="text" placeholder="Ciudad" value={ciudad} onChange={(e) => setCiudad(e.target.value)} />
        <input type="text" placeholder="País" value={pais} onChange={(e) => setPais(e.target.value)} />

        <button onClick={handleRegister} className="login-btn" disabled={loading}>{loading ? "Registrando..." : "Registrar negocio"}</button>
        <button  className="login-btn" onClick={() => navigate("/elegirNegocio")}>Registrar negocio más tarde</button>

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

export default PantallaRegistroNegocio;
