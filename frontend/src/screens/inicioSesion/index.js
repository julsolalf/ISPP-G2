import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import axios from "axios";
import "../../css/paginasBase/styles.css";

function PantallaInicioSesion() {
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const [empleadosResponse, duenosResponse] = await Promise.all([
        axios.get("http://localhost:8080/api/empleados"),
        axios.get("http://localhost:8080/api/dueños"),
      ]);

      const empleado = empleadosResponse.data.find(
        (user) =>
          user.user.username === usuario && user.user.password === password
      );
      const dueño = duenosResponse.data.find(
        (user) =>
          user.user.username === usuario && user.user.password === password
      );

      if (empleado) {
        localStorage.setItem("user", JSON.stringify(empleado));
        navigate("/inicioEmpleado");
      }
      else if (dueño) {
        localStorage.setItem("user", JSON.stringify(dueño));
        navigate("/inicioDueño");
      } else {
        throw new Error("Credenciales incorrectas.");
      }
    } catch (error) {
      console.error("Error al iniciar sesión:", error);
      alert("Usuario no encontrado o credenciales incorrectas.");
    }
  };

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
        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Iniciar Sesión</h2>

        <input
          type="Usuario"
          placeholder="Usuario"
          value={usuario}
          onChange={(e) => setUsuario(e.target.value)}
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button onClick={handleLogin} className="login-btn">Iniciar Sesión</button>

        <Link to="/recuperarcontraseña" className="forgot-password-link">
          ¿Has olvidado la contraseña?
        </Link>
      </div>
    </div>
  );
}

export default PantallaInicioSesion;
