import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import "../../../css/listados/styles.css";

const Productos = () => {
  const [products, setProducts] = useState([]);
  const [selectedProducts, setSelectedProducts] = useState([]);
  const navigate = useNavigate();
  const mesaId = localStorage.getItem("mesaId");

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const token = localStorage.getItem('token');
        const res = await fetch("http://localhost:8080/api/productosVenta/negocio/1", {
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

  const handleAddToComanda = async () => {
    try {
      const token = localStorage.getItem('token');
      let pedidoId = localStorage.getItem(`pedidoId_${mesaId}`);

      if (!pedidoId) {
        const pedidoResponse = await fetch("http://localhost:8080/api/pedidos/dto", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            mesaId: mesaId,
            empleadoId: 1,
            negocioId: 1,
            precioTotal: 0,
            fecha: null,
          }),
        });

        if (!pedidoResponse.ok) throw new Error("Error creando pedido");

        const pedidoData = await pedidoResponse.json();
        pedidoId = pedidoData.id;
        localStorage.setItem(`pedidoId_${mesaId}`, pedidoId);
      }

      for (const product of selectedProducts) {
        const response = await fetch("http://localhost:8080/api/lineasDePedido", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            cantidad: product.cantidad,
            precioUnitario: product.precioVenta,
            pedidoId: parseInt(pedidoId),
            productoId: product.id,
          }),
        });

        if (!response.ok) throw new Error("Error al crear línea de pedido");
        await response.json();
      }

      navigate(`/pedidos/mesa/${mesaId}`);
    } catch (error) {
      console.error("Error al añadir productos a la comanda:", error);
      alert("No se pudo añadir productos a la comanda.");
    }
  };

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
        <button onClick={() => navigate("/TPV")} className="back-button">
          ⬅ Volver
        </button>
        <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        <h1 className="title">GastroStock</h1>
        <h2>🧾 Productos para mesa {mesaId}</h2>

        <button className="ver-btn mb-4" onClick={() => navigate(`/pedidos/mesa/${mesaId}`)}>
          Ver Comanda
        </button>

        <div className="empleados-grid">
          {products.map(product => (
            <div key={product.id} className="empleado-card">
              <h3>{product.name}</h3>
              <p>{product.precioVenta.toFixed(2)} €</p>
              <button className="ver-btn" onClick={() => handleProductSelect(product)}>
                Añadir
              </button>
            </div>
          ))}
        </div>

        <div className="comanda-card mt-6 p-4 rounded-xl bg-black text-black shadow-lg">
          <h2 className="text-black font-semibold mb-2">Comanda Actual</h2>
          <div style={{color:"black",}}>
          {selectedProducts.length === 0 ? (
            <p>No hay productos seleccionados.</p>
          ) : (
            selectedProducts.map(p => (
              <div key={p.id} className="flex justify-between items-center border-b py-2">
                <div style={{color:"black",}}>{p.name} - {p.precioVenta.toFixed(2)} € x {p.cantidad}</div>
                <div>
                  <button onClick={() => handleQuantityChange(p.id, -1)} className="px-2">➖</button>
                  <button onClick={() => handleQuantityChange(p.id, 1)} className="px-2">➕</button>
                </div>
              </div>
            ))
          )}
          </div>
          <button
            className="mt-4 bg-blue-600 text-black px-4 py-2 rounded"
            onClick={handleAddToComanda}
            disabled={selectedProducts.length === 0}
          >
            Añadir a Comanda
          </button>
        </div>
      </div>
    </div>
  );
};

export default Productos;
