import React, { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const API_URL = "http://localhost:8080/api";

const obtenerLotesPorProducto = async (productoId) => {
  try {
    const response = await fetch(`${API_URL}/lotes/producto/${productoId}`);
    if (!response.ok) {
      throw new Error("Error al obtener los lotes");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener lotes:", error);
    return [];
  }
};

function VerProducto() {
  const navigate = useNavigate();
  const location = useLocation();
  const [producto, setProducto] = useState(null);
  const [ingredientes, setIngredientes] = useState([]);
  const [cantidadTotal, setCantidadTotal] = useState(0); // Para almacenar la cantidad total calculada
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const { productoId, categoria } = location.state || {};

  useEffect(() => {
    const cargarProducto = async () => {
      try {
        if (!productoId || !categoria) {
          console.error("Producto o categoría no disponible.");
          return;
        }

        // Determinar el endpoint según la categoría
        const endpoint =
          categoria === "VENTA"
            ? `/productosVenta/${productoId}`
            : `/productosInventario/${productoId}`;

        // Obtener el producto por su ID y la categoría correspondiente
        const response = await fetch(`${API_URL}${endpoint}`);
        if (!response.ok) throw new Error("Error al obtener los datos del producto");

        const productoData = await response.json();
        setProducto(productoData);

        // Si el producto pertenece a "VENTA", obtener los ingredientes
        if (categoria === "VENTA") {
          const ingredientesResponse = await fetch(
            `${API_URL}/ingredientes/productoVenta/${productoId}`
          );
          if (!ingredientesResponse.ok) throw new Error("Error al obtener ingredientes");

          const ingredientesData = await ingredientesResponse.json();
          setIngredientes(ingredientesData);
        }

        // Si el producto pertenece a INVENTARIO, obtener los lotes y calcular la cantidad total
        if (categoria === "INVENTARIO") {
          const lotes = await obtenerLotesPorProducto(productoId);
          const cantidadTotal = lotes.reduce((acc, lote) => acc + lote.cantidad, 0);
          setCantidadTotal(cantidadTotal); // Asignamos la cantidad total calculada
        }
      } catch (error) {
        console.error("Error al cargar producto:", error);
      }
    };

    cargarProducto();
  }, [productoId, categoria]);

  if (!producto) {
    return <h2>Producto no encontrado</h2>;
  }

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
          <Bell size={30} className="icon" onClick={toggleNotifications} />
          <User size={30} className="icon" onClick={toggleUserOptions} />
        </div>

        {showNotifications && (
          <div className="notification-bubble">
            <div className="notification-header">
              <strong>Notificaciones</strong>
              <button className="close-btn" onClick={toggleNotifications}>
                X
              </button>
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
              <button className="close-btn" onClick={toggleUserOptions}>
                X
              </button>
            </div>
            <ul>
              <li>
                <button className="user-btn" onClick={() => navigate("/perfil")}>
                  Ver Perfil
                </button>
              </li>
              <li>
                <button className="user-btn" onClick={() => navigate("/logout")}>
                  Cerrar Sesión
                </button>
              </li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">
          ⬅ Volver
        </button>

        <div className="producto-card">
          <h1 className="producto-nombre">{producto.name}</h1>

          {/* Mostrar cantidad y stock mínimo solo si es producto de INVENTARIO */}
          {categoria === "INVENTARIO" && (
            <>
              <p className="producto-atributo">
                <strong>Cantidad total:</strong> {cantidadTotal}
              </p>
              <p className="producto-atributo">
                <strong>Stock mínimo:</strong> {producto.cantidadAviso}
              </p>
              <p className="producto-atributo">
                <strong>Precio compra:</strong> ${producto.precioCompra}
              </p>
              {cantidadTotal <= producto.cantidadAviso && (
                <p className="producto-alerta">⚠ Stock bajo</p>
              )}
            </>
          )}

          {/* Mostrar precio de venta si es producto de VENTA */}
          {categoria === "VENTA" && (
            <>
              <p className="producto-atributo">
                <strong>Precio venta:</strong> ${producto.precioVenta}
              </p>
              <div>
                <h3>Ingredientes:</h3>
                {ingredientes.length > 0 ? (
                  <ul>
                    {ingredientes.map((ingrediente, index) => (
                      <li key={index}>{ingrediente.productoInventario.name}</li>
                    ))}
                  </ul>
                ) : (
                  <p>No tiene ingredientes.</p>
                )}
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export default VerProducto;