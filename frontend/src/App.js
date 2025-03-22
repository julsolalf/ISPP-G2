
import { BrowserRouter as Router, Routes, Route, useNavigate, useLocation } from "react-router-dom";
import "./App.css";
import PantallaInicioSesion from "./screens/inicioSesion/index.js";
import PantallaRegistro from "./screens/registro/index.js";
import PantallaRecuperarContraseña from "./screens/recuperarContraseña/index.js";
import PantallaMasInfo from "./screens/masInformacion/index.js";
import PantallaInicioDueño from "./screens/inicioDueño/index.js";
import PantallaInicioEmpleado from "./screens/inicioEmpleado/index.js";
import PantallaPlanes from "./screens/planes/index.js";
import TPV from "./screens/r/index.js";
import Categorias from "./screens/productoInventario/list.js";
import Proveedores from "./screens/listaProveedores/proveedores.js";
import Empleados from "./screens/listaEmpleados/empleados.js";
function AppScreen() {
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
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <div className="buttons">
          <button className="login-btn" onClick={() => navigate("/inicioSesion")}>
            Iniciar Sesión
          </button>
          <button className="register-btn" onClick={() => navigate("/registarse")}>
            Registrarse
          </button>
        </div>
        <button className="info-btn" onClick={() => navigate("/masInformacion")}>
          Más Información
          </button>
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
        <Route path="/" element={<AppScreen />} />
        <Route path="/inicioSesion" element={<PantallaInicioSesion />} />
        <Route path="/recuperarContraseña" element={<PantallaRecuperarContraseña />} />
        <Route path="/registarse" element={<PantallaRegistro />} />
        <Route path="/masInformacion" element={<PantallaMasInfo />} />
        <Route path="/inicioDueño" element={<PantallaInicioDueño />} />
        <Route path="/inicioEmpleado" element={<PantallaInicioEmpleado />} />
        <Route path="/planes" element={<PantallaPlanes />} />
      </Routes>
    </Router>
  );
}

export default App;
