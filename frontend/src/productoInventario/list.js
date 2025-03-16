import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const categorias = [
  {
    id: 1,
    nombre: "Bebidas",
    productos: [
      { nombre: "Coca-Cola", cantidad: 20, alertaStock: 5 },
      { nombre: "Agua", cantidad: 50, alertaStock: 10 },
      { nombre: "Cerveza", cantidad: 15, alertaStock: 3 },
    ],
  },
  {
    id: 2,
    nombre: "Carnes",
    productos: [
      { nombre: "Pollo", cantidad: 10, alertaStock: 2 },
      { nombre: "Res", cantidad: 8, alertaStock: 3 },
      { nombre: "Cerdo", cantidad: 12, alertaStock: 4 },
    ],
  },
  {
    id: 3,
    nombre: "Lácteos",
    productos: [
      { nombre: "Leche", cantidad: 30, alertaStock: 7 },
      { nombre: "Queso", cantidad: 25, alertaStock: 5 },
      { nombre: "Yogur", cantidad: 18, alertaStock: 4 },
    ],
  },
];

function Categorias() {
  const navigate = useNavigate();
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);
  const [productoSeleccionado, setProductoSeleccionado] = useState(null);


  const styles = {
    container: {
      textAlign: "center",
      padding: "20px",
    },
    button: {
      margin: "5px",
      padding: "10px 15px",
      fontSize: "16px",
      cursor: "pointer",
      border: "none",
      borderRadius: "5px",
    },
    categoriaBoton: {
      backgroundColor: "#007bff",
      color: "white",
    },
    categoriaBotonHover: {
      backgroundColor: "#0056b3",
    },
    productoItem: {
      listStyle: "none",
      cursor: "pointer",
      fontSize: "18px",
      margin: "5px 0",
      padding: "10px",
      background: "#f8f9fa",
      borderRadius: "5px",
    },
    productoItemHover: {
      background: "#e9ecef",
    },
    productoDetalle: {
      marginTop: "20px",
      padding: "15px",
      border: "1px solid #ddd",
      borderRadius: "5px",
      display: "inline-block",
      backgroundColor: "#f1f1f1",
    },
    volverBoton: {
      backgroundColor: "#dc3545",
      color: "white",
      marginBottom: "15px",
    },
    volverBotonHover: {
      backgroundColor: "#c82333",
    },
  };


  return (
    <div style={styles.container}>
      <h1>Categorías de Inventario</h1>

      {/* Botón para volver a la vista principal */}
      <button
        style={{ ...styles.button, ...styles.volverBoton }}
        onMouseOver={(e) => (e.target.style.backgroundColor = styles.volverBotonHover.backgroundColor)}
        onMouseOut={(e) => (e.target.style.backgroundColor = styles.volverBoton.backgroundColor)}
        onClick={() => navigate("/")}
      >
        Volver a Inicio
      </button>

      {/* Lista de categorías */}
      <div>
        {categorias.map((categoria) => (
          <button
            key={categoria.id}
            style={{ ...styles.button, ...styles.categoriaBoton }}
            onMouseOver={(e) => (e.target.style.backgroundColor = styles.categoriaBotonHover.backgroundColor)}
            onMouseOut={(e) => (e.target.style.backgroundColor = styles.categoriaBoton.backgroundColor)}
            onClick={() => {
              setCategoriaSeleccionada(categoria);
              setProductoSeleccionado(null);
            }}
          >
            {categoria.nombre}
          </button>
        ))}
      </div>

      {/* Mostrar productos si hay una categoría seleccionada */}
      {categoriaSeleccionada && (
        <div>
          <h2>{categoriaSeleccionada.nombre}</h2>
          <ul>
            {categoriaSeleccionada.productos.map((producto, index) => (
              <li
                key={index}
                style={styles.productoItem}
                onMouseOver={(e) => (e.target.style.backgroundColor = styles.productoItemHover.background)}
                onMouseOut={(e) => (e.target.style.backgroundColor = styles.productoItem.background)}
                onClick={() => setProductoSeleccionado(producto)}
              >
                {producto.nombre}
              </li>
            ))}
          </ul>
        </div>
      )}

      {/* Mostrar detalles del producto si se ha seleccionado uno */}
      {productoSeleccionado && (
        <div style={styles.productoDetalle}>
          <h2>{productoSeleccionado.nombre}</h2>
          <p><strong>Cantidad:</strong> {productoSeleccionado.cantidad}</p>
          <p><strong>Alerta de stock:</strong> {productoSeleccionado.alertaStock}</p>
        </div>
      )}
    </div>
  );

  
}

export default Categorias;
