import React, { useState } from "react";

const products = [
  { id: 1, name: "Café", price: 1.5 },
  { id: 2, name: "Té", price: 1.2 },
  { id: 3, name: "Zumo de naranja", price: 1.0 },
];



const TPV = () => {
  const [orders, setOrders] = useState([]);
  const [total, setTotal] = useState(0);

  const addOrder = (product) => {
    setOrders([...orders, product]);
    setTotal((prevTotal) => prevTotal + product.price);
  };

  const clearOrders = () => {
    setOrders([]);
    setTotal(0);
  };

  return(<div style={{ display: "flex", gap: "20px", padding: "20px" }}>
    {/* Lista de productos */}
    <div style={{ flex: 1, padding: "10px", background: "#fff", borderRadius: "10px" }}>
    <h2 style={{ color: "#000" }}>Productos</h2>
      {products.map((product) => (
        <button key={product.id} onClick={() => addOrder(product)} style={{ display: "block", margin: "5px", padding: "10px", background: "#4CAF50", color: "#fff", border: "none", borderRadius: "5px" }}>
          {product.name} - ${product.price.toFixed(2)}
        </button>
      ))}
    </div>

    {/* Pedido */}
    <div style={{ flex: 1, padding: "10px", background: "#fff", borderRadius: "10px" }}>
    <h2 style={{ color: "#000" }}>Pedido</h2>
      <ul>
        {orders.map((order, index) => (
          <li key={index} style={{ color: "#000" }}>
          {order.name} - ${order.price.toFixed(2)}
        </li>
        ))}
      </ul>
      <h3 style={{ color: "#000" }}>Total: ${total.toFixed(2)}</h3>
      <button onClick={clearOrders} style={{ background: "#FF5733", color: "#000", padding: "10px", border: "none", borderRadius: "5px" }}>Limpiar Pedido</button>
    </div>
  </div>);
};

export default TPV;

