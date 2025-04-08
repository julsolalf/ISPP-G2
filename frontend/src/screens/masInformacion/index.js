import { useNavigate } from "react-router-dom";
import "../../css/paginasBase/styles.css";

function PantallaMasInfo() {
  const navigate = useNavigate();

  return (
    <div className="page-container">
      <div className="fondo">
        <button className="back-button" style={{fontSize:"25px"}} onClick={() => navigate("/")}>← Volver</button>
          <h1 className="title">¿Qué es gastroStock?</h1>
          <p style={{marginTop:"5%", marginLeft:"10%",fontSize:"20px", width:"80%", alignItems:"center"}}>
            <strong style={{fontSize:"20px"}}>GastroStock</strong> es una plataforma innovadora de Punto de Venta (TPV) disenada específicamente para el sector hostelero. Nuestro objetivo es optimizar la gestión de negocios, permitiendo un control eficiente del inventario, reducción de pérdidas y una mejor previsión de la demanda. A través de un modelo de negocio FREEMIUM, ofrecemos una versión gratuita con funciones esenciales y una versión premium con herramientas avanzadas potenciadas por inteligencia artificial (IA), brindando así soluciones inteligentes y adaptadas a las necesidades de tu negocio.
          </p>
          <h2 style={{marginTop:"2%", fontSize:"32px"}}>Planes y Precios</h2>
          <div className="pricing">
            <div className="plan">
              <h3 style={{fontSize:"40px"}}>Free</h3>
              <ul style={{height:"80%"}}>
                <li style={{fontSize:"30px", color:"black", marginTop:"5%"}}>Gestión de inventario</li>
                <li style={{fontSize:"30px", color:"black", marginTop:"5%"}}>Gestión de ventas</li>
                <li style={{fontSize:"30px", color:"black", marginTop:"5%"}}>Notificaciones por fecha de caducidad</li>
              </ul>
              <h3 style={{height:"20%", fontSize:"40px", color:"black"}}>0€/mes</h3>
            </div>
            <div className="plan">
              <h3 style={{fontSize:"40px"}}>Premium</h3>
              <ul style={{height:"80%"}}>
                <li style={{fontSize:"30px", color:"black", marginTop:"5%"}}>Todas las funciones del plan Free</li>
                <li style={{fontSize:"30px", color:"black", marginTop:"5%"}}>Panel de estado de negocio</li>
                <li style={{fontSize:"30px", color:"black", marginTop:"5%"}}>Gestor de restock y control de pérdidas</li>
                <li style={{fontSize:"30px", color:"black", marginTop:"5%"}}>Alertas personalizadas</li>
              </ul>
              <h3 style={{height:"20%", fontSize:"40px", color:"black"}}>25€/mes</h3>
            </div>
            <div className="plan">
              <h3 style={{fontSize:"40px"}}>Pilotos</h3>
              <ul style={{height:"80%"}}>
                <li style={{fontSize:"30px", color:"black", marginTop:"10%"}}>Acceso a todas las funciones del plan Premium</li>
                <li style={{fontSize:"30px", color:"black", marginTop:"10%"}}>Acceso gratuito durante los primeros 2 meses</li>
              </ul>
              <h3 style={{height:"20%", fontSize:"35px", color:"black"}}>5€/mes durante el primer año</h3>
            </div>
          </div>
          <button onClick={() => navigate("/")} className="login-btn">Volver al Inicio</button>
      </div>
    </div>
  );
}

export default PantallaMasInfo;
