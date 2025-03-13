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

  return(
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
      <button key={product.id} onClick={() => addOrder(product)} className="producto-button" style={{ backgroundImage:`url(${product.image})` }}>            
      <span className="producto-name"> {product.name} - {product.price.toFixed(2)}€ </span>           
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
      <h3 style={{ color: "#000" }}>Total: {total.toFixed(2)}€</h3>
      <button onClick={clearOrders} style={{ background: "#FF5733", color: "#000", padding: "10px", border: "none", borderRadius: "5px" }}>Limpiar Pedido</button>
    </div>
  </div>
  </div>
  );
};

export default TPV;

