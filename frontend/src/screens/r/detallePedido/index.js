import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

const DetallesPedido = () => {
  const { pedidoId } = useParams();  // Usar el ID del pedido
  const [lineas, setLineas] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchLineas = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/lineasDePedido/pedido/${pedidoId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) throw new Error('Error al cargar las líneas de pedido');
        const data = await response.json();
        setLineas(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setCargando(false);
      }
    };

    fetchLineas();
  }, [pedidoId]);

  const actualizarCantidad = async (linea, cambio) => {
    const nuevaCantidad = linea.cantidad + cambio;

    if (nuevaCantidad <= 0) {
      // Eliminar la línea de pedido si la cantidad es 0
      await fetch(`http://localhost:8080/api/lineasDePedido/${linea.id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
      });
    } else {
      // Actualizar la cantidad de la línea de pedido
      await fetch(`http://localhost:8080/api/lineasDePedido/${linea.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${localStorage.getItem('token')}`,
        },
        body: JSON.stringify({ ...linea, cantidad: nuevaCantidad }),
      });
    }
    // Recargar las líneas de pedido
    const response = await fetch(`http://localhost:8080/api/lineasDePedido/dto/pedido/${pedidoId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('token')}`,
      },
    });
    const data = await response.json();
    setLineas(data);
  };

  if (cargando) return <p>Cargando...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div>
      <button onClick={() => navigate(-1)}>Volver</button>
      <h1>Detalles del Pedido #{pedidoId}</h1>
      {lineas.map(linea => (
        <div key={linea.id}>
          <h3>{linea.productoName}</h3>
          <p>{linea.precioUnitario} € x {linea.cantidad}</p>
          <button onClick={() => actualizarCantidad(linea, -1)}>-</button>
          <button onClick={() => actualizarCantidad(linea, 1)}>+</button>
        </div>
      ))}
    </div>
  );
};

export default DetallesPedido;
