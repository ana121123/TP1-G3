import React, { useState, useEffect }  from "react";
import "./AltaTitularForm.css"; 
import ResumenTitular from "./ResumenTitular";

import axios from "axios";


const AltaTitularForm = () => { 
  const formularioInicial = {
    tipoDocumento: "",
    nroDocumento: "",
    apellido: "",
    nombre: "",
    fechaNacimiento: "",
    direccion: "",
    grupoSanguineo: "",
    factorRH: "",
    donante: "",
    cuit: "",
    localidad: "",
    observaciones: "",
  };
  const [formData, setFormData] = useState(formularioInicial);
  const [titularRegistrado, setTitularRegistrado] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);

  const [localidades, setLocalidades] = useState([]);
  const [localidadSeleccionada, setLocalidadSeleccionada] = useState("");

  const tiposDocumento = ["DNI (Documento Nacional de Identidad)", "LE (Libreta de Enrolamiento)", "LC (Libreta Cívica)", "CI (Cédula de Identidad)", "Otro"];
  const gruposSanguineos = ["A", "B", "AB", "O"];
  const factores = ["+", "-"];
  const [errores, setErrores] = useState({});
  //const [mensaje, setMensaje] = useState(null); // para mensajes al usuario (éxito o error)
  const [mensajeError, setMensajeError] = useState(null);

  const extraerTipoDocumento = (valor) => {
    return valor.split('(')[0].trim(); // Se queda con "DNI" o "LC", y saca espacios extra
  };

  useEffect(() => {
    axios.get("/api/localidades/listar")
      .then(response => {
        setLocalidades(response.data);
      })
      .catch(error => {
        console.error("Error al obtener las localidades:", error);
      });
  }, []);

  useEffect(() => {
      if (mensajeError) {
        alert(mensajeError);
      }
    }, [mensajeError]);


  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "cuit") {
      const cuitFormateado = formatearCUIT(value);
      setFormData((prev) => ({ ...prev, [name]: cuitFormateado }));
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const valido = validar();
    console.log("¿Formulario válido?", valido); 
    setMensajeError("");
  
    if (valido) {
      
       const datosParaEnviar = {
          nombre: formData.nombre,
          apellido: formData.apellido,
          tipoDocumento: extraerTipoDocumento(formData.tipoDocumento),
          numeroDocumento: formData.nroDocumento,
          fechaNacimiento: formData.fechaNacimiento,
          direccion: formData.direccion,
          grupoSanguineo: formData.grupoSanguineo,
          factorRH: formData.factorRH === "+" ? "Positivo" : "Negativo",
          donante: formData.donante === "SI" ? true : false,
          cuit: formData.cuit.replace(/-/g, ""),
          localidad: localidadSeleccionada,
          observaciones: formData.observaciones,
        };
      try {
        console.log("Datos a enviar:", datosParaEnviar);
        const response = await axios.post("/api/titular/crear", datosParaEnviar);
        if (response.status === 200 || response.status === 201) {
          //setMensaje("Titular registrado correctamente.");
          setTitularRegistrado(response.data);
          setModalOpen(true);
          setFormData(formularioInicial);
          setLocalidadSeleccionada("");
          setErrores({});
        } else {
          setMensajeError("Error inesperado al registrar titular.");
        }
      } catch (error) {
        /*if (error.response && error.response.data && error.response.data.message) {
          setMensajeError(error.response.data.message);
        } else {
          setMensajeError("Error al comunicarse con el servidor.");
        }*/
      /*if (error.response && error.response.status === 400) {
        setErrores(error.response.data); // acá se guardan los errores por campo
      } else {
        setMensajeError("Error al comunicarse con el servidor.");
      }*/
      if (error.response && error.response.status === 400) {
        const erroresBackend = error.response.data;

        // Detectar si hay errores específicos de campos (excepto 'documento' o 'globalError')
        const camposError = Object.keys(erroresBackend).filter(
          key => key !== 'documento' && key !== 'globalError'
        );

        if (camposError.length > 0) {
          // Hay errores en campos => mostrar mensaje genérico
          setMensajeError("Hay errores en los campos o en el formato, revisá los datos.");
          //setErrores({});  // Limpio errores detallados para que no se muestren
        } else if (erroresBackend.documento) {
          // Mostrar error específico de titular duplicado
          setMensajeError(erroresBackend.documento);
          //setErrores({});
        } else if (erroresBackend.globalError) {
          // Mostrar error general (parseo, etc)
          setMensajeError(erroresBackend.globalError);
          //setErrores({});
        } else {
          // Por si hay otro tipo de error, mostrar algo por defecto
          setMensajeError("Error en la solicitud.");
          //setErrores({});
        }

      } else {
        setMensajeError("Error al comunicarse con el servidor.");
      }
      }
    }
  };

  // Validaciones
  const isValidDate = (dateString) => {
    const date = new Date(dateString);
    const now = new Date();
    return !isNaN(date.getTime()) && date <= now && date.getFullYear() > 1900;
  };

  const isValidDNI = (nroDocumento) => /^\d{7,8}$/.test(nroDocumento);

  const isValidCUIT = (cuit) => /^\d{2}-\d{8}-\d$/.test(cuit);

  const formatearCUIT = (valor) => {
    // Quitamos todo lo que no sea número
    const numeros = valor.replace(/\D/g, '');
    if (numeros.length <= 2) return numeros;
    if (numeros.length <= 10) return `${numeros.slice(0, 2)}-${numeros.slice(2)}`;
    if (numeros.length <= 11) return `${numeros.slice(0, 2)}-${numeros.slice(2, 10)}-${numeros.slice(10)}`;
    return `${numeros.slice(0, 2)}-${numeros.slice(2, 10)}-${numeros.slice(10, 11)}`;
  };

  const validar = () => {
    const errores = {};
    if (!isValidDate(formData.fechaNacimiento)) {
      errores.fechaNacimiento = 'Fecha de nacimiento inválida';
    }

    if (!isValidDNI(formData.nroDocumento)) {
      errores.nroDocumento = 'DNI inválido. Debe tener 7 u 8 dígitos numéricos';
    }else if (formData.nroDocumento.length > 50) {
      errores.nroDocumento = "El número de documento puede tener hasta 50 caracteres.";
    } else if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9]+$/.test(formData.nroDocumento)) {
      errores.nroDocumento = "El número de documento solo puede contener letras y números.";
    }

    if (!isValidCUIT(formData.cuit)) {
      errores.cuit = 'CUIT inválido';
    } else {
        const cuitCentral = formData.cuit.split("-")[1];
        const dni = formData.nroDocumento.padStart(8, "0"); // Asegura 8 dígitos
        if (cuitCentral !== dni) {
          errores.cuit = 'El CUIT no coincide con el DNI ingresado';
        } else if (formData.cuit.length > 50) {
            errores.cuit = "El CUIT puede tener hasta 50 caracteres.";
          } else if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9-]+$/.test(formData.cuit)) {
            errores.cuit = "El CUIT solo puede contener letras, números y guiones.";
          }
    }
    if (!/^[a-zA-ZÁÉÍÓÚÑáéíóúñ\s]+$/.test(formData.nombre) || formData.nombre.length < 2 || formData.nombre.length > 50){
      errores.nombre = 'Nombre inválido';
    }

    if (!/^[a-zA-ZÁÉÍÓÚÑáéíóúñ\s]+$/.test(formData.apellido) || formData.apellido.length < 2 || formData.apellido.length > 50) {
      errores.apellido = 'Apellido inválido';
    }

    const tipoDoc = extraerTipoDocumento(formData.tipoDocumento);
    if (tipoDoc.length > 50) {
      errores.tipoDocumento = "El tipo de documento puede tener hasta 50 caracteres.";
    } else if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/.test(tipoDoc)) {
        errores.tipoDocumento = "El tipo de documento solo puede contener letras y espacios.";
    }

    if (formData.direccion.length > 50) {
      errores.direccion = "La dirección puede tener hasta 50 caracteres.";
    } else if (!/^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\s]+$/.test(formData.direccion)) {
      errores.direccion = "La dirección solo puede contener letras, números y espacios.";
    }

    if (formData.observaciones && formData.observaciones.length > 500) {
      errores.observaciones = "Las observaciones pueden tener hasta 500 caracteres.";
    }

    setErrores(errores);
    return Object.keys(errores).length === 0;
  };



  return (
    <>
    <form onSubmit={handleSubmit} className="form-container">
      <h2>Registrar Titular</h2>
      {mensajeError && <p className="mensaje-error">{mensajeError}</p>}

      <div className="formulario-grid">
         <div className="campo">
          <label htmlFor="tipoDocumento">Tipo de Documento</label>
          <select id="tipoDocumento" name="tipoDocumento" value={formData.tipoDocumento} onChange={handleChange} required>
            <option value="">Seleccionar</option>
            {tiposDocumento.map((g) => (
              <option key={g} value={g}>{g}</option>
            ))}
          </select>
           {errores.tipoDocumento && <p className="error">{errores.tipoDocumento}</p>}
         </div>

         <div className="campo">
            <label htmlFor="nroDocumento">Número de Documento</label>
            <input id="nroDocumento" type="text" name="nroDocumento" value={formData.nroDocumento} onChange={handleChange} required/>
            {errores.nroDocumento && <p className="error">{errores.nroDocumento}</p>}
         </div>

          <div className="campo">
            <label htmlFor="apellido">Apellido</label>
            <input id="apellido" type="text" name="apellido" value={formData.apellido} onChange={handleChange} required/>
            {errores.apellido && <p className="error">{errores.apellido}</p>}
          </div>

          <div className="campo">
            <label htmlFor="nombre">Nombre</label>
            <input id="nombre" type="text" name="nombre" value={formData.nombre} onChange={handleChange} required/>
            {errores.nombre && <p className="error">{errores.nombre}</p>}
          </div>

          <div className="campo">
            <label htmlFor="fechaNacimiento">Fecha de Nacimiento</label>
            <input id="fechaNacimiento"type="date" name="fechaNacimiento" value={formData.fechaNacimiento} onChange={handleChange} required/>
            {errores.fechaNacimiento && <p className="error">{errores.fechaNacimiento}</p>}
          </div>

          <div className="campo">
            <label htmlFor="direccion">Dirección</label>
            <input id="direccion" type="text" name="direccion" value={formData.direccion} onChange={handleChange} required/>
             {errores.direccion && <p className="error">{errores.direccion}</p>}
          </div>

          <div className="campo">
            <label htmlFor="grupoSanguineo">Grupo Sanguíneo</label>
            <select id="grupoSanguineo" name="grupoSanguineo" value={formData.grupoSanguineo} onChange={handleChange} required>
              <option value="">Seleccionar</option>
              {gruposSanguineos.map((g) => (
                <option key={g} value={g}>{g}</option>
              ))}
            </select>
          </div>

          <div className="campo">
            <label htmlFor="factorRH">Factor RH</label>
            <select id="factorRH" name="factorRH" value={formData.factorRH} onChange={handleChange} required>
              <option value="">Seleccionar</option>
              {factores.map((f) => (
                <option key={f} value={f}>{f}</option>
              ))}
            </select>
          </div>

          <div className="campo">
            <label htmlFor="donante">Donante de órganos</label>
            <select id="donante" name="donante" value={formData.donante} onChange={handleChange} required>
              <option value="">Seleccionar</option>
              <option value="SI">Sí</option>
              <option value="NO">No</option>
            </select>
          </div>

          <div className="campo">
            <label htmlFor="cuit">CUIT</label>
            <input id="cuit" type="text" name="cuit" value={formData.cuit} onChange={handleChange} required/>
            {errores.cuit && <p className="error">{errores.cuit}</p>}
          </div>

          <div className="campo">
              <label htmlFor="localidad">Localidad:</label>
            <select
            id="localidad"
            value={localidadSeleccionada}
            onChange={(e) => setLocalidadSeleccionada(e.target.value)} required>
            <option value="">Seleccionar</option>
            {localidades.map((loc) => (
              <option key={loc.id} value={loc.name}>
                {loc.name}  {/* <-- name, no nombre */}
              </option>
            ))}
          </select>
          </div>


          <div className="campo" style={{ gridColumn: "1 / -1" }}>
            <label htmlFor="observaciones">Observaciones</label>
            <textarea id="observaciones" name="observaciones" value={formData.observaciones} onChange={handleChange} />
             {errores.observaciones && <p className="error">{errores.observaciones}</p>}
          </div>
        
           <div className="campo" style={{ gridColumn: "1 / -1", textAlign: "center" }}>
              <button type="submit">Registrar titular</button>
          </div>
      </div>
    </form>

  <ResumenTitular
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        titular={titularRegistrado}
        />
    </>
//ver que el resumen no pueda cerrarse si tocas otra cosa, que quede bloq hasta que se aprieta la cruz.
);
};

export default AltaTitularForm;
