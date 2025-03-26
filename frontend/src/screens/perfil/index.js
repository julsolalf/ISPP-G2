import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../css/perfil/styles.css";
import { MenuIconos } from "../../components/MenuIconos";

function PantallaPerfil() {
  const navigate = useNavigate();
  const [user, setUser] = useState(null);

  useEffect(() => {
    const userId = localStorage.getItem("userId");

    if (!userId) {
      console.error("No hay usuario autenticado");
      navigate("/inicioSesion");
      return;
    }

    fetch(`http://localhost:8080/api/users/${userId}`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Error al obtener los datos del usuario");
        }
        return response.json();
      })
      .then((data) => setUser(data))
      .catch((error) => console.error("Error:", error));
  }, [navigate]);

  if (!user) return <p>Cargando perfil...</p>;

  return (
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
      }}
    >
      <div className="content">
        <MenuIconos />

        <div className="profile_head">
          <img src="default_profile.png" className="profile_picture" alt="Perfil" />
          <p>{user.firstName + " " + user.lastName}</p>
        </div>

        <div className="profile_details">
          <table>
            <tbody>
              <tr>
                <th>Teléfono:</th>
                <td>{user.numTelefono || "No disponible"}</td>
              </tr>
              <tr>
                <th>Email:</th>
                <td>{user.email || "No disponible"}</td>
              </tr>
              <tr>
                <th>Rol:</th>
                <td>{user.authority ? user.authority.authority : "No asignado"}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default PantallaPerfil;
