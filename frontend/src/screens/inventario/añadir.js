import { useState } from "react";
import { MenuIconos } from "../../components/MenuIconos"
import "../../css/inventario/styles.css";

function PantallaAñadirCategoria(){
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

    const handleSubmit =  async (data) => {
        await fetch('api/categorias',
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
                <MenuIconos/>
                <h1>Añadir categoría</h1>

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
                    <input type="submit" value="Añadir"/>
                </form>
        </div>
        </div>
    )
}

export default PantallaAñadirCategoria