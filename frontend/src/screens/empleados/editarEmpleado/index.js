import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import "../../../css/listados/styles.css";
import { Bell, User, Eye, EyeOff } from "lucide-react";

const obtenerEmpleado = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/empleados/${localStorage.getItem("empleadoId")}`);
    return response.data;
  } catch (error) {
    console.error("Error al obtener el empleado:", error);
    return null;
  }
};

const token = localStorage.getItem("token");
const negocioId = localStorage.getItem("negocioId")

const actualizarEmpleado = async (id, empleado) => {
    try {
      const response = await fetch(`http://localhost:8080/api/empleados/${id}`, {
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

function EditarEmpleado() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
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

  useEffect(() => {
    const cargarEmpleado = async () => {
      const data = await obtenerEmpleado(id);
      if (data) {
        setEmpleado({
          username: data.user.username || "",  
          password: data.user.password || "",  // No deberías traer la contraseña, el usuario la debe ingresar si se edita
          firstName: data.firstName || data.nombre || "",  
          lastName: data.lastName || data.apellido || "",  
          email: data.email || "",  
          numTelefono: data.numTelefono || data.telefono || "",  
          tokenEmpleado: data.tokenEmpleado || "",  
          descripcion: data.descripcion || "",  
          negocio: 1 || null  
        });
      }
    };
    cargarEmpleado();
  }, [id]);
  

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
    <div className="content">
      <div className="icon-container-right">
        <Bell size={30} className="icon" />
        <User size={30} className="icon" />
      </div>
      <button onClick={() => navigate(`/verEmpleado/${localStorage.getItem("empleadoId")}`)} className="back-button">⬅ Volver</button>
      <h1>Editar Empleado</h1>
      <form className="form-container" onSubmit={handleSubmit}>
        <input type="text" name="nombre" value={empleado.nombre} onChange={handleChange} placeholder="Nombre" required />
        <input type="text" name="apellido" value={empleado.apellido} onChange={handleChange} placeholder="Apellido" required />
        <input type="text" name="telefono" value={empleado.telefono} onChange={handleChange} placeholder="Teléfono" required />
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
  );
  
}

export default EditarEmpleado;
