import React, { useEffect, useState } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { Bell, User } from "lucide-react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import "../../../css/listados/styles.css";
import Notificaciones from "../../components/Notifications";

const ConfirmarPendiente = () => {
  const { carritoId } = useParams();
  const navigate = useNavigate();
  const [lineasDeCarrito, setLineasDeCarrito] = useState([]);
  const [precioTotal, setPrecioTotal] = useState(0);
  const [fechasCaducidad, setFechasCaducidad] = useState({});
  const token = localStorage.getItem("token");
  const proveedorId = localStorage.getItem("proveedorId");
  const negocioId = localStorage.getItem("negocioId");
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
    const obtenerLineasDeCarrito = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/lineasDeCarrito/carrito/${carritoId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error("Error al obtener las líneas de carrito");
        }

        const data = await response.json();
        setLineasDeCarrito(data);

        // Calcular el precio total
        let total = 0;
        const fechasIniciales = {};
        data.forEach((linea) => {
          total += linea.precioLinea; // Sumar el precio de cada línea
          fechasIniciales[linea.id] = new Date(); // Inicializar las fechas de caducidad
        });
        setPrecioTotal(total);
        setFechasCaducidad(fechasIniciales);
      } catch (error) {
        console.error("Error al obtener las líneas de carrito:", error);
        setLineasDeCarrito([]);
      }
    };

    obtenerLineasDeCarrito();
  }, [carritoId, token]);

  const confirmarRecepcion = async () => {
    try {
      const fechaHoy = new Date().toISOString().split("T")[0];
      const reabastecimiento = {
        fecha: fechaHoy,
        precioTotal: precioTotal,
        referencia: `REF-${carritoId}-${Date.now()}`,
        proveedor: { id: parseInt(proveedorId) },
        negocio: { id: parseInt(negocioId) }
      };

      // 1. Crear el reabastecimiento
      const reabResponse = await fetch("http://localhost:8080/api/reabastecimientos", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(reabastecimiento)
      });

      if (!reabResponse.ok) {
        throw new Error("Error al crear el reabastecimiento");
      }

      // Obtener el ID del reabastecimiento recién creado
      const reabData = await reabResponse.json();

      // 2. Crear los lotes
      for (const linea of lineasDeCarrito) {
        const lote = {
          cantidad: linea.cantidad,
          fechaCaducidad: fechasCaducidad[linea.id].toISOString().split("T")[0],
          producto: { id: linea.producto.id },
          reabastecimiento: { id: reabData.id }
        };

        const loteResponse = await fetch("http://localhost:8080/api/lotes", {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          },
          body: JSON.stringify(lote)
        });

        if (!loteResponse.ok) {
          throw new Error("Error al crear lote para producto " + linea.producto.name);
        }
      }

      // 3. Eliminar el carrito
      const deleteResponse = await fetch(`http://localhost:8080/api/carritos/${carritoId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      if (!deleteResponse.ok) {
        throw new Error("Error al eliminar el carrito");
      }

      alert("Recepción confirmada, lotes generados y carrito eliminado.");
      navigate(`/verCarritosPendientes/${proveedorId}`);
    } catch (error) {
      console.error("Error al confirmar recepción:", error);
      alert("Ocurrió un error al confirmar la recepción.");
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


        <button onClick={() => navigate(`/verCarritosPendientes/${proveedorId}`)} className="back-button">
          ⬅ Volver
        </button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>

        <h1 className="title">GastroStock</h1>
        <h2>Confirmar Recepción del Carrito</h2>

        <div className="productos-container">
          {lineasDeCarrito.length === 0 ? (
            <h3>No hay líneas en este carrito.</h3>
          ) : (
            <div className="productos-grid">
              {lineasDeCarrito.map((linea) => (
                <div key={linea.id} className="producto-card">
                  <h3>{linea.producto.name}</h3>
                  <p>Cantidad: {linea.cantidad}</p>
                  <p>Precio por unidad: {linea.precioLinea / linea.cantidad} €</p>
                  <p>Precio total: {linea.precioLinea} €</p>
                  <p>
                    Fecha de caducidad:
                    <DatePicker
                      selected={fechasCaducidad[linea.id]}
                      onChange={(date) =>
                        setFechasCaducidad({ ...fechasCaducidad, [linea.id]: date })
                      }
                      dateFormat="yyyy-MM-dd"
                    />
                  </p>
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="precio-total">
          <h3>Precio Total Carrito: {precioTotal.toFixed(2)} €</h3>
        </div>

        <button className="button" onClick={confirmarRecepcion}>
          Confirmar Recepción
        </button>
      </div>
    </div>
  );
};

export default ConfirmarPendiente;
