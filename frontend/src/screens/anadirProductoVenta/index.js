import React, { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

function PantallaAñadirProductoCarta() {
  const navigate = useNavigate();
  const [nombre, setNombre] = useState("");
  const [precioVenta, setPrecioVenta] = useState("");
  const [categoriaNombre, setCategoriaNombre] = useState("");
  const [categoriaId, setCategoriaId] = useState("");
  const [productosInventario, setProductosInventario] = useState([]);
  const [ingredientes, setIngredientes] = useState([{ productoInventarioId: "", cantidad: "" }]);
  const negocioId = 1;
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false); // Estado para la modal de logout
  const storedNegocioId = localStorage.getItem("negocioId");
  const storedCategoriaId = localStorage.getItem("id");

  // Cargar categoría
  useEffect(() => {
    const storedCategoriaNombre = localStorage.getItem("categoriaNombre");
    setCategoriaNombre(storedCategoriaNombre);
  
    const token = localStorage.getItem("token");
  
    if (storedCategoriaNombre && storedNegocioId && token) {
      fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/categorias/negocio/${storedNegocioId}/venta`, {
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
            // Filtramos por nombre y por pertenencia explícita a VENTA
            const categoriaDelNegocio = data.find(
              categoria =>
                categoria.name === storedCategoriaNombre && categoria.pertenece === "VENTA"
            );
            if (categoriaDelNegocio) {
              setCategoriaId(categoriaDelNegocio.id);
            } else {
              alert("No se encontró la categoría de VENTA con ese nombre para este negocio.");
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
      alert("No se encontró la categoría o el negocioId o el token en el almacenamiento local.");
    }
  }, []);
  

  // Cargar productos de inventario
  useEffect(() => {
    // Cargar los productos del inventario
    const cargarProductosInventario = async () => {
      try {
        const response = await fetch("https://ispp-2425-g2.ew.r.appspot.com/api/productosInventario", {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        });
  
        if (!response.ok) {
          throw new Error(`Error HTTP: ${response.status}`);
        }
  
        const data = await response.json();
        setProductosInventario(data);
      } catch (err) {
        console.error("Error cargando productos inventario:", err);
        alert("Hubo un problema al cargar los productos del inventario.");
      }
    };
  
    cargarProductosInventario();
  }, []);

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

  const handleIngredienteChange = (index, field, value) => {
    const newIngredientes = [...ingredientes];
    newIngredientes[index][field] = value;
    setIngredientes(newIngredientes);
  };

  const addIngredienteField = () => {
    setIngredientes([...ingredientes, { productoInventarioId: "", cantidad: "" }]);
  };

  const removeIngredienteField = (index) => {
    const newIngredientes = ingredientes.filter((_, i) => i !== index);
    setIngredientes(newIngredientes);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
  
    try {
      // Validación de campos requeridos
      if (!nombre || nombre.trim() === "") {
        throw new Error("El nombre del producto no puede estar vacío.");
      }
  
      if (!precioVenta || isNaN(precioVenta) || precioVenta <= 0) {
        throw new Error("El precio de venta debe ser un número positivo.");
      }
  
      if (!categoriaId) {
        throw new Error("La categoría no puede ser nula o vacía.");
      }
  
      // Validar ingredientes
      if (ingredientes.some(ing => !ing.productoInventarioId || !ing.cantidad)) {
        throw new Error("Todos los ingredientes deben tener producto y cantidad.");
      }
  
      // Verificar que todos los ingredientes tengan cantidades válidas
      ingredientes.forEach(ingrediente => {
        if (isNaN(ingrediente.cantidad) || ingrediente.cantidad <= 0) {
          throw new Error("La cantidad de los ingredientes debe ser un número positivo.");
        }
      });
  
      const token = localStorage.getItem("token");
      if (!token) {
        throw new Error("Token de autenticación no encontrado.");
      }
  
      // Crear producto de venta
      const productoResponse = await fetch("https://ispp-2425-g2.ew.r.appspot.com/api/productosVenta", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          name: nombre.trim(),
          precioVenta: parseFloat(precioVenta),
          categoriaId: parseInt(categoriaId),
        }),
      });
  
      if (!productoResponse.ok) {
        const errorText = await productoResponse.text();
        throw new Error(`Error al crear el producto de venta: ${errorText}`);
      }
  
      const productoCreado = await productoResponse.json();
  
      // Crear ingredientes vinculados
      for (const ingrediente of ingredientes) {
        if (ingrediente.productoInventarioId && ingrediente.cantidad) {
          const ingredienteResponse = await fetch("https://ispp-2425-g2.ew.r.appspot.com/api/ingredientes", {
            method: "POST",
            headers: { 
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify({
              cantidad: parseInt(ingrediente.cantidad),
              productoInventario: { id: ingrediente.productoInventarioId },
              productoVenta: { id: productoCreado.id }
            }),
          });
  
          if (!ingredienteResponse.ok) {
            const errorText = await ingredienteResponse.text();
            console.error("Error al crear ingrediente:", errorText);
            throw new Error("Hubo un problema al añadir un ingrediente.");
          }
        }
      }
  
      alert("Producto añadido con éxito");
      navigate(-1);
  
    } catch (error) {
      console.error("Error al añadir el producto y sus ingredientes:", error);
      alert(error.message || "Hubo un problema al añadir el producto.");
    }
  };
  
  return (
    <div className="home-container" style={{ backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`, backgroundSize: "cover", backgroundPosition: "center", height: "100vh", display: "flex", alignItems: "center", justifyContent: "center", textAlign: "center" }}>
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
        <button onClick={() => navigate("/cartaDueno")} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>        
        <h1 className="title">GastroStock</h1>
        <h2>Añadir Producto</h2>

        <form onSubmit={handleSubmit} className="form-container">
          <>Introduce los datos del producto:</>
          <input type="text" placeholder="Nombre del producto" value={nombre} onChange={(e) => setNombre(e.target.value)} required />
          <input type="number" placeholder="Precio de venta" value={precioVenta} onChange={(e) => setPrecioVenta(e.target.value)} required />
          {categoriaNombre && <div><strong>Categoría: </strong><span>{categoriaNombre}</span></div>}

          <hr />
          <h3>Ingredientes</h3>
          {ingredientes.map((ingrediente, index) => (
            <div key={index} style={{ marginBottom: "10px" }}>
              <select
                value={ingrediente.productoInventarioId}
                onChange={(e) => handleIngredienteChange(index, "productoInventarioId", e.target.value)}
                required
              >
                <option value="">Selecciona producto de inventario</option>
                {productosInventario.map((producto) => (
                  <option key={producto.id} value={producto.id}>
                    {producto.name}
                  </option>
                ))}
              </select>
              <input
                type="number"
                placeholder="Cantidad"
                value={ingrediente.cantidad}
                onChange={(e) => handleIngredienteChange(index, "cantidad", e.target.value)}
                required
              />
              <button type="button" onClick={() => removeIngredienteField(index)}>Eliminar</button>
            </div>
          ))}
          <button type="button" onClick={addIngredienteField}>+ Añadir Ingrediente</button>

          <input type="submit" value="Añadir Producto" className="button" />
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
      </div>
    </div>
  );
}

export default PantallaAñadirProductoCarta;