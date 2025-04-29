import React, { useEffect, useState } from "react";
import { useNavigate, Link, useParams } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";
import Notificaciones from "../../components/Notifications";

const token = localStorage.getItem("token");
const negocioId = localStorage.getItem("negocioId");


const obtenerProveedor = async (id) => {
  try {
    const response = await fetch(`http://localhost:8080/api/proveedores/${id}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      throw new Error("Error al obtener el proveedor");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener el proveedor:", error);
    return null;
  }
};

const actualizarProveedor = async (id, proveedor) => {
  try {
    const response = await fetch(`http://localhost:8080/api/proveedores/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(proveedor),
    });

    if (!response.ok) {
      const errorData = await response.json(); // Obtener detalles de la respuesta de error
      throw new Error(errorData.message || "Error al actualizar el proveedor");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al actualizar el proveedor:", error.message);
    return null;
  }
};

function EditarProveedor() {
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  
  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const [showLogoutModal, setShowLogoutModal] = useState(false); 

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  const navigate = useNavigate();
  const { id } = useParams(); // Usamos useParams para obtener el ID de la URL
  const [proveedor, setProveedor] = useState({
    name: "",
    direccion: "",
    email: "",
    telefono: "",
    negocioId: negocioId,
  });
  const [loading, setLoading] = useState(true); // Estado de carga

  // Este useEffect se ejecutará cada vez que cambie el ID
  useEffect(() => {
    const cargarProveedor = async () => {
      setLoading(true); // Marcamos como cargando
      const data = await obtenerProveedor(id);
      if (data) {
        setProveedor(data); // Actualizamos el estado con los datos del nuevo proveedor
      }
      setLoading(false); // Terminamos la carga
    };
    cargarProveedor();
  }, [id]); // Dependencia añadida para que recargue los datos cada vez que cambie el ID

  const handleChange = (e) => {
    setProveedor({ ...proveedor, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const proveedorActualizado = {
      ...proveedor,
      negocioId: negocioId,
    };
    const actualizado = await actualizarProveedor(id, proveedorActualizado);
    if (actualizado) navigate(`/verProveedor/${id}`);
  };

  if (loading) {
    return <div>Cargando...</div>;
  }

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
              <li><button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">
          ⬅ Volver
        </button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>Editar Proveedor</h2>
        <form className="form-container" onSubmit={handleSubmit}>
          <input
            type="text"
            name="name"
            value={proveedor.name}
            onChange={handleChange}
            placeholder="Nombre"
            required
          />
          <input
            type="text"
            name="direccion"
            value={proveedor.direccion}
            onChange={handleChange}
            placeholder="Dirección"
            required
          />
          <input
            type="email"
            name="email"
            value={proveedor.email}
            onChange={handleChange}
            placeholder="Correo Electrónico"
            required
          />
          <input
            type="tel"
            name="telefono"
            value={proveedor.telefono}
            onChange={handleChange}
            placeholder="Teléfono"
            required
          />
          <button type="submit" className="button">
            💾 Guardar
          </button>
        </form>

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
      </div>
    </div>
  );
}

export default EditarProveedor;
