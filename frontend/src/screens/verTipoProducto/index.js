import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";
import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";

function VerTipoProducto() {
  const { categoriaId } = useParams();
  const navigate = useNavigate();
  const [showLogoutModal, setShowLogoutModal] = useState(false); 
  const token = localStorage.getItem("token"); 
  const categoria = localStorage.getItem("categoriaNombre");
  const [productos, setProductos] = useState([]);
  const [filtro, setFiltro] = useState(""); 
  const [ordenAscendente, setOrdenAscendente] = useState(true); 
  const [ordenPorCantidad, setOrdenPorCantidad] = useState(false); 

  const obtenerProductosPorCategoria = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/productosInventario/categoria/${categoria}`, {
        headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
        }
      });
  
      if (!response.ok) {
        throw new Error("Error al obtener los productos de la categoría");
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error("Error al obtener los productos:", error);
      return [];
    }
  };

  useEffect(() => {
    const cargarProductos = async () => {
      const productosCategoria = await obtenerProductosPorCategoria();
      setProductos(productosCategoria);
    };
    cargarProductos();
  }, [categoriaId]);

  const productosFiltrados = productos
    .filter((producto) =>
      producto.name.toLowerCase().includes(filtro.toLowerCase())
    )
    .sort((a, b) => {
      if (ordenPorCantidad) {
        return ordenAscendente
          ? a.cantidadDeseada - b.cantidadDeseada
          : b.cantidadDeseada - a.cantidadDeseada;
      } else {
        return ordenAscendente
          ? a.name.localeCompare(b.name)
          : b.name.localeCompare(a.name);
      }
    });

  const exportarProductosPDF = () => {
    const doc = new jsPDF();
    doc.text(`Productos en la categoría: ${categoria}`, 14, 10);
    autoTable(doc, {
      startY: 20,
      head: [["Nombre","Precio de compra", "Cantidad Deseada", "Cantidad de Aviso"]],
      body: productos.map((producto) => [
        producto.name,
        producto.precioCompra,
        producto.cantidadDeseada,
        producto.cantidadAviso,
      ]),
    });
    doc.save(`productos_${categoria}.pdf`);
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate("/"); // Redirigir a la pantalla de inicio de sesión
  };

  return (
      <div className="content">
        <div className="icon-container-right">
          <Bell size={30} className="icon" />
          <User size={30} className="icon" />
        </div>

        <button onClick={() => navigate("/inventario")} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>Productos</h2>
        
        <div className="button-container3">
          <button className="button" onClick={() => navigate("/anadirProductoInventario")}>➕ Añadir</button>
          <button className="button" onClick={exportarProductosPDF}>📥 Exportar</button>
          
          <div className="filter-container">
            <button className="filter-btn">🔍 Filtrar</button>
            <div className="filter-options">
              <input
                type="text"
                placeholder="Filtrar por nombre"
                value={filtro}
                onChange={(e) => setFiltro(e.target.value)}
                className="filter-input"
              />
              <div className="sort-options">
                <button onClick={() => {
                    setOrdenPorCantidad(false);
                    setOrdenAscendente(true);
                  }} 
                  className="sort-btn">
                  🔼Nombre Ascendente
                </button>
                <button onClick={() => {
                    setOrdenPorCantidad(false);
                    setOrdenAscendente(false);
                  }} 
                  className="sort-btn">
                  🔽Nombre Descendente
                </button>
                <button onClick={() => {
                    setOrdenPorCantidad(true);
                    setOrdenAscendente(true);
                  }} 
                  className="sort-btn">
                  🔼Cantidad Ascendente
                </button>
                <button onClick={() => {
                    setOrdenPorCantidad(true);
                    setOrdenAscendente(false);
                  }} 
                  className="sort-btn">
                  🔽Cantidad Descendente
                </button>
              </div>
            </div>
          </div>
        </div>

        {productosFiltrados.length === 0 ? (
          <h3>No hay productos en esta categoría</h3>
        ) : (
          <div className="empleados-grid">
            {productosFiltrados.map((producto) => (
              <div key={producto.id} className="empleado-card" 
                onClick={() => {
                  localStorage.setItem("productoId", producto.id);
                  navigate(`/categoria/${localStorage.getItem("categoriaNombre")}/producto/${producto.id}`);
                }}
                style={{ cursor: "pointer" }}>
                <h3>{producto.name}</h3>
                <p>Cantidad: {producto.cantidadDeseada}</p>
                {producto.cantidadDeseada <= producto.cantidadAviso && (
                  <p style={{ color: "red" }}>⚠ Stock bajo</p>
                )}
              </div>
            ))}
          </div>
        )}
        
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
  );
}

export default VerTipoProducto;
