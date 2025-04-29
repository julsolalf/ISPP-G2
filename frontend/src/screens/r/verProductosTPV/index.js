import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const Productos = ({ mesaId }) => {
  const [products, setProducts] = useState([]);
  const [selectedProducts, setSelectedProducts] = useState([]);
  const [totalPrice, setTotalPrice] = useState(0);
  const history = useNavigate();

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const token = localStorage.getItem('token');  // Obtener el token
        const res = await fetch("http://localhost:8080/api/productosVenta/negocio/1", {
          method: "GET",
          credentials: "include",  // Incluir las credenciales
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,  // Incluir el token de autorización
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
    setSelectedProducts([...selectedProducts, product]);
    setTotalPrice(prevTotal => prevTotal + product.precioVenta);
  };

  const handleCreateOrder = async () => {
    try {
      const token = localStorage.getItem('token');  // Obtener el token
  
      // Obtener la fecha en formato correcto
      const fecha = new Date().toISOString();  // Asegurarse de que sea compatible con el servidor
  
      // Crear el pedido
      const res = await fetch('http://localhost:8080/api/pedidos/dto', {
        method: 'POST',  // Incluir las credenciales
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          fecha: fecha,  // Fecha en el formato adecuado
          precioTotal: totalPrice.toFixed(2),  // Asegúrate de que el precio es un número positivo
          mesaId: mesaId,  // Asegúrate de que este valor es válido
          empleadoId: 1,  // Cambiar por el empleado real si es necesario
          negocioId: 1,   // Cambiar por el negocio real si es necesario
        }),
      });  

      if (!res.ok) throw new Error('Error al crear el pedido');
      const pedido = await res.json();
  
      // Crear las líneas de pedido
      for (const product of selectedProducts) {
        await fetch('http://localhost:8080/api/lineasDePedido', {
          method: 'POST',
          credentials: "include",  // Incluir las credenciales
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            cantidad: 1,
            precioUnitario: product.precioVenta,
            pedidoId: pedido.id,
            productoId: product.id,
          }),
        });
      }
  
      alert('Pedido creado con éxito.');
      history.push('/tpv'); // Regresa a la pantalla de mesas
  
    } catch (error) {
      console.error("Error al crear el pedido:", error);
      alert("Error al crear el pedido.");
    }
  };
  

  return (
    <div className="productos-container">
      <h1>Selecciona los productos para la mesa {mesaId}</h1>
      <div className="productos">
        {products.map((product) => (
          <button key={product.id} onClick={() => handleProductSelect(product)}>
            {product.name} - {product.precioVenta.toFixed(2)}€
          </button>
        ))}
      </div>

      <div className="selected-products">
        <h2>Productos seleccionados</h2>
        {selectedProducts.map((product, index) => (
          <div key={index}>
            {product.name} - {product.precioVenta.toFixed(2)}€
          </div>
        ))}
        <h3>Total: {totalPrice.toFixed(2)}€</h3>
      </div>

      <button onClick={handleCreateOrder}>Crear Pedido</button>
    </div>
  );
};

export default Productos;
