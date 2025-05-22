import { useState } from "react";
import { useEffect } from "react";
import { useNavigate, Link  } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/inicio/styles.css";
import Notificaciones from "../../components/Notifications";

function HomeScreen() {
  const navigate = useNavigate();
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout
  const [showReabastecimientoModal, setShowReabastecimientoModal] = useState(false);
  const [proveedoresProximos, setProveedoresProximos] = useState([]);

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

  useEffect(() => {
    const fetchReabastecimientos = async () => {
        try {
          const token = localStorage.getItem("token");
          const negocioId = localStorage.getItem("negocioId");
      
          const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/reabastecimientos/negocio/${negocioId}`, {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
          });
      
          if (response.ok) {
            const data = await response.json();
      
            const hoy = new Date(); // Para uno de ejemplo poner "2025-05-30"
            const tresDiasDespues = new Date(hoy);
            tresDiasDespues.setDate(hoy.getDate() + 7);
      
            const reabastecimientosProximos = data.filter((r) => {
              const fecha = new Date(r.fecha);
              return fecha >= hoy && fecha <= tresDiasDespues;
            });
      
            if (reabastecimientosProximos.length > 0) {
              setShowReabastecimientoModal(true);
              const proveedores = [...new Set(reabastecimientosProximos.map((r) => r.proveedor.name))];
              setProveedoresProximos(proveedores);
              localStorage.setItem('reabastecimientos', JSON.stringify(proveedores));
            } else {
              // Limpiar notificaciones si ya no hay reabastecimientos próximos
              localStorage.removeItem('reabastecimientos');
              setProveedoresProximos([]);
            }
          }
        } catch (error) {
          console.error("Error al obtener reabastecimientos:", error);
        }
      };    
    
      fetchReabastecimientos();


    const storedReabastecimientos = localStorage.getItem("reabastecimientos");
    if (storedReabastecimientos) {
      setProveedoresProximos(JSON.parse(storedReabastecimientos));
    } else {
      setProveedoresProximos([]);
    }
  }, []);
    
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

        <button onClick={() => navigate("/elegirNegocio")} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>        
        <h1 className="title">GastroStock</h1>
        <h2>Inicio</h2>

        <div className="button-container1">
            <button className="menu-btn" onClick={() => navigate("/empleados")}><span role="img" aria-label="empleados">👨‍💼</span> Empleados</button>
            <button className="menu-btn" onClick={() => navigate("/dashboard")}><span role="img" aria-label="dashboard">📊</span> Dashboard</button>
            <button className="menu-btn" onClick={() => navigate("/inventario")}><span role="img" aria-label="inventario">📦</span> Inventario</button>
            <button className="menu-btn" onClick={() => navigate("/ventas")}><span role="img" aria-label="ventas">💰</span> Ventas</button>
            <button className="menu-btn" onClick={() => navigate("/cartaDueno")}><span role="img" aria-label="carta">🍽️</span> Carta</button>
            <button className="menu-btn" onClick={() => navigate("/proveedores")}><span role="img" aria-label="proveedores">📋</span> Proveedores</button>
        </div>
        
        {showReabastecimientoModal && (
            <div className="modal-overlay">
              <div className="modal">
                <h3>¡Atención!</h3>
                <p>Pronto recibirás un reabastecimiento de los siguientes proveedores:</p>
                <ul>
                  {proveedoresProximos.map((nombre, index) => (
                    <li key={index}>{nombre}</li>
                  ))}
                </ul>
                <p>Añade los productos al carrito</p>
                <button className="confirm-btn" onClick={() => setShowReabastecimientoModal(false)}>Cerrar</button>
              </div>
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

      </div>
    </div>
  );
}

export default HomeScreen;
