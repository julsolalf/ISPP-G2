import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const token = localStorage.getItem("token");
const negocioId = localStorage.getItem("negocioId");
const id = localStorage.getItem("proveedorId");

const obtenerProveedor = async (id) => {
  try {
    const response = await fetch(`http://localhost:8080/api/proveedores/${id}`,
       {
        method: "GET",
          headers: { "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
           }});
    if (!response.ok) {
      throw new Error("Error al obtener el proveedor");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener el proveedor:", error);
    return null;
  }
};

const actualizarProveedor = async (id, proveedor) => {
  try {
    const response = await fetch(`http://localhost:8080/api/proveedores/${id}`, {
      method: "PUT",
        headers: { "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
         },
        body: JSON.stringify(proveedor),
      });

    if (!response.ok) {
      const errorData = await response.json();  // Obtener detalles de la respuesta de error
      throw new Error(errorData.message || "Error al actualizar el proveedor");
    }

    return await response.json();
  } catch (error) {
    console.error("Error al actualizar el proveedor:", error.message);
    return null;
  }
};

function EditarProveedor() {
  const navigate = useNavigate();
  const [proveedor, setProveedor] = useState({
    name: "",
    direccion: "",
    email: "",
    telefono: "",
    negocio: {id:negocioId},
  });

  useEffect(() => {
    const cargarProveedor = async () => {
      const data = await obtenerProveedor(id);
      if (data) setProveedor(data);
    };
    cargarProveedor();
  }, []);

  const handleChange = (e) => {
    setProveedor({ ...proveedor, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const actualizado = await actualizarProveedor(id, proveedor);
    if (actualizado) navigate(`/verProveedor/${id}`);
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
        textAlign: "center",
      }}
    >
      <div className="content">
        <div className="icon-container-right">
          <Bell size={30} className="icon" />
          <User size={30} className="icon" />
        </div>
        <button onClick={() => navigate(-1)} className="back-button">
          ⬅ Volver
        </button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>Editar Proveedor</h2>
        <form className="form-container" onSubmit={handleSubmit}>
          <input
            type="text"
            name="name"
            value={proveedor.name}
            onChange={handleChange}
            placeholder="Nombre"
            required
          />
          <input
            type="text"
            name="direccion"
            value={proveedor.direccion}
            onChange={handleChange}
            placeholder="Dirección"
            required
          />
          <input
            type="email"
            name="email"
            value={proveedor.email}
            onChange={handleChange}
            placeholder="Correo Electrónico"
            required
          />
          <input
            type="tel"
            name="telefono"
            value={proveedor.telefono}
            onChange={handleChange}
            placeholder="Teléfono"
            required
          />
          <button type="submit" className="button">
            💾 Guardar
          </button>
        </form>
      </div>
    </div>
  );
}

export default EditarProveedor;
