import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/dashboard/styles.css";
import { Bell, User } from "lucide-react";
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line
} from "recharts";
import html2canvas from "html2canvas";
import { jsPDF } from "jspdf";

function Dashboard() {
  const navigate = useNavigate();
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const [productsSoldData, setProductsSoldData] = useState([]);
  const [salesData, setSalesData] = useState([]);
  const [revenueData, setRevenueData] = useState([]);
  const [lowStockData, setLowStockData] = useState([]);
  const [emergencyStockData, setEmergencyStockData] = useState([]);

  useEffect(() => {
    // Obtener datos de productos vendidos (Producto más vendido)
    fetch("http://localhost:8080/api/lineasDePedido")
      .then((response) => response.json())
      .then((data) => {
        const productsSold = data.reduce((acc, item) => {
          if (acc[item.producto_id]) {
            acc[item.producto_id].cantidad += item.cantidad;
            acc[item.producto_id].totalVendido += item.precio_unitario * item.cantidad;
          } else {
            acc[item.producto_id] = {
              nombre: item.name,
              cantidad: item.cantidad,
              precio: item.precio_unitario,
              totalVendido: item.precio_unitario * item.cantidad,
            };
          }
          return acc;
        }, {});

        const productsArray = Object.values(productsSold);
        setProductsSoldData(productsArray);
      })
      .catch((error) => console.error("Error al obtener productos vendidos:", error));

    // Obtener datos de ventas por semana
    fetch("http://localhost:8080/api/pedidos")
      .then((response) => response.json())
      .then((data) => {
        const sales = data.map((pedido) => {
          const day = new Date(pedido.fecha);
          return {
            day: day.toLocaleString("es-ES", { weekday: "short" }),
            total: pedido.precio_total,
          };
        });
        setSalesData(sales);
      })
      .catch((error) => console.error("Error al obtener ventas:", error));

    // Obtener ingresos semanales
    fetch("http://localhost:8080/api/pedidos")
      .then((response) => response.json())
      .then((data) => {
        const revenue = data.map((pedido) => {
          const day = new Date(pedido.fecha);
          return {
            day: day.toLocaleString("es-ES", { weekday: "short" }),
            revenue: pedido.precio_total,
          };
        });
        setRevenueData(revenue);
      })
      .catch((error) => console.error("Error al obtener ingresos:", error));

    // Obtener productos con stock bajo
    fetch("http://localhost:8080/api/productosInventario")
      .then((response) => response.json())
      .then((data) => {
        const lowStock = data.filter(item => {
          const cantidadDisponible = item.lotes && Array.isArray(item.lotes)
            ? item.lotes.reduce((sum, lote) => sum + lote.cantidad, 0)
            : 0;

          return item.cantidad_deseada - item.cantidad_aviso > cantidadDisponible;
        });
        setLowStockData(lowStock);
      })
      .catch((error) => console.error("Error al obtener productos con stock bajo:", error));

    // Obtener productos en stock de emergencia
    fetch("http://localhost:8080/api/productosInventario")
      .then((response) => response.json())
      .then((data) => {
        const emergencyStock = data.filter(item => {
          const cantidadDisponible = item.lotes && Array.isArray(item.lotes)
            ? item.lotes.reduce((sum, lote) => sum + lote.cantidad, 0)
            : 0;

          return cantidadDisponible <= item.cantidad_aviso;
        });
        setEmergencyStockData(emergencyStock);
      })
      .catch((error) => console.error("Error al obtener productos con stock de emergencia:", error));
  }, []);

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  const handleLogout = () => {
    localStorage.removeItem("userToken");
    navigate("/");
  };

  // Datos para las gráficas
  const salesChartData = salesData.map((sales) => ({
    day: sales.day,
    sales: sales.total,
  }));

  const revenueChartData = revenueData.map((revenue) => ({
    day: revenue.day,
    revenue: revenue.revenue,
  }));

  const lowStockChartData = lowStockData.map((item) => ({
    product: item.name,
    stock: item.lotes.reduce((sum, lote) => sum + lote.cantidad, 0),
  }));

  const emergencyStockChartData = emergencyStockData.map((item) => ({
    product: item.name,
    stock: item.lotes.reduce((sum, lote) => sum + lote.cantidad, 0),
  }));

  const downloadPDF = () => {
    const pdf = new jsPDF();
    const metricElements = document.querySelectorAll(".metric");

    metricElements.forEach((element, index) => {
      html2canvas(element).then((canvas) => {
        const imgData = canvas.toDataURL("image/png");

        if (index > 0) {
          pdf.addPage();
        }

        pdf.addImage(imgData, "PNG", 10, 10, 180, 100);

        if (index === metricElements.length - 1) {
          pdf.save("dashboard.pdf");
        }
      });
    });
  };

  return (
    <div className="home-container" style={{ backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`, backgroundSize: "cover", backgroundPosition: "center", height: "100vh", display: "flex", flexDirection: "column", alignItems: "center", justifyContent: "flex-start", textAlign: "center", padding: "20px", overflow: "hidden" }}>
      <div className="content">
        <div className="icon-container-right">
          <Bell size={30} className="icon" onClick={toggleNotifications} />
          <User size={30} className="icon" onClick={toggleUserOptions} />
        </div>

        {showNotifications && (
          <div className="notification-bubble">
            <div className="notification-header">
              <strong>Notificaciones</strong>
              <button className="close-btn" onClick={toggleNotifications}>X</button>
            </div>
            <ul>
              <li>Notificación 1</li>
              <li>Notificación 2</li>
              <li>Notificación 3</li>
            </ul>
          </div>
        )}

        {showUserOptions && (
          <div className="notification-bubble user-options">
            <div className="notification-header">
              <strong>Usuario</strong>
              <button className="close-btn" onClick={toggleUserOptions}>X</button>
            </div>
            <ul>
              <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
              <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
              <li><button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>

        <div className="dashboard-grid">
          <div className="metric">
            <h3>Producto más Vendido</h3>
            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={productsSoldData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="nombre" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="cantidad" fill="#8884d8" />
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="metric">
            <h3>Ventas Semanales</h3>
            <ResponsiveContainer width="100%" height={250}>
              <LineChart data={salesChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="day" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="sales" stroke="#82ca9d" />
              </LineChart>
            </ResponsiveContainer>
          </div>

          <div className="metric">
            <h3>Ingreso Semanal</h3>
            <ResponsiveContainer width="100%" height={250}>
              <LineChart data={revenueChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="day" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="revenue" stroke="#82ca9d" />
              </LineChart>
            </ResponsiveContainer>
          </div>

          <div className="metric">
            <h3>Productos con Stock Bajo</h3>
            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={lowStockChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="product" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="stock" fill="#82ca9d" />
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="metric">
            <h3>Productos Bajo Stock de Emergencia</h3>
            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={emergencyStockChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="product" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="stock" fill="#82ca9d" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        <div className="download-button-container">
          <button onClick={downloadPDF} className="download-pdf-button">Descargar PDF</button>
        </div>
        {showLogoutModal && (
          <div className="modal-overlay">
            <div className="modal">
              <h3>¿Estás seguro que deseas abandonar la sesión?</h3>
              <div className="modal-buttons">
                <button className="confirm-btn" onClick={handleLogout}>Sí</button>
                <button className="cancel-btn" onClick={() => setShowLogoutModal(false)}>No</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Dashboard;
