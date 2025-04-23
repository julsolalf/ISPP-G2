import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const VerCarritoProveedor = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const proveedorId = localStorage.getItem("proveedorId"); // Asumiendo que ya tienes el ID del proveedor guardado
  const [productos, setProductos] = useState([]);
  const [carrito, setCarrito] = useState([]);
  
  // Obtener los productos del proveedor
  const obtenerProductosPorProveedor = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/productosInventario/proveedor/${proveedorId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });

      if (!response.ok) {
        throw new Error("Error al obtener los productos del proveedor");
      }

      const data = await response.json();
      setProductos(data);
    } catch (error) {
      console.error("Error al obtener los productos:", error);
      setProductos([]);
    }
  };

  useEffect(() => {
    obtenerProductosPorProveedor();
  }, [proveedorId]);

  const handleCantidadChange = (productoId, cantidad) => {
    // Actualizar la cantidad del producto en el carrito
    setCarrito((prevCarrito) => {
      const index = prevCarrito.findIndex(item => item.id === productoId);
      if (index !== -1) {
        const newCarrito = [...prevCarrito];
        newCarrito[index].cantidad = cantidad;
        return newCarrito;
      }
      return [...prevCarrito, { id: productoId, cantidad }];
    });
  };

  const añadirAlCarrito = (productoId, cantidad) => {
    if (cantidad > 0) {
      setCarrito((prevCarrito) => {
        const index = prevCarrito.findIndex(item => item.id === productoId);
        if (index !== -1) {
          const newCarrito = [...prevCarrito];
          newCarrito[index].cantidad += cantidad;
          return newCarrito;
        }
        return [...prevCarrito, { id: productoId, cantidad }];
      });
    }
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

        <button onClick={() => navigate(`/verProveedor/${proveedorId}`)} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>Productos del proveedor</h2>


        <div className="productos-container">
          {productos.length === 0 ? (
            <h3>No hay productos para este proveedor</h3>
          ) : (
            <div className="productos-grid">
              {productos.map((producto) => (
                <div key={producto.id} className="producto-card">
                  <h3>{producto.name}</h3>
                  <div className="producto-info">
                    <label>
                      Cantidad:
                      <input
                        type="number"
                        min="0"
                        defaultValue={0}
                        onChange={(e) =>
                          handleCantidadChange(producto.id, parseInt(e.target.value))
                        }
                      />
                    </label>
                    <button
                      className="add-to-cart-btn"
                      onClick={() =>
                        añadirAlCarrito(producto.id, parseInt(document.getElementById(producto.id).value))
                      }
                    >
                      Añadir al carrito
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Aquí podrías mostrar el carrito si quieres */}
        <div className="carrito-container">
          <h3>Carrito:</h3>
          <ul>
            {carrito.map((item) => (
              <li key={item.id}>
                Producto {item.id} - Cantidad: {item.cantidad}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default VerCarritoProveedor;
