import React, { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import "../../css/listados/styles.css";
import { Bell, User } from "lucide-react";

const token = localStorage.getItem("token");

const obtenerVenta = async (id) => {
  try {
    const response = await fetch(`http://localhost:8080/api/pedidos/${id}`,
      {headers: { "Content-Type": "application/json" ,
        Authorization: `Bearer ${token}`,
      },}
    );
    if (!response.ok) {
      throw new Error("Error al obtener la venta");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener la venta:", error);
    return null;
  }
};

const obtenerEmpleados = async (negocioId) => {
  try {
    const response = await fetch(`http://localhost:8080/api/empleados/negocio/${negocioId}`,
      {headers: { "Content-Type": "application/json" ,
        Authorization: `Bearer ${token}`,
      },}
    );
    if (!response.ok) {
      throw new Error("Error al obtener los empleados");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener empleados:", error);
    return [];
  }
};

const obtenerMesas = async (negocioId) => {
  try {
    const response = await fetch(`http://localhost:8080/api/mesas/negocio/${negocioId}`,
      {headers: { "Content-Type": "application/json" ,
        Authorization: `Bearer ${token}`,
      },}
    );
    if (!response.ok) {
      throw new Error("Error al obtener las mesas");
    }
    return await response.json();
  } catch (error) {
    console.error("Error al obtener mesas:", error);
    return [];
  }
};

const actualizarVenta = async (id, venta, negocioId) => {
    try {
      const response = await fetch(`http://localhost:8080/api/pedidos/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" ,
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          fecha: venta.fecha,
          precioTotal: venta.precioTotal,
          empleado: { id: venta.empleado.id },  
          mesa: { id: venta.mesa.id },         
          negocio: { id: negocioId }            
        }),
      });
  
      if (!response.ok) {
        throw new Error("Error al actualizar la venta");
      }
      return await response.json(); 
    } catch (error) {
      console.error("Error al actualizar la venta:", error);
      return null;
    }
  };
  
function EditarVenta() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [venta, setVenta] = useState({
    fecha: "",
    precioTotal: "",
    empleado: { id: "", firstName: "", lastName: "" },
    mesa: { id: "", name: "" }
  });
  const [empleados, setEmpleados] = useState([]);
  const [mesas, setMesas] = useState([]);

  
  const negocioId = localStorage.getItem("negocioId");

  useEffect(() => {
    const cargarDatos = async () => {
      const ventaData = await obtenerVenta(id);
      if (ventaData) setVenta(ventaData);

      const empleadosData = await obtenerEmpleados(negocioId);
      const mesasData = await obtenerMesas(negocioId);

      setEmpleados(empleadosData);
      setMesas(mesasData);
    };
    cargarDatos();
  }, [id, negocioId]);

  const handleChange = (e) => {
    setVenta({ ...venta, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const actualizado = await actualizarVenta(id, venta,negocioId);
    if (actualizado) navigate(`/ventas/${id}`);
  };

  return (
    <div className="content">
      <div className="icon-container-right">
        <Bell size={30} className="icon" />
        <User size={30} className="icon" />
      </div>
      <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
      <Link to="/inicioDueno">
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
      </Link>          <h1 className="title">GastroStock</h1>
      <h2>Editar Pedido</h2>
      <form className="form-container" onSubmit={handleSubmit}>
      <input
          type="datetime-local"
          name="fecha"
          value={venta.fecha ? venta.fecha.slice(0, 16) : ""}  
          onChange={handleChange}
          required
        />
        <input
          type="number"
          name="precioTotal"
          value={venta.precioTotal}
          onChange={handleChange}
          placeholder="Precio Total"
          required
        />
        <select
          name="empleado"
          value={venta.empleado.id}
          onChange={(e) => setVenta({ ...venta, empleado: { ...venta.empleado, id: e.target.value } })}
          required
        >
          <option value="">Seleccionar Empleado</option>
          {empleados.map((empleado) => (
            <option key={empleado.id} value={empleado.id}>
              {empleado.firstName} {empleado.lastName}
            </option>
          ))}
        </select>
        <select
          name="mesa"
          value={venta.mesa.id}
          onChange={(e) => setVenta({ ...venta, mesa: { ...venta.mesa, id: e.target.value } })}
          required
        >
          <option value="">Seleccionar Mesa</option>
          {mesas.map((mesa) => (
            <option key={mesa.id} value={mesa.id}>
              {mesa.name}
            </option>
          ))}
        </select>
        <button type="submit" className="button">💾 Guardar</button>
      </form>
    </div>
  );
}

export default EditarVenta;
