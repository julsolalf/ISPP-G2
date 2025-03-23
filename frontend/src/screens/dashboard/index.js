import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/dashboard/styles.css";
import { Bell, User } from "lucide-react";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, LineChart, Line } from "recharts";
import html2canvas from "html2canvas";
import { jsPDF } from "jspdf";

function Dashboard() {
  const navigate = useNavigate();
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);

  const [productsSoldData, setProductsSoldData] = useState([]);
  const [salesData, setSalesData] = useState([]);
  const [revenueData, setRevenueData] = useState([]);
  const [lowStockData, setLowStockData] = useState([]);
  const [emergencyStockData, setEmergencyStockData] = useState([]);

  useEffect(() => {
    // Simulación de la carga de datos
    setProductsSoldData([15, 30, 25, 40, 60, 80, 100]);
    setSalesData([100, 150, 200, 250, 300, 350, 400]);
    setRevenueData([0, 250, 500, 750, 1000, 1250, 1500]);
    setLowStockData([3, 2, 5, 4, 3, 1, 2]);
    setEmergencyStockData([1, 1, 0, 1, 2, 0, 0]);
  }, []);

  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const salesChartData = [
    { day: 'Lun', sales: salesData[0] },
    { day: 'Mar', sales: salesData[1] },
    { day: 'Mié', sales: salesData[2] },
    { day: 'Jue', sales: salesData[3] },
    { day: 'Vie', sales: salesData[4] },
    { day: 'Sáb', sales: salesData[5] },
    { day: 'Dom', sales: salesData[6] },
  ];

  const revenueChartData = [
    { day: 'Lun', revenue: revenueData[0] },
    { day: 'Mar', revenue: revenueData[1] },
    { day: 'Mié', revenue: revenueData[2] },
    { day: 'Jue', revenue: revenueData[3] },
    { day: 'Vie', revenue: revenueData[4] },
    { day: 'Sáb', revenue: revenueData[5] },
    { day: 'Dom', revenue: revenueData[6] },
  ];

  const lowStockChartData = [
    { product: 'Producto 1', stock: lowStockData[0] },
    { product: 'Producto 2', stock: lowStockData[1] },
    { product: 'Producto 3', stock: lowStockData[2] },
    { product: 'Producto 4', stock: lowStockData[3] },
    { product: 'Producto 5', stock: lowStockData[4] },
  ];

  const emergencyStockChartData = [
    { product: 'Producto 1', stock: emergencyStockData[0] },
    { product: 'Producto 2', stock: emergencyStockData[1] },
    { product: 'Producto 3', stock: emergencyStockData[2] },
    { product: 'Producto 4', stock: emergencyStockData[3] },
    { product: 'Producto 5', stock: emergencyStockData[4] },
  ];

  // Función para descargar el PDF
  const downloadPDF = () => {
    const pdf = new jsPDF();

    const metricElements = document.querySelectorAll(".metric");

    metricElements.forEach((element, index) => {
      // Captura cada gráfica
      html2canvas(element).then((canvas) => {
        const imgData = canvas.toDataURL("image/png");

        if (index > 0) {
          pdf.addPage(); // Agregar nueva página si no es la primera
        }

        // Agregar la imagen al PDF
        pdf.addImage(imgData, "PNG", 10, 10, 180, 100); // Ajusta la posición y el tamaño

        // Al finalizar, guarda el PDF
        if (index === metricElements.length - 1) {
          pdf.save("dashboard.pdf");
        }
      });
    });
  };

  return (
    <div
      className="home-container"
      style={{
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
        overflow: "hidden",
      }}
    >
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
              <li>
                <button className="user-btn" onClick={() => navigate("/perfil")}>Ver Perfil</button>
              </li>
              <li>
                <button className="user-btn" onClick={() => navigate("/planes")}>Ver planes</button>
              </li>
              <li>
                <button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button>
              </li>
            </ul>
          </div>
        )}
        <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>

        <div className="dashboard-grid">
          <div className="metric">
            <h3>Productos más Vendidos</h3>
            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={lowStockChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="product" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="stock" fill="#8884d8" />
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
                <Line type="monotone" dataKey="revenue" stroke="#8884d8" />
              </LineChart>
            </ResponsiveContainer>
          </div>

          <div className="metric">
            <h3>Artículos con Menos Stock</h3>
            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={lowStockChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="product" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="stock" fill="#FF5733" />
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className="metric">
            <h3>Artículos por Debajo del Stock de Emergencia</h3>
            <ResponsiveContainer width="100%" height={250}>
              <BarChart data={emergencyStockChartData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="product" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="stock" fill="#FF8C00" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Botón de descarga PDF dentro de la cuadrícula */}
        <div className="download-button-container">
          <button onClick={downloadPDF} className="download-pdf-button">Descargar PDF</button>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
