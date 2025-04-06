import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const obtenerProducto = async (id) => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosVenta/${localStorage.getItem("productoId")}`);
    if (!response.ok) throw new Error("Error al obtener el producto");
    return await response.json();
  } catch (error) {
    console.error("Error al obtener el producto:", error);
    return null;
  }
};

const actualizarProducto = async (id, producto) => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosVenta/${producto.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(producto),
    });
    if (!response.ok) throw new Error("Error al actualizar el producto");
    return await response.json();
  } catch (error) {
    console.error("Error al actualizar el producto:", error);
    return null;
  }
};

function EditarProductoCarta() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [producto, setProducto] = useState({ name: "", precioVenta: "" });
  const [showModal, setShowModal] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  const handleLogout = () => {
    localStorage.removeItem("userToken"); // Eliminamos el token del usuario
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  useEffect(() => {
    const cargarProducto = async () => {
      const data = await obtenerProducto(id);
      if (data) setProducto(data);
    };
    cargarProducto();
  }, [id]);

  const handleChange = (e) => {
    setProducto({ ...producto, [e.target.name]: e.target.value });
  };

  const handleConfirmSave = async () => {
    const actualizado = await actualizarProducto(id, producto);
    if (actualizado) navigate(`/categoriaVenta/${producto.categoria?.name}/producto/${id}`);
    setShowModal(false);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setShowModal(true); // Mostrar el modal en lugar de guardar directamente
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
          <Bell size={30} className="icon" onClick={toggleNotifications} />
          <User size={30} className="icon" onClick={toggleUserOptions} />
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
              <li>Notificación 3</li>
            </ul>
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
        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>        
        <h1 className="title">GastroStock</h1>
        <h2>Editar Producto</h2>

        <form className="form-container" onSubmit={handleSubmit}>
          <input type="text" name="name" value={producto.name} onChange={handleChange} placeholder="Nombre" required />
          <input type="number" name="precioVenta" value={producto.precioVenta} onChange={handleChange} placeholder="Precio de Venta" required />
          <button type="submit" className="button">💾 Guardar</button>
        </form>

        {/* Modal de Confirmación para Logout */}
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

        {/* Modal de Confirmación */}
        {showModal && (
          <div className="modal-overlay">
            <div className="modal">
              <h3>¿Deseas guardar los cambios?</h3>
              <div className="modal-buttons">
                <button onClick={handleConfirmSave} className="button confirm">✅ Confirmar</button>
                <button onClick={() => setShowModal(false)} className="button cancel">❌ Cancelar</button>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Estilos inline para el modal */}
      <style>{`
        .modal-overlay {
          position: fixed;
          top: 0; left: 0; right: 0; bottom: 0;
          background: rgba(0,0,0,0.6);
          display: flex;
          justify-content: center;
          align-items: center;
          z-index: 999;
        }
        .modal {
          background: white;
          padding: 2rem;
          border-radius: 12px;
          box-shadow: 0 5px 15px rgba(0,0,0,0.3);
          text-align: center;
        }
        .modal-buttons {
          display: flex;
          justify-content: space-around;
          margin-top: 1rem;
        }
        .button.confirm {
          background-color: #28a745;
          color: white;
        }
        .button.cancel {
          background-color: #dc3545;
          color: white;
        }
      `}</style>
    </div>
  );
}

export default EditarProductoCarta;
