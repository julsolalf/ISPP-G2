import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

function PantallaSubscriptionSuccess() {
  const location = useLocation();
  const navigate = useNavigate();
  const [message, setMessage] = useState("Procesando pago...");

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const sessionId = params.get("session_id");

    async function verifySession() {
      
        if (sessionId) {
          const token = localStorage.getItem("token");
          const response = await fetch(`http://localhost:8080/api/subscriptions/status`, {
            headers: { Authorization: `Bearer ${token}` },
          });
          const result = await response.json();
          
          // Check if the planType is PREMIUM and status is ACTIVE
          if (result.planType === "PREMIUM" && result.status === "ACTIVE") {
            setMessage("¡Pago realizado exitosamente! Gracias por actualizar tu suscripción.");
          } else {
            setMessage("Hubo un problema al verificar el pago.");
          }
        } else {
          setMessage("No se encontró el session_id.");
        }
      }
    verifySession();
  }, [location]);

  const handleRedirect = () => {
    navigate("/dashboard"); // O la ruta que desees para continuar
  };

  return (
    <div style={{ padding: "20px", textAlign: "center" }}>
      <h1>{message}</h1>
      <button onClick={handleRedirect}>Ir a mi Dashboard</button>
    </div>
  );
}

export default PantallaSubscriptionSuccess;