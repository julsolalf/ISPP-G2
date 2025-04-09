import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User, Eye, EyeOff } from "lucide-react";

function EditarEmpleado() {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const token = localStorage.getItem("token");
  const negocioId = localStorage.getItem("negocioId") 
  const empleadoId = localStorage.getItem("empleadoId")
  const [empleado, setEmpleado] = useState({
    username: "",
    password: "",
    firstName: "",
    lastName: "",
    email: "",
    numTelefono: "",
    tokenEmpleado: "",
    descripcion: "",
    negocio: negocioId
  });

const obtenerEmpleado = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/empleados/${empleadoId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error al obtener el empleado:", error);
    return null;
  }
};


const actualizarEmpleado = async (empleadoId, empleado) => {
    try {
      const response = await fetch(`http://localhost:8080/api/empleados/${empleadoId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
         },
        body: JSON.stringify(empleado),
      });
      
      if (!response.ok) {
        throw new Error("Error al actualizar el empleado");
      }
      
      return await response.json();
    } catch (error) {
      console.error("Error al actualizar el empleado:", error);
      return null;
    }
  };

  useEffect(() => {
    const cargarEmpleado = async () => {
      const data = await obtenerEmpleado(empleadoId);
      if (data) {
        setEmpleado({
          username: data.user.username || "",
          password: "",  // La contraseña no se debe traer, el usuario la ingresa si se edita
          firstName: data.firstName || "",
          lastName: data.lastName || "",
          email: data.email || "",
          numTelefono: data.numTelefono || "",
          tokenEmpleado: data.tokenEmpleado || "",
          descripcion: data.descripcion || "",
          negocio: negocioId || null,
        });
      }
    };
    cargarEmpleado();
  }, [empleadoId]); 
  

  const handleChange = (e) => {
    const { name, value } = e.target;
    const newNameMap = {
      nombre: "firstName",
      apellido: "lastName",
      telefono: "numTelefono"
    };
  
    setEmpleado({ ...empleado, [newNameMap[name] || name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Asegurar que se envíe un valor en negocio y evitar contraseña vacía
    const empleadoFinal = {
      ...empleado,
      password: empleado.password || undefined, // Solo enviar si se cambia
      negocio: empleado.negocio ?? 1 // Asigna un valor por defecto si es necesario
    };
  
    console.log("Datos enviados:", empleadoFinal); // Para depuración
  
    const actualizado = await actualizarEmpleado(localStorage.getItem("empleadoId"), empleadoFinal);
    if (actualizado) navigate(`/empleados`);
  };

  return (
    <div className="home-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        height: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
      }}>
      <div className="content">
        <div className="icon-container-right">
          <Bell size={30} className="icon" />
          <User size={30} className="icon" />
        </div>
        <button onClick={() => navigate(`/verEmpleado/${empleadoId}`)} className="back-button">⬅ Volver</button>
        <h1>Editar Empleado</h1>
        <form className="form-container" onSubmit={handleSubmit}>
          <input type="text" name="nombre" value={empleado.firstName} onChange={handleChange} placeholder="Nombre" required />
          <input type="text" name="apellido" value={empleado.lastName} onChange={handleChange} placeholder="Apellido" required />
          <input type="text" name="telefono" value={empleado.numTelefono} onChange={handleChange} placeholder="Teléfono" required />
          <input type="text" name="email" value={empleado.email} onChange={handleChange} placeholder="Email" required />
          <input type="text" name="username" value={empleado.username} onChange={handleChange} placeholder="User name" required />
          
          <div style={{ position: "relative", width: "100%" }}>
            <input
              type={showPassword ? "text" : "password"}
              name="password"
              value={empleado.password}
              onChange={handleChange}
              placeholder="Password"
              required
              style={{ paddingRight: "40px", width: "100%" }}
            />
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              style={{
                position: "absolute",
                right: "10px",
                top: "50%",
                transform: "translateY(-50%)",
                background: "none",
                border: "none",
                cursor: "pointer",
              }}
            >
              {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
            </button>
          </div>
          
          <input type="text" name="tokenEmpleado" value={empleado.tokenEmpleado} onChange={handleChange} placeholder="Token Empleado" required />
          <textarea name="descripcion" value={empleado.descripcion} onChange={handleChange} placeholder="Descripción" required />
          <button type="submit" className="button">💾 Guardar</button>
        </form>
      </div>
    </div>
  );
  
}

export default EditarEmpleado;
