import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import "../../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const obtenerEmpleado = async () => {
  try {
    const response = await fetch(`http://localhost:8080/api/empleados/${localStorage.getItem("empleadoId")}`);
    if (!response.ok) {
      throw new Error("Error al obtener el empleado");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener el empleado:", error);
    return null;
  }
};

function VerEmpleado() {
  const { id } = useParams(); // Obtener ID desde la URL
  const navigate = useNavigate();
  const [empleado, setEmpleado] = useState(null);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

  useEffect(() => {
    const cargarEmpleado = async () => {
      const data = await obtenerEmpleado(id);
      setEmpleado(data);
    };
    cargarEmpleado();
  }, [id]);

  const eliminarEmpleado = async () => {
    try {
      const response = await fetch(`http://localhost:8080/api/empleados/${localStorage.getItem("empleadoId")}`, {
        method: "DELETE",
      });
      if (!response.ok) {
        throw new Error("Error al eliminar el empleado");
      }
      navigate("/empleados"); // Redirigir a la lista de empleados
    } catch (error) {
      console.error("Error al eliminar el empleado:", error);
    }
  };

  if (!empleado) {
    return <h2>Empleado no encontrado</h2>;
  }

  return (
    <div className="content">
      <div className="icon-container-right">
        <Bell size={30} className="icon" onClick={() => setShowNotifications(!showNotifications)} />
        <User size={30} className="icon" onClick={() => setShowUserOptions(!showUserOptions)} />
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
            <button className="close-btn" onClick={() => setShowUserOptions(false)}>X</button>
          </div>
          <ul>
            <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
            <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
            <li><button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button></li>
          </ul>
        </div>
      )}

      <button onClick={() => navigate("/empleados")} className="back-button">⬅ Volver</button>
      <Link to="/inicioDueno">
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
      </Link>
      <h1 className="title">GastroStock</h1>
      <h2>Empleado</h2>
      <div className="empleado-card">
        <h1 className="empleado-nombre">{empleado.firstName} {empleado.lastName}</h1>
        <p><strong>Email:</strong> {empleado.email}</p>
        <p><strong>Teléfono:</strong> {empleado.numTelefono}</p>
        <p><strong>Descripción:</strong> {empleado.descripcion}</p>

        <button style={{ background: "#157E03", color: "white" }} onClick={() => navigate(`/editarEmpleado/${empleado.user.username}`)}>Editar Empleado</button>
        <button style={{ background: "#9A031E", color: "white" }} onClick={() => setShowDeleteModal(true)}>Eliminar Empleado</button>
      </div>

      {showDeleteModal && (
        <div className="modal-overlay">
          <div className="modal">
            <h3>¿Está seguro que desea eliminar este empleado?</h3>
            <div className="modal-buttons">
              <button className="confirm-btn" onClick={eliminarEmpleado}>Sí</button>
              <button className="cancel-btn" onClick={() => setShowDeleteModal(false)}>No</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default VerEmpleado;
