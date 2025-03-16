import React, { useState } from "react";
import "./styles.css";

const products = [
  { id: 1, name: "Café", price: 1.5, image: "/imagesProductos/cafe.jpg" },
  { id: 2, name: "Té", price: 1.2, image: "/imagesProductos/te.jpg" },
  { id: 3, name: "Zumo de naranja", price: 1.0, image: "/imagesProductos/zumoNaranja.jpg" },
  { id: 4, name: "Carne con Tomate", price: 1.5, image: "/imagesProductos/carneTomate.jpg" },
  { id: 5, name: "Tortilla", price: 1.0, image: "/imagesProductos/tortilla.jpg" },
  { id: 6, name: "Agua", price: 0.8, image: "/imagesProductos/agua.jpg" },
  { id: 7, name: "Cerveza", price: 2.5, image: "/imagesProductos/cerveza.jpg" },
  { id: 8, name: "Queso", price: 2.0, image: "/imagesProductos/queso.jpg" },
];

const TPV = () => {
  // Inicializamos 8 pedidos independientes
  const [orders, setOrders] = useState(
    Array.from({ length: 8 }, () => ({ items: [], total: 0 }))
  );
  // Estado para el pedido activo (null = vista de selección)
  const [activeOrder, setActiveOrder] = useState(null);

  const addOrderItem = (product) => {
    if (activeOrder === null) return; // Si no ha seleccionado ningún pedido, no hacer nada
    const newOrders = orders.map((order, index) => {
      if (index === activeOrder) {
        return {
          items: [...order.items, product],
          total: order.total + product.price,
        };
      }
      return order;
    });
    setOrders(newOrders);
  };

  const clearActiveOrder = () => {
    if (activeOrder === null) return;
    const newOrders = orders.map((order, index) =>
      index === activeOrder ? { items: [], total: 0 } : order
    );
    setOrders(newOrders);
  };

  return (
    <div style={{ padding: "20px" }}>
      {/* Logo en la parte superior central */}
      <div className="logo-container">
        <img src="/gastrostockLogo.png" alt="App Logo" className="app-logo" />
      </div>

      <div style={{ display: "flex", gap: "20px", padding: "20px" }}>
        {/* Lista de productos */}
        <div style={{ flex: 1, padding: "10px", background: "#fff", borderRadius: "10px" }}>
          <h2 style={{ color: "#000" }}>Productos</h2>
          {products.map((product) => (
            <button 
              key={product.id} 
              onClick={() => addOrderItem(product)}
              className="producto-button" 
              style={{ backgroundImage: `url(${product.image})` }}
            >            
              <span className="producto-name">
                {product.name} - {product.price.toFixed(2)}€
              </span>           
            </button>
          ))}
        </div>

        {/* Gestión de pedidos */}
        <div style={{ flex: 1, padding: "10px", background: "#fff", borderRadius: "10px" }}>
          {activeOrder === null ? (
            <>
              <h2 style={{ color: "#000" }}>Pedidos</h2>
              <div className="orders-grid">
                {orders.map((order, index) => (
                  <button 
                    key={index} 
                    className="order-button" 
                    onClick={() => setActiveOrder(index)}
                    style={{ backgroundImage: `url(/imagesProductos/mesa.png)`}}
                  >
                    Pedido {index + 1}
                  </button>
                ))}
              </div>
            </>
          ) : (
            <>
              <h2 style={{ color: "#000" }}>Detalle del Pedido {activeOrder + 1}</h2>
              <ul>
                {orders[activeOrder].items.map((item, idx) => (
                  <li key={idx} style={{ color: "#000" }}>
                    {item.name} - ${item.price.toFixed(2)}
                  </li>
                ))}
              </ul>
              <h3 style={{ color: "#000" }}>Total: {orders[activeOrder].total.toFixed(2)}€</h3>
              <button 
                onClick={clearActiveOrder} 
                style={{ background: "#FF5733", color: "#000", padding: "10px", border: "none", borderRadius: "5px", marginRight: "10px" }}>
                Limpiar Pedido
              </button>
              <button 
                onClick={() => setActiveOrder(null)} 
                style={{ background: "#6c757d", color: "#fff", padding: "10px", border: "none", borderRadius: "5px" }}>
                Volver a Pedidos
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default TPV;