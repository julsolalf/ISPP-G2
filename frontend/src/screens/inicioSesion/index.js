import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/paginasBase/styles.css";

function PantallaInicioSesion() {
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: usuario,
          password: password,
        }),
      });

      if (!response.ok) {
        const text = await response.text();
        throw new Error(`Error en login: ${response.status} - ${text}`);
      }
      const data = await response.json();
      const token = data.token;
      localStorage.setItem("token", token);

      const userResponse = await fetch("http://localhost:8080/api/users/me", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!userResponse.ok) {
        const errText = await userResponse.text();
        throw new Error(`Error al obtener usuario: ${userResponse.status} - ${errText}`);
      }
      const user = await userResponse.json();
      localStorage.setItem("user", JSON.stringify(user));

      if (user.authority.authority === "empleado") {
        const empleadoResponse = await fetch(`http://localhost:8080/api/empleados/user/${user.id}`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const empleado = await empleadoResponse.json();
        localStorage.setItem("duenoId", empleado.id); 
        navigate("/inicioEmpleado");
      } else if (user.authority.authority === "dueno") {
        const duenoResponse = await fetch(`http://localhost:8080/api/duenos/user/${user.id}`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        const dueno = await duenoResponse.json();
        localStorage.setItem("duenoId", dueno.id); 
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
    <div className="page-container">
      <div className="fondo-login">
        <button onClick={() => navigate("/")} className="back-button" style={{marginLeft:"30%", fontSize:"24px"}}>⬅ Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <div className="clearfix"></div>
        <h1 className="title" style={{marginTop:"10%"}}>GastroStock</h1>
        <div className="clearfix"></div>
        <h2>Iniciar Sesión</h2>
        <div className="clearfix" style={{marginTop:"5%"}}></div>
        <div style={{textAlign :"left", width:"80%", marginLeft:"20%"}}>
          <strong style={{fontSize:"24px"}}>Usuario:</strong>
          <div className="clearfix"></div>
          <input
            type="Usuario"
            placeholder="Usuario"
            value={usuario}
            onChange={(e) => setUsuario(e.target.value)}
            style={{width:"70%"}}
          />
          <div className="clearfix" style={{marginTop:"5%"}}></div>
          <strong style={{fontSize:"24px"}}>Contraseña:</strong>
          <div className="clearfix"></div>
          <input
            type="password"
            placeholder="Contrasena"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={{width:"70%"}}
          />
        </div>
        <div className="clearfix"></div>
        <button onClick={handleLogin} className="login-btn" style={{marginTop:"5%", fontSize:"26px"}}>Iniciar Sesión</button>
        <div className="clearfix"></div>
        <Link to="/recuperarcontrasena" className="forgot-password-link">
          ¿Has olvidado la contrasena?
        </Link>
    </div>
  </div>
  );
}

export default PantallaInicioSesion;
