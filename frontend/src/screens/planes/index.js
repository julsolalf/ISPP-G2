import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { Bell, User } from "lucide-react"
import "../../css/planes/styles.css"
import { Accordion, AccordionSummary, AccordionDetails, Typography } from "@mui/material"
import { ChevronDown } from "lucide-react"

// Icons components
const ArrowLeftIcon = (props) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="16"
    height="16"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    {...props}
  >
    <path d="m12 19-7-7 7-7" />
    <path d="M19 12H5" />
  </svg>
)

const CheckCircleIcon = (props) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="16"
    height="16"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    {...props}
  >
    <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
    <path d="m9 11 3 3L22 4" />
  </svg>
)

const FileTextIcon = (props) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="18"
    height="18"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    {...props}
  >
    <path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z" />
    <polyline points="14 2 14 8 20 8" />
    <line x1="16" x2="8" y1="13" y2="13" />
    <line x1="16" x2="8" y1="17" y2="17" />
    <line x1="10" x2="8" y1="9" y2="9" />
  </svg>
)

const InstagramIcon = (props) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="20"
    height="20"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    {...props}
  >
    <rect width="20" height="20" x="2" y="2" rx="5" ry="5" />
    <path d="M16 11.37A4 4 0 1 1 12.63 8 4 4 0 0 1 16 11.37z" />
    <line x1="17.5" x2="17.51" y1="6.5" y2="6.5" />
  </svg>
)

const TikTokIcon = (props) => (
  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="currentColor" {...props}>
    <path d="M19.321 5.289a5.32 5.32 0 0 1-3.094 1.026 5.335 5.335 0 0 1-3.292-1.131v5.069c0 3.56-2.883 6.444-6.444 6.444a6.414 6.414 0 0 1-3.678-1.159 6.401 6.401 0 0 1-2.767-5.286 6.406 6.406 0 0 1 6.444-6.444c.339 0 .675.027 1.002.08v3.1a3.358 3.358 0 0 0-1.002-.153 3.352 3.352 0 0 0-3.348 3.348 3.352 3.352 0 0 0 3.348 3.348c1.827 0 3.418-1.51 3.348-3.417l-.034-11.155h3.012a5.32 5.32 0 0 0 5.313 5.313v-3.603h.002Z" />
  </svg>
)

// Dropdown component
const Dropdown = ({ isOpen, onClose, title, children }) => {
  if (!isOpen) return null

  return (
    <>
      <div className="dropdown-menu">
        {title && <div className="dropdown-header">{title}</div>}
        <div className="dropdown-content">{children}</div>
      </div>
      <div
        style={{
          position: "fixed",
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          zIndex: 40,
        }}
        onClick={onClose}
      />
    </>
  )
}

// Modal component
const Modal = ({ isOpen, onClose, title, description, children, footer, className }) => {
  if (!isOpen) return null

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className={`modal ${className || ""}`} onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          {title && <h3 className="modal-title">{title}</h3>}
          {description && <p className="modal-description">{description}</p>}
        </div>
        <div className="modal-content">{children}</div>
        {footer && <div className="modal-footer">{footer}</div>}
      </div>
    </div>
  )
}

// Feature item component
const FeatureItem = ({ children }) => (
  <li className="feature-item">
    <span className="feature-icon">
      <CheckCircleIcon />
    </span>
    <span>{children}</span>
  </li>
)

// Plan card component
const PlanCard = ({ title, price, features, buttonText, isActive, onClick }) => (
  <div className={`plan-card ${isActive ? "active" : ""}`}>
    <div className="card-header">
      {isActive && <span className="plan-badge">Plan Actual</span>}
      <h3 className="plan-title">{title}</h3>
      <div className="plan-price">{price}</div>
    </div>
    <div className="card-content">
      <ul className="feature-list">
        {features.map((feature, index) => (
          <FeatureItem key={index}>{feature}</FeatureItem>
        ))}
      </ul>
    </div>
    <div className="card-footer">
      <button className={`button ${isActive ? "button-outline" : "button-primary"}`} onClick={onClick}>
        {buttonText}
      </button>
    </div>
  </div>
)

// Terms and Conditions sections
const termsAndConditionsSections = [
  {
    title: "1. Introducción",
    content:
      "Este Acuerdo regula el acceso y uso de la plataforma GASTROSTOCK. Al continuar, el Cliente acepta estos términos.",
  },
  {
    title: "2. Definiciones",
    content:
      "GASTROSTOCK: Plataforma tecnológica en la nube. Cliente: Persona física o jurídica usuaria. Plan FREE: Funcionalidades básicas. Plan PREMIUM: Funcionalidades avanzadas. SLA: Compromisos de disponibilidad y soporte.",
  },
  {
    title: "3. Objeto del Acuerdo",
    content: "El objetivo es mejorar la eficiencia operativa de negocios de hostelería mediante el uso de GASTROSTOCK.",
  },
  {
    title: "4. Planes y Tarifas",
    content:
      "Plan FREE: inventario manual, alertas básicas, estadísticas mínimas. Plan PREMIUM: IA, predicción de demanda, gestión de pedidos, actualización automática, alertas avanzadas. Precio: 25€/mes. Oferta: 2 meses gratis + 5€/mes durante el primer año.",
  },
  {
    title: "5. Condiciones de Uso",
    content:
      "El Cliente debe usar la plataforma de forma lícita. Está prohibido el uso indebido, compartir credenciales, o manipular la plataforma.",
  },
  {
    title: "6. Disponibilidad y SLA",
    content:
      "GASTROSTOCK garantiza un 99% de disponibilidad mensual. Las incidencias críticas serán resueltas en un máximo de 48 horas laborables.",
  },
  {
    title: "7. Tratamiento de Datos",
    content:
      "Los datos serán usados para mejorar la experiencia del usuario. No se compartirán sin consentimiento expreso, salvo obligación legal.",
  },
  {
    title: "8. Propiedad Intelectual",
    content:
      "Todos los elementos de la plataforma son propiedad exclusiva del Proveedor. Prohibida su copia, modificación o ingeniería inversa.",
  },
  {
    title: "9. Limitación de Responsabilidad",
    content:
      "El Proveedor no será responsable de daños indirectos, fallos por causas ajenas o mal uso por parte del Cliente. El límite de responsabilidad es el importe pagado en los últimos 12 meses.",
  },
  {
    title: "10. Modificaciones del Acuerdo",
    content:
      "Cualquier cambio será notificado con antelación. El uso continuado implica aceptación si se informa adecuadamente.",
  },
  {
    title: "11. Cancelación",
    content:
      "El Cliente puede cancelar en cualquier momento. El Proveedor puede cancelar ante incumplimientos graves. Los datos serán eliminados según la política vigente.",
  },
  {
    title: "12. Legislación Aplicable",
    content:
      "Este acuerdo se rige por la legislación española. Cualquier disputa se resolverá ante los tribunales correspondientes.",
  },
  {
    title: "13. Aceptación",
    content:
      "El uso de la plataforma implica aceptación del acuerdo, con el mismo valor legal que una firma manuscrita.",
  },
]

// Main component
function PantallaPlanes() {
  const navigate = useNavigate()
  const [showNotifications, setShowNotifications] = useState(false)
  const [showUserOptions, setShowUserOptions] = useState(false)
  const [showLogoutModal, setShowLogoutModal] = useState(false)
  const [showTermsModal, setShowTermsModal] = useState(false)
  const [isFreePlanActive] = useState(true) //, setIsFreePlanActive
  const [isPremiumPlanActive] = useState(false) // setIsPremiumPlanActive
  const [expandedSection, setExpandedSection] = useState(null)

  // Handle logout
  const handleLogout = () => {
    localStorage.removeItem("userToken")
    navigate("/")
  }

  // Handle accordion change
  const handleAccordionChange = (panel) => (event, isExpanded) => {
    setExpandedSection(isExpanded ? panel : null)
  }

  // Free plan features
  const freePlanFeatures = ["Gestor del inventario", "Alertas personalizadas", "Estadísticas mínimas"]

  // Premium plan features
  const premiumPlanFeatures = [
    "Todas las funciones del plan Free",
    "IA para gestión optimizada",
    "Análisis y predicción de demanda",
    "Gestor de proveedores y restock",
    "Alertas avanzadas",
  ]

  return (
    <div
      className="page-container"
      style={{
        backgroundImage: `url(${process.env.PUBLIC_URL}/background-spices.jpg)`,
      }}
    >
      <div className="main-container">
        {/* Header */}
        <div className="header">
          <button className="back-button" onClick={() => navigate(-1)}>
            <ArrowLeftIcon />
            <span>Volver</span>
          </button>

          <div className="header-actions">
            <div className="dropdown">
              <Bell
                className="icon-button"
                onClick={() => {
                  setShowNotifications(!showNotifications)
                  if (showUserOptions) setShowUserOptions(false)
                }}
                aria-label="Notificaciones"
              />
              
              <Dropdown isOpen={showNotifications} onClose={() => setShowNotifications(false)} title="Notificaciones">
                <div className="dropdown-empty">No tienes notificaciones nuevas</div>
              </Dropdown>
            </div>

            <div className="dropdown">
              <User
                className="icon-button"
                onClick={() => {
                  setShowUserOptions(!showUserOptions)
                  if (showNotifications) setShowNotifications(false)
                }}
                aria-label="Opciones de usuario"
              >
              </User>
              <Dropdown isOpen={showUserOptions} onClose={() => setShowUserOptions(false)} title="Usuario">
                <button
                  className="dropdown-item"
                  onClick={() => {
                    setShowUserOptions(false)
                    navigate("/perfil")
                  }}
                >
                  Mi perfil
                </button>
                <div className="dropdown-divider"></div>
                <button
                  className="dropdown-item"
                  onClick={() => {
                    setShowUserOptions(false)
                    setShowLogoutModal(true)
                  }}
                >
                  Cerrar sesión
                </button>
              </Dropdown>
            </div>
          </div>
        </div>

        {/* Logo and title */}
        <div className="logo-section">
          <img src={`${process.env.PUBLIC_URL}/gastrostockLogoSinLetra.png`} alt="GastroStock Logo" className="logo" />
          <h1 className="title">GastroStock</h1>
          <p className="subtitle">Planes y Precios</p>
        </div>

        {/* Plans */}
        <div className="plans-grid">
          <PlanCard
            title="Plan Free"
            price="0€/mes"
            features={freePlanFeatures}
            buttonText={isFreePlanActive ? "PLAN ACTUAL" : "ACTIVAR"}
            isActive={isFreePlanActive}
            onClick={() => navigate("/plan-actual")}
          />

          <PlanCard
            title="Plan Premium"
            price="25€/mes"
            features={premiumPlanFeatures}
            buttonText={isPremiumPlanActive ? "PLAN ACTUAL" : "MEJORAR"}
            isActive={isPremiumPlanActive}
            onClick={() => navigate("/plan-activar-premium")}
          />
        </div>

        {/* Terms and conditions button */}
        <button className="terms-button" onClick={() => setShowTermsModal(true)}>
          <FileTextIcon />
          <span>Ver Términos y Condiciones</span>
        </button>

        {/* Social media section */}
        <div className="social-section">
          <p className="social-title">Síguenos en nuestras redes sociales:</p>
          <div className="social-icons">
            <a
              href="https://www.instagram.com/gastro.stock"
              target="_blank"
              rel="noopener noreferrer"
              className="social-icon"
              aria-label="Instagram"
            >
              <InstagramIcon />
            </a>
            <a
              href="https://www.tiktok.com/@gastro.stock"
              target="_blank"
              rel="noopener noreferrer"
              className="social-icon"
              aria-label="TikTok"
            >
              <TikTokIcon />
            </a>
          </div>
        </div>

        {/* Logout modal */}
        <Modal
          isOpen={showLogoutModal}
          onClose={() => setShowLogoutModal(false)}
          title="Cerrar sesión"
          description="¿Está seguro que desea abandonar la sesión?"
          footer={
            <>
              <button className="button button-outline" onClick={() => setShowLogoutModal(false)}>
                Cancelar
              </button>
              <button className="button button-primary" onClick={handleLogout} style={{ marginLeft: "0.5rem" }}>
                Cerrar sesión
              </button>
            </>
          }
        />

        {/* Terms modal */}
        <Modal
          isOpen={showTermsModal}
          onClose={() => setShowTermsModal(false)}
          title="Términos y Condiciones"
          className="terms-modal"
          footer={
            <button className="button button-primary" onClick={() => setShowTermsModal(false)}>
              Cerrar
            </button>
          }
        >
          <div className="terms-container">
            <div className="terms-intro">
              <p>
                Bienvenido a GastroStock. Estos términos y condiciones establecen las reglas y regulaciones para el uso
                de nuestra plataforma.
              </p>
            </div>

            <div className="terms-sections">
              {termsAndConditionsSections.map((section, index) => (
                <Accordion
                  key={index}
                  expanded={expandedSection === index}
                  onChange={handleAccordionChange(index)}
                  className="terms-accordion"
                >
                  <AccordionSummary
                    expandIcon={<ChevronDown className="terms-accordion-icon" />}
                    className="terms-accordion-summary"
                  >
                    <Typography className="terms-accordion-title">{section.title}</Typography>
                  </AccordionSummary>
                  <AccordionDetails className="terms-accordion-details">
                    <Typography className="terms-accordion-content">{section.content}</Typography>
                  </AccordionDetails>
                </Accordion>
              ))}
            </div>

            <div className="terms-footer">
              <p>Última actualización: 26 de marzo de 2025</p>
              <p>
                Si tiene alguna pregunta sobre estos términos, póngase en contacto con nosotros en{" "}
                <a href="mailto:gastrostock@gmail.com" className="terms-link">
                  gastrostock@gmail.com
                </a>
              </p>
            </div>
          </div>
        </Modal>
      </div>
    </div>
  )
}

export default PantallaPlanes