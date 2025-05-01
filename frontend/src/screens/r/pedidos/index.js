import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

const PantallaLineasDePedido = () => {
  const { mesaId } = useParams();
  const [pedidos, setPedidos] = useState([]);
  const [lineasPorPedido, setLineasPorPedido] = useState({});
  const [cargando, setCargando] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    const mesaId = localStorage.getItem('mesaId');

    const fetchPedidos = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/pedidos/mesa/${mesaId}`, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          }
        });
        if (!response.ok) throw new Error('Error al obtener los pedidos');
        const data = await response.json();
        setPedidos(data);

        // Luego obtenemos las líneas de cada pedido
        for (const pedido of data) {
          const res = await fetch(`http://localhost:8080/api/lineasDePedido/dto/pedido/${pedido.id}`, {
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${token}`,
            }
          });
          if (res.ok) {
            const lineas = await res.json();
            setLineasPorPedido(prev => ({ ...prev, [pedido.id]: lineas }));
          }
        }

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
            <h3>🧾 Pedido #{pedido.id}</h3>
            <p><strong>📅 Fecha:</strong> {new Date(pedido.fecha).toLocaleString()}</p>
            <p><strong>💰 Total:</strong> {pedido.precioTotal?.toFixed(2)}€</p>

            {lineasPorPedido[pedido.id]?.length > 0 ? (
              <>
                <h4 style={{ marginTop: '1rem' }}>🍽️ Productos:</h4>
                <ul>
                  {lineasPorPedido[pedido.id].map((linea) => (
                    <li key={linea.id}>
                      {linea.cantidad}x {linea.producto?.nombre || 'Producto'} - {linea.precioLinea.toFixed(2)}€
                    </li>
                  ))}
                </ul>
              </>
            ) : (
              <p style={{ fontStyle: 'italic' }}>Sin productos.</p>
            )}

            <button
              onClick={() => navigate(`/pedido/${pedido.id}`)}
              style={{
                marginTop: '1rem',
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
