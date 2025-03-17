import { useNavigate } from "react-router-dom";
import "../../css/inicioSesion-registro/styles.css";

function MoreInfoScreen() {
  const navigate = useNavigate();

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
      <div className="info-content">
        <h1 className="title">Sobre Nosotros</h1>
        <p>
          <strong>GastroStock</strong> somos.....
        </p>

        <h2>Planes y Precios</h2>
        <div className="pricing">
          <div className="plan">
            <h3>Free</h3>
            <p>0€/mes</p>
            <p>Control de stock</p>
            <p></p>
          </div>
          <div className="plan">
            <h3>Premium</h3>
            <p>25€/mes</p>
            <p>Funciones free</p>
            <p>Inteligencia Artificial</p>
          </div>
          <div className="plan">
            <h3>Pilotos</h3>
            <p>5€/mes durante dos año</p>
            <p>Tras esto, 25€/mes</p>
            <p>Funciones premium</p>
          </div>
        </div>

        <h2>Contacto</h2>
        <p>📧 Email: gastrostock@gmail.com</p>
        <p>📞 Teléfono: 678 12 34 56</p>
        <p>📍 Dirección: Av. Reina Mercedes, s/n, 41012, Sevilla</p>

        <button onClick={() => navigate("/")} className="login-btn">Volver al Inicio</button>
      </div>
    </div>
  );
}

export default MoreInfoScreen;
