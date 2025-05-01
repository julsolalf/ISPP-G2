import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";
import Notificaciones from "../../components/Notifications";

function PantallaAñadirProducto() {
  const navigate = useNavigate();
  const [nombre, setNombre] = useState("");
  const [precioCompra, setPrecioCompra] = useState("");
  const [cantidadDeseada, setCantidadDeseada] = useState("");
  const [cantidadAviso, setCantidadAviso] = useState("");
  const [categoriaNombre, setCategoriaNombre] = useState("");
  const [categoriaId, setCategoriaId] = useState("");
  const [proveedorId, setProveedorId] = useState("");
  const [proveedores, setProveedores] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const token = localStorage.getItem("token");
  const storedNegocioId = localStorage.getItem("negocioId");

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  useEffect(() => {
    const storedCategoriaNombre = localStorage.getItem("categoriaNombre");
    setCategoriaNombre(storedCategoriaNombre);
  
    const token = localStorage.getItem("token");
  
    if (storedCategoriaNombre && storedNegocioId && token) {
      fetch(`http://localhost:8080/api/categorias/negocio/${storedNegocioId}/inventario`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }
          return response.json();
        })
        .then(data => {
          if (Array.isArray(data)) {
            const categoriaDelNegocio = data.find(
              categoria =>
                categoria.name === storedCategoriaNombre && categoria.pertenece === "INVENTARIO"
            );
            if (categoriaDelNegocio) {
              setCategoriaId(categoriaDelNegocio.id);
            } else {
              alert("No se encontró la categoría de INVENTARIO con ese nombre para este negocio.");
            }
          } else {
            alert("La respuesta de la API no es una lista de categorías");
          }
        })
        .catch(error => {
          console.error("Error obteniendo el ID de la categoría:", error);
          alert("Hubo un problema al obtener el ID de la categoría");
        });
    } else {
      alert("No se encontró la categoría, el negocioId o el token en el almacenamiento local.");
    }

    // Obtener proveedores del negocio
    if (storedNegocioId) {
      fetch(`http://localhost:8080/api/proveedores/negocio/${storedNegocioId}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      })
        .then(res => res.json())
        .then(data => {
          setProveedores(data);
        })
        .catch(error => {
          console.error("Error al obtener los proveedores:", error);
          alert("No se pudieron cargar los proveedores.");
        });
    }
  }, []);

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!nombre.trim() || !precioCompra || !cantidadDeseada || !cantidadAviso || !proveedorId) {
      alert("Todos los campos son obligatorios, incluyendo el proveedor");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/productosInventario", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          name: nombre,
          precioCompra: parseFloat(precioCompra),
          cantidadDeseada: parseInt(cantidadDeseada),
          cantidadAviso: parseInt(cantidadAviso),
          categoriaId: parseInt(categoriaId),
          proveedorId: parseInt(proveedorId),
        }),
      });

      if (!response.ok) {
        throw new Error("Error al añadir el producto");
      }

      alert("Producto añadido con éxito");
      navigate(-1);
    } catch (error) {
      console.error("Error en la solicitud:", error);
      alert("Hubo un problema al añadir el producto");
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
              <li><button className="user-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
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

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>Añadir Producto</h2>

        <form onSubmit={handleSubmit} className="form-container">
          <>Introduce los datos del producto:</>
          <input type="text" placeholder="Nombre del producto" value={nombre} onChange={(e) => setNombre(e.target.value)} required />
          <input type="number" placeholder="Precio de compra" value={precioCompra} onChange={(e) => setPrecioCompra(e.target.value)} required />
          <input type="number" placeholder="Cantidad deseada" value={cantidadDeseada} onChange={(e) => setCantidadDeseada(e.target.value)} required />
          <input type="number" placeholder="Cantidad aviso" value={cantidadAviso} onChange={(e) => setCantidadAviso(e.target.value)} required />
          
          {categoriaNombre && (
            <div>
              <strong>Categoría: </strong>
              <span>{categoriaNombre}</span>
            </div>
          )}

          <label>Selecciona un proveedor:</label>
          <select value={proveedorId} onChange={(e) => setProveedorId(e.target.value)} required>
            <option value="">-- Selecciona un proveedor --</option>
            {proveedores.map((proveedor) => (
              <option key={proveedor.id} value={proveedor.id}>
                {proveedor.name}
              </option>
            ))}
          </select>

          <input type="submit" value="Añadir" className="button" />
        </form>
      </div>
    </div>
  );
}

export default PantallaAñadirProducto;
