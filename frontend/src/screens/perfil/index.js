import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, User } from "lucide-react";
import "../../css/perfil/styles.css";
import { MenuIconos } from "../../components/MenuIconos";

function PantallaPerfil() {
  const navigate = useNavigate();

  const [user, setUser] = useState( 
    {"first_name": "Carlos",
    "last_name": "Perez",
    "email" :  "carlos.perez@gmail.com",
    "num_telefono" : "123456789",}
  );

  /*{"first_name": "Juan",
    "last_name": "Garcia",
    "email" :  "juan.garcia@gmail.com",
    "num_telefono" : "987654321",
    "descripcion" : "Cocina",
    "negocio" : {
      "name" : "Restaurante La Trattoria"
    }
  }*/

  /*useEffect(loadUser);

  function loadUser(){
    user_ = fetch("")
  }*/


  return(
      <div className="content">
        
        <MenuIconos/>

        <div className="profile_head">
          <img src="default_profile.png" className="profile_picture"/>
          <p>{user.first_name + " " + user.last_name}</p>
        </div>

        <div className="profile_details">
          <table>
            <tr>
              <th>Teléfono:</th>
              <tr>{user.num_telefono}</tr>
            </tr>
            <tr>
              <th>Email:</th>
              <tr>{user.email}</tr>
            </tr>
          </table>
        </div>
      </div>
    )
}

export default PantallaPerfil;
