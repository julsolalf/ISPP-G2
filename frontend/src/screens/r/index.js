import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const TPV = () => {
  const [mesas, setMesas] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    const token = localStorage.getItem("token");  // Obtener el token del localStorage

    const fetchMesas = async () => {
      try {
        const res = await fetch(`http://localhost:8080/api/mesas/negocio/1`, {
          method: "GET",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,  // Incluir el token en los encabezados
          },
        });

        if (!res.ok) throw new Error("Error al obtener mesas");
        const data = await res.json();
        setMesas(data);  // Guardar las mesas en el estado
      } catch (error) {
        console.error("Error cargando mesas:", error);
      }
    };

    fetchMesas();  // Llamar a la función de obtener mesas
  }, []);

  const handleMesaClick = (mesaId) => {
    navigate(`/productos/${mesaId}`);
  };
  

  return (
    <div className="mesa-container">
      <button onClick={() => navigate(-1)} className="mb-4">⬅️ Volver</button>
      <h1>Selecciona una Mesa</h1>
      <div className="mesas">
        {mesas.map((mesa) => (
          <button key={mesa.id} onClick={() => {
            localStorage.setItem("mesaId", mesa.id);  // Guardar el ID de la mesa en el localStorage
            handleMesaClick(mesa.id)}}>
            {mesa.name || `Mesa ${mesa.id}`}
          </button>
        ))}
      </div>
    </div>
  );
};

export default TPV;
