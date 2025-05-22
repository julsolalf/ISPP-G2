import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";
import Notificaciones from "../../components/Notifications";

const VerCarritoProveedor = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const proveedorId = localStorage.getItem("proveedorId");
  const [productos, setProductos] = useState([]);
  const [carrito, setCarrito] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  useEffect(() => {
    const obtenerProductosPorProveedor = async () => {
      try {
        const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/productosInventario/proveedor/${proveedorId}`, {
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

    obtenerProductosPorProveedor();
  }, [proveedorId]);

  const handleCantidadChange = (productoId, cantidad) => {
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

  // Función para calcular el próximo día de reparto basado en los días de reparto disponibles
const getProximoDiaReparto = (diasReparto) => {
  const dias = ["SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];
  const hoy = new Date();
  const diaHoy = hoy.getDay(); // Día actual (0-6, donde 0 es domingo)
  
  // Convertimos los días de reparto a números (de acuerdo con el array dias[]).
  const diasDisponibles = diasReparto
    .map(dia => dias.indexOf(dia.diaSemana))
    .filter(dia => dia >= diaHoy); // Filtramos días que no son ya pasados.

  // Si no hay días futuros, agregamos los días de la semana pasada
  if (diasDisponibles.length === 0) {
    diasDisponibles.push(...diasReparto.map(dia => dias.indexOf(dia.diaSemana)));
  }

  const proximoDia = diasDisponibles[0]; // Seleccionamos el primer día disponible
  const proximaFecha = new Date(hoy);
  proximaFecha.setDate(hoy.getDate() + (proximoDia - diaHoy + 7) % 7); // Calculamos la fecha del próximo día de reparto

  return proximaFecha.toISOString().split('T')[0]; // Devolvemos la fecha en formato YYYY-MM-DD
};

  const hacerPedido = async () => {
    // Filtrar productos seleccionados
    const productosSeleccionados = carrito.filter(item => item.cantidad > 0);
    if (productosSeleccionados.length === 0) {
      alert("Debes seleccionar al menos un producto.");
      return;
    }
  
    // Calcular precio total
    let precioTotal = 0;
    productosSeleccionados.forEach(item => {
      const producto = productos.find(p => p.id === item.id);
      if (producto) {
        precioTotal += item.cantidad * producto.precioCompra;
      }
    });
  
    // Obtener el próximo día de reparto
    const responseReparto = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/diasReparto/proveedor/${proveedorId}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });
  
    if (!responseReparto.ok) {
      alert("Error al obtener los días de reparto.");
      return;
    }
  
    const diasReparto = await responseReparto.json();
    let proximoReparto = getProximoDiaReparto(diasReparto); // Función que calcula el próximo día
  
    // Crear el carrito
    const nuevoCarrito = {
      precioTotal,
      proveedor: { id: parseInt(proveedorId) },
      diaEntrega: proximoReparto,
      productos: productosSeleccionados
    };
  
    try {
      // Enviar solicitud para crear el carrito
      const carritoRes = await fetch("https://ispp-2425-g2.ew.r.appspot.com/api/carritos", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(nuevoCarrito),
      });
  
      if (!carritoRes.ok) {
        throw new Error("Error al crear el carrito");
      }
  
      const carritoCreado = await carritoRes.json();
  
      // Crear las líneas de carrito
      for (const item of productosSeleccionados) {
        const producto = productos.find((p) => p.id === item.id);
        const precioLinea = producto.precioCompra * item.cantidad;
  
        const linea = {
          cantidad: item.cantidad,
          precioLinea,
          carrito: { id: carritoCreado.id },
          producto: { id: item.id },
        };
  
        const lineaRes = await fetch("https://ispp-2425-g2.ew.r.appspot.com/api/lineasDeCarrito", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify(linea),
        });
  
        if (!lineaRes.ok) {
          throw new Error("Error al guardar una línea de carrito");
        }
      }
  
      alert("Pedido realizado correctamente");
      navigate(`/verProveedor/${proveedorId}`);
            
    } catch (error) {
      console.error("Error al hacer el pedido:", error);
      alert("Ocurrió un error al procesar el pedido");
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
          <Bell size={30} className="icon" onClick={toggleNotifications} />
          <User size={30} className="icon" onClick={toggleUserOptions} />
        </div>  

        {showNotifications && (
                  <div className="icon-container-right">
                  <Notificaciones />
                  <User size={30} className="icon" onClick={toggleUserOptions} />
                </div>
                )}

        {showUserOptions && (
          <div className="notification-bubble user-options">
            <div className="notification-header">
              <strong>Usuario</strong>
              <button className="close-btn" onClick={toggleUserOptions}>X</button>
            </div>
            <ul>
              <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
              <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
              <button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button>
            </ul>
          </div>
        )}

        {showLogoutModal && (
          <div className="modal-overlay">
            <div className="modal">
              <h3>¿Está seguro que desea abandonar la sesión?</h3>
              <div className="modal-buttons">
                <button className="confirm-btn" onClick={handleLogout}>Sí</button>
                <button className="cancel-btn" onClick={() => setShowLogoutModal(false)}>No</button>
              </div>
            </div>
          </div>
        )}

        <button onClick={() => navigate(`/verProveedor/${proveedorId}`)} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>Carrito - Proveedor</h2>

        <div className="productos-container">
          {productos.length === 0 ? (
            <h3>No hay productos para este proveedor</h3>
          ) : (
            <div className="productos-grid">
              {productos.map((producto) => (
                <div key={producto.id} className="producto-card">
                  <h3>{producto.name}</h3>
                  <p>Precio: {producto.precioCompra.toFixed(2)} €</p>
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
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        <button className="button" onClick={hacerPedido}>
          Hacer pedido
        </button>
      </div>
    </div>
  );
};

export default VerCarritoProveedor;
