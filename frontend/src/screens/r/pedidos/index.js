import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

const PantallaLineasDePedido = () => {
  const { mesaId } = useParams();
  const [pedidos, setPedidos] = useState([]);
  const [cargando, setCargando] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    const fetchPedidos = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/pedidos/mesa/${mesaId}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          }
        });
        if (!response.ok) throw new Error('Error al obtener los pedidos');
        const data = await response.json();
        setPedidos(data);
      } catch (error) {
        console.error(error);
      } finally {
        setCargando(false);
      }
    };

    fetchPedidos();
  }, [mesaId]);

  if (cargando) return <p>Cargando pedidos...</p>;

  return (
    <div className="pedido-container">
      <button onClick={() => navigate(-1)} style={{ marginBottom: '1rem' }}>
        ⬅️ Volver
      </button>
      <h2>Pedidos para la Mesa #{mesaId}</h2>
      {pedidos.length === 0 ? (
        <p>No hay pedidos para esta mesa.</p>
      ) : (
        pedidos.map((pedido) => (
          <div key={pedido.id} className="pedido-card" style={{
            border: '1px solid #ccc',
            borderRadius: '12px',
            padding: '1rem',
            marginBottom: '1rem',
            boxShadow: '0 2px 6px rgba(0,0,0,0.1)',
          }}>
            <h3 style={{ marginBottom: '0.5rem' }}>🧾 Pedido #{pedido.id}</h3>
            <p><strong>📅 Fecha:</strong> {new Date(pedido.fecha).toLocaleString()}</p>
            <p><strong>💰 Total:</strong> {pedido.precioTotal?.toFixed(2)}€</p>
            <hr style={{ margin: '0.5rem 0' }} />
            {/* Botón para navegar a la pantalla de detalles del pedido */}
            <button
              onClick={() => navigate(`/pedido/${pedido.id}`)} // Redirige a la pantalla de detalles del pedido
              style={{
                padding: '0.5rem 1rem',
                backgroundColor: '#9B1D42',
                color: '#fff',
                border: 'none',
                borderRadius: '8px',
                cursor: 'pointer',
              }}
            >
              Ver detalles
            </button>
          </div>
        ))
      )}
    </div>
  );
};

export default PantallaLineasDePedido;
