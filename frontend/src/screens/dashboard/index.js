import React, { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "../../css/dashboard/styles.css";
import { Bell, User } from "lucide-react";
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line
} from "recharts";
import html2canvas from "html2canvas";
import { jsPDF } from "jspdf";
import Notificaciones from "../../components/Notifications";

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

  const token = localStorage.getItem("token");
  const negocioId = localStorage.getItem("negocioId");

  const toggleUserOptions = () => {
    setShowUserOptions(!showUserOptions);
  };

  useEffect(() => {
    if (!negocioId || !token) return;

    const fetchData = async (url, onSuccess, errorMessage) => {
      try {
        const response = await fetch(url, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) throw new Error(`${errorMessage} (${response.status})`);
        const data = await response.json();
        onSuccess(data);
      } catch (err) {
        console.error(errorMessage, err);
      }
    };

    fetchData(
      `http://localhost:8080/api/productoVenta/masVendido/${negocioId}`,
      (data) => {
        const parsed = Object.entries(data).map(([producto, cantidad]) => ({
          nombre: producto,
          cantidad,
        }));
        setProductsSoldData(parsed);
      },
      "Error al obtener productos más vendidos:"
    );

    fetchData(
      `http://localhost:8080/api/pedido/volumen/${negocioId}`,
      (data) => {
        const parsed = Object.entries(data).map(([semana, total]) => ({
          day: `Semana ${semana}`,
          total,
        }));
        setSalesData(parsed);
      },
      "Error al obtener volumen de pedidos:"
    );

    fetchData(
      `http://localhost:8080/api/pedido/ingresos/${negocioId}`,
      (data) => {
        const parsed = Object.entries(data).map(([mes, ingreso]) => ({
          day: mes,
          revenue: ingreso,
        }));
        setRevenueData(parsed);
      },
      "Error al obtener ingresos mensuales:"
    );

    fetchData(
      `http://localhost:8080/api/productoInventario/menosCantidad/${negocioId}`,
      (data) => {
        const parsed = Object.entries(data).map(([producto, cantidad]) => ({
          product: producto,
          stock: cantidad,
        }));
        setLowStockData(parsed);
      },
      "Error al obtener inventario bajo:"
    );

    fetchData(
      `http://localhost:8080/api/productoInventario/aviso/${negocioId}`,
      (data) => {
        const parsed = Object.entries(data).map(([producto, cantidad]) => ({
          product: producto,
          stock: cantidad,
        }));
        setEmergencyStockData(parsed);
      },
      "Error al obtener stock de emergencia:"
    );
  }, [negocioId, token]);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/");
  };

  const salesChartData = salesData.map((sales) => ({
    day: sales.day,
    sales: sales.total,
  }));

  const revenueChartData = revenueData.map((revenue) => ({
    day: revenue.day,
    revenue: revenue.revenue,
  }));

  const lowStockChartData = lowStockData.map((item) => ({
    product: item.product,
    stock: item.stock,
  }));

  const emergencyStockChartData = emergencyStockData.map((item) => ({
    product: item.product,
    stock: item.stock,
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
    <div className="home-container" style={{
      backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
      backgroundSize: "cover",
      backgroundPosition: "center",
      height: "100vh",
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      justifyContent: "flex-start",
      textAlign: "center",
      padding: "20px",
      overflow: "hidden"
    }}>
      <div className="content">
        <div className="icon-container-right">
          <Bell size={30} className="icon" onClick={() => setShowNotifications(!showNotifications)} />
          <User size={30} className="icon" onClick={() => setShowUserOptions(!showUserOptions)} />
        </div>

        {showNotifications && (
          <div className="icon-container-right">
            <Notificaciones />
            <User size={30} className="icon" onClick={toggleUserOptions} />
          </div>
        )}

        {showUserOptions && (
          <div className="notification-bubble user-options">
            <div className="notification-header">
              <strong>Usuario</strong>
              <button className="close-btn" onClick={() => setShowUserOptions(false)}>X</button>
            </div>
            <ul>
              <li><button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button></li>
              <li><button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button></li>
              <li><button className="user-btn logout-btn" onClick={() => setShowLogoutModal(true)}>Cerrar Sesión</button></li>
            </ul>
          </div>
        )}

        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
        <Link to="/inicioDueno">
          <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
        </Link>
        <h1 className="title">GastroStock</h1>
        <h2>DashBoard</h2>

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
