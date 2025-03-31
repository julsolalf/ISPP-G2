import React, { useState } from "react";
import { MenuIconos } from "../../components/MenuIconos"
import "../../css/inventario/styles.css";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";

function PantallaAnadirCategoria(){
    const [negocios, setNegocios] = useState(
        [
            {
                id: 1,
                name: "Restaurante La Trattoria"
            },
            {
                id: 2,
                name: "Restaurante Burguer"
            }
        ]
    )

      const navigate = useNavigate();
    const [showNotifications, setShowNotifications] = useState(false);
      const [showUserOptions, setShowUserOptions] = useState(false);
    const [searchTerm, setSearchTerm] = useState("");
      
    
      const toggleNotifications = () => setShowNotifications(!showNotifications);
      const toggleUserOptions = () => setShowUserOptions(!showUserOptions);
    
    const handleSubmit =  async (data) => {
        await fetch('http://localhost:8080/api/categorias',
            {
                method: 'POST',
                body: JSON.stringify({
                    name: 'HOLA',
                    categoria: {
                        id: '1'
                    }
                })
            }
        )
    }
    

    return(
        <div
            className="home-container"
            style={{
            backgroundImage: `url(${process.env.PUBLIC_URL + "/background-spices.jpg"})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
            height: "100vh",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            textAlign: "center",
        }}>
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
                    <li><button className="user-btn" onClick={() => navigate("/logout")}>Cerrar Sesión</button></li>
                    </ul>
                </div>
                )}

                <button onClick={() => navigate(-1)} className="back-button">⬅ Volver</button>
                <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
                <h1 className="title">GastroStock</h1>
                <MenuIconos/>
                <h1>Anadir categoría</h1>

                <form action={handleSubmit}>
                    <label>Nombre</label>
                    <input 
                        type="text" 
                        name="name" 
                    />

                    <label>Negocio</label>
                    <select 
                        name="negocio_id" 
                    >
                        {negocios.map(negocio => <option value={negocio.id}>{negocio.name}</option>)}
                    </select>
                    <input type="submit" value="Anadir"/>
                </form>
        </div>
        </div>
    )
}

export default PantallaAnadirCategoria