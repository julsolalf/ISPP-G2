import { useEffect, useState } from "react"
import { User, Phone, Bell, LogOut, ChevronRight, X, Menu, CreditCard } from "lucide-react"
import { useNavigate } from "react-router-dom"
import "../../css/perfil/styles.css";

function PantallaPerfil() {
  const [user, setUser] = useState(null)
  const [showNotifications, setShowNotifications] = useState(false)
  const [showUserOptions, setShowUserOptions] = useState(false)
  const [showLogoutModal, setShowLogoutModal] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    const token = localStorage.getItem("token")

    // Fetch user profile
    const fetchUserData = async () => {
      try {
        const response = await fetch("https://ispp-2425-g2.ew.r.appspot.com/api/users/me", {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        })

        if (response.ok) {
          const userData = await response.json()
          setUser(userData)
        } else {
          console.error("Error al obtener el usuario: ", response.status)
        }
      } catch (error) {
        console.error("Error al obtener datos del usuario:", error)
      }
    }

    fetchUserData()
  }, [])

  // Toggle Notifications
  const toggleNotifications = () => {
    setShowNotifications(!showNotifications)
    if (showUserOptions) setShowUserOptions(false)
  }

  // Toggle User Options (perfil, logout)
  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions)
    if (showNotifications) setShowNotifications(false)
  }

  // Handle Logout
  const handleLogout = () => {
    localStorage.clear()
    navigate("/") // Redirigir a la pantalla de inicio de sesión
  }

  // Close all popups when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (showNotifications || showUserOptions) {
        const popups = document.querySelectorAll(".popup-menu")
        let clickedInside = false

        popups.forEach((popup) => {
          if (popup.contains(event.target)) {
            clickedInside = true
          }
        })

        const icons = document.querySelectorAll(".menu-trigger")
        icons.forEach((icon) => {
          if (icon.contains(event.target)) {
            clickedInside = true
          }
        })

        if (!clickedInside) {
          setShowNotifications(false)
          setShowUserOptions(false)
        }
      }
    }

    document.addEventListener("mousedown", handleClickOutside)
    return () => {
      document.removeEventListener("mousedown", handleClickOutside)
    }
  }, [showNotifications, showUserOptions])

  return (
    <div className="profile-container">
      <div className="profile-card">
        <div className="profile-header">
          <div className="profile-picture-container">
            <img
              src="/default_profile.png"
              alt="Profile"
              className="profile-picture"
              onError={(e) => {
                e.target.onerror = null
                e.target.src = "https://via.placeholder.com/200x200?text=Usuario"
              }}
            />
          </div>
          <h1 className="profile-name">
            {user?.first_name || "Nombre"} {user?.last_name || "Apellido"}
          </h1>
          <p className="profile-username">@{user?.username}</p>
        </div>

        <div className="profile-details">
          <div className="detail-item">
            <div className="detail-icon">
              <User size={20} />
            </div>
            <div className="detail-content">
              <span className="detail-label">Username</span>
              <span className="detail-value">{user?.username}</span>
            </div>
          </div>

          <div className="detail-item">
            <div className="detail-icon">
              <Phone size={20} />
            </div>
            <div className="detail-content">
              <span className="detail-label">Teléfono</span>
              <span className="detail-value">{user?.num_telefono || "No disponible"}</span>
            </div>
          </div>
        </div>

        <div className="profile-actions">
          <button className="action-button" onClick={() => navigate("/planes")}>
            <CreditCard size={18} />
            <span>Ver planes</span>
          </button>
          <button className="action-button logout" onClick={() => setShowLogoutModal(true)}>
            <LogOut size={18} />
            <span>Cerrar Sesión</span>
          </button>
        </div>

        <div className="profile-icons">
          <Bell
            size={24}
            className={`icon menu-trigger ${showNotifications ? "active" : ""}`}
            onClick={toggleNotifications}
          />
          <Menu
            size={24}
            className={`icon menu-trigger ${showUserOptions ? "active" : ""}`}
            onClick={toggleUserOptions}
          />
        </div>

        {showNotifications && (
          <div className="popup-menu notification-menu">
            <div className="popup-header">
              <strong>Notificaciones</strong>
              <button className="close-btn" onClick={toggleNotifications}>
                <X size={18} />
              </button>
            </div>
            {[1, 2, 3].length > 0 ? (
              <ul className="notification-list">
                <li className="notification-item">
                  <div className="notification-content">
                    <p className="notification-text">Se ha actualizado tu plan de suscripción</p>
                    <span className="notification-time">Hace 2 horas</span>
                  </div>
                </li>
                <li className="notification-item">
                  <div className="notification-content">
                    <p className="notification-text">Nuevo mensaje del sistema</p>
                    <span className="notification-time">Ayer</span>
                  </div>
                </li>
                <li className="notification-item">
                  <div className="notification-content">
                    <p className="notification-text">Bienvenido a la plataforma</p>
                    <span className="notification-time">Hace 3 días</span>
                  </div>
                </li>
              </ul>
            ) : (
              <div className="empty-state">
                <p>No tienes notificaciones</p>
              </div>
            )}
          </div>
        )}

        {showUserOptions && (
          <div className="popup-menu user-menu">
            <div className="popup-header">
              <strong>Menú</strong>
              <button className="close-btn" onClick={toggleUserOptions}>
                <X size={18} />
              </button>
            </div>
            <ul className="menu-list">
              <li className="menu-item" onClick={() => navigate("/planes")}>
                <CreditCard size={18} />
                <span>Ver planes</span>
                <ChevronRight size={16} className="menu-arrow" />
              </li>
              <li className="menu-item" onClick={() => navigate("/perfil")}>
                <User size={18} />
                <span>Ver perfil</span>
                <ChevronRight size={16} className="menu-arrow" />
              </li>
              <li className="menu-item logout" onClick={() => setShowLogoutModal(true)}>
                <LogOut size={18} />
                <span>Cerrar Sesión</span>
                <ChevronRight size={16} className="menu-arrow" />
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
                <button className="cancel-btn" onClick={() => setShowLogoutModal(false)}>
                  No
                </button>
                <button className="confirm-btn" onClick={handleLogout}>
                  Sí
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

export default PantallaPerfil