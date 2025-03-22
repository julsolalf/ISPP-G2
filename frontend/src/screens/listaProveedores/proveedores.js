import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Proveedores() {
  const navigate = useNavigate();
  const [proveedores, setProveedores] = useState([
    { nombre: "Distribuidora A", telefono: "123-456-7890", descripcion: "Proveedor de bebidas y refrescos" },
    { nombre: "Proveedor B", telefono: "987-654-3210", descripcion: "Especialista en carnes y embutidos" },
    { nombre: "Comercializadora C", telefono: "456-789-0123", descripcion: "Lácteos y productos derivados" },
  ]);
  const [nuevoNombre, setNuevoNombre] = useState("");
  const [nuevoTelefono, setNuevoTelefono] = useState("");
  const [nuevaDescripcion, setNuevaDescripcion] = useState("");

  const agregarProveedor = () => {
    if (nuevoNombre.trim() === "" || nuevoTelefono.trim() === "" || nuevaDescripcion.trim() === "") return;
    setProveedores([...proveedores, { nombre: nuevoNombre, telefono: nuevoTelefono, descripcion: nuevaDescripcion }]);
    setNuevoNombre("");
    setNuevoTelefono("");
    setNuevaDescripcion("");
  };

  const eliminarProveedor = (index) => {
    setProveedores(proveedores.filter((_, i) => i !== index));
  };

  const styles = {
    container: { textAlign: "center", padding: "20px" },
    button: { margin: "5px", padding: "10px 15px", fontSize: "16px", cursor: "pointer", border: "none", borderRadius: "5px" },
    volverBoton: { backgroundColor: "#dc3545", color: "white" },
    volverBotonHover: { backgroundColor: "#c82333" },
    agregarBoton: { backgroundColor: "#28a745", color: "white" },
    eliminarBoton: { backgroundColor: "#d9534f", color: "white", marginLeft: "10px" },
    input: { margin: "5px", padding: "8px", fontSize: "16px", borderRadius: "5px", border: "1px solid #ccc" },
    proveedorItem: { listStyle: "none", fontSize: "18px", margin: "5px 0", padding: "10px", background: "#f8f9fa", borderRadius: "5px", display: "flex", flexDirection: "column", alignItems: "flex-start" },
  };

  return (
    <div style={styles.container}>
      <h1>Lista de Proveedores</h1>
      
      <button
        style={{ ...styles.button, ...styles.volverBoton }}
        onMouseOver={(e) => (e.target.style.backgroundColor = styles.volverBotonHover.backgroundColor)}
        onMouseOut={(e) => (e.target.style.backgroundColor = styles.volverBoton.backgroundColor)}
        onClick={() => navigate("/")}
      >
        Volver a Inicio
      </button>

      <h2>Añadir Proveedor</h2>
      <input
        type="text"
        placeholder="Nombre"
        style={styles.input}
        value={nuevoNombre}
        onChange={(e) => setNuevoNombre(e.target.value)}
      />
      <input
        type="text"
        placeholder="Teléfono"
        style={styles.input}
        value={nuevoTelefono}
        onChange={(e) => setNuevoTelefono(e.target.value)}
      />
      <input
        type="text"
        placeholder="Descripción"
        style={styles.input}
        value={nuevaDescripcion}
        onChange={(e) => setNuevaDescripcion(e.target.value)}
      />
      <button style={{ ...styles.button, ...styles.agregarBoton }} onClick={agregarProveedor}>
        Añadir
      </button>

      <h2>Lista de Proveedores</h2>
      <ul>
        {proveedores.map((proveedor, index) => (
          <li key={index} style={styles.proveedorItem}>
            <strong>{proveedor.nombre}</strong>
            <p><strong>Teléfono:</strong> {proveedor.telefono}</p>
            <p><strong>Descripción:</strong> {proveedor.descripcion}</p>
            <button style={{ ...styles.button, ...styles.eliminarBoton }} onClick={() => eliminarProveedor(index)}>
              Eliminar
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Proveedores;