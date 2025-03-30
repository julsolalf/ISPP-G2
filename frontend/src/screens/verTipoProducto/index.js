import React, { useEffect, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const API_URL = "http://localhost:8080/api";

const obtenerCategorias = async () => {
  try {
    const response = await fetch(`${API_URL}/categorias`);
    if (!response.ok) throw new Error("Error al obtener las categorías");
    return await response.json();
  } catch (error) {
    console.error("Error al obtener categorías:", error);
    return [];
  }
};

const obtenerProductos = async (nombreCategoria, tipo) => {
  try {
    const url = tipo === "VENTA"
      ? `${API_URL}/productosVenta/categoriaVenta/${nombreCategoria}`
      : `${API_URL}/productosInventario/categoria/${nombreCategoria}`;
    const response = await fetch(url);
    if (!response.ok) throw new Error("Error al obtener productos");
    return await response.json();
  } catch (error) {
    console.error("Error al obtener productos:", error);
    return [];
  }
};

const obtenerIngredientes = async (productoVentaId) => {
  try {
    const response = await fetch(`${API_URL}/ingredientes/productoVenta/${productoVentaId}`);
    if (!response.ok) throw new Error("Error al obtener los ingredientes");
    return await response.json();
  } catch (error) {
    console.error("Error al obtener ingredientes:", error);
    return [];
  }
};

const obtenerLotesPorProducto = async (productoId) => {
  try {
    const response = await fetch(`${API_URL}/lotes/producto/${productoId}`);
    if (!response.ok) throw new Error("Error al obtener los lotes");
    return await response.json();
  } catch (error) {
    console.error("Error al obtener lotes:", error);
    return [];
  }
};

function VerTipoProducto() {
  const { categoriaId } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const [productos, setProductos] = useState([]);
  const [categoria, setCategoria] = useState(null);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

  useEffect(() => {
    const cargarDatos = async () => {
      try {
        const categorias = await obtenerCategorias();
        const categoriaEncontrada = categorias.find(cat => cat.id === Number(categoriaId));
        if (!categoriaEncontrada) throw new Error("Categoría no encontrada");
        
        setCategoria(categoriaEncontrada);
        const nombreCategoria = location.state?.nombreCategoria;
        const tipo = location.state?.tipo || "INVENTARIO";
        const productosCategoria = await obtenerProductos(nombreCategoria, tipo);
        
        const productosProcesados = await Promise.all(
          productosCategoria.map(async (producto) => {
            if (producto.categoria.pertenece === "VENTA") {
              const ingredientes = await obtenerIngredientes(producto.id);
              return { ...producto, ingredientes: ingredientes.length > 0 ? ingredientes : [] };
            } else {
              const lotes = await obtenerLotesPorProducto(producto.id);
              const cantidadTotal = lotes.reduce((acc, lote) => acc + lote.cantidad, 0);
              return { ...producto, lotes, cantidadTotal };
            }
          })
        );

        setProductos(productosProcesados);
      } catch (error) {
        console.error("Error cargando datos:", error);
      }
    };

    cargarDatos();
  }, [categoriaId, location.state]);

  return (
    <div
      className="home-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        height: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <div className="content">
        <div className="icon-container-right">
          <Bell size={30} className="icon" onClick={() => setShowNotifications(!showNotifications)} />
          <User size={30} className="icon" onClick={() => setShowUserOptions(!showUserOptions)} />
        </div>
        <div className="icon-container-left">
          <button className="back-button" onClick={() => navigate(-1)}>⬅ Volver</button>
        </div>
        {showNotifications && (
          <div className="notification-bubble">
            <div className="notification-header">
              <strong>Notificaciones</strong>
              <button className="close-btn" onClick={() => setShowNotifications(false)}>X</button>
            </div>
            <ul>
              <li>Notificación 1</li>
              <li>Notificación 2</li>
            </ul>
          </div>
        )}
        {showUserOptions && (
          <div className="notification-bubble user-options">
            <div className="notification-header">
              <strong>Usuario</strong>
              <button className="close-btn" onClick={() => setShowUserOptions(false)}>X</button>
            </div>
            <ul>
              <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
              <li><button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}
        <h1>{categoria?.emoticono} {categoria?.nombre}</h1>
        <div className="empleados-grid">
          {productos.length === 0 ? (
            <p>No hay productos disponibles en esta categoría.</p>
          ) : (
            productos.map((producto, index) => (
              <div
                key={index}
                className="empleado-card"
                onClick={() => navigate(`/producto/${producto.id}`, { state: { productoId: producto.id, categoria: producto.categoria?.pertenece } })}
              >
                <div className="producto-nombre">{producto.name}</div>
                {producto.categoria?.pertenece === "INVENTARIO" && <p>Cantidad total: {producto.cantidadTotal}</p>}
                {producto.categoria?.pertenece === "VENTA" && (
                  <div className="producto-atributo">
                    <p>Ingredientes:</p>
                    {producto.ingredientes?.length > 0 ? (
                      <ul>{producto.ingredientes.map((ingrediente, i) => <li key={i}>{ingrediente.productoInventario.name}</li>)}</ul>
                    ) : (
                      <p>No tiene ingredientes.</p>
                    )}
                  </div>
                )}
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

export default VerTipoProducto;
