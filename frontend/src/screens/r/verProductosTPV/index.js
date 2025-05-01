import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Productos = ({ mesaId }) => {
  const [products, setProducts] = useState([]);
  const [selectedProducts, setSelectedProducts] = useState([]);
  const [mesaOcupada, setMesaOcupada] = useState(false);
  const history = useNavigate();

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

    const ocupada = localStorage.getItem(`mesa_${localStorage.getItem("mesaId")}_ocupada`) === "true";
    console.log("Mesa ocupada:", ocupada);
    setMesaOcupada(ocupada);

    fetchProducts();
  }, [mesaId]);

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
      let pedidoId = localStorage.getItem("pedidoId");
      const mesaId = localStorage.getItem("mesaId");
  
      if (mesaOcupada === false) {
        // Crear nuevo pedido
        const pedidoResponse = await fetch("http://localhost:8080/api/pedidos/dto", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            mesaId: localStorage.getItem("mesaId"),
            empleadoId: 1,
            negocioId: 1,
            precioTotal: selectedProducts.reduce((acc, p) => acc + p.precioVenta * p.cantidad, 0),
            fecha: new Date().toISOString()
          }),
        });
  
        if (!pedidoResponse.ok) throw new Error("Error creando pedido");
  
        const pedidoData = await pedidoResponse.json();
        pedidoId = pedidoData.id;
        localStorage.setItem("pedidoId", pedidoId);
        setMesaOcupada(true);
        localStorage.setItem(`mesa_${mesaId}_ocupada`, "true");

  
        // Crear líneas de pedido
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
  
          if (!response.ok) {
            throw new Error("Error al crear línea de pedido");
          }
  
          const lineaCreada = await response.json();
          console.log("Línea de pedido creada:", lineaCreada);
  
          // Aquí puedes acceder al ID
          const lineaId = lineaCreada.id;
          localStorage.setItem("lineaId", lineaId);
        }
  
        // Redirigir a pantalla de línea de pedido
        history(`/pedidos/mesa/${mesaId}`);
      } else {
        // Si la mesa ya está ocupada, actualizar las líneas de pedido existentes
        for (const product of selectedProducts) {
          const lineaId = localStorage.getItem("lineaId"); // Obtener el ID de la línea de pedido guardado
          const updatedLinea = {
            id: parseInt(lineaId), // Asumiendo que tienes solo una línea, actualízala
            cantidad: product.cantidad,
            precioUnitario: product.precioVenta,
            precioLinea: product.cantidad * product.precioVenta,
            estado: false, // o true si ya salió de cocina
            pedidoId: parseInt(pedidoId),
            productoId: product.id,
          };
  
          const response = await fetch(`http://localhost:8080/api/lineasDePedido/${lineaId}`, {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Bearer ${token}`,
            },
            body: JSON.stringify(updatedLinea),
          });
  
          if (!response.ok) {
            throw new Error("Error al actualizar línea de pedido");
          }
  
          const lineaActualizada = await response.json();
          console.log("Línea de pedido actualizada:", lineaActualizada);
        }
  
        // Redirigir a pantalla de línea de pedido
        history(`/pedidos/mesa/${mesaId}`);
      }
    } catch (error) {
      console.error("Error al añadir productos a la comanda:", error);
      alert("No se pudo añadir productos a la comanda.");
    }
  };  

  return (
    <div className="p-4">
      <button onClick={() => history(-1)} className="mb-4">⬅️ Volver</button>
      <h1 className="text-xl font-bold mb-2">Productos para mesa {mesaId}</h1>

      <button onClick={() => history(`/pedidos/mesa/${mesaId}`)} className="mb-4">
        Ver Comanda
      </button>

      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 gap-4 mb-6">
        {products.map(product => (
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
        <h2 className="text-lg font-semibold mb-2">Comanda Actual</h2>
        {selectedProducts.length === 0 ? (
          <p>No hay productos seleccionados.</p>
        ) : (
          selectedProducts.map(p => (
            <div key={p.id} className="flex justify-between items-center border-b py-2">
              <div>{p.name} - {p.precioVenta.toFixed(2)} € x {p.cantidad}</div>
              <div>
                <button onClick={() => handleQuantityChange(p.id, -1)} className="px-2">➖</button>
                <button onClick={() => handleQuantityChange(p.id, 1)} className="px-2">➕</button>
              </div>
            </div>
          ))
        )}
      </div>

      <button
        className="mt-6 bg-blue-600 text-white px-4 py-2 rounded"
        onClick={handleAddToComanda}
        disabled={selectedProducts.length === 0}
      >
        Añadir a Comanda
      </button>
    </div>
  );
};

export default Productos;
