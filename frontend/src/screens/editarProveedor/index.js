import React, { useEffect, useState } from "react";
import { useNavigate, Link, useParams } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const token = localStorage.getItem("token");
const negocioId = localStorage.getItem("negocioId");

const obtenerProveedor = async (id) => {
  try {
    const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/proveedores/${id}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
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
    const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/proveedores/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(proveedor),
    });

    if (!response.ok) {
      const errorData = await response.json(); // Obtener detalles de la respuesta de error
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
  const { id } = useParams(); // Usamos useParams para obtener el ID de la URL
  const [proveedor, setProveedor] = useState({
    name: "",
    direccion: "",
    email: "",
    telefono: "",
    negocioId: negocioId,
  });
  const [loading, setLoading] = useState(true); // Estado de carga

  // Este useEffect se ejecutará cada vez que cambie el ID
  useEffect(() => {
    const cargarProveedor = async () => {
      setLoading(true); // Marcamos como cargando
      const data = await obtenerProveedor(id);
      if (data) {
        setProveedor(data); // Actualizamos el estado con los datos del nuevo proveedor
      }
      setLoading(false); // Terminamos la carga
    };
    cargarProveedor();
  }, [id]); // Dependencia añadida para que recargue los datos cada vez que cambie el ID

  const handleChange = (e) => {
    setProveedor({ ...proveedor, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const proveedorActualizado = {
      ...proveedor,
      negocioId: negocioId,
    };
    const actualizado = await actualizarProveedor(id, proveedorActualizado);
    if (actualizado) navigate(`/verProveedor/${id}`);
  };

  if (loading) {
    return <div>Cargando...</div>;
  }

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
