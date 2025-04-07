import { useState } from "react";
import { Navbar, NavLink, NavItem, Nav, NavbarToggler, Collapse} from "reactstrap";
import { useNavigate, Link } from 'react-router-dom';
import "./css/global/navbar.css"

function AppNavbar() {
  const navigate = useNavigate();
  const [collapsed, setCollapsed] = useState(true);

  const toggleNavbar = () => setCollapsed(!collapsed);

  let left = <></>;
  let right = <></>;

  right = (
    <>
      <NavItem>
        <NavLink style={{ color: "white" }} id="dashboard" tag={Link} to="/dashboard">
          Dashboard
        </NavLink>
      </NavItem>
      <NavItem>
        <NavLink style={{ color: "white" }} id="inventario" tag={Link} to="/inventario">
          Inventario
        </NavLink>
      </NavItem>
      <NavItem>
        <NavLink style={{ color: "white" }} id="ventas" tag={Link} to="/ventas">
          Ventas
        </NavLink>
      </NavItem>
      <NavItem>
        <NavLink style={{ color: "white" }} id="empleados" tag={Link} to="/empleados">
          Empleados
        </NavLink>
      </NavItem>
      <NavItem>
        <NavLink style={{ color: "white" }} id="proveedores" tag={Link} to="/proveedores">
          Proveedores
        </NavLink>
      </NavItem>
      <NavItem>
        <NavLink style={{ color: "white" }} id="info" tag={Link} to="/masInformacion">
          Sobre GastroStock
        </NavLink>
      </NavItem>
    </>
  );

  const handleClick = () => {
    navigate('/');
  };

  return (
    <div>
      <Navbar expand="md" dark color="dark">
        <div onClick={handleClick} className="navbar-brand">
          GastroStock
        </div>
        <NavbarToggler onClick={toggleNavbar} className="ms-2" />
        <Collapse isOpen={!collapsed} navbar>
          <Nav className="me-auto mb-2 mb-lg-0" navbar>
            {left}
          </Nav>
          <Nav className="ms-auto mb-2 mb-lg-0" navbar>
            {right}
          </Nav>
        </Collapse>
      </Navbar>
    </div>
  );
}

export default AppNavbar;
