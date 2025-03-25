import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";

export function MenuIconos(){
    const navigate = useNavigate();
    const [showNotifications, setShowNotifications] = useState(false);
    const [showUserOptions, setShowUserOptions] = useState(false);

    const toggleNotifications = () => {
        setShowNotifications(!showNotifications);
    };

    const toggleUserOptions = () => {
       setShowUserOptions(!showUserOptions);
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
                  <button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button>
                </li>
              </ul>
            </div>
          )}
        </>
    )
}