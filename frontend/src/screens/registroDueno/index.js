import { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { Bell, User } from "lucide-react";
import "../../css/paginasBase/styles.css";

function PantallaRegistroDueno() {
  const [ownerFirstName, setOwnerFirstName] = useState("");
  const [ownerLastName, setOwnerLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [showUserOptions, setShowUserOptions] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [modalSeen, setModalSeen] = useState(false);
  const [canAccept, setCanAccept] = useState(false);
  const navigate = useNavigate();
  const agreementRef = useRef(null);
  const openModal = () => setModalOpen(true);
  const closeModal = () => {
    setModalSeen(true);
    setModalOpen(false);
  };
  const toggleNotifications = () => setShowNotifications(!showNotifications);
  const toggleUserOptions = () => setShowUserOptions(!showUserOptions);

  const handleRegister = async () => {
    if (password !== confirmPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }


    const data = {
      username: usuario,
      password: password,
      firstName: ownerFirstName,
      lastName: ownerLastName,
      email: email,
      numTelefono: phone,
    };

    try {
      setLoading(true);
      const registerResponse = await axios.post("https://ispp-2425-g2.ew.r.appspot.com/api/auth/register", data);
      console.log("Registro exitoso:", registerResponse.data);

      const loginResponse = await fetch("https://ispp-2425-g2.ew.r.appspot.com/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: usuario,
          password: password,
        }),
      });


      const loginData = await loginResponse.json();
      const token = loginData.token;
      localStorage.setItem("token", token);

      const userResponse = await fetch("https://ispp-2425-g2.ew.r.appspot.com/api/users/me", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const user = await userResponse.json();
      localStorage.setItem("user", JSON.stringify(user));

      const duenoResponse = await fetch(`https://ispp-2425-g2.ew.r.appspot.com/api/duenos/user/${user.id}`, {
        method: "GET",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const dueno = await duenoResponse.json();
      localStorage.setItem("duenoId", dueno.id);
      navigate("/elegirNegocio");
    } catch (error) {
      console.error("Error en el registro o login:", error.response?.data || error.message);
      alert(error.response?.data?.message || "Error al registrar/iniciar sesión.");
    } finally {
      setLoading(false);
    }
  };

  const handleScroll = () => {
    const el = agreementRef.current;
    if (el && el.scrollTop + el.clientHeight >= el.scrollHeight - 10) {
      setCanAccept(true);
    }
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
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
        padding: "20px",
        overflowY: "auto"
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
              <button className="close-btn" onClick={toggleNotifications}>
                X
              </button>
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
              <button className="close-btn" onClick={toggleUserOptions}>
                X
              </button>
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

        <button className="back-button" onClick={() => navigate("/")}>
      ← Volver
    </button>

    <img src="/gastrostockLogoSinLetra.png" alt="App Logo" className="app-logo" />
    <h1 className="title">GastroStock</h1>
    <h2>Registrarse</h2>

    <input type="text" placeholder="Nombre del dueno" value={ownerFirstName} onChange={(e) => setOwnerFirstName(e.target.value)} />
    <input type="text" placeholder="Apellidos del dueno" value={ownerLastName} onChange={(e) => setOwnerLastName(e.target.value)} />
    <input type="email" placeholder="Correo Electrónico" value={email} onChange={(e) => setEmail(e.target.value)} />
    <input type="tel" placeholder="Teléfono" value={phone} onChange={(e) => setPhone(e.target.value)} />
    <input type="text" placeholder="Usuario" value={usuario} onChange={(e) => setUsuario(e.target.value)} />
        <input type="password" placeholder="Contrasena" value={password} onChange={(e) => setPassword(e.target.value)} />
        <input type="password" placeholder="Confirmar Contrasena" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} />
        <div className="checkbox-container custom-checkbox-container">
          <input
            type="checkbox"
            id="agreementCheckbox"
            checked={modalSeen}
            readOnly
            className="custom-checkbox"
          />
          <label htmlFor="agreementCheckbox">
            Confirmo que he leído y aceptado los <span className="agreement-link" onClick={openModal}>términos del Acuerdo de Servicio</span> de GastroStock, y me comprometo a cumplirlos como parte del uso responsable de esta plataforma.
          </label>
        </div>

        {modalOpen && (
          <div className="modal-overlay">
            <div className="modal-content">
              <div
                ref={agreementRef}
                onScroll={handleScroll}
                className="modal-text"
                style={{
                  textAlign: "left",
                  maxHeight: "400px",
                  overflowY: "auto",
                  padding: "10px",
                  border: "1px solid #ccc",
                  borderRadius: "4px",
                  marginBottom: "20px"
                }}
              >
                <h2>ACUERDO DE SERVICIO</h2>

                <p className="section-title"><strong>GASTROSTOCK</strong></p>
                <p><strong>Fecha de entrada en vigor: 26/3/25</strong></p>

                <p className="section-title">1. Introducción</p>
                <p>El presente Acuerdo de Servicio (“Acuerdo”) establece los términos y condiciones bajo los cuales el Usuario (“Cliente”) accede y utiliza la plataforma GASTROSTOCK.</p>
                <p>Al marcar la casilla correspondiente durante el proceso de registro o activación del servicio, el Cliente acepta todos los términos establecidos en este Acuerdo. Si el Cliente no está de acuerdo, no debe utilizar la plataforma.</p>

                <p className="section-title">2. Definiciones</p>
                <p><strong>GASTROSTOCK:</strong> Plataforma tecnológica en la nube para negocios de hostelería, que incluye control de inventario, análisis predictivo, gestión de proveedores y automatización de procesos.</p>
                <p><strong>Cliente:</strong> Persona física o jurídica que accede y utiliza GASTROSTOCK.</p>
                <p><strong>Plan FREE:</strong> Nivel gratuito con funcionalidades básicas.</p>
                <p><strong>Plan PREMIUM:</strong> Nivel de pago con funcionalidades avanzadas basadas en inteligencia artificial.</p>
                <p><strong>SLA:</strong> Compromisos de disponibilidad y soporte asumidos por el Proveedor.</p>

                <p className="section-title">3. Objeto del Acuerdo</p>
                <p>Este Acuerdo regula el acceso y uso de GASTROSTOCK por parte del Cliente, a través de sus diferentes planes, con el objetivo de mejorar la eficiencia operativa en negocios de hostelería.</p>

                <p className="section-title">4. Planes de Servicio y Tarifas</p>
                <p><strong>4.1 Plan FREE:</strong><br />
                  - Gestión manual de inventario<br />
                  - Alertas básicas<br />
                  - Estadísticas mínimas de consumo</p>

                <p><strong>4.2 Plan PREMIUM:</strong><br />
                  - Asistente personal con IA<br />
                  - Predicción de demanda y oferta<br />
                  - Gestión de proveedores y pedidos<br />
                  - Actualización automática del inventario<br />
                  - Análisis detallado y alertas personalizadas</p>

                <p><strong>4.3 Tarifas:</strong><br />
                  - Precio estándar: 25 €/mes<br />
                  - Oferta para usuarios piloto: 2 meses gratis + 5 €/mes durante el primer año</p>

                <p><strong>4.4 A tener en cuenta:</strong>
                  El Proveedor podrá modificar los precios con al menos 30 días de antelación y justificación clara.</p>

                <p className="section-title">5. Condiciones de Uso</p>
                <p>El Cliente se compromete a utilizar la plataforma de forma lícita y responsable. Queda prohibido usar la plataforma para fines ilegales, interferir en su funcionamiento o compartir credenciales con terceros. El acceso podrá restringirse en caso de infracción grave, previa notificación y oportunidad de subsanación.</p>

                <p className="section-title">6. Disponibilidad y SLA</p>
                <p>Compromiso de disponibilidad del 99% mensual (excluyendo mantenimiento o fuerza mayor). Las incidencias críticas se resolverán en un plazo máximo de 48 horas laborables. El Cliente dispondrá de soporte técnico básico.</p>

                <p className="section-title">7. Tratamiento de Datos y Confidencialidad</p>
                <p>Los datos se utilizarán para personalizar la experiencia y mejorar el servicio mediante inteligencia artificial. No se compartirán datos personales sin consentimiento explícito o requerimiento legal. El Cliente podrá ejercer sus derechos según la normativa vigente.</p>

                <p className="section-title">8. Propiedad Intelectual</p>
                <p>Todo el contenido, software, diseños y algoritmos de la plataforma son propiedad exclusiva del Proveedor. Se prohíbe su copia, modificación o ingeniería inversa.</p>

                <p className="section-title">9. Limitación de Responsabilidad</p>
                <p>El Proveedor será responsable por daños derivados de negligencia grave o conducta dolosa. No será responsable por daños indirectos, interrupciones por causas ajenas o errores imputables al Cliente, salvo que la ley indique lo contrario. En ningún caso la responsabilidad total excederá el importe pagado por el Cliente en los 12 meses anteriores al incidente, salvo disposición legal contraria.</p>

                <p className="section-title">10. Modificaciones del Acuerdo</p>
                <p>Las modificaciones se comunicarán con antelación y justificación clara. El Cliente podrá revisar los cambios y, si lo desea, resolver el Acuerdo sin penalización. El uso continuado solo implica aceptación si se ha informado claramente y se ha dado un plazo razonable para rechazar.</p>

                <p className="section-title">11. Resolución del Acuerdo</p>
                <p>El Cliente podrá cancelar su cuenta en cualquier momento. El Proveedor podrá rescindir el Acuerdo en caso de incumplimiento grave, previa notificación y posibilidad de subsanación, salvo riesgo legal o técnico. Los datos del Cliente se eliminarán conforme a la política vigente.</p>

                <p className="section-title">12. Legislación y Jurisdicción</p>
                <p>Este Acuerdo se rige por la legislación española y respeta la normativa de protección del consumidor aplicable en el país de residencia del Cliente. Las controversias se resolverán ante los tribunales competentes.</p>

                <p className="section-title">13. Aceptación del Acuerdo</p>
                <p>Al marcar la casilla correspondiente, el Cliente declara haber leído, comprendido y aceptado el contenido íntegro del presente Acuerdo. Esta forma de aceptación tendrá el mismo valor legal que una firma manuscrita conforme a la legislación sobre contratación electrónica.</p>
              </div>

              <button
                onClick={() => {
                  setModalSeen(true);
                  setModalOpen(false);
                }}
                className="login-btn"
                disabled={!canAccept}
                style={{
                  backgroundColor: !canAccept ? "#999" : undefined,
                  cursor: !canAccept ? "not-allowed" : "pointer"
                }}
              >
                He leído y acepto
              </button>
            </div>
          </div>
        )}

        <button
          onClick={handleRegister}
          className={`login-btn ${(!modalSeen || loading) ? "disabled" : ""}`}
          disabled={loading || !modalSeen}
        >
          {loading ? "Registrando..." : "Registrarse"}
        </button>
      </div >
    </div >
  );
}

export default PantallaRegistroDueno;
