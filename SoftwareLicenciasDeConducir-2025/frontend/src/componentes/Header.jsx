import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';

function Header() {
  return (
    <header className="header">
        <Link to="/" className="logo">
          <img src="/gob_santafe.png" alt="Logo Santa Fe" />
        </Link>
      <h1 className="titulo">Sistema de Emisión de Licencias</h1>
      <Link to="/login" className="login">
          <img src="/avatar.png" alt="Login" />
      </Link>
    </header>
  );
}

export default Header;