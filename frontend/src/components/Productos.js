import React from "react";

const productos = [
  { id: 1, name: "Café", price: 1.5 },
  { id: 2, name: "Té", price: 1.2 },
  { id: 3, name: "Jugo", price: 2.0 }
];

function Productos() {
  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-4">Productos</h2>
      <div className="space-y-2">
        {productos.map((producto) => (
          <div key={producto.id} className="border p-2 rounded">
            {producto.name} - ${producto.price}
          </div>
        ))}
      </div>
    </div>
  );
}

export default Productos;