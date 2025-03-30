import React, { useEffect, useState } from "react";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const obtenerCategorias = async () => {
  try {
    const response = await fetch("http://localhost:8080/api/categorias");
    if (!response.ok) {
      throw new Error("Error al obtener las categorías");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener categorías:", error);
    return [];
  }
};

const obtenerProductos = async (nombreCategoria, tipo) => {
  try {
    let url =
      tipo === "VENTA"
        ? `http://localhost:8080/api/productosVenta/categoriaVenta/${nombreCategoria}`
        : `http://localhost:8080/api/productosInventario/categoria/${nombreCategoria}`;

    const response = await fetch(url);
    if (!response.ok) {
      throw new Error("Error al obtener productos");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener productos:", error);
    return [];
  }
};

const obtenerIngredientes = async (productoVentaId) => {
  try {
    const response = await fetch(
      `http://localhost:8080/api/ingredientes/productoVenta/${productoVentaId}`
    );
    if (!response.ok) {
      throw new Error("Error al obtener los ingredientes");
    }

    const ingredientes = await response.json();

    console.log(`Ingredientes para producto ${productoVentaId}:`, ingredientes);

    return ingredientes;
  } catch (error) {
    console.error("Error al obtener ingredientes:", error);
    return [];
  }
};

const obtenerLotesPorProducto = async (productoId) => {
  try {
    const response = await fetch(`http://localhost:8080/api/lotes/producto/${productoId}`);
    if (!response.ok) {
      throw new Error("Error al obtener los lotes");
    }
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

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  useEffect(() => {
    const cargarDatos = async () => {
      try {
        const categorias = await obtenerCategorias();
        const categoriaEncontrada = categorias.find(
          (cat) => cat.id === Number(categoriaId)
        );
  
        if (!categoriaEncontrada) {
          console.error("Categoría no encontrada");
          return;
        }
  
        setCategoria(categoriaEncontrada);
        console.log("Categoría encontrada:", categoriaEncontrada);
  
        const nombreCategoria = location.state?.nombreCategoria;
        if (!nombreCategoria) {
          console.error("El nombre de la categoría no está disponible.");
          return;
        }
  
        const tipo = location.state?.tipo || "INVENTARIO";
        const productosCategoria = await obtenerProductos(nombreCategoria, tipo);
  
        console.log("Productos de la categoría:", productosCategoria);
  
        const productosConIngredientesYLotes = await Promise.all(
          productosCategoria.map(async (producto) => {
            if (producto.categoria.pertenece === "VENTA") {
              const ingredientes = await obtenerIngredientes(producto.id);
              console.log(`Producto: ${producto.name}, Ingredientes:`, ingredientes);
              return { ...producto, ingredientes: ingredientes.length > 0 ? ingredientes : [] };
            } else {
              // Si es producto de INVENTARIO, obtener los lotes y calcular la cantidad
              const lotes = await obtenerLotesPorProducto(producto.id);
              const cantidadTotal = lotes.reduce((acc, lote) => acc + lote.cantidad, 0);
              return { ...producto, lotes, cantidadTotal };
            }
          })
        );
  
        setProductos(productosConIngredientesYLotes);
        console.log("Productos con ingredientes y lotes:", productosConIngredientesYLotes);
      } catch (error) {
        console.error("Error cargando datos:", error);
      }
    };
  
    cargarDatos();
  }, [categoriaId, location.state?.nombreCategoria, location.state?.tipo]);

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
          <Bell size={30} className="icon" onClick={toggleNotifications} />
          <User size={30} className="icon" onClick={toggleUserOptions} />
        </div>

        <div className="icon-container-left">
          <button className="back-button" onClick={() => navigate(-1)}>
          ⬅  Volver
          </button>
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
              <li>Notificación 3</li>
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
                <button className="user-btn" onClick={() => navigate("/planes")}>
                  Ver planes
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


        <div className="empleados-grid">
          {productos.length === 0 ? (
            <p>No hay productos disponibles en esta categoría.</p>
          ) : (
            productos.map((producto, index) => (
              <div
                key={index}
                className="empleado-card"
                onClick={() =>
                  navigate(`/producto/${producto.id}`, {
                    state: {
                      productoId: producto.id,
                      categoria: producto.categoria?.pertenece, 
                    },
                  })
                }
                style={{ cursor: "pointer" }}
              >
                <div className="producto-nombre">{producto.name}</div>
               
                {producto.categoria?.pertenece === "INVENTARIO" && (
                  <div className="producto-atributo">
                    <p>Cantidad total: {producto.cantidadTotal}</p>
                  </div>
                )}

                {producto.categoria?.pertenece === "VENTA" && 
                  <div className="producto-atributo">
                    <p>Ingredientes:</p>
                    {producto.ingredientes && producto.ingredientes.length > 0 ? (
                     <ul>
                        {producto.ingredientes.map((ingrediente, i) => (
                          <li key={i}>{ingrediente.productoInventario.name}</li>
                        ))}
                      </ul>
                    ) : (
                      <p>No tiene ingredientes.</p>
                    )}
                  </div>
                }
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

export default VerTipoProducto;