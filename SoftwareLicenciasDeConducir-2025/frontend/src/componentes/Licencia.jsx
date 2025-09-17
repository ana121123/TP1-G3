import { useLocation } from 'react-router-dom';
//import React, { useState, useEffect }  from "react";
//import axios from "axios";
import "./Licencia.css";

const Licencia = () => {
  const location = useLocation();
  //const [titular, setTitular] = useState(null);
 // const [noEncontrado, setNoEncontrado] = useState(false);
  const {titular, admin } = location.state || {};

  /*useEffect(() => {
    const buscarTitular = async () => {
      setTitular(null);
      setNoEncontrado(false);

      try {
        const response = await axios.get('/api/titular/obtener', {
          params: {
            tipoDocumento: "DNI",
            numeroDocumento: "44234904",
          },
        });

        if (response.data) {
          setTitular(response.data);
          console.log("Valor de titular:", titular);
        } else {
          setNoEncontrado(true);
        }
      } catch (error) {
        console.error('Error al buscar titular:', error);
        setNoEncontrado(true);
      }
    };

    buscarTitular();
  }, []);

  useEffect(() => {
    console.log("titular actualizado:", titular);
  }, [titular]);*/

 /* if (noEncontrado) {
    return <p>No se encontró el titular</p>;
  }*/

  if (!titular) {
    return <p>Cargando datos del titular...</p>;
  }

 const licencias = titular?.licenciasVigentes || [];

  // Obtener la lista de clases (solo categorías y subcategorías)
  const clases = licencias.map(l => l.claseLicencia.subcategoria);

  // Ordenar por fecha de emisión descendente
  const licenciasOrdenadas = [...licencias].sort(
    (a, b) => new Date(b.fechaEmision) - new Date(a.fechaEmision)
  );

  // Tomar la última licencia emitida (la más reciente)
  const ultimaLicencia = licenciasOrdenadas[0] || {};

  // Extraer datos de la última licencia (con fallback)
  const fechaEmision = ultimaLicencia.fechaEmision || "";
  const fechaVencimiento = ultimaLicencia.fechaVencimiento || "";
  const observaciones = ultimaLicencia.observaciones || "-";
  const costo = ultimaLicencia.costo ?? "-"; // Usar ?? en vez de || si el valor puede ser 0
  const localidad = ultimaLicencia.localidad || "-";
  const ultClase = ultimaLicencia.claseLicencia.categoria|| "-";

  /*
  const titular = {
    nombre: "Alex Marcelo",
    apellido: "Oggier",
    fechaNacimiento: "5 FEB 2001",
    direccion: "12 de Octubre Nº: 2040",
    localidad: "Humboldt",
    provincia: "Santa Fe",
    numeroDocumento: "42703982",
    edad:"24"
  };

  const licencia = {
    numero: "42703982",
    clases: ["A2.2", "B1"],
    fechaEmision: "15 MAY 2025",
    fechaVencimiento: "31 ENE 2030",
  };

  const datosDorso = {
    observaciones: "CONDUCE CON LENTES AÉREOS.",
    grupoSanguineo: "0+",
    donante: true,
    organismo: "Humboldt",
    responsable: "Matich, Hernán Pablo",
    detallesClases: {
      "A2.2": {
        categoria: "A2",
        subcategoria: "A2.2",
        titulo: "Triciclos y cuatriciclos",
        descripcion: "Motocicleta, ciclomotor y triciclo hasta 300cc.",
      },
      B1: {
        categoria: "B1",
        subcategoria: "",
        titulo: "Automóviles, utilitarios, camionetas, vans",
        descripcion:
          "Automóviles, utilitarios, camioneta y casa rodante con motor hasta 3500kg. de peso total.",
      },
    },
  };
*/

  /*const admin = {
    nombre: "Laura",
    apellido: "Pérez",
    usuario: "lperez"
  };*/

  //const costoLicencia = 2500; // Ejemplo de costo*/

  const fechaHoraActual = new Date().toLocaleString('es-AR', {
    dateStyle: 'short',
    timeStyle: 'short'
  });

  /*const handlePrint = () => {
    window.print();
  };*/

  function handlePrint(tipo) {
    document.body.classList.remove("licencia-print", "comprobante-print");

    if (tipo === "licencia") {
      document.body.classList.remove("licencia-print", "comprobante-print");
      document.body.classList.add("licencia-print");
      
    } else if (tipo === "comprobante") {
      document.body.classList.remove("licencia-print", "comprobante-print");
      document.body.classList.add("comprobante-print");
    }

    window.print();

    // Opcional: quitar la clase después de imprimir
   /* setTimeout(() => {
      document.body.classList.remove("licencia-print", "comprobante-print");
    }, 1000);*/
  }


  return (
    <div className="licencia-contenedor">
      <div className="licencias-vertical">
      {/* FRENTE DE LA LICENCIA*/}
      <div className="licencia-card">
        <div className="imagen-fondo-superior" />
        <div className="licencia-header">
          <div>
            <h1>Licencia</h1>
            <h2>Nacional de Conducir</h2>
          </div>
          <img src="/escudo-blanco.png" alt="Logo del organismo" className="logo-organismo-blanco" />
        </div>
        <div className="franja-amarilla">
           <h3>ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTIN</h3>
        </div>
        <div className="licencia-body">
          <div className="licencia-left">
            <div className="foto-placeholder"></div>
            <div className="licencia-left-right">
              <div className="dato">
                <p className="etiqueta"><b>Nombre</b> / Name</p>
                <p className="valor">{titular.nombre}</p>
              </div>

              <div className="dato">
                <p className="etiqueta"><b>Apellido</b> / Surname</p>
                <p className="valor">{titular.apellido}</p>
              </div>

              <div className="dato">
                <p className="etiqueta"><b>F. Nacimiento</b> / Birth Date</p>
                <p className="valor">{titular.fechaNacimiento}</p>
              </div>

              <div className="dato">
                <p className="etiqueta"><b>Domicilio</b> / Address</p>
                <p className="valor">{titular.direccion}</p>
              </div>

              <div className="dato">
                <p className="etiqueta"><b>Localidad</b> / City</p>
                <p className="valor">{titular.localidad}</p>
              </div>

              <div className="dato">
                <p className="etiqueta"><b>Provincia</b> / Province</p>
                <p className="valor">{"Santa Fe"}</p>
              </div>
              </div>
            </div>

          <div className="licencia-right">
            <div className="dato-numero">
              <p className="etiqueta-numero"><b>N° de Licencia</b> / Licence N°</p>
              <p className="numero">{titular.numeroDocumento}</p>
            </div>

            <div className="dato-clase">
              <p className="etiqueta-clase"><b>Clases</b> / Class</p> 
              <p className="clases">{clases.join(" ")}</p>
            </div>

            <div className="dato-emision">
              <p className="etiqueta-emision"><b>Otorgamiento</b> / Issuance</p>
              <p className="valor-emision">{fechaEmision}</p>
            </div>

            <div className="dato-venc">
              <p className="etiqueta-venc"><b>Próxima Revisión</b> / Next Review</p>
              <p className="valor-venc">{fechaVencimiento}</p>
            </div>

          </div>
        </div>

        <div className="licencia-footer">
           <div className="footer-izq">
            <img src="seguridad-vial.png" alt="Logo 1" className="logo1" />
            <img src="logo-transporte.png" alt="Logo 2" className="logo2" />
          </div>
          <div className="footer-der">
            <p className="etiqueta-firma"><b>Firma del Titular</b> / Signature</p>
          </div>
         
      </div>
      </div>


      {/* DORSO DE LA LICENCIA*/}
      <div className="licencia-card-dorso">
        <div className="imagen-fondo-superior-dorso" />
        <div className="franja-azul-1"></div>

        <div className="franja-contenido">
          <div className="franja-contenido-top">
            <h1>&gt;&gt;&gt;&gt;&gt;</h1>
          </div>

          <div className="franja-contenido-medium">
            <div className="observaciones">
              <p className="etiqueta-obs"><b>Observaciones</b></p>
              <p className="valor-obs">{observaciones}</p>
            </div>
          </div>
            
          <div className="franja-contenido-bottom">
            <div className="grupo-sangre">
              <p className="etiqueta-gp">G.Sanguíneo</p>
              <p className="valor-gp">{titular.grupoSanguineo}{titular.factorRH === 'positivo' ? '+' : '-'}</p>
            </div>
            <div className="donante">
              <p className="etiqueta-gp">Donante</p>
              <p className="valor-gp">{titular.donante ? "SÍ" : "NO"}</p>
            </div>
          </div>

        </div>

        <div className="franja-arg">
          <h1>INA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA/ARGENTINA</h1>
        </div> 
        <div className="franja-azul-2"></div>

        <div className="clases-detalle">
          <div className="franja-raya-arg">
            <span className="flecha-grande">&gt;</span>
            <hr className="linea-raya" />
            <span className="texto-arg">ARG</span>
          </div> 
          <div className="categorias-limit">
            {clases.map((clase) => {
            const licencia = titular.licenciasVigentes.find(
              (lic) => lic.claseLicencia.subcategoria === clase
            );

            const detalle = licencia?.claseLicencia;
            return detalle ? (
              <div key={clase} className="clase-item">
                {/* Línea superior: categoría + título */}
                <div className="clase-encabezado">
                  <span className="clase-codigo">{detalle.categoria}</span>
                  <span className="clase-titulo">{detalle.titulo}</span>
                </div>

                {/* Línea inferior: subcategoría | descripción */}
                <div className="clase-subdescripcion">
                  <span className="subcategoria">{detalle.subcategoria}</span>
                  <span className="barra">|</span>
                  <span className="descripcion">{detalle.descripcion}</span>
                </div>
              </div>
            ) : null;
          })}
          </div>
        
          <div className="footer-dorso">
            <p className= "organismo"><b>Organismo:</b> {localidad}</p>
            <img src="escudo-negro.png" alt="Logo del organismo" className="logo-organismo" />
          </div>
        </div>
        </div>
  
        <button onClick={() => handlePrint("licencia")} className="btn-print">Imprimir Licencia</button>
      </div>
      

      {/* COMPROBANTE */}
      <div className="cont">
      <div className="comprobante-contenedor">
        <div className="comprobante-header">
          <div className="comprobante-titulo">COMPROBANTE DE EMISIÓN</div>
          <div className="comprobante-fecha">{fechaHoraActual}</div>
        </div>
          
          <hr />

          <div className="comprobante-t"><b>DATOS DEL USUARIO ADMINISTRATIVO</b></div>
          <div className="comprobante-dato"><b>Nombre:</b> {admin.nombre}</div>
          <div className="comprobante-dato"><b>Apellido:</b> {admin.apellido}</div>
          <div className="comprobante-dato"><b>Usuario:</b> {admin.usuario}</div>

          <hr />

          <div className="comprobante-t"><b>DATOS DEL TITULAR</b></div>
          <div className="comprobante-dato"><b>Nombre:</b> {titular.nombre}</div>
          <div className="comprobante-dato"><b>Apellido:</b> {titular.apellido}</div>
          <div className="comprobante-dato"><b>DNI:</b> {titular.numeroDocumento}</div>
          <div className="comprobante-dato"><b>Edad:</b> {titular.comprobanteDTO.edadTitular}</div>

          <hr />

          <div className="comprobante-t"><b>DATOS DE LA LICENCIA EMITIDA</b></div>
          <div className="comprobante-dato"><b>Clase:</b> {ultClase}</div>
          <div className="comprobante-dato"><b>Localidad:</b> {titular.localidad}</div>
          <div className="comprobante-dato"><b>Observaciones:</b> {observaciones}</div>
          <div className="comprobante-dato"><b>Costo:</b> ${costo}</div>

          <div className="comprobante-footer">
            <div className="comprobante-agrad" style={{ marginTop: '20px', textAlign: 'center' }}>
             *** Gracias  ***
            </div>
          </div>

          </div>
            <button onClick={() => handlePrint("comprobante")} className="btn-print">Imprimir Comprobante</button>
          </div>
       </div>


  );
};

export default Licencia;