import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Bell } from "lucide-react";
import "../css/inicio/styles.css";

function Notificaciones() {
  const navigate = useNavigate();
  const [showNotifications, setShowNotifications] = useState(false);
  const [proveedoresProximos, setProveedoresProximos] = useState([]);
  const [productosConStockBajo, setProductosConStockBajo] = useState([]);

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const handleStockClick = async (producto) => {
  setShowNotifications(false);
  try {
    const token = localStorage.getItem("token");
    const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/productosInventario/name/${encodeURIComponent(producto.name)}`, {
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      const productoCompleto = await response.json();
      const proveedorId = productoCompleto.proveedor?.id;

      if (proveedorId) {
        navigate(`/verCarritoProveedor/${proveedorId}`);
      } else {
        console.warn("No se encontró proveedor para el producto:", producto.name);
      }
    } else {
      console.error("Error al buscar producto por nombre:", producto.name);
    }
  } catch (error) {
    console.error("Error en handleStockClick:", error);
  }
};


  useEffect(() => {
    const token = localStorage.getItem("token");
    const negocioId = localStorage.getItem("negocioId");

    const fetchReabastecimientos = async () => {
      try {
        const response = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/reabastecimientos/negocio/${negocioId}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          const hoy = new Date();
          const tresDiasDespues = new Date(hoy);
          tresDiasDespues.setDate(hoy.getDate() + 7);

          const reabastecimientosProximos = data.filter((r) => {
            const fecha = new Date(r.fecha);
            return fecha >= hoy && fecha <= tresDiasDespues;
          });

          if (reabastecimientosProximos.length > 0) {
            const proveedores = [...new Set(reabastecimientosProximos.map((r) => r.proveedor.name))];
            setProveedoresProximos(proveedores);
            localStorage.setItem("reabastecimientos", JSON.stringify(proveedores));
          } else {
            localStorage.removeItem("reabastecimientos");
            setProveedoresProximos([]);
          }
        }
      } catch (error) {
        console.error("Error al obtener reabastecimientos:", error);
      }
    };

    const fetchProductosConStockBajo = async () => {
      try {
        const [productosRes, lotesRes] = await Promise.all([
          fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/productosInventario/negocio/${negocioId}`, {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }),
          fetch("https://ispp-2425-g2.ew.r.appspot.com/api/lotes", {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json",
            },
          }),
        ]);

        if (!productosRes.ok || !lotesRes.ok) {
          throw new Error("Error al obtener datos");
        }

        const productosData = await productosRes.json();
        const lotesData = await lotesRes.json();

        const cantidadPorProducto = {};
        lotesData.forEach((lote) => {
          const productoId = lote.producto?.id;
          if (productoId != null) {
            cantidadPorProducto[productoId] = (cantidadPorProducto[productoId] || 0) + lote.cantidad;
          }
        });

        const productosBajoStock = productosData
          .map((producto) => {
            const cantidadTotal = cantidadPorProducto[producto.id] || 0;
            return {
              ...producto,
              cantidad: cantidadTotal,
            };
          })
          .filter((producto) => producto.cantidad < producto.cantidadAviso);

        setProductosConStockBajo(productosBajoStock);
      } catch (error) {
        console.error("Error al obtener productos con stock bajo:", error);
      }
    };

    fetchReabastecimientos();
    fetchProductosConStockBajo();
  }, []);

  return (
    <>
      <Bell size={30} className="icon" onClick={toggleNotifications} />

      {showNotifications && (
        <div className="notification-bubble">
          <div className="notification-header">
            <strong>Notificaciones</strong>
            <button className="close-btn" onClick={toggleNotifications}>X</button>
          </div>
          <ul>
            {productosConStockBajo.length > 0 ? (
              productosConStockBajo.map((producto, index) => (
                <li key={index} onClick={() => handleStockClick(producto)} className="clickable2">
                  📉 <span className="subrayado">Stock bajo: {producto.name} ({producto.cantidad} / {producto.cantidadAviso})</span>
                </li>
              ))
            ) : (
              <li>No hay productos con stock bajo</li>
            )}
            {proveedoresProximos.length > 0 ? (
              proveedoresProximos.map((proveedor, index) => (
                <li key={index}>📦 Reabastecimiento próximo: {proveedor}</li>
              ))
            ) : (
              <li>No hay reabastecimientos próximos</li>
            )}
            {productosConStockBajo.length === 0 && proveedoresProximos.length === 0 && (
              <li>✅ Todo en orden</li>
            )}
          </ul>
        </div>
      )}
    </>
  );
}

export default Notificaciones;
