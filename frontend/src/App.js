
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
import AppNavbar from "./AppNavbar";
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
import PantallaRegistroNegocio from "./screens/registroNegocio/index.js";
import PantallaElegirNegocio from "./screens/elegirNegocio/index.js";
import PantallaVerEmpleado from "./screens/empleados/verEmpleado/index.js";
import PantallaEditarEmpleado from "./screens/empleados/editarEmpleado/index.js";
import PantallaVerProveedor from "./screens/verProveedor/index.js";
import PantallaEditarProveedor from "./screens/editarProveedor/index.js";
import PantallaEditarVenta from "./screens/editarVenta/index.js";
import PantallaVentaEmpleado from "./screens/ventasEmpleado/index.js";
import PantallaVerVentaEmpleado from "./screens/ventasEmpleado/ventaEspecifica/index.js";
import Home from "./screens/home/index.js";

function App() {

  let publicRoutes = (
    <>
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
      <Route path="/verEmpleado/:empleadoId" element={<PantallaVerEmpleado />} />
      <Route path="/editarEmpleado/:empleadoId" element={<PantallaEditarEmpleado />} />
      <Route path="/alertaStock" element={<PantallaAlertaStock />} />
      <Route path="/perdidas" element={<PantallaPerdidas />} />
      <Route path="/anadirEmpleado" element={<PantallaAnadirEmpleado />} />
      <Route path="/anadirProveedor" element={<PantallaAnadirProveedor />} />
      <Route path="/perfil" element={<PantallaPerfil/>} />
      <Route path="/anadirCategoria" element={<PantallaAnadirCategoria/>} />
      <Route path="/registroNegocio" element={<PantallaRegistroNegocio/>} />
      <Route path="/elegirNegocio" element={<PantallaElegirNegocio/>} />
      <Route path="/ventas/:ventaId" element={<VerVentaEspecifica />} />
      <Route path="/editarProductoInventario/:id" element={<EditarProducto />} />
      <Route path="/verProveedor/:id" element={<PantallaVerProveedor />} />
      <Route path="/editarProveedor/:id" element={<PantallaEditarProveedor />} />
      <Route path="/editarVenta/:id" element={<PantallaEditarVenta />} />
      <Route path="/ventasEmpleado" element={<PantallaVentaEmpleado />} />
      <Route path="/ventasEmpleado/:ventaId" element={<PantallaVerVentaEmpleado />} />
    </>
  )

  return (
    <div>
      <Router>
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
          <Routes>
            <Route path="/" exact={true} element={<Home/>} />
            {publicRoutes}
          </Routes>
          </div>
        </Router>
      </div>

  )};

export default App;
