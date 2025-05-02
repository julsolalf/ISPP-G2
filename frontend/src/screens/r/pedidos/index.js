import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const PantallaPedidoDetalle = () => {
  const [pedido, setPedido] = useState(null);
  const [lineasDePedido, setLineasDePedido] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [finalizado, setFinalizado] = useState(false);
  const navigate = useNavigate();
  
  const mesaId = localStorage.getItem("mesaId"); // Obtener el ID de la mesa desde localStorage
  const pedidoId = localStorage.getItem(`pedidoId_${mesaId}`); // Obtener el ID del pedido de la mesa

  useEffect(() => {
    if (!pedidoId) {
      alert("No se encontró un pedido para esta mesa.");
      navigate(`/productos/${mesaId}`); // o la ruta correspondiente
      return;
    }
  
    const token = localStorage.getItem('token');
    
    const fetchPedido = async () => {
      try {
        const pedidoResponse = await fetch(`http://localhost:8080/api/pedidos/dto/${pedidoId}`, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          }
        });
        if (!pedidoResponse.ok) throw new Error('Error al obtener el pedido');
        const pedidoData = await pedidoResponse.json();
        setPedido(pedidoData);
  
        const lineasResponse = await fetch(`http://localhost:8080/api/lineasDePedido/dto/pedido/${pedidoId}`, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          }
        });
        if (!lineasResponse.ok) throw new Error('Error al obtener las líneas de pedido');
        const lineasData = await lineasResponse.json();
        setLineasDePedido(lineasData);
  
      } catch (error) {
        console.error(error);
      } finally {
        setCargando(false);
      }
    };
  
    fetchPedido();
  }, [pedidoId, navigate]);
  

  const finalizarPedido = async () => {
    const token = localStorage.getItem('token');
  
    // Sumar los precios de todas las líneas de pedido
    const total = lineasDePedido.reduce((sum, linea) => sum + linea.precioLinea, 0);
  
    const fechaFinalizacion = new Date().toISOString(); // Obtener la fecha de finalización
  
    try {
      // Actualizar el pedido con el precio total y la fecha de finalización
      const response = await fetch(`http://localhost:8080/api/pedidos/dto/${pedidoId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          mesaId: mesaId,
          empleadoId: 1,
          negocioId: 1,
          precioTotal: parseFloat(total), // El precio total calculado
          fecha: fechaFinalizacion, // Fecha de finalización
        }),
      });
  
      if (!response.ok) throw new Error('Error al finalizar el pedido');
      
      // Limpiar localStorage
      localStorage.removeItem(`pedidoId_${mesaId}`);
      localStorage.removeItem('mesaId');
  
      // Redirigir al listado de mesas
      navigate('/TPV'); // Cambia esta ruta a la que corresponda con tu aplicación
  
      setFinalizado(true);
      alert(`✅ Pedido #${pedidoId} finalizado.`);
    } catch (error) {
      console.error(error);
      alert('❌ Error al finalizar el pedido.');
    }
  };
  

  if (cargando) return <p>Cargando...</p>;

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
    <div className="content">
      <button onClick={() => navigate(-1)} className="back-button">
      ⬅ Volver
      </button>
      <h2 style={{ marginTop: '1rem' }}>Pedidos de la mesa actual</h2>
      {pedido ? (
        <div className="empleado-card">
          <h1>🧾 Pedido #{pedido.id}</h1>
          <p><strong>📅 Fecha:</strong> {new Date(pedido.fecha).toLocaleString()}</p>
          <p><strong>💰 Total:</strong> {pedido.precioTotal?.toFixed(2)}€</p>

          <h1 style={{ marginTop: '1rem' }}>🍽️ Productos:</h1>
          {lineasDePedido.length > 0 ? (
            <ul>
              {lineasDePedido.map((linea) => (
                <li key={linea.id}>
                  {linea.cantidad}x {linea.nombreProducto || 'Producto'} - {linea.precioLinea.toFixed(2)}€
                </li>
              ))}
            </ul>
          ) : (
            <p style={{ fontStyle: 'italic' }}>Sin productos.</p>
          )}

          <button
            onClick={finalizarPedido}
            disabled={finalizado}
            style={{
              marginTop: '1rem',
              padding: '0.5rem 1rem',
              backgroundColor: finalizado ? '#ccc' : '#228B22',
              color: '#fff',
              border: 'none',
              borderRadius: '8px',
              cursor: finalizado ? 'not-allowed' : 'pointer',
            }}
          >
            {finalizado ? '✅ Finalizado' : 'Finalizar pedido'}
          </button>
          <button
              onClick={() => navigate(`/pedido/${pedido.id}`)}
              style={{
                marginTop: '0.5rem',
                padding: '0.5rem 1rem',
                backgroundColor: '#FFA500',
                color: '#fff',
                border: 'none',
                borderRadius: '8px',
                cursor: 'pointer',
              }}
            >
              ✏️ Editar pedido
            </button>

        </div>
      ) : (
        <p>No se encontró el pedido.</p>
      )}
    </div>
    </div>
  );
};

export default PantallaPedidoDetalle;
