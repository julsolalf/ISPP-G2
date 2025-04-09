import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const token = localStorage.getItem("token");
const productoId = localStorage.getItem("productoId");

const obtenerProducto = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosVenta/${productoId}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });
    if (!response.ok) {
      throw new Error("Error al obtener el producto");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener el producto:", error);
    return null;
  }
};

const actualizarProducto = async (producto) => {
  try {
    const response = await fetch(`http://localhost:8080/api/productosVenta/${productoId}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify(producto),
    });
    if (!response.ok) {
      throw new Error("Error al actualizar el producto");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al actualizar el producto:", error);
    return null;
  }
};

const obtenerIngredientes = async (productoVentaId) => {
  try {
    const token = localStorage.getItem("token");
    if (!token) {
      console.error("No se encontró el token de autenticación.");
      return;
    }

    const response = await fetch(`http://localhost:8080/api/ingredientes/productoVenta/${productoVentaId}`, {
      method: "GET", 
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    console.log("Estado de la respuesta:", response.status);

    if (response.status === 403) {
      throw new Error("No tienes permisos para acceder a este recurso.");
    }

    if (!response.ok) {
      throw new Error(`Error HTTP: ${response.status}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Error al obtener los ingredientes", error.message);
    return null;
  }
};


const agregarIngrediente = async (productoVentaId, productoInventarioId, cantidad) => {
  try {
    const token = localStorage.getItem("token");

    if (!token) {
      throw new Error("No se encontró el token de autorización.");
    }

    const response = await fetch("http://localhost:8080/api/ingredientes", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`, // Asegúrate de que el token esté incluido en los encabezados
      },
      body: JSON.stringify({
        productoVenta: { id: productoVentaId },
        productoInventario: { id: productoInventarioId },
        cantidad: cantidad,
      }),
    });

    if (!response.ok) {
      throw new Error(`Error HTTP: ${response.status} - ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error("Error al agregar ingrediente:", error);
    alert("Hubo un problema al agregar el ingrediente. Verifica tus permisos o el token.");
    return null;
  }
};


const eliminarIngrediente = async (ingredienteId) => {
  try {
    const token = localStorage.getItem("token"); // Obtener el token de localStorage

    if (!token) {
      throw new Error("No se encontró el token de autorización.");
    }

    const response = await fetch(`http://localhost:8080/api/ingredientes/${ingredienteId}`, {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`, // Asegúrate de que el token esté incluido en los encabezados
      },
    });

    if (!response.ok) {
      throw new Error(`Error HTTP: ${response.status} - ${response.statusText}`);
    }

    // Verificamos si la respuesta tiene contenido antes de intentar parsearla
    if (response.status === 204) {
      // Si el estado es 204 (No Content), la eliminación fue exitosa, pero no hay respuesta de datos.
      console.log("Ingrediente eliminado correctamente, pero sin respuesta de contenido.");
      return { success: true }; // Devolvemos un objeto de éxito
    }

    // Si la respuesta tiene contenido, lo procesamos como JSON
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error al eliminar ingrediente:", error);
    alert("Hubo un problema al eliminar el ingrediente. Verifica tus permisos o el token.");
    return null;
  }
};




function EditarProductoCarta() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [producto, setProducto] = useState({ name: "", precioVenta: "", categoria: { id: "" }, ingredientes: [], });
  const [showModal, setShowModal] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout
  const [ingredientes, setIngredientes] = useState([]);
  const [nuevoIngredienteId, setNuevoIngredienteId] = useState("");
  const [nuevaCantidad, setNuevaCantidad] = useState("");
  const [productosInventario, setProductosInventario] = useState([]);

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
    const cargarIngredientes = async () => {
      try {
        const data = await obtenerIngredientes(id);
        if (data) {
          setIngredientes(data);
        } else {
          console.warn("No se pudieron cargar los ingredientes.");
          setIngredientes([]);
        }
      } catch (error) {
        console.error("Error al cargar los ingredientes:", error.message);
        setIngredientes([]); // Reseteamos la lista de ingredientes si hay error
      }
    };

    const cargarInventario = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/productosInventario", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
    
        console.log("Estado de la respuesta:", res.status);
    
        if (res.status === 403) {
          throw new Error("No tienes permisos para acceder a este recurso.");
        }
    
        if (!res.ok) {
          throw new Error(`Error HTTP: ${res.status}`);
        }
    
        const data = await res.json();
        setProductosInventario(data);
      } catch (error) {
        console.error("Error cargando inventario:", error.message);
        alert("Hubo un problema al cargar los productos del inventario.");
        setProductosInventario([]);
      }
    };
    
    
    

    cargarProducto();
    cargarIngredientes();
    cargarInventario();
  }, [id]);

  const handleAgregarIngrediente = async () => {
    if (!nuevoIngredienteId || !nuevaCantidad) return alert("Rellena ambos campos");

    await agregarIngrediente(id, nuevoIngredienteId, parseFloat(nuevaCantidad));
    const actualizados = await obtenerIngredientes(id);
    setIngredientes(actualizados);
    setNuevoIngredienteId("");
    setNuevaCantidad("");
  };

  const handleEliminarIngrediente = async (ingredienteId) => {
    await eliminarIngrediente(ingredienteId);
    setIngredientes(await obtenerIngredientes(id));
  };

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

          <h3>Ingredientes</h3>
          <ul style={{ listStyle: "none", padding: 0 }}>
            {ingredientes.map((ing) => (
              <li key={ing.id}>
                🧂 {ing.productoInventario.name} - {ing.cantidad}
                <button type="button" onClick={() => handleEliminarIngrediente(ing.id)} style={{ marginLeft: "10px" }}>❌</button>
              </li>
            ))}
          </ul>

          <div style={{ marginTop: "1rem" }}>
            <select value={nuevoIngredienteId} onChange={(e) => setNuevoIngredienteId(e.target.value)}>
              <option value="">Selecciona un ingrediente</option>
              {productosInventario.map((pi) => (
                <option key={pi.id} value={pi.id}>{pi.name}</option>
              ))}
            </select>
            <input
              type="number"
              placeholder="Cantidad"
              value={nuevaCantidad}
              onChange={(e) => setNuevaCantidad(e.target.value)}
              style={{ marginLeft: "10px" }}
            />
            <button type="button" onClick={handleAgregarIngrediente} style={{ marginLeft: "10px" }}>
              ➕ Añadir
            </button>
          </div>

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
