// Importaciones existentes...
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
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

  // Cargar categoría
  useEffect(() => {
    const storedCategoriaNombre = localStorage.getItem("categoriaNombre");
    setCategoriaNombre(storedCategoriaNombre);

    if (storedCategoriaNombre) {
      fetch(`http://localhost:8080/api/categorias/nombre/${storedCategoriaNombre}`)
        .then(res => res.json())
        .then(data => {
          const categoriaDelNegocio = data.find(c => c.negocio.id === negocioId);
          if (categoriaDelNegocio) setCategoriaId(categoriaDelNegocio.id);
          else alert("No se encontró la categoría para este negocio");
        });
    }
  }, []);

  // Cargar productos de inventario
  useEffect(() => {
    fetch("http://localhost:8080/api/productosInventario")
      .then(res => res.json())
      .then(data => setProductosInventario(data))
      .catch(err => console.error("Error cargando productos inventario:", err));
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
    if (!nombre.trim() || !precioVenta || !categoriaId) {
      alert("Todos los campos son obligatorios");
      return;
    }

    try {
      // Crear producto de venta
      const productoResponse = await fetch("http://localhost:8080/api/productosVenta", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          name: nombre,
          precioVenta: parseFloat(precioVenta),
          categoria: { id: categoriaId }
        }),
      });

      if (!productoResponse.ok) throw new Error("Error al crear el producto de venta");

      const productoCreado = await productoResponse.json();

      // Crear ingredientes vinculados
      for (const ingrediente of ingredientes) {
        if (ingrediente.productoInventarioId && ingrediente.cantidad) {
          await fetch("http://localhost:8080/api/ingredientes", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              cantidad: parseInt(ingrediente.cantidad),
              productoInventario: { id: ingrediente.productoInventarioId },
              productoVenta: { id: productoCreado.id }
            }),
          });
        }
      }

      alert("Producto añadido con éxito");
      navigate(-1);
    } catch (error) {
      console.error("Error al añadir el producto y sus ingredientes:", error);
      alert("Hubo un problema al añadir el producto");
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
        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
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
