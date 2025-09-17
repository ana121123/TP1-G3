import React, { useState, useEffect }  from "react";
import { useNavigate } from 'react-router-dom';
import './EmitirLicenciaForm.css';
import { Link } from 'react-router-dom';
import axios from "axios";

function EmitirLicenciaForm() {
  const navigate = useNavigate();

  const [tipoDocumento, setTipoDocumento] = useState('');
  const [numeroDocumento, setNumeroDocumento] = useState('');
  const [titular, setTitular] = useState(null);
  const [noEncontrado, setNoEncontrado] = useState(false);
  const [errores, setErrores] = useState({});
  const [mensajeError, setMensajeError] = useState(null);
  const [licenciaEmitida, setLicenciaEmitida] = useState(null);

  const [edad, setEdad] = useState('');
  //const [clase, setClase] = useState('');
  const [observaciones, setObservaciones] = useState('');


  const [localidades, setLocalidades] = useState([]);
  const [localidadSeleccionada, setLocalidadSeleccionada] = useState("");

  const [clases, setClases] = useState([]);
  const [claseSeleccionada, setClaseSeleccionada] = useState("");

  const [subclases, setSubclases] = useState([]);
  const [subclaseSeleccionada, setSubclaseSeleccionada] = useState("");
  

  const tiposDocumento = ["DNI", "LE", "LC", "CI", "Otro"];

  const [admin, setAdmin] = useState({
    nombre: '',
    apellido: '',
    usuario: 'admin',
  });

  const soloLetrasYEspacios = (texto) => /^[A-Za-zÁÉÍÓÚÑáéíóúñ\s]+$/.test(texto.trim());

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
    axios.get("/api/clases/listarCategorias")
      .then(response => {
        console.log("Categorias recibidas:", response.data);
        setClases(response.data);
      })
      .catch(error => {
        console.error("Error al obtener las clases:", error);
      });
  }, []);

  useEffect(() => {
    axios.get("/api/clases/listarSubcategorias")
      .then(response => {
        setSubclases(response.data);
      })
      .catch(error => {
        console.error("Error al obtener las subclases:", error);
      });
  }, []);

  useEffect(() => {
    if (mensajeError) {
      alert(mensajeError);
    }
  }, [mensajeError]);

  useEffect(() => {
  if (licenciaEmitida) {
      alert(`Licencia emitida correctamente`);
      navigate('/licencia', { state: { titular: licenciaEmitida, admin } });
    }
  }, [licenciaEmitida, navigate, admin]);

  const buscarTitular = async () => {
    setTitular(null);
    setNoEncontrado(false);

    try {
      const response = await axios.get('/api/titular/obtener', {
        params: {
          tipoDocumento: tipoDocumento,
          numeroDocumento: numeroDocumento.trim(),
        },
      });

      if (response.data) {
        setTitular(response.data);
      } else {
        setNoEncontrado(true);
      }
    } catch (error) {
      console.error('Error al buscar titular:', error);
      setNoEncontrado(true);
    }
  };

  
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!titular) {
      alert('Debe buscar y seleccionar un titular primero.');
      return;
    }

    // Función de validación
    const validar = () => {
      const errores = {};
      const edadMinima = ['C', 'D', 'E'].includes(claseSeleccionada) ? 21 : 17;

      if (!soloLetrasYEspacios(admin.nombre)) {
        errores.nombre = 'El nombre solo debe contener letras y espacios';
      }
      if (!soloLetrasYEspacios(admin.apellido)) {
        errores.apellido = 'El apellido solo debe contener letras y espacios';
      }
      if (edad < edadMinima) {
        errores.edad = `Para la clase ${claseSeleccionada}, la edad mínima es ${edadMinima} años`;
      }
      if (!claseSeleccionada) {
        errores.clase = 'Debe seleccionar una clase de licencia.';
      }
      // Aquí puedes agregar más validaciones si necesitas

      setErrores(errores);
      return Object.keys(errores).length === 0;
    };

    // Validar datos
    const valido = validar();
    console.log("¿Formulario válido?", valido);
    setMensajeError("");

    if (!valido) {
      return; // Si no es válido, no continuar
    }

    // Preparar datos para enviar
    const datosParaEnviar = {
      clase: {
        categoria:claseSeleccionada,
        subcategoria:subclaseSeleccionada
      },
      observaciones: observaciones,
      tipoDocumento: titular.tipoDocumento,
      numeroDocumento: titular.numeroDocumento,
      edadTitular: parseInt(edad),
      username: admin.usuario,
      localidad: localidadSeleccionada
    };

    try {
      console.log("Datos a enviar:", datosParaEnviar);
      const response = await axios.post("/api/licencia/emitir", datosParaEnviar);

      if (response.status === 200 || response.status === 201) {
        setLicenciaEmitida(response.data);
        //navigate('/licencia', { state: { titular: response.data, admin } });
        // Reseteo campos luego del envío exitoso
        setLocalidadSeleccionada("");
        setClaseSeleccionada("");
        setSubclaseSeleccionada("");
        setEdad("");
        setObservaciones("");
        setErrores({});
      } else {
        setMensajeError("Error inesperado al emitir licencia.");
      }

    } catch (error) {
      if (error.response && error.response.status === 400) {
        const erroresBackend = error.response.data;
        console.log("Error 400: ", erroresBackend);

        // Buscá el primer mensaje disponible en el objeto de error
        const mensajeEspecifico = Object.values(erroresBackend)?.[0];

        if (mensajeEspecifico) {
          setMensajeError(mensajeEspecifico);
        } else {
          setMensajeError("Error en la solicitud.");
        }
      } else {
        setMensajeError("Error al comunicarse con el servidor.");
      }
    }
  };

    

  return (
    <div className="form-container">
      <div className="busqueda-dni">
        <h2>Emitir Licencia de Conducir</h2>
        {mensajeError && <p className="mensaje-error">{mensajeError}</p>}

        {/* Búsqueda de titular */}
        <label>Buscar titular por número de documento</label>
        <div className="input-con-boton">

           <select value={tipoDocumento} onChange={(e) => setTipoDocumento(e.target.value)}>
            <option value="">Seleccionar</option>
            {tiposDocumento.map((g) => (
              <option key={g} value={g}>{g}</option>
            ))}
          </select>

          <input
            type="text"
            placeholder="Ingrese el número de documento"
            value={numeroDocumento}
            onChange={(e) => setNumeroDocumento(e.target.value)}
          />
          <button type="button" onClick={buscarTitular}>
            Buscar
          </button>
        </div>
      </div>

      {noEncontrado && (
          <div className="no-encontrado">
            <p>No se encontró ningún titular con ese número de documento.</p>
            <Link to="/alta-titular" className="boton-link">Registrar Titular</Link>
          </div>
      )}

    <hr />

        {/* Formulario solo si hay titular */}
        {titular && (
          <form onSubmit={handleSubmit}>
            <div className="form-columnas">
              {/* Columna izquierda: Titular */}
              <div className="columna">
                <h3>Datos del Titular</h3>
                <p><strong>Nombre:</strong> {titular.nombre}</p>
                <p><strong>Apellido:</strong> {titular.apellido}</p>
                <p><strong>Nro Documento:</strong> {titular.numeroDocumento}</p>

                <label>Clase de Licencia</label>
                <select value={claseSeleccionada} onChange={(e) => setClaseSeleccionada(e.target.value)} required>
                  <option value="">Seleccione una clase</option>
                  {clases.map((c) => (
                    <option key={c} value={c}>{c}</option>
                  ))}
                </select>

                <label>Subclase</label>
                <select value={subclaseSeleccionada} onChange={(e) => setSubclaseSeleccionada(e.target.value)} required>
                  <option value="">Seleccione una subclase</option>
                  {subclases
                    .filter(s => claseSeleccionada && s.startsWith(claseSeleccionada))
                    .map(s => (
                      <option key={s} value={s}>
                        {s}
                      </option>
                    ))}
                </select>

                <label>Edad del Titular</label>
                <input
                  type="number"
                  value={edad}
                  onChange={(e) => setEdad(e.target.value)}
                  required
                  placeholder="Ingrese la edad"
                  min="16"
                />
                {errores.edad && <p className="error">{errores.edad}</p>}

                <label>Localidad licencia</label>
                <select
                  id="localidad"
                  value={localidadSeleccionada}
                  onChange={(e) => setLocalidadSeleccionada(e.target.value)} required>
                  <option value="">Seleccionar</option>
                  {localidades.map((loc) => (
                    <option key={loc.id} value={loc.name}>
                      {loc.name}  
                    </option>
                  ))}
                </select>

                <label>Observaciones</label>
                <textarea
                  value={observaciones}
                  onChange={(e) => setObservaciones(e.target.value)}
                  placeholder="Opcional"
                />
              </div>


              {/* Columna derecha: Admin */}
              <div className="columna">
                <h3>Datos del Usuario Administrativo</h3>
                <label>Nombre</label>
                <input
                  type="text"
                  value={admin.nombre}
                  onChange={(e) => setAdmin({ ...admin, nombre: e.target.value })}
                  required
                />
                {errores.nombre && <p className="error">{errores.nombre}</p>}
                <label>Apellido</label>
                <input
                  type="text"
                  value={admin.apellido}
                  onChange={(e) => setAdmin({ ...admin, apellido: e.target.value })}
                  required
                />
                {errores.apellido && <p className="error">{errores.apellido}</p>}
                <label>Usuario en el sistema</label>
                <input
                  type="text"
                  value={admin.usuario}
                  readOnly
                  disabled
                />
              </div>
            </div>

            <div className="boton-submit">
              <button type="submit">Emitir Licencia</button>
            </div>
      
          </form>
        )}
  </div>
  );
}

export default EmitirLicenciaForm;
