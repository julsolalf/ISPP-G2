
import { BrowserRouter as Router, Routes, Route, useNavigate, useLocation } from "react-router-dom";
import "./App.css";
import PantallaInicioSesion from "./screens/inicioSesion/index.js";
import PantallaRegistroDueño from "./screens/registroDueño/index.js";
import PantallaRecuperarContraseña from "./screens/recuperarContraseña/index.js";
import PantallaMasInfo from "./screens/masInformacion/index.js";
import PantallaInicioDueño from "./screens/inicioDueño/index.js";
import PantallaInicioEmpleado from "./screens/inicioEmpleado/index.js";
import PantallaPlanes from "./screens/planes/index.js";
import TPV from "./screens/r/index.js";

import Dashboard from "./screens/dashboard/index.js";
import PantallaInventario from "./screens/inventario/index.js";
import PantallaProveedores from "./screens/proveedores/index.js";
import PantallaEmpleados from "./screens/empleados/index.js";
import VerTipoProducto from "./screens/verTipoProducto/index.js";
import VerProducto from "./screens/verTipoProducto/verProducto/index.js";
import PantallaAlertaStock from "./screens/alertaStock/index.js";
import PantallaPerdidas from "./screens/perdidas/index.js";
import PantallaAñadirEmpleado from "./screens/añadirEmpleado/index.js";
import PantallaAñadirProveedor from "./screens/añadirProveedor/index.js";
import PantallaPerfil from "./screens/perfil/index.js";
import PantallaAñadirCategoria from "./screens/añadirCategoria/index.js";
import PantallaAñadirCategoria from "./screens/inventario/añadir.js";
import VerVentas from "./screens/ventas/index.js";
import VerVentaEspecifica from "./screens/ventas/ventaEspecifica/index.js";


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
          <button className="register-btn" onClick={() => navigate("/registroDueño")}>
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
        <Route path="/registroDueño" element={<PantallaRegistroDueño />} />
        <Route path="/masInformacion" element={<PantallaMasInfo />} />
        <Route path="/inicioDueño" element={<PantallaInicioDueño />} />
        <Route path="/inicioEmpleado" element={<PantallaInicioEmpleado />} />
        <Route path="/planes" element={<PantallaPlanes />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/inventario" element={<PantallaInventario />} />
        <Route path="/ventas" element={<VerVentas/>} />
        <Route path="/verTipoProducto/:categoriaId" element={<VerTipoProducto />} />
        <Route path="/categoria/:categoriaId/producto/:productoNombre" element={<VerProducto />} />
        <Route path="/proveedores" element={<PantallaProveedores />} />
        <Route path="/empleados" element={<PantallaEmpleados />} />
        <Route path="/alertaStock" element={<PantallaAlertaStock />} />
        <Route path="/perdidas" element={<PantallaPerdidas />} />
        <Route path="/añadirEmpleado" element={<PantallaAñadirEmpleado />} />
        <Route path="/añadirProveedor" element={<PantallaAñadirProveedor />} />
        <Route path="/perfil" element={<PantallaPerfil/>} />
        <Route path="/añadirCategoria" element={<PantallaAñadirCategoria/>} />
        <Route path="/ventas/:ventaId" element={<VerVentaEspecifica />} />

      </Routes>
    </Router>
  );
}

export default App;
