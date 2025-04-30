import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Productos = ({ mesaId }) => {
  const [products, setProducts] = useState([]);
  const [selectedProducts, setSelectedProducts] = useState([]);
  const history = useNavigate();

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const token = localStorage.getItem('token');
        const res = await fetch("http://localhost:8080/api/productosVenta/negocio/1", {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (!res.ok) throw new Error("Error al obtener productos");
        const data = await res.json();
        setProducts(data);
      } catch (error) {
        console.error("Error cargando productos:", error);
      }
    };

    fetchProducts();
  }, []);

  const handleProductSelect = (product) => {
    const index = selectedProducts.findIndex(p => p.id === product.id);
    if (index !== -1) {
      const updated = [...selectedProducts];
      updated[index].cantidad += 1;
      setSelectedProducts(updated);
    } else {
      setSelectedProducts([...selectedProducts, { ...product, cantidad: 1 }]);
    }
  };

  const handleQuantityChange = (productId, delta) => {
    const updated = selectedProducts
      .map(p => (p.id === productId ? { ...p, cantidad: p.cantidad + delta } : p))
      .filter(p => p.cantidad > 0);
    setSelectedProducts(updated);
  };

  const getTotal = () =>
    selectedProducts.reduce((acc, p) => acc + p.precioVenta * p.cantidad, 0).toFixed(2);

  const handleCreateOrder = async () => {
    try {
      const token = localStorage.getItem('token');
      const fecha = new Date().toISOString().slice(0, 16);

      const res = await fetch('http://localhost:8080/api/pedidos/dto', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          fecha,
          precioTotal: parseFloat(getTotal()),
          mesaId: localStorage.getItem("mesaId"),
          empleadoId: 1,
          negocioId: 1,
        }),
      });

      if (!res.ok) throw new Error('Error al crear el pedido');
      const pedido = await res.json();

      for (const product of selectedProducts) {
        await fetch('http://localhost:8080/api/lineasDePedido', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            cantidad: product.cantidad,
            precioUnitario: product.precioVenta,
            pedidoId: pedido.id,
            productoId: product.id,
          }),
        });
      }

      alert('Pedido creado con éxito.');
      history(`/pedidos/mesa/${localStorage.getItem("mesaId")}`);
    } catch (error) {
      console.error("Error al crear el pedido:", error);
      alert("Error al crear el pedido.");
    }
  };

  return (
    <div className="p-4">
      <button onClick={() => history(-1)} className="mb-4">⬅️ Volver</button>
      <h1 className="text-xl font-bold mb-2">Selecciona productos para la mesa {mesaId}</h1>
      <button onClick={() => history(`/pedidos/mesa/${localStorage.getItem("mesaId")}`)} style={{ marginBottom: '1rem' }}>
        Ver Pedidos
      </button>
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4 mb-6">
        {products.map((product) => (
          <div key={product.id} className="border rounded-xl p-4 shadow-md">
            <h3 className="font-semibold">{product.name}</h3>
            <p>{product.precioVenta.toFixed(2)} €</p>
            <button
              className="mt-2 bg-green-500 text-white px-2 py-1 rounded"
              onClick={() => handleProductSelect(product)}
            >
              Añadir
            </button>
          </div>
        ))}
      </div>

      <div>
        <h2 className="text-lg font-semibold mb-2">Pedido Actual</h2>
        {selectedProducts.length === 0 ? (
          <p>No hay productos seleccionados.</p>
        ) : (
          <div>
            {selectedProducts.map((p) => (
              <div key={p.id} className="flex justify-between items-center border-b py-2">
                <div>
                  {p.name} - {p.precioVenta.toFixed(2)} € x {p.cantidad}
                </div>
                <div>
                  <button onClick={() => handleQuantityChange(p.id, -1)} className="px-2">➖</button>
                  <button onClick={() => handleQuantityChange(p.id, 1)} className="px-2">➕</button>
                </div>
              </div>
            ))}
            <h3 className="mt-4 font-bold">Total: {getTotal()} €</h3>
          </div>
        )}
      </div>

      <button
        className="mt-6 bg-blue-600 text-white px-4 py-2 rounded"
        onClick={handleCreateOrder}
        disabled={selectedProducts.length === 0}
      >
        Crear Pedido
      </button>
    </div>
  );
};

export default Productos;
