import { BrowserRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import "./App.css";
import LoginScreen from "./screens/inicioSesion/index.js";
import RegisterScreen from "./screens/registro/index.js";

function HomeScreen() {
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
        textAlign: "center"
      }}
    >
      <div className="content">
        <img src="/gastrostockLogo.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <div className="buttons">
          <button className="login-btn" onClick={() => navigate("/login")}>
            Iniciar Sesión
          </button>
          <button className="register-btn" onClick={() => navigate("/register")}>
            Registrarse
          </button>
        </div>
        <button className="info-btn">Más Información</button>
        <p className="contact-info">
          <strong>Av. Reina Mercedes, s/n, 41012, Sevilla</strong><br />
          Contacto: 678 12 34 56
        </p>
      </div>
    </div>
  );
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomeScreen />} />
        <Route path="/login" element={<LoginScreen />} />
        <Route path="/register" element={<RegisterScreen />} />
      </Routes>
    </Router>
  );
}

export default App;
