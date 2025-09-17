import React from 'react';
import { Link } from 'react-router-dom';

import './PaginaPrincipal.css';

function PaginaPrincipal() {
  return (
    <div className="pagina-principal">

      <main  style={{ flex: 1 }} className="bienvenida-contenedor">
        <div className="bienvenida-texto">
          <h2>
            Bienvenid@ al<br />
            <span className="titulo-color">Sistema de Emisión de <br />Licencias de Santa Fe</span>
          </h2>
          <div className="menu-botones">
            <Link to="/emitir-licencia" className="boton-link">Emitir Licencia</Link>
            <Link to="/alta-titular" className="boton-link">Dar de alta Titular</Link>
            <Link to="/licencias-vigentes" className="boton-link">Licencias Vigentes</Link>
            <Link to="/licencias-expiradas" className="boton-link">Licencias Expiradas</Link>
            <Link to="/alta-usuario" className="boton-link">Usuarios Adm</Link>
          </div>
        </div>

        <div className="bienvenida-imagen">
          <img src="/imagen2.png" alt="Imagen ilustrativa" />
        </div>
      </main>

    </div>
  );
}

export default PaginaPrincipal;
