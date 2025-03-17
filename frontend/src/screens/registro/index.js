import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/inicioSesion-registro/styles.css";

function RegisterScreen() {
  const [businessName, setBusinessName] = useState("");
  const [ownerFirstName, setOwnerFirstName] = useState("");
  const [ownerLastName, setOwnerLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const navigate = useNavigate();

  const handleRegister = () => {
    if (password !== confirmPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }

    console.log("Registrando con:", {
      businessName,
      ownerFirstName,
      ownerLastName,
      email,
      phone,
      password
    });

    navigate("/");
  };

  return (
    <div 
      className="home-container"
      style={{ 
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        backgroundPosition: "center"
      }}
    >
      <div className="content">
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Registrarse</h2>

        <input
          type="text"
          placeholder="Nombre del negocio"
          value={businessName}
          onChange={(e) => setBusinessName(e.target.value)}
        />
        <input
          type="text"
          placeholder="Nombre del dueño"
          value={ownerFirstName}
          onChange={(e) => setOwnerFirstName(e.target.value)}
        />
        <input
          type="text"
          placeholder="Apellidos del dueño"
          value={ownerLastName}
          onChange={(e) => setOwnerLastName(e.target.value)}
        />
        <input
          type="email"
          placeholder="Correo Electrónico"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="tel"
          placeholder="Teléfono"
          value={phone}
          onChange={(e) => setPhone(e.target.value)}
        />
        <input
          type="password"
          placeholder="Contraseña"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="password"
          placeholder="Confirmar Contraseña"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
        />
        
        <button onClick={handleRegister} className="login-btn">Registrarse</button>
      </div>
    </div>
  );
}

export default RegisterScreen;
