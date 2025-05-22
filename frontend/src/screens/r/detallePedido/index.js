import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import "../../../css/listados/styles.css";

const DetallesPedido = () => {
  const { pedidoId } = useParams();
  const [lineas, setLineas] = useState([]);
  const [pedido, setPedido] = useState(null);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState('');
  const [lineasModificadas, setLineasModificadas] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPedido = async () => {
      try {
        const token = localStorage.getItem('token');
        console.log('Cargando información del pedido...');
        const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/pedidos/dto/${pedidoId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) throw new Error('Error al cargar el pedido');
        const data = await response.json();
        console.log('Pedido cargado:', data);
        setPedido(data);
      } catch (err) {
        console.error(err);
        setError(err.message);
      }
    };

    const fetchLineas = async () => {
      try {
        const token = localStorage.getItem('token');
        console.log('Cargando líneas de pedido...');
        const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/lineasDePedido/dto/pedido/${pedidoId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
        });
        if (!response.ok) throw new Error('Error al cargar las líneas de pedido');
        const data = await response.json();
        console.log('Líneas de pedido cargadas:', data);
        setLineas(data);
        setLineasModificadas(data);
      } catch (err) {
        console.error(err);
        setError(err.message);
      }
    };

    fetchPedido();
    fetchLineas();
  }, [pedidoId]);

  const actualizarCantidad = (linea, cambio) => {
    const nuevaCantidad = linea.cantidad + cambio;
    if (nuevaCantidad <= 0) {
      setLineasModificadas(lineasModificadas.filter(item => item.id !== linea.id));
    } else {
      const nuevasLineas = lineasModificadas.map(item =>
        item.id === linea.id ? { ...item, cantidad: nuevaCantidad } : item
      );
      setLineasModificadas(nuevasLineas);
    }
  };

  const actualizarPedido = async () => {
    try {
      const token = localStorage.getItem('token');
      const pedidoDto = {
        id: pedido.id,
        fecha: pedido.fecha,
        precioTotal: pedido.precioTotal,
        mesaId: pedido.mesaId,
        empleadoId: pedido.empleadoId,
        negocioId: pedido.negocioId,
      };
  
      console.log('Actualizando pedido con los datos:', pedidoDto);
  
      const responsePedido = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/pedidos/dto/${pedidoId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(pedidoDto),
      });
  
      if (!responsePedido.ok) {
        const errorMessage = await responsePedido.text();
        throw new Error(`Error al actualizar el pedido: ${errorMessage}`);
      }
  
      for (const linea of lineasModificadas) {
        const lineaDto = {
          cantidad: linea.cantidad,
          precioUnitario: linea.precioUnitario,  // Asegúrate de enviar el precio unitario
          pedidoId: pedido.id,  // ID del pedido
          productoId: linea.productoId,  // ID del producto
        };
  
        const responseLinea = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/lineasDePedido/${linea.id}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(lineaDto),
        });
  
        if (!responseLinea.ok) {
          const errorMessage = await responseLinea.text();
          throw new Error(`Error al actualizar la línea de pedido: ${errorMessage}`);
        }
      }
  
      alert('Pedido actualizado con éxito');
      setLineas(lineasModificadas);
      navigate(-1);  // Redirigir a la pantalla anterior
    } catch (err) {
      console.error(err);
      setError(err.message);
    }
  };

  if (error) return <p>Error: {error}</p>;

  return (
    <div
      className="home-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        minHeight: "100vh",
        padding: "20px",
        color: "#fff"
      }}
    >
      <div className='content'>
      <button onClick={() => navigate(-1)} className="back-button">
      ⬅ Volver
      </button>
      <h2 style={{ marginTop: '1rem' }}>Editando pedido actual</h2>
      <div className="empleado-card">    
      <h1>🧾 Detalles del Pedido #{pedidoId}</h1>
      {lineasModificadas.map(linea => (
        <div key={linea.id}>
          <h3>{linea.nombreProducto}</h3>
          <p>{linea.precioUnitario} € x {linea.cantidad}</p>
          <button onClick={() => actualizarCantidad(linea, -1)}>-</button>
          <button onClick={() => actualizarCantidad(linea, 1)}>+</button>
        </div>
      ))}
      <button onClick={actualizarPedido}>Actualizar Pedido</button>
      </div>
      </div>
    </div>
  );
};

export default DetallesPedido;
