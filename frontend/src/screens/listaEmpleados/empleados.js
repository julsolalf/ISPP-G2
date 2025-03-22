import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/paginaEmpleados/styles.css";

function Empleados() {
  const navigate = useNavigate();
  const [empleados, setEmpleados] = useState([
    { nombre: "Juan Pérez", telefono: "123-456-7890", rol: "Cajero", codigo: "EMP001", turno: "Mañana", posicion: "Caja 1", fechaCreacion: "2023-06-15" },
    { nombre: "María Gómez", telefono: "987-654-3210", rol: "Supervisor", codigo: "EMP002", turno: "Tarde", posicion: "Supervisión", fechaCreacion: "2022-11-22" },
    { nombre: "Carlos Ramírez", telefono: "456-789-0123", rol: "Repartidor", codigo: "EMP003", turno: "Noche", posicion: "Reparto", fechaCreacion: "2024-01-10" },
  ]);
  const [nuevoNombre, setNuevoNombre] = useState("");
  const [nuevoTelefono, setNuevoTelefono] = useState("");
  const [nuevoRol, setNuevoRol] = useState("");
  const [nuevoTurno, setNuevoTurno] = useState("");
  const [nuevaPosicion, setNuevaPosicion] = useState("");

  const agregarEmpleado = () => {
    if (!nuevoNombre || !nuevoTelefono || !nuevoRol || !nuevoTurno || !nuevaPosicion) return;
    const nuevoEmpleado = {
      nombre: nuevoNombre,
      telefono: nuevoTelefono,
      rol: nuevoRol,
      turno: nuevoTurno,
      posicion: nuevaPosicion,
      codigo: `EMP${empleados.length + 1}`,
      fechaCreacion: new Date().toISOString().split("T")[0],
    };
    setEmpleados([...empleados, nuevoEmpleado]);
    setNuevoNombre("");
    setNuevoTelefono("");
    setNuevoRol("");
    setNuevoTurno("");
    setNuevaPosicion("");
  };

  return (
    <div className="home-container">
      <div className="content empleados-content">
        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <h1 className="title">Lista de Empleados</h1>

        <h2>Añadir Empleado</h2>
        <div className="form-container">
          <input type="text" placeholder="Nombre" value={nuevoNombre} onChange={(e) => setNuevoNombre(e.target.value)} />
          <input type="text" placeholder="Teléfono" value={nuevoTelefono} onChange={(e) => setNuevoTelefono(e.target.value)} />
          <input type="text" placeholder="Rol" value={nuevoRol} onChange={(e) => setNuevoRol(e.target.value)} />
          <input type="text" placeholder="Turno" value={nuevoTurno} onChange={(e) => setNuevoTurno(e.target.value)} />
          <input type="text" placeholder="Posición" value={nuevaPosicion} onChange={(e) => setNuevaPosicion(e.target.value)} />
          <button className="button" onClick={agregarEmpleado}>Añadir</button>
        </div>

        <h2>Lista de Empleados</h2>
        <div className="empleados-grid">
          {empleados.map((empleado, index) => (
            <div key={index} className="empleado-card">
              <h3>{empleado.nombre}</h3>
              <p><strong>Rol:</strong> {empleado.rol}</p>
              <p><strong>Teléfono:</strong> {empleado.telefono}</p>
              <p><strong>Turno:</strong> {empleado.turno}</p>
              <p><strong>Posición:</strong> {empleado.posicion}</p>
              <p><strong>Código:</strong> {empleado.codigo}</p>
              <p><strong>Fecha Creación:</strong> {empleado.fechaCreacion}</p>
              <button className="elim-btn" onClick={() => setEmpleados(empleados.filter((_, i) => i !== index))}>Eliminar</button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export default Empleados;