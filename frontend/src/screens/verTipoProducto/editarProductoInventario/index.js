import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const obtenerProducto = async (id) => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosInventario/${id}`);
    if (!response.ok) {
      throw new Error("Error al obtener el producto");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener el producto:", error);
    return null;
  }
};

const actualizarProducto = async (id, producto) => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosInventario/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(producto),
    });
    if (!response.ok) {
      throw new Error("Error al actualizar el producto");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al actualizar el producto:", error);
    return null;
  }
};

function EditarProducto() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [producto, setProducto] = useState({ name: "", precioCompra: "", cantidadDeseada: "", cantidadAviso: "" });

  useEffect(() => {
    const cargarProducto = async () => {
      const data = await obtenerProducto(id);
      if (data) setProducto(data);
    };
    cargarProducto();
  }, [id]);

  const handleChange = (e) => {
    setProducto({ ...producto, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const actualizado = await actualizarProducto(id, producto);
    if (actualizado) navigate(`/categoria/${producto.categoria?.name}/producto/${id}`);
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
        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <h1>Editar Producto</h1>
        <form className="form-container" onSubmit={handleSubmit}>
          <input type="text" name="name" value={producto.name} onChange={handleChange} placeholder="Nombre" required />
          <input type="number" name="precioCompra" value={producto.precioCompra} onChange={handleChange} placeholder="Precio de Compra" required />
          <input type="number" name="cantidadDeseada" value={producto.cantidadDeseada} onChange={handleChange} placeholder="Cantidad Deseada" required />
          <input type="number" name="cantidadAviso" value={producto.cantidadAviso} onChange={handleChange} placeholder="Cantidad Aviso" required />
          <button type="submit" className="button">💾 Guardar</button>
        </form>
      </div>
    </div>
  );
}

export default EditarProducto;