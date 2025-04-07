import { useNavigate } from "react-router-dom";
import "../../css/paginasBase/styles.css";

function PantallaMasInfo() {
  const navigate = useNavigate();

  return (
    <div className="info-content">
      <h1 className="title">Sobre Nosotros</h1>
      <p>
        <strong>GastroStock</strong> es una plataforma innovadora de Punto de Venta (TPV) disenada específicamente para el sector hostelero. Nuestro objetivo es optimizar la gestión de negocios, permitiendo un control eficiente del inventario, reducción de pérdidas y una mejor previsión de la demanda. A través de un modelo de negocio FREEMIUM, ofrecemos una versión gratuita con funciones esenciales y una versión premium con herramientas avanzadas potenciadas por inteligencia artificial (IA), brindando así soluciones inteligentes y adaptadas a las necesidades de cada negocio.
      </p>
      <h2>Planes y Precios</h2>
      <div className="pricing">
        <div className="plan">
          <h3>Free</h3>
          <p>0€/mes</p>
          <p>Gestor del inventario</p>
          <p>Alertas personalizadas</p>
          <p>Estadísticas mínimas</p>
        </div>
        <div className="plan">
          <h3>Premium</h3>
          <p>25€/mes</p>
          <p>Todas las funciones del plan Free</p>
          <p>IA personal para gestión optimizada</p>
          <p>Análisis detallado y predictivo</p>
          <p>Predicción de la oferta y la demanda</p>
          <p>Gestor de proveedores</p>
          <p>Gestor de restock y control de pérdidas</p>
          <p>Alertas personalizadas avanzadas</p>
          <p>Gestor del inventario automatizado</p>
        </div>
        <div className="plan">
          <h3>Pilotos</h3>
          <p>5€/mes durante el primer ano</p>
          <p>Tras esto, 25€/mes</p>
          <p>Acceso a todas las funciones del plan Premium</p>
          <p>Acceso gratuito durante los primeros 2 meses</p>
        </div>
      </div>
      <h2>Contacto</h2>
      <p>📧 Email: gastrostock@gmail.com</p>
      <p>📞 Teléfono: 678 12 34 56</p>
      <p>📍 Dirección: Av. Reina Mercedes, s/n, 41012, Sevilla</p>
      <button onClick={() => navigate("/")} className="login-btn">Volver al Inicio</button>
    </div>
  );
}

export default PantallaMasInfo;
