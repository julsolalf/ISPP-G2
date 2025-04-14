import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

function PantallaRegistroDueno() {
  const [ownerFirstName, setOwnerFirstName] = useState("");
  const [ownerLastName, setOwnerLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const navigate = useNavigate();

  const handleRegister = async () => {
    if (password !== confirmPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }
  
    const data = {
      username: usuario,
      password: password,
      firstName: ownerFirstName,
      lastName: ownerLastName,
      email: email,
      numTelefono: phone,
    };
   
    try {
      setLoading(true);
      
      const registerResponse = await axios.post("http://localhost:7070/api/auth/register", data);
      console.log("Registro exitoso:", registerResponse.data);
  
      const loginResponse = await fetch("http://localhost:7070/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: usuario,
          password: password,
        }),
      });
  
      const loginData = await loginResponse.json();
      const token = loginData.token;
      localStorage.setItem("token", token);
  
      const userResponse = await fetch("http://localhost:7070/api/users/me", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const user = await userResponse.json();
      localStorage.setItem("user", JSON.stringify(user));
  
      const duenoResponse = await fetch(`http://localhost:7070/api/duenos/user/${user.id}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const dueno = await duenoResponse.json();
      localStorage.setItem("duenoId", dueno.id);
      navigate("/elegirNegocio");
    } catch (error) {
      console.error("Error en el registro o login:", error.response?.data || error.message);
      alert(error.response?.data?.message || "Error al registrar/iniciar sesión.");
    } finally {
      setLoading(false);
    }
  };
  
  
  return (
    <div className="page-container">
      <div className="fondo">
        <button className="back-button" style={{fontSize:"25px"}} onClick={() => navigate("/")}>← Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title" style={{paddingTop:"5%"}}>GastroStock</h1>
        <h2 style={{marginBottom:"3%"}}>Ingrese sus datos</h2>
        <div className="clearfix"></div>
        <div className="clearfix"></div>
        <div style={{textAlign :"left", width:"80%", marginLeft:"20%"}}>
          <strong style={{fontSize:"24px"}}>Nombre y apellidos:</strong>
          <div className="clearfix"></div>
          <input type="text" placeholder="Nombre del dueno" style={{width:"18.5%"}} value={ownerFirstName} onChange={(e) => setOwnerFirstName(e.target.value)} />
          <input type="text" placeholder="Apellidos del dueno" style={{marginLeft:"5%", width:"45%"}} value={ownerLastName} onChange={(e) => setOwnerLastName(e.target.value)} />
          <div className="clearfix" style={{marginTop:"1%"}}></div>
          <strong style={{fontSize:"24px"}}>Correo electrónico:</strong>
          <div className="clearfix"></div>
          <input type="email" placeholder="Correo Electrónico" style={{width:"70%"}} value={email} onChange={(e) => setEmail(e.target.value)} />
          <div className="clearfix" style={{marginTop:"1%"}}></div>
          <strong style={{fontSize:"24px"}}>Teléfono:</strong>
          <div className="clearfix"></div>
          <input type="tel" placeholder="Teléfono" style={{width:"70%"}} value={phone} onChange={(e) => setPhone(e.target.value)} />
          <div className="clearfix" style={{marginTop:"1%"}}></div>
          <strong style={{fontSize:"24px"}}>Nombre de usuario:</strong>
          <div className="clearfix"></div>
          <input type="text" placeholder="Usuario" style={{width:"70%"}} value={usuario} onChange={(e) => setUsuario(e.target.value)} />
          <div className="clearfix" style={{marginTop:"1%"}}></div>
          <strong style={{fontSize:"24px"}}>Contraseña:</strong>
          <div className="clearfix"></div>
          <input type="password" placeholder="Contrasena" style={{width:"32.5%", margin:"0%"}} value={password} onChange={(e) => setPassword(e.target.value)} />
          <input type="password" placeholder="Confirmar Contrasena" style={{width:"32.5%", marginLeft:"3%"}} value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
        </div>
        <div className="clearfix"></div>
        <button onClick={handleRegister} className="login-btn" disabled={loading} style={{marginTop:"5%"}}>
          {loading ? "Registrando..." : "Registrarse"}
        </button>
      </div>
    </div>
  );
}

export default PantallaRegistroDueno;
