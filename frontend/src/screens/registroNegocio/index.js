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
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
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
      const response = await fetch("http://localhost:8080/api/negocios", {
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
  <div className="page-container">
    <div className="fondo">
      <div>
        <button onClick={() => navigate("/")} className="back-button" style={{marginLeft:"15%", fontSize:"28px"}}>⬅ Volver</button>
        <Bell size={40} style={{marginLeft:"80%",marginTop:"1%"}}className="icon" onClick={toggleNotifications} />
        <User size={40} style={{marginLeft:"3%"}} className="icon" onClick={toggleUserOptions} />
      </div>
      {showNotifications && (
      <div className="notification-bubble" style={{marginRight:"17.5%",marginTop:"5%"}}>
        <div className="notification-header">
          <strong>Notificaciones</strong>
          <button className="close-btn" onClick={toggleNotifications}>X</button>
        </div>
        <ul>
          <li>Notificación 1</li>
          <li>Notificación 2</li>
          <li>Notificación 3</li>
        </ul>
      </div>)}
      {showUserOptions && (
        <div className="notification-bubble user-options" style={{marginRight:"13.5%",marginTop:"5%"}}>
          <div className="notification-header">
            <strong>Usuario</strong>
            <button className="close-btn" onClick={toggleUserOptions}>X</button>
          </div>
          <ul>
            <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
            <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
            <button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
          </ul>
        </div>)}
      <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
      <div className="clearfix"/>
      <h1 className="title" style={{marginTop:"5%"}}>GastroStock</h1>
      <div className="clearfix"/>
      <h2>Registrar negocio</h2>
      <div style={{textAlign :"left", width:"80%", marginLeft:"10%"}}>
        <strong style={{fontSize:"22px"}}>Nombre del negocio:</strong>
        <div className="clearfix"/>
        <input type="text" placeholder="Nombre del negocio" value={name} onChange={(e) => setName(e.target.value)} />
        <div className="clearfix" style={{marginTop:"1%"}}/>
        <strong style={{fontSize:"22px"}}>Token de negocio:</strong>
        <div className="clearfix"/>
        <input type="text" placeholder="Token de negocio" value={tokenNegocio} onChange={(e) => setTokenNegocio(e.target.value)} />
        <div className="clearfix" style={{marginTop:"1%"}}/>
        <strong style={{fontSize:"22px"}}>País:</strong>
        <strong style={{fontSize:"22px", marginLeft:"33%"}}>Ciudad:</strong>
        <strong style={{fontSize:"22px", marginLeft:"38%"}}>Código Postal:</strong>
        <div className="clearfix"/>
        <input type="text" style={{width:"33%"}} placeholder="País" value={pais} onChange={(e) => setPais(e.target.value)} />
        <input type="text" style={{width:"40%", marginLeft:"3%"}} placeholder="Ciudad" value={ciudad} onChange={(e) => setCiudad(e.target.value)} />
        <input type="text" style={{width:"15%", marginLeft:"3%"}} placeholder="Código Postal" value={codigoPostal} onChange={(e) => setCodigoPostal(e.target.value)} />
        <div className="clearfix" style={{marginTop:"1%"}}/>
        <strong style={{fontSize:"22px"}}>Dirección:</strong>
        <div className="clearfix"/>
        <input type="text" placeholder="Dirección" value={direccion} onChange={(e) => setDireccion(e.target.value)} />
      </div>
      <div className="clearfix" style={{marginTop:"1%"}}/>
      <button onClick={handleRegister} className="login-btn" disabled={loading} style={{fontSize:"26px"}}>{loading ? "Registrando..." : "Registrar negocio"}</button>
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
