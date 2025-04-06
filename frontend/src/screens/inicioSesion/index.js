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
      const response = await axios.post("http://localhost:8080/api/auth/login", {
        username: usuario,
        password: password
      });
  
      const token = response.data.token;
      localStorage.setItem("token", token);
  
      const userResponse = await axios.get("http://localhost:8080/api/users/me", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      const user = userResponse.data;
      localStorage.setItem("user", JSON.stringify(user));
  
      if (user.authority.authority === "empleado") {
        navigate("/inicioEmpleado");
      } else if (user.authority.authority === "dueno") {
        navigate("/elegirNegocio");
      } else {
        navigate("/"); 
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
        <button onClick={() => navigate("/")} className="back-button">⬅ Volver</button>
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
          placeholder="Contrasena"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button onClick={handleLogin} className="login-btn">Iniciar Sesión</button>

        <Link to="/recuperarcontrasena" className="forgot-password-link">
          ¿Has olvidado la contrasena?
        </Link>
      </div>
    </div>
  );
}

export default PantallaInicioSesion;
