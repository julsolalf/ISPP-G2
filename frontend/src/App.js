
import { BrowserRouter as Router, Routes, Route, useNavigate, useLocation } from "react-router-dom";
import "./App.css";
import PantallaInicioSesion from "./screens/inicioSesion/index.js";
import PantallaRegistroDueno from "./screens/registroDueno/index.js";
import PantallaRecuperarContrasena from "./screens/recuperarContrasena/index.js";
import PantallaMasInfo from "./screens/masInformacion/index.js";
import PantallaInicioDueno from "./screens/inicioDueno/index.js";
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
import PantallaAnadirEmpleado from "./screens/anadirEmpleado/index.js";
import PantallaAnadirProveedor from "./screens/anadirProveedor/index.js";
import PantallaPerfil from "./screens/perfil/index.js";
import PantallaAnadirCategoria from "./screens/anadirCategoria/index.js";
import VerVentas from "./screens/ventas/index.js";
import VerVentaEspecifica from "./screens/ventas/ventaEspecifica/index.js";
import AnadirProductoInventario from "./screens/anadirProductoInventario/index.js";
import EditarProducto from "./screens/verTipoProducto/editarProductoInventario/index.js";


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
          <button className="register-btn" onClick={() => navigate("/registroDueno")}>
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
        <Route path="/recuperarContrasena" element={<PantallaRecuperarContrasena />} />
        <Route path="/registroDueno" element={<PantallaRegistroDueno />} />
        <Route path="/masInformacion" element={<PantallaMasInfo />} />
        <Route path="/inicioDueno" element={<PantallaInicioDueno />} />
        <Route path="/inicioEmpleado" element={<PantallaInicioEmpleado />} />
        <Route path="/planes" element={<PantallaPlanes />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/inventario" element={<PantallaInventario />} />
        <Route path="/ventas" element={<VerVentas/>} />
        <Route path="/verTipoProducto/:categoriaNombre" element={<VerTipoProducto />} />
        <Route path="/categoria/:categoriaNombre/producto/:productoNombre" element={<VerProducto />} />
        <Route path="/anadirProductoInventario" element={<AnadirProductoInventario />} />
        <Route path="/proveedores" element={<PantallaProveedores />} />
        <Route path="/empleados" element={<PantallaEmpleados />} />
        <Route path="/alertaStock" element={<PantallaAlertaStock />} />
        <Route path="/perdidas" element={<PantallaPerdidas />} />
        <Route path="/anadirEmpleado" element={<PantallaAnadirEmpleado />} />
        <Route path="/anadirProveedor" element={<PantallaAnadirProveedor />} />
        <Route path="/perfil" element={<PantallaPerfil/>} />
        <Route path="/anadirCategoria" element={<PantallaAnadirCategoria/>} />
        <Route path="/ventas/:ventaId" element={<VerVentaEspecifica />} />
        <Route path="/editarProductoInventario/:id" element={<EditarProducto />} />

      </Routes>
    </Router>
  );
}

export default App;
