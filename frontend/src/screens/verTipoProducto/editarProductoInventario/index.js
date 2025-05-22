import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

function EditarProducto() {
  const navigate = useNavigate();
  const [producto, setProducto] = useState({
    name: "",
    precioCompra: "",
    cantidadDeseada: "",
    cantidadAviso: "",
    categoriaId: "",
    proveedorId: "",
  });
  const [proveedores, setProveedores] = useState([]);
  const [categoriaNombre, setCategoriaNombre] = useState("");

  const storedNegocioId = localStorage.getItem("negocioId");
  const token = localStorage.getItem("token");
  const productoId = localStorage.getItem("productoId");

  const obtenerProducto = async (id) => {
    try {
      const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/productosInventario/${id}`, {
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
      const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/productosInventario/${productoId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
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

  useEffect(() => {
    // Obtener proveedores
    if (storedNegocioId) {
      fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/proveedores/negocio/${storedNegocioId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then((res) => res.json())
        .then((data) => setProveedores(data))
        .catch((error) => {
          console.error("Error al obtener los proveedores:", error);
          alert("No se pudieron cargar los proveedores.");
        });
    }

    // Obtener producto
    if (productoId) {
      obtenerProducto(productoId).then((data) => {
        if (data) {
          setProducto({
            name: data.name,
            precioCompra: data.precioCompra,
            cantidadDeseada: data.cantidadDeseada,
            cantidadAviso: data.cantidadAviso,
            categoriaId: data.categoria.id,
            proveedorId: data.proveedor.id,
          });
          setCategoriaNombre(data.categoria.name);
        }
      });
    }
  }, []);

  const handleChange = (e) => {
    setProducto({ ...producto, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!producto.name || !producto.precioCompra || !producto.cantidadDeseada || !producto.cantidadAviso || !producto.proveedorId) {
      alert("Todos los campos son obligatorios");
      return;
    }

    const actualizado = await actualizarProducto(producto);
    if (actualizado) {
      navigate(`/categoria/${categoriaNombre}/producto/${productoId}`);
    }
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
        <button onClick={() => navigate(`/categoria/${categoriaNombre}/producto/${productoId}`)} className="back-button">
          ⬅ Volver
        </button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>Editar Producto</h2>

        <form onSubmit={handleSubmit} className="form-container">
          <input
            type="text"
            name="name"
            value={producto.name}
            onChange={handleChange}
            placeholder="Nombre del producto"
            required
          />
          <input
            type="number"
            name="precioCompra"
            value={producto.precioCompra}
            onChange={handleChange}
            placeholder="Precio de compra"
            required
          />
          <input
            type="number"
            name="cantidadDeseada"
            value={producto.cantidadDeseada}
            onChange={handleChange}
            placeholder="Cantidad deseada"
            required
          />
          <input
            type="number"
            name="cantidadAviso"
            value={producto.cantidadAviso}
            onChange={handleChange}
            placeholder="Cantidad de aviso"
            required
          />
          {categoriaNombre && (
            <div>
              <strong>Categoría: </strong>
              <span>{categoriaNombre}</span>
            </div>
          )}

          <label>Selecciona un proveedor:</label>
          <select
            name="proveedorId"
            value={producto.proveedorId}
            onChange={handleChange}
            required
          >
            <option value="">-- Selecciona un proveedor --</option>
            {proveedores.map((proveedor) => (
              <option key={proveedor.id} value={proveedor.id}>
                {proveedor.name}
              </option>
            ))}
          </select>

          <input type="submit" value="Guardar Cambios" className="button" />
        </form>
      </div>
    </div>
  );
}

export default EditarProducto;
