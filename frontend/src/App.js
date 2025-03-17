import "./App.css";
import TPV from "./screens/r/index.js";
import Categorias from "./productoInventario/list.js";
import Proveedores from "./listaProveedores/proveedores.js";
import { BrowserRouter as Router, Routes, Route, useNavigate, useLocation } from "react-router-dom";

function App() {
  return (
    <Router>
      <AppContent />
    </Router>
  );
}

function AppContent() {
  const navigate = useNavigate();
  const location = useLocation(); // Detecta en qué ruta estás

  return (
    <div className="App">
      {location.pathname === "/" && (
        <header className="App-header">
          <div className="logo-container">
            <img src="/gastrostockLogo.png" alt="App Logo" className="app-logo" />
          </div>
          <div className="button-container">
            <button onClick={() => console.log("Ir a Iniciar Sesión")}>
              Iniciar Sesión
            </button>
            <button onClick={() => console.log("Ir a Registrarse")}>
              Registrarse
            </button>
            <button onClick={() => console.log("Mostrar Más Información")}>
              Más Información
            </button>
            <button onClick={() => navigate("/categorias")}>
              Ver Categorías
            </button>
            <button onClick={() => navigate("/proveedores")}>
              Ver Proveedores
            </button>
          </div>
        </header>
      )}

      <main>
        <Routes>
          <Route path="/" element={<p>Bienvenido a la aplicación</p>} />
          <Route path="/categorias" element={<Categorias />} />
          <Route path="/proveedores" element={<Proveedores />} />
        </Routes>
      </main>
    </div>
  );
}

export default App;