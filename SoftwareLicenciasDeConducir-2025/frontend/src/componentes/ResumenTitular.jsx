import React from "react";
import "./ResumenTitular.css";

const ResumenTitular = ({ titular, isOpen, onClose }) => {
  if (!isOpen) return null;

  return (
    <div className="resumen-overlay" onClick={onClose}>
      <div className="resumen-content" onClick={e => e.stopPropagation()}>
        <button className="resumen-close" onClick={onClose}>X</button>
        <h2>Resumen del Titular</h2>
        {titular ? (
          <div>
            <p><strong>Tipo de Documento:</strong> {titular.tipoDocumento}</p>
            <p><strong>Documento:</strong> {titular.numeroDocumento}</p>
            <p><strong>Apellido:</strong> {titular.apellido}</p>
            <p><strong>Nombre:</strong> {titular.nombre}</p>
            <p><strong>Fecha de nacimiento:</strong> {titular.fechaNacimiento}</p>
            <p><strong>Dirección:</strong> {titular.direccion}</p>
            <p><strong>Grupo Sanguíneo:</strong> {titular.grupoSanguineo}</p>
            <p><strong>Factor RH:</strong> {titular.factorRH}</p>
            <p><strong>Donante:</strong> {titular.donante ? "Sí" : "No"}</p>
            <p><strong>Cuit:</strong> {titular.cuit}</p>
            <p><strong>Localidad:</strong> {titular.localidad}</p>
            <p><strong>Observaciones:</strong> {titular.observaciones}</p>
            
          </div>
        ) : (
          <p>No hay datos para mostrar.</p>
        )}
      </div>
    </div>
  );
};

export default ResumenTitular;
