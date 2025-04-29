import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const VerEntregasProveedor = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const proveedorId = localStorage.getItem("proveedorId");
  const [carritos, setCarritos] = useState([]);

  useEffect(() => {
    const obtenerCarritos = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/carritos/proveedor/${proveedorId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (!response.ok) {
          throw new Error("Error al obtener los carritos del proveedor");
        }

        const data = await response.json();
        setCarritos(data);
      } catch (error) {
        console.error("Error al obtener los carritos:", error);
        setCarritos([]);
      }
    };

    obtenerCarritos();
  }, [proveedorId]);

  const handleClick = (carritoId) => {
    navigate(`/confirmarPendiente/${carritoId}`);
  };

  return (
    <div
      className="home-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        minHeight: "100vh",
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

        <button onClick={() => navigate(`/verProveedor/${proveedorId}`)} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>Entregas del Proveedor</h2>

        <div className="productos-container">
          {carritos.length === 0 ? (
            <h3>No hay entregas registradas para este proveedor</h3>
          ) : (
            <div className="productos-grid">
              {carritos.map((carrito) => (
                <div
                  key={carrito.id}
                  className="producto-card clickable"
                  onClick={() => handleClick(carrito.id)}
                  style={{ cursor: "pointer" }}
                >
                  <h3>Carrito #{carrito.id}</h3>
                  <p>Fecha de entrega: {carrito.diaEntrega}</p>
                  <p>Precio total: {carrito.precioTotal.toFixed(2)} €</p>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default VerEntregasProveedor;
