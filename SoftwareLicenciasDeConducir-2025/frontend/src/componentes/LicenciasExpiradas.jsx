import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./LicenciasExpiradas.css"; 
import axios from "axios";

const LicenciasExpiradas = () => { 
    const navigate = useNavigate();
    const [licencias, setLicencias] = useState([]);
    const [filtros, setFiltros] = useState({
        tipoDocumento: "",
        numeroDocumento: "",
        nombre: "",
        apellido: "",
    });
    const [cargando, setCargando] = useState(false);

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
        .get(`/api/licencia/vencidas?${query}`)
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
    
    const handleSubmit = async (e) => {
        e.preventDefault();

    };
    console.log("licencias:", licencias);

    return (
        <>
        <form onSubmit={handleSubmit} className="form-container">
        <h2>Licencias Expiradas</h2>
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
                    <th>Expiró</th>
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
                    return (
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
                            className="btn-renovar-ex"
                            onClick={(e) => {
                            e.preventDefault();
                            handleRenovarClick(lic);
                            }}
                            >
                            Renovar
                        </button>
                    </td>
                    </tr>    
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

export default LicenciasExpiradas;