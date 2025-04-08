import "../../css/home/styles.css";
import { useNavigate } from "react-router-dom";

export default function Home(){
    const navigate = useNavigate();

    return(
      <div className="page-container">
        <div className="content-img">
          <img src="/Mascota-GastroStock.png" alt="App Logo" className="app-logo" />
        </div>
        <div className="clearfix"></div>
        <div className="content-container">
          <div className="content">
            <h1 className="title" style={{fontSize:"40px"}}>¡Bienvenido de nuevo!</h1>
            <button className="login-btn" onClick={() => navigate("/inicioSesion")}>
              Iniciar Sesión
            </button>
          </div>
          <div className="clearfix"></div>
          <div className="content-bottom">
            <strong className="title" style={{fontSize:"38px"}}>¿Eres nuevo aquí?</strong>
            <div className="clearfix"></div>
            <button className="info-btn" onClick={() => navigate("/masInformacion")}>
              Conoce nuestra App
              </button>
              <div className="clearfix"></div>
              <button className="register-btn" onClick={() => navigate("/registroDueno")}>
                Registrarse
              </button>
          </div>
          <div className="clearfix"></div>
          <p className="contact-info">
            <strong>Av. Reina Mercedes, s/n, 41012, Sevilla</strong><br />
            Contacto: 678 12 34 56
          </p>
        </div>
      </div>
    );
}