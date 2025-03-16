
import "./App.css";
import TPV from "./screens/r/index.js";
//import Productos from "./components/Productos.js";

function App() {
  return (
    <div className="App">
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
        </div>
      </header>
    </div>
  );
}

export default App;