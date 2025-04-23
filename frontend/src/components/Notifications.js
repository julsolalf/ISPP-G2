import { useState, useEffect } from "react";
import { Bell } from "lucide-react";
import "../css/inicio/styles.css";

function Notificaciones() {
  const [showNotifications, setShowNotifications] = useState(false);
  const [proveedoresProximos, setProveedoresProximos] = useState([]);
  const [showReabastecimientoModal, setShowReabastecimientoModal] = useState(false);

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  useEffect(() => {
    const fetchReabastecimientos = async () => {
        try {
          const token = localStorage.getItem("token");
          const negocioId = localStorage.getItem("negocioId");
      
          const response = await fetch(`http://localhost:8080/api/reabastecimientos/negocio/${negocioId}`, {
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
    <>
      <Bell size={30} className="icon" onClick={toggleNotifications} />

      {showNotifications && (
        <div className="notification-bubble">
          <div className="notification-header">
            <strong>Notificaciones</strong>
            <button className="close-btn" onClick={toggleNotifications}>X</button>
          </div>
          <ul>
            {proveedoresProximos.length > 0 ? (
              proveedoresProximos.map((proveedor, index) => (
                <li key={index}>Reabastecimiento próximo: {proveedor}</li>
              ))
            ) : (
              <>
                <li>Notificación 1</li>
                <li>Notificación 2</li>
                <li>Notificación 3</li>
              </>
            )}
          </ul>
        </div>
      )}

        
    </>
  );
}

export default Notificaciones;
