import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/inicioSesion-registro/styles.css";

function RecoverPasswordScreen() {
  const [email, setEmail] = useState("");
  const navigate = useNavigate();

  const handleSubmit = () => {
    // Aquí podrías hacer la lógica de envío del correo de recuperación
    console.log("Enviando correo de recuperación a:", email);
    alert("Se ha enviado un correo para restablecer tu contraseña.");
    navigate("/"); // Redirigir al inicio o a donde desees
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
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Recuperar Contraseña</h2>

        <p>Introduce el correo electrónico al que está asociada tu cuenta. Se enviará un correo electrónico a la dirección indicada con un enlace para restablecer la contraseña.</p>
        
        <input
          type="email"
          placeholder="Correo Electrónico"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <button onClick={handleSubmit} className="login-btn">Enviar</button>
      </div>
    </div>
  );
}

export default RecoverPasswordScreen;
