import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const categoriasIniciales = [
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
  const [categorias, setCategorias] = useState(categoriasIniciales);
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState(null);
  const [productoSeleccionado, setProductoSeleccionado] = useState(null);
  const [nuevoProducto, setNuevoProducto] = useState({ nombre: "", cantidad: "", alertaStock: "" });

  const agregarProducto = () => {
    if (!nuevoProducto.nombre || !nuevoProducto.cantidad || !nuevoProducto.alertaStock) return;
    const nuevasCategorias = categorias.map((categoria) => {
      if (categoria.id === categoriaSeleccionada.id) {
        return { ...categoria, productos: [...categoria.productos, { ...nuevoProducto, cantidad: Number(nuevoProducto.cantidad), alertaStock: Number(nuevoProducto.alertaStock) }] };
      }
      return categoria;
    });
    setCategorias(nuevasCategorias);
    setNuevoProducto({ nombre: "", cantidad: "", alertaStock: "" });
  };

  const eliminarProducto = (index) => {
    const nuevasCategorias = categorias.map((categoria) => {
      if (categoria.id === categoriaSeleccionada.id) {
        return { ...categoria, productos: categoria.productos.filter((_, i) => i !== index) };
      }
      return categoria;
    });
    setCategorias(nuevasCategorias);
  };

  const styles = {
    container: { textAlign: "center", padding: "20px" },
    button: { margin: "5px", padding: "10px 15px", fontSize: "16px", cursor: "pointer", border: "none", borderRadius: "5px" },
    categoriaBoton: { backgroundColor: "#007bff", color: "white" },
    categoriaBotonHover: { backgroundColor: "#0056b3" },
    productoItem: { listStyle: "none", cursor: "pointer", fontSize: "18px", margin: "5px 0", padding: "10px", background: "#f8f9fa", borderRadius: "5px", display: "flex", justifyContent: "space-between", alignItems: "center" },
    productoItemHover: { background: "#e9ecef" },
    productoDetalle: { marginTop: "20px", padding: "15px", border: "1px solid #ddd", borderRadius: "5px", display: "inline-block", backgroundColor: "#f1f1f1" },
    volverBoton: { backgroundColor: "#dc3545", color: "white", marginBottom: "15px" },
    volverBotonHover: { backgroundColor: "#c82333" },
    input: { margin: "5px", padding: "8px", fontSize: "16px", borderRadius: "5px", border: "1px solid #ccc" },
  };

  return (
    <div style={styles.container}>
      <h1>Categorías de Inventario</h1>
      <button
        style={{ ...styles.button, ...styles.volverBoton }}
        onMouseOver={(e) => (e.target.style.backgroundColor = styles.volverBotonHover.backgroundColor)}
        onMouseOut={(e) => (e.target.style.backgroundColor = styles.volverBoton.backgroundColor)}
        onClick={() => navigate("/")}
      >
        Volver a Inicio
      </button>
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
      {categoriaSeleccionada && (
        <div>
          <h2>{categoriaSeleccionada.nombre}</h2>
          <ul>
            {categoriaSeleccionada.productos.map((producto, index) => (
              <li key={index} style={styles.productoItem}>
                {producto.nombre} - Cantidad: {producto.cantidad} - Alerta: {producto.alertaStock}
                <button onClick={() => eliminarProducto(index)} style={{ ...styles.button, backgroundColor: "#d9534f", color: "white", marginLeft: "10px" }}>Eliminar</button>
              </li>
            ))}
          </ul>
          <h3>Añadir Producto</h3>
          <input type="text" placeholder="Nombre" style={styles.input} value={nuevoProducto.nombre} onChange={(e) => setNuevoProducto({ ...nuevoProducto, nombre: e.target.value })} />
          <input type="number" placeholder="Cantidad" style={styles.input} value={nuevoProducto.cantidad} onChange={(e) => setNuevoProducto({ ...nuevoProducto, cantidad: e.target.value })} />
          <input type="number" placeholder="Alerta Stock" style={styles.input} value={nuevoProducto.alertaStock} onChange={(e) => setNuevoProducto({ ...nuevoProducto, alertaStock: e.target.value })} />
          <button onClick={agregarProducto} style={{ ...styles.button, backgroundColor: "#28a745", color: "white" }}>Añadir</button>
        </div>
      )}
    </div>
  );
}

export default Categorias;