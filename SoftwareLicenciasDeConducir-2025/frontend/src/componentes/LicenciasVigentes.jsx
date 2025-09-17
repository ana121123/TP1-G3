import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./LicenciasVigentes.css"; 
import axios from "axios";

const LicenciasVigentes = () => { 
    const navigate = useNavigate();
    const [licencias, setLicencias] = useState([]);
    const [filtros, setFiltros] = useState({
        tipoDocumento: "",
        numeroDocumento: "",
        nombre: "",
        apellido: "",
        grupoSanguineo: "",
        factorRH: "",
        donante: "",
    });
    const [cargando, setCargando] = useState(false);
    const [licenciaExpandida, setLicenciaExpandida] = useState(null);

    const handleFiltroChange = (e) => {
        const { name, value } = e.target;
        setFiltros((prev) => ({ ...prev, [name]: value }));
    };

    const construirQuery = () => {
        const query = new URLSearchParams();
        Object.entries(filtros).forEach(([clave, valor]) => {
        if (valor) query.append(clave, valor);
        });
        return query.toString();
    };

    const fetchLicencias = () => {
        setCargando(true);
        
        const query = construirQuery();
        axios
        .get(`/api/licencia/vigentes?${query}`)
        .then((res) => {
            // Si res.data no es array, lo convertimos en uno vacío para evitar errores
            if (Array.isArray(res.data)) {
                setLicencias(res.data);
            } else {
                console.warn("Respuesta inesperada: no es array", res.data);
                setLicencias([]);
            }
        })
        .catch((err) => {
            console.error("Error al obtener licencias", err);
        })
        .finally(() => setCargando(false));

    };

    useEffect(() => {
        fetchLicencias(); // carga inicial
    }, []);

    useEffect(() => {
        const delay = setTimeout(() => {
        fetchLicencias(); // cada vez que cambia un filtro
        }, 300);
        return () => clearTimeout(delay);
    }, [filtros]);

    const handleRenovarClick = async (lic) => {
        if (!lic.puedeRenovar) {
            alert("La licencia aún no puede renovarse.");
            return;
        }

        try {
            const res = await axios.post("/api/licencia/renovar", null, {
            params: {
                tipoDocumento: lic.tipoDocumento,
                numeroDocumento: lic.numeroDocumento,
                claseLicencia: lic.claseLicencia
            }
            });

            const titular = res.data;

            navigate("/licencia-copia", {
            state: {
                titular,
                admin: { usuario: "admin" }
            }
            });
        } catch (error) {
            console.error("Error al buscar titular:", error);
            alert("Ocurrió un error al obtener el titular.");
        }
    };
    
    const handleCopiaClick = async (lic) => {

        try {
            const res = await axios.post("/api/licencia/copia", null, {
            params: {
                tipoDocumento: lic.tipoDocumento,
                numeroDocumento: lic.numeroDocumento,
                claseLicencia: lic.claseLicencia
            }
            });

            const titular = res.data;

            navigate("/licencia-copia", {
            state: {
                titular,
                admin: { usuario: "admin" }
            }
            });
        } catch (error) {
            console.error("Error al buscar titular:", error);
            alert("Ocurrió un error al obtener el titular.");
        }
    };

    const handleModificarClick = async (lic) => {

        try {
            const res = await axios.get("/api/titular/obtener", {
            params: {
                tipoDocumento: lic.tipoDocumento,
                numeroDocumento: lic.numeroDocumento,
            }
            });

            const titular = res.data;

            navigate("/modificar-datos-licencia", {
            state: {
                titular
            }
            });
        } catch (error) {
            console.error("Error al buscar titular:", error);
            alert("Ocurrió un error al obtener el titular.");
        }
    };


    const handleSubmit = async (e) => {
        e.preventDefault();

    };
    console.log("licencias:", licencias);

    return (
        <>
        <form onSubmit={handleSubmit} className="form-container">
        <h2>Licencias Vigentes</h2>
       {/* {mensajeError && <p className="mensaje-error">{mensajeError}</p>} */}

        {/* Filtros */}
        <div className="filtros-grid">
            <select
            name="tipoDocumento"
            value={filtros.tipoDocumento}
            onChange={handleFiltroChange}
            >
            <option value="">Tipo de documento</option>
            <option value="DNI">DNI</option>
            <option value="LE">LE</option>
            <option value="LC">LC</option>
            <option value="CI">CI</option>
            <option value="OTRO">Otro</option>
            </select>
            <input
            type="text"
            name="numeroDocumento"
            placeholder="Numero de Documento"
            value={filtros.numeroDocumento}
            onChange={handleFiltroChange}
            />
            <input
            type="text"
            name="nombre"
            placeholder="Nombre"
            value={filtros.nombre}
            onChange={handleFiltroChange}
            />
            <input
            type="text"
            name="apellido"
            placeholder="Apellido"
            value={filtros.apellido}
            onChange={handleFiltroChange}
            />
            <select
            name="grupoSanguineo"
            value={filtros.grupoSanguineo}
            onChange={handleFiltroChange}
            >
            <option value="">Grupo Sanguineo</option>
            <option value="A">A</option>
            <option value="B">B</option>
            <option value="AB">AB</option>
            <option value="O">O</option>
            </select>
            <select
            name="factorRH"
            value={filtros.factorRH}
            onChange={handleFiltroChange}
            >
            <option value="">Factor RH</option>
            <option value="positivo">+</option>
            <option value="negativo">-</option>
            </select>
            <select
            name="donante"
            value={filtros.donante}
            onChange={handleFiltroChange}
            >
            <option value="">Donante</option>
            <option value="si">Si</option>
            <option value="no">No</option>
            </select>
        </div>

        {/* Tabla de licencias */}
        {cargando ? (
            <p>Cargando licencias...</p>
        ) : (
            <div className="tabla-licencias-container">
            <table className="tabla-licencias">
                <thead>
                <tr>
                    <th>Clase</th>
                    <th>Expira</th>
                    <th>Nombre</th>
                    <th>Apellido</th>
                    <th>Tipo</th>
                    <th>Documento</th>
                    <th>Grupo</th>
                    <th>RH</th>
                    <th>Donante</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                {licencias.map((lic) => {
                    const idUnico = lic.numeroDocumento + "-" + lic.claseLicencia;
                    return (
                    <React.Fragment key={lic.numeroDocumento + lic.claseLicencia}>
                    <tr>
                    <td>{lic.claseLicencia}</td>
                    <td>{lic.fechaExpiracion}</td>
                    <td>{lic.nombreTitular} </td>
                    <td>{lic.apellidoTitular}</td>
                    <td>{lic.tipoDocumento}</td>
                    <td>{lic.numeroDocumento}</td>
                    <td>{lic.grupoSanguineoTitular}</td>
                    <td>{lic.factorRHTitular === "Positivo" ? "+" : "-"}</td>
                    <td>{lic.donanteTitular ? "Si" : "No"}</td>
                    <td>
                        <button
                            className="btn-ver"
                            onClick={(e) => {
                            e.preventDefault();
                            setLicenciaExpandida(idUnico === licenciaExpandida ? null : idUnico);
                            }}
                        >
                            {idUnico === licenciaExpandida ? "Ocultar" : "Ver"}
                        </button>
                    </td>
                    </tr>
                    {idUnico === licenciaExpandida && (
                        <tr className="fila-acciones">
                            <td colSpan="10">
                            <div className="acciones-expandido">
                                <button
                                    className="btn-renovar"
                                    onClick={(e) => {
                                        e.preventDefault();
                                        handleRenovarClick(lic);
                                    }}
                                    >
                                    Renovar
                                </button>
                                <button
                                    className="btn-renovar"
                                    onClick={(e) => {
                                        e.preventDefault();
                                        handleModificarClick(lic);
                                    }}
                                    >
                                    Modificar datos
                                </button>
                                <button
                                    className="btn-renovar"
                                    onClick={(e) => {
                                        e.preventDefault();
                                        handleCopiaClick(lic);
                                    }}
                                    >
                                    Emitir copia
                                 </button>
                            </div>
                            </td>
                        </tr>
                        )}
                    </React.Fragment>     
                    );       
                    })}
                </tbody>
            </table>
            </div>
        )}

        </form>

  
        </>
    );
};

export default LicenciasVigentes;