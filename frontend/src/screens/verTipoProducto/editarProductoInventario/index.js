import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const token = localStorage.getItem("token");
const productoId = localStorage.getItem("productoId");

const obtenerProducto = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosInventario/${productoId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      throw new Error("Error al obtener el producto");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener el producto:", error);
    return null;
  }
};

const actualizarProducto = async (producto) => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosInventario/${productoId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
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
  const [producto, setProducto] = useState({
    name: "",
    precioCompra: "",
    cantidadDeseada: "",
    cantidadAviso: "",
    categoriaId: "",
  });

  useEffect(() => {
    const cargarProducto = async () => {
      const data = await obtenerProducto();
      if (data) {
        setProducto({
          name: data.name || "",
          precioCompra: data.precioCompra || "",
          cantidadDeseada: data.cantidadDeseada || "",
          cantidadAviso: data.cantidadAviso || "",
          categoriaId: data.categoria?.id || "", // Aseguramos que extraemos el ID
        });
      }
    };
    cargarProducto();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProducto((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const actualizado = await actualizarProducto(producto);
    if (actualizado) navigate(`/categoria/${actualizado.categoria?.name}/producto/${productoId}`);
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
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>Editar Producto</h2>
        <form className="form-container" onSubmit={handleSubmit}>
          <input
            type="text"
            name="name"
            value={producto.name}
            onChange={handleChange}
            placeholder="Nombre"
            required
          />
          <input
            type="number"
            name="precioCompra"
            value={producto.precioCompra}
            onChange={handleChange}
            placeholder="Precio de Compra"
            required
          />
          <input
            type="number"
            name="cantidadDeseada"
            value={producto.cantidadDeseada}
            onChange={handleChange}
            placeholder="Cantidad Deseada"
            required
          />
          <input
            type="number"
            name="cantidadAviso"
            value={producto.cantidadAviso}
            onChange={handleChange}
            placeholder="Cantidad Aviso"
            required
          />
          <input
            type="number"
            name="categoriaId"
            value={producto.categoriaId}
            onChange={handleChange}
            placeholder="ID de Categoría"
            required
          />
          <button type="submit" className="button">💾 Guardar</button>
        </form>
      </div>
    </div>
  );
}

export default EditarProducto;
