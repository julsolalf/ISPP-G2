import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";

export function MenuIconos(){
    const navigate = useNavigate();
    const [showNotifications, setShowNotifications] = useState(false);
    const [showUserOptions, setShowUserOptions] = useState(false);
    const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout

    const toggleNotifications = () => {
        setShowNotifications(!showNotifications);
    };

    const toggleUserOptions = () => {
       setShowUserOptions(!showUserOptions);
    };

    const handleLogout = () => {
      localStorage.removeItem("userToken"); // Eliminamos el token del usuario
      navigate("/inicioSesion"); // Redirigir a la pantalla de inicio de sesión
    };

    return(
        <>
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
        </>
    )
}